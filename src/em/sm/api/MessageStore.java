/**
 * MessageStore.java
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

import java.util.*;

/** The MessageStore persists the Message objects using the
 * device-specific persistent storage.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public interface MessageStore extends ObservableStore {

    /** Stores the Message in the device-specific persistent storage.
     * <b>Implementation note:</b> operation should overwrite the message with 
     * the same id in the storage.
     *
     * @param msg The message to be stored
     * @throws SecureMessengerException In case if the store operation failed 
     * (storage is full or serialization/deserialization has failed).
     */    
    public void store(Message msg) throws SecureMessengerStoreException;
    
    /** Removes the Message with the given id from the persistent storage.
     *
     * @param id Id of the message to be removed from the storage.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public void remove(long id) throws SecureMessengerStoreException;
    
    /** Returns the list of the message ids presented in the storage.
     *
     * @return The array of the message ids present in the store.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public long[] listIds() throws SecureMessengerStoreException;
    
    /** Retrievs the Message object from the storage given the id of
     * the message.
     *
     * @param id The id of the message to retrieve.
     * @return The message with the given id.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public Message getById(long id) throws SecureMessengerStoreException;
    
}
