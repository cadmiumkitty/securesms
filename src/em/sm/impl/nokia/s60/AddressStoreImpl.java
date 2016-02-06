/**
 * AddressStoreImpl.java
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

import java.util.*;
import javax.microedition.rms.*;
import em.sm.api.*;

/** Implementation of the <code>AddressStore</code> interface.
 *
 * @author  Eugene Morozov
 * @version 1.1.0
 */
public final class AddressStoreImpl extends ObservableStoreImpl 
        implements AddressStore {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //

    
    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
    
    private String name;
    private RecordStore recordStore;
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new <code>AddressStoreImpl</code> instance.
     * The additional string parameter allows us to create different 
     * AddressStores (for example different address books).
     */
    AddressStoreImpl(String storeName) {
        name = storeName;
    }

    
    // -----------------------------------------------------------------------
    //
    // Interface implementation
    //
    //
    
    // Javadoc inherited from the interface
    public synchronized void store(Address address) 
            throws SecureMessengerStoreException {
        
        // Check if recordStore has been initialized
        if (recordStore == null) {
            initRecordStore();
        }
        
        // Prepare filter
        AddressRecordFilter filter = new AddressRecordFilter(
                address.getId(), null, null);
        
        // Request the enumeration
        RecordEnumeration enum = null;
        try {
            enum = recordStore.enumerateRecords(filter, null, false);
        
            // Serialize the Address
            byte[] serializedAddress = ObjectSerializer.serializeAddress(
                    address);
        
            // If record with the same Id has been found it ought to be 
            // replaced, if not found - add new record
            if (enum.hasNextElement()) {
        
                // Replace the existing record
                recordStore.setRecord(enum.nextRecordId(), serializedAddress, 
                        0, serializedAddress.length);
            } else {
            
                // Put the new record
                recordStore.addRecord(serializedAddress, 0, 
                        serializedAddress.length);
            }
        } catch (RecordStoreException rse) {
            
            // Notify the caller
            throw new SecureMessengerStoreException(rse.toString());
        } finally {
            
            // Make sure the enumeration has been destroyed before the call 
            // to the method returns
            if (enum != null) {
                enum.destroy();
            }
        }
        
        // Notify the observers
        notifyAdded(address);
    }
    
    // Javadoc inherited from the interface
    public synchronized void remove(long id) 
            throws SecureMessengerStoreException {

        // Check if recordStore has been initialized
        if (recordStore == null) {
            initRecordStore();
        }

        // Get the object 
        Address address = getById(id);

        // Prepare filter
        AddressRecordFilter filter = new AddressRecordFilter(id, null, null);
        
        // Request the enumeration
        RecordEnumeration enum = null;
        try {
            enum = recordStore.enumerateRecords(filter, null, false);
            if (enum.hasNextElement()) {
        
                // Replace the record
                recordStore.deleteRecord(enum.nextRecordId());
            } else {
            
                // Notify the caller
                throw new SecureMessengerStoreException(
                        "There is no address with the Id [" 
                        + id + "] to remove. ");
            }
        } catch (RecordStoreException rse) {
            
            // Notify the caller
            throw new SecureMessengerStoreException(rse.toString());
        } finally {
            
            // Make sure the enumeration destroyed
            if (enum != null) {
                enum.destroy();
            }
        }
        
        // Notify the observers of the store
        notifyRemoved(address);
    }
    
    // Javadoc inherited from the interface
    public synchronized Address getById(long id) 
            throws SecureMessengerStoreException {

        // Check if recordStore has been initialized
        if (recordStore == null) {
            initRecordStore();
        }

        // Prepare filter
        AddressRecordFilter filter = new AddressRecordFilter(id, null, null);
        
        // Request the enumeration
        RecordEnumeration enum = null;
        try {
            enum = recordStore.enumerateRecords(filter, null, false);
            if (enum.hasNextElement()) {
        
                // Deserialize the address, destroy enumeration and return the
                // address to caller
                return ObjectSerializer.deserializeAddress(enum.nextRecord());
            } else {
            
                // Notify the user
                throw new SecureMessengerStoreException(
                        "There is no address with the Id [" + id + "]. ");
            } 
        } catch (RecordStoreException rse) {
            
            // Notify the caller
            throw new SecureMessengerStoreException(rse.toString());
        } finally {
            
            // Make sure the enumeration destroyed
            if (enum != null) {
                enum.destroy();
            }
        }
    }

    // Javadoc inherited from the interface
    public synchronized Address getByNumber(String number) 
            throws SecureMessengerStoreException {

        // Check if recordStore has been initialized
        if (recordStore == null) {
            initRecordStore();
        }

        // Prepare filter
        AddressRecordFilter filter = new AddressRecordFilter(0, null, number);
        
        // Request the enumeration
        RecordEnumeration enum = null;
        try {
            enum = recordStore.enumerateRecords(filter, null, false);
            if (enum.hasNextElement()) {
        
                // Deserialize the address, destroy enumeration and return the
                // address to caller
                return ObjectSerializer.deserializeAddress(enum.nextRecord());
            } else {
            
                // Notify the caller
                throw new SecureMessengerStoreException(
                        "There is no address with the number [" 
                        + number + "]. ");
            } 
        } catch (RecordStoreException rse) {
            
            // Notify the caller
            throw new SecureMessengerStoreException(rse.toString());
        } finally {
            
            // Make sure the enumeration destroyed
            if (enum != null) {
                enum.destroy();
            }
        }
    }
    
    // Javadoc inherited from the interface
    public synchronized long[] listIds() throws SecureMessengerStoreException {
        
        // Check if recordStore has been initialized
        if (recordStore == null) {
            initRecordStore();
        }
        
        // Request the enumeration
        RecordEnumeration enum = null;
        try {
            
            // Create the array of IDs
            long[] ids = new long[recordStore.getNumRecords()];
            int idCounter = 0;
            
            // Enumerate records
            enum = recordStore.enumerateRecords(null, null, false);
            while(enum.hasNextElement()) {
                ids[idCounter] = ObjectSerializer.deserializeAddress(
                        enum.nextRecord()).getId();
                idCounter++;
            } 
            
            return ids;
        } catch (RecordStoreException rse) {
            
            // Notify the caller
            throw new SecureMessengerStoreException(rse.toString());
        } finally {
            
            // Make sure the enumeration destroyed
            if (enum != null) {
                enum.destroy();
            }
        }
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Helper methods 
    //
    //
    
    // Javadoc inherited from the interface
    private void initRecordStore() throws SecureMessengerStoreException {
        
        try {
            
            // Get the RecordStore instance for the given name,
            // create the underlying RDS store if needed.
            recordStore = RecordStore.openRecordStore(name, true);
        } catch (RecordStoreException rse) {
            
            // Rethrow the exception
            throw new SecureMessengerStoreException(rse.toString());
        }
    }
    
}
