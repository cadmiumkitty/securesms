/**
 * Address.java
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

/** An address interface. Address is used in the Message object and stored
 * persistently using the AddressStore object.
 *
 * @author Eugene Morozov
 * @version 0.9.0
 */
public interface Address {
    
    /** Returns a unique address Id.
     *
     * @return A unique address Id.
     */    
    public long getId();
    
    /** Returns a user name associated with the address.
     *
     * @return A user name associated with the address.
     */    
    public String getName();
    
    /** Returns a phone number associated with the address.
     *
     * @return A phone number associated with the address.
     */    
    
    public String getNumber();
    /** Returns a key associated with the address.
     *
     * <b>Note:</b> the key essentialy should be the same at the both sides 
     * of the communication link (at least for the initial implementation with 
     * the simmetric encription).
     *
     * @return A key associated with the address.
     */    
    public byte[] getKey();
    
    /** Sets a user name of the Address.
     *
     * @param name The user name.
     */    
    public void setName(String name);
    
    /** Sets a phone number of the Address.
     *
     * @param number The Phone Number
     */    
    
    public void setNumber(String number);
    
    /** Sets a key of the address.
     *
     * @param key The key.
     */    
    public void setKey(byte[] key);
    
}
