/**
 * AddressHandler.java
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

/** An AddressHandler interface. It can handle address/key exchange between
 * handsets (IR, Bluetooth or any other form of local key exchange).
 * Please note that the send() and receive() calls are always synchronious.
 *
 * @author Eugene Morozov
 * @version 0.9.0
 */
public interface AddressHandler {

    /** Sends an Address using the established link (IR for example).
     *
     * @param address Address to be sent.
     * @throws SecureMessengerException In case of connection failure
     */    
    public void send(Address address) throws SecureMessengerIOException;
    
    /** Receives the Address object using the the established link (IR for example).
     *
     * @return The received address.
     * @throws SecureMessengerException In case of the connection failure.
     */    
    public Address receive() throws SecureMessengerIOException;
    
    /** Initializes the AddressHandler.
     *
     * @throws SecureMessengerException In case of the initialization problems.
     */    
    public void init() throws SecureMessengerException;
    
    /** Shuts the AddressHandler down.
     */    
    public void shutdown();
    
}
