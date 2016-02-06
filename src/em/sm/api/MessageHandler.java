/**
 * MessageHandler.java
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

package em.sm.api;

/** A MessageHandler interface. Message handler is responsible for 
 * sending and receiving the data over preferred channel (SMS for example).
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public interface MessageHandler extends StoreObserver {

    /** Sends the message over preferred device/network dependent channel.
     * <b>Note:</b> this call is synchronious, however should be interrupted by
     * a call to shutdown(). 
     *
     * @param msg Message to be sent.
     * @throws SecureMessengerException In case if any problems (network 
     * failure, etc.)
     */    
    public void send(Message msg) throws SecureMessengerException;
    
    /** Sets a message store for the incoming messages.
     *
     * @param messageStore The message store to use for incoming messages.
     */    
    public void setIncomingMessageStore(MessageStore messageStore);
    
    /** Sets a message store for the outgoing messages.
     *
     * @param messageStore The message store to use for the outgoing messages.
     */    
    public void setOutgoingMessageStore(MessageStore messageStore);
    
    /** Sets an address store that can be looked up in the event of receiving 
     * the Message from a certain number.
     *
     * @param addressStore The address store to use with the MessageHandler.
     */    
    public void setIncomingAddressStore(AddressStore addressStore);
    
    /** Sets a transcriptor that can be use to cipher and decipher messages.
     *
     * @param transcriptor The Transcriptor to use with this MessageHandler
     */    
    public void setTranscriptor(Transcriptor transcriptor);
    
    /** Initializes the MessageHandler.
     * This is very device-specific. Implemantation may, for example, create
     * the connection for each phone number in the address book.
     *
     * @throws SecureMessengerException In case of initialization problems 
     * (address format errors, etc.).
     */    
    public void init() throws SecureMessengerException;
    
    /** Shuts the MessageHandler down.
     */    
    public void shutdown();
    
    /** Notifies the Store observer that the object has been removed from the
     * store.
     *
     * @param obj Object that has been removed from the store.
     * @param store The store the object has been removed from.
     */
    public void objectRemoved(Object obj, ObservableStore store);
    
    /** Notifies the Store observer that the object has been added to the
     * store.
     *
     * @param obj Object that has been added to the store.
     * @param store The store the object has been added to.
     */
    public void objectAdded(Object obj, ObservableStore store);
    
}
