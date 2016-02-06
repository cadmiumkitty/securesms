/**
 * AddressStore.java
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

/** The AddressStore persists the Address objects using the
 * device-specific persistent storage.
 *
 * @author Eugene Morozov
 * @version 0.9.0
 */
public interface AddressStore extends ObservableStore {

    /** Stores the address in the device-specific persistent storage.
     * <b>Implementation note:</b> operation should overwrite the address with the
     * same id in the storage.
     *
     * @param address The address to be stored
     * @throws SecureMessengerException In case if the store operation failed (storage is full or
     * serialization/deserialization has failed).
     */    
    public void store(Address address) throws SecureMessengerStoreException;
    
    /** Removes the address with the given id from the persistent storage.
     *
     * @param id Id of the address to be removed from the store.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public void remove(long id) throws SecureMessengerStoreException;
    
    /** Returns the list of the address ids presented in the storage.
     *
     * @return The array of the address ids present in the store.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public long[] listIds() throws SecureMessengerStoreException;
    
    /** Retrievs the Address object from the storage given the id of
     * the address.
     *
     * @param id The id of the address to retrieve.
     * @return The address with the given id.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public Address getById(long id) throws SecureMessengerStoreException;
    
    /** Retrievs the Address object from the storage given the phone number of
     * the address.
     *
     * @param number The number of the address to retrieve.
     * @return The address with the given phone number.
     * @throws SecureMessengerException In case if the store operation failed.
     */    
    public Address getByNumber(String number) 
            throws SecureMessengerStoreException;
    
}

