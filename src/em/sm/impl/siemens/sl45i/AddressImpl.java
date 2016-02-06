/**
 * AddressImpl.java
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

package em.sm.impl.siemens.sl45i;

import em.sm.api.Address;

/** Independent implementation of the Address interface.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public final class AddressImpl implements Address {

    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
  
    private long id;
    private String name;
    private String number;
    private byte[] key;
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new <code>AddressImpl</code> instance.
     */
    AddressImpl(long addressId, String addressName, String addressNumber, 
            byte[] addressKey) {

        // Set variables
        id = addressId;
        name = addressName;
        number = addressNumber;
        key = addressKey;
    }

    
    // -----------------------------------------------------------------------
    //
    // Accessors
    //
    //

    // Javadoc inherited from the interface
    public long getId() {
        return id;
    }

    // Javadoc inherited from the interface    
    public String getName() {
        return name;
    }

    // Javadoc inherited from the interface
    public String getNumber() {
        return number;
    }
    
    // Javadoc inherited from the interface
    public byte[] getKey() {
        return key;
    }
    
    // Javadoc inherited from the interface
    public void setKey(byte[] addressKey) {
        key = addressKey;
    }
    
    // Javadoc inherited from the interface    
    public void setNumber(String addressNumber) {
        number = addressNumber;
    }
    
    // Javadoc inherited from the interface    
    public void setName(String addressName) {
        name = addressName;
    }
    
}
