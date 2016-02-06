/**
 * MessageHandlerImpl.java
 *
 * Copyright (C) 2002 Eugene Morozov (xonixboy@hotmail.com)
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation; either version 2 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program; if not, write to the 
 *      Free Software Foundation, Inc., 
 *      59 Temple Place, Suite 330, 
 *      Boston, MA 02111-1307 
 *      USA
 */

package em.sm.impl.nokia.s60;

import em.sm.api.*;
import em.sm.util.*;
import java.io.*;
import java.util.*;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.BinaryMessage;
import javax.microedition.io.Connector;

/** 
 * Implementation of the MessageHandler.
 * This implementation is Nokia Series 60 specific, but may be compatible 
 * with other phones supporting WMA.
 *
 * @author  Eugene Morozov
 * @version 1.1.0
 */
public class MessageHandlerImpl 
        implements MessageHandler, MessageListener, Runnable {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //

    private static MessageConnection connection;
    private static final String MESSAGE_ENCODING = "UTF-8";
    
    // XXX Don't care about Siemens compatibility at the moment as there are
    // too many things involved, for example storing phone model information
    // alongside user name and MSISDN in the address book.
    private static final int port = 9087;

    
    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
    
    private Transcriptor transcriptor;
    private MessageStore incomingMessageStore;
    private MessageStore outgoingMessageStore;
    private AddressStore incomingAddressStore;
    
    private int[] incomingAddressStoreLock = new int[0];
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** The constructor made the package-visible for security reasons
     */
    MessageHandlerImpl() {
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Interface implementation
    //
    //
    
    public synchronized void init() throws SecureMessengerException {
        
        String connectionString = "sms://:" + port;
        
        try {
            connection = (MessageConnection)Connector.open(connectionString);
            connection.setMessageListener(this);
        } catch (IOException ioe) {
            throw new SecureMessengerException("Could not open connection to "
                    + "SMS port.");
        }
    }
    
    public void shutdown() {
        
        try {
            connection.setMessageListener(null);
            connection.close();
        } catch (IOException ioe) {
            // Ignored purposefully - nothing we can do at this point
        }
    }
    
    public synchronized void setTranscriptor(Transcriptor handlerTranscriptor) {
        
        // Set the transcriptor
        transcriptor = handlerTranscriptor;
    }
    
    public synchronized void setIncomingMessageStore(MessageStore messageStore) {
                
        // Set the incoming message store
        incomingMessageStore = messageStore;
    }

    public void setOutgoingMessageStore(MessageStore messageStore) {
        
        // Set the outgoing message store
        outgoingMessageStore = messageStore;
    }
    
    public synchronized void setIncomingAddressStore(
            AddressStore addressStore) {
                
        // Set the incoming message store
        incomingAddressStore = addressStore;
    }

    public void send(Message msg) throws SecureMessengerException {
        
        // Use the key supplied to transcode the message and send it
        if (transcriptor != null) {
            
            synchronized (connection) {
            
                // Encode the message using the key
                byte[] cypherMessageText;
                try {

                    cypherMessageText = transcriptor.encode(
                            msg.getText().getBytes(MESSAGE_ENCODING), 
                            msg.getAddress().getKey());
                } catch (UnsupportedEncodingException uee) {

                    // XXX Do our best to encode the message - 
                    // use the default encoding if UTF-8 is not supported
                    cypherMessageText = transcriptor.encode(
                            msg.getText().getBytes(), 
                            msg.getAddress().getKey());
                }

                // Find the connection to use and send the message
                // Create listener for each address in the AddressStore
                BinaryMessage wmaMessage = (BinaryMessage)connection.newMessage(
                        MessageConnection.BINARY_MESSAGE);
                String wmaAddress = new StringBuffer(
                        "sms://").append(
                        msg.getAddress().getNumber()).append(
                        ":").append(
                        port).toString();
                wmaMessage.setAddress(wmaAddress);
                wmaMessage.setPayloadData(cypherMessageText);
                
                try {
                    connection.send(wmaMessage);
                    outgoingMessageStore.store(msg);
                } catch (SecurityException se) {
                    throw new SecureMessengerIOException(
                        se.toString());
                } catch (IOException ioe) {
                    throw new SecureMessengerException(
                            "Could not send message.");
                }
            }
        } else {
            
            throw new SecureMessengerException("Transcriptor not set.");
        }
    }
    
    private void receive() {
    
        if (incomingAddressStore != null 
                && incomingMessageStore != null
                && transcriptor != null) {

            synchronized (connection) {
                try {

                    // Store the message in the message store
                    // Note: use fixed factory class name as we know for sure 
                    // which factory should be used.
                    ObjectFactory factory = ObjectFactory.getObjectFactory(
                            "em.sm.impl.nokia.s60.NokiaObjectFactory");
                    
                    javax.wireless.messaging.Message wmaMessage = 
                            connection.receive();
                    
                    if (!(wmaMessage instanceof BinaryMessage)) {
                        
                        // It is safe to skip non-binary message, return.
                        return;
                    }
                    
                    BinaryMessage binaryWmaMessage = (BinaryMessage)wmaMessage;

                    // WMA address is in format "sms://<number>:<port>", so
                    // parse it here to remove prefix and port number
                    // XXX Do real phones use "+" in MSISDN?
                    String wmaAddress = wmaMessage.getAddress();
                    String number = wmaAddress.substring(
                            6, wmaAddress.indexOf(":", 6));
                    
                    Address address = incomingAddressStore.getByNumber(number);
                    byte[] messageText = transcriptor.decode(
                            binaryWmaMessage.getPayloadData(), 
                            address.getKey());

                    // Create new message
                    Message message;
                    try {

                        message = factory.getMessage(address,
                                new String(messageText, MESSAGE_ENCODING), 0);
                    } catch (UnsupportedEncodingException uee) {

                        // Note: Do our best to decode the message - use
                        // the default encoding if nessesary
                        message = factory.getMessage(address,
                                new String(messageText), 0);
                    }

                    // Store the message
                    incomingMessageStore.store(message);
                } catch (IOException ioe) {
                    // Nothing we can do at this point
                    // XXX Can we?
                } catch (SecureMessengerException sme) {
                    // Nothing we can do at this point
                    // XXX Can we?
                }
            }
        } 

        // Else: Nothing we can do at this point
        // XXX Can we?
    }


    // -----------------------------------------------------------------------
    //
    // Implementation of the StoreObserver interface
    //
    //
    
    /** Acts on removed address - removes the corresponding 
     * Connection Listener. 
     */
    public void objectRemoved(Object obj, ObservableStore store) {

        // Note: initial implementation is Siemens-specific. In case of
        // Nokia Series 60 we should not deregister any listeners when adding
        // or removing an address to/from the address store.
    }
    
    /** Acts on added address - adds the corresponding 
     * Connection Listener. 
     */
    public void objectAdded(Object obj, ObservableStore store) {

        // Note: initial implementation is Siemens-specific. In case of
        // Nokia Series 60 we should not deregister any listeners when adding
        // or removing an address to/from the address store.
    }

    
    // -----------------------------------------------------------------------
    //
    // Implementation of the StoreObserver interface
    //
    //
    
    /**
     * Hook for incoming message notification
     *
     * @param messageConnection MessageConnection on which the message 
     *      was received.
     */
    public void notifyIncomingMessage(MessageConnection messageConnection) {
        
        // To avoid spending too much memory we use same instance of
        // Runnable (this) to start all threads.
        new Thread(this).start();
    }

    // -----------------------------------------------------------------------
    //
    // Inner private classes - recepients of the incoming messages
    //
    //
    
    /**
     * Implementation of runnable interface
     */
    public void run() {
        receive();
    }
    
}
