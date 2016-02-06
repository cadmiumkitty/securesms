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

package em.sm.impl.siemens.m50;

import em.sm.api.*;
import em.sm.util.*;
import java.io.*;
import java.util.*;
import com.siemens.mp.io.Connection;
import com.siemens.mp.io.ConnectionListener;
import com.siemens.mp.NotAllowedException;

/** Implementation of the MessageHandler.
 * This implementation is SiemensSL45i-specific and uses the Siemens API for 
 * SMS messages.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageHandlerImpl implements MessageHandler {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //

    private static Vector connections = new Vector();
    private static final String MESSAGE_ENCODING = "UTF-8";
    
    // Note: it is not clear whether this ID should be unique and obtained
    // from Siemens or we should just make our best guess and peek one.
    private static final String APP_ID = "909087";

    
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
    // Inner private classes - recepients of the incoming messages
    //
    //
    
    /** This is the implementation of the <code>ConnectionListener</code> 
     * interface should be kept short to avoid spending too much memory.
     */
    private class AddressConnection implements ConnectionListener {
        
        private Connection connection;
        private String number;
        
        /** Creates the new instance of the <code>AddressConnection</code>.
         * 
         * @param addressNumber The phone number to send/receive the data on.
         */
        public AddressConnection(String addressNumber) {
            number = addressNumber;
        }

        /** Returns the phone number the connection is open to.
         *
         * @return The phone number the connection is opened to.
         */
        public String getNumber() {
            return number;
        }
        
        /** Initialises the listener - opens the connection and
         * sets itself as a listener.
         */
         public void init() throws SecureMessengerException {
            
             // Connection that listens on a given phone number
             connection = new Connection("SMS:" 
                    + EncDec.normalizeAddressString(number) + ":" + APP_ID);
             connection.setListener(this);
        }

        /** Shuts the connection down
         */
        public void shutdown() {
            
            // Sets the connection to null
            // to enforce garbage collection
            // XXX Does this help at all? How does the SL45i GC the 
            // connections?
            connection = null;
        }
        
        /** Receives data on the connection
         */
        public void receiveData(byte[] data) {
            
            receive(number, data);
        }
        
        /** Sends data on the connection
         */
        public void sendData(byte[] data) throws SecureMessengerIOException {
            
            try {
                
                // Send data down the wire
                connection.send(data);
            } catch (NotAllowedException nae) {
                
                // Rethrow the exception
                throw new SecureMessengerIOException(nae.toString());
            }
        }
    }
    
    
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

        if (incomingAddressStore != null) {
            
            // Create listener for each address in the AddressStore
            long[] ids = incomingAddressStore.listIds();
            int idsLength = ids.length;
            Address address;
            for (int i = 0; i < idsLength; i++) {
                address = incomingAddressStore.getById(ids[i]);
                AddressConnection addressConnection = new AddressConnection(
                        address.getNumber());
                addressConnection.init();
                connections.addElement(addressConnection);
            }
        } else {
            
            // Inform the user
            throw new SecureMessengerException("Address store was not set.");
        }
    }
    
    public void shutdown() {

        // XXX Does nothing in this implementation
        // Should probably get the connection iterator and shut them down
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
            String number = msg.getAddress().getNumber();
            Enumeration enum = connections.elements();
            while (enum.hasMoreElements()) {
                AddressConnection connection = 
                        (AddressConnection)enum.nextElement();
                if (connection.getNumber().equals(number)) {

                    // Send the data using the connection that has been 
                    // initialized already
                    connection.sendData(cypherMessageText);
                    
                    // Put the message into the outgoing message store
                    outgoingMessageStore.store(msg);
                    
                    // No need to go through enumeration any further
                    break;
                }
            }
        } else {
            
            throw new SecureMessengerException("Transcriptor not set.");
        }
    }
    
    private void receive(String addressNumber, byte[] data) {
    
        if (incomingAddressStore != null 
                && incomingMessageStore != null
                && transcriptor != null) {
        
            try {
                
                // Store the message in the message store
                // Note: use fixed factory class name as we know for sure which 
                // factory should be used.
                ObjectFactory factory = ObjectFactory.getObjectFactory(
                        "em.sm.impl.siemens.m50.SiemensObjectFactory");
                
                Address address = incomingAddressStore.getByNumber(
                        addressNumber);

                byte[] messageText = transcriptor.decode(
                        data, address.getKey());

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
            } catch (SecureMessengerException sme) {

                // Nothing we can do at this point
                // XXX Can we?
            }
        } else {

            // Nothing we can do at this point
            // XXX Can we?
        } 
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
        
        if (obj instanceof Address) {
            int connectionsSize = connections.size();
            Address address = (Address)obj;
            for (int i = 0; i < connectionsSize; i++) {
                AddressConnection addressConnection = 
                        (AddressConnection)connections.elementAt(i);
                if (addressConnection.getNumber().equals(address.getNumber())) {
                    addressConnection.shutdown();
                    connections.removeElementAt(i);
                    break;
                }
            }
        }
    }
    
    /** Acts on added address - adds the corresponding 
     * Connection Listener. 
     */
    public void objectAdded(Object obj, ObservableStore store) {

        if (obj instanceof Address) {
            
            // Check if we already have the connection for this number opened
            int connectionsSize = connections.size();
            boolean connectionExists = false;
            Address address = (Address)obj;
            for (int i = 0; i < connectionsSize; i++) {
                AddressConnection addressConnection = 
                        (AddressConnection)connections.elementAt(i);
                if (addressConnection.getNumber().equals(address.getNumber())) {
                    connectionExists = true;
                    break;
                }
            }
            
            if (!connectionExists) {
                AddressConnection addressConnection = new AddressConnection(
                        ((Address)obj).getNumber());
                try {
                    addressConnection.init();
                    connections.addElement(addressConnection);
                } catch (SecureMessengerException sme) {
                    // There is nothing we can do about it at this stage so just 
                    // ignore the exception.
                }
            }
        }
    }
    
}
