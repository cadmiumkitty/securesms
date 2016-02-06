/**
 * ObjectFactory.java
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

import em.sm.api.*;
import em.sm.impl.siemens.sl45i.*;

/** The ObjectFactory should be used to obtain any platform-specific 
 * implementation of the standard interfaces.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public abstract class ObjectFactory {

    // -----------------------------------------------------------------------
    //
    // Static variables
    //
    //
    
    /** Implementation of the singleton.
     */
    private static ObjectFactory theObjectFactory;
    
    
    // -----------------------------------------------------------------------
    //
    // Static methods
    //
    //
    
    /** Returns the device-specific implementation of the ObjectFactory.
     * <b>Note:</b> The ObjectFactory is a singleton.
     *
     * @param className The ObjectFactory classname to be used.
     * @return The object factory instanciated using the className provided.
     * @throws SecureMessengerException In case of the problems with the 
     *         ObjectFactory instanciation.
     */    
    public static final synchronized ObjectFactory getObjectFactory(
            String className) 
                    throws SecureMessengerException {
                
        // Obtain the factory
        if (theObjectFactory == null) {
            try {
                
                // Check the class name supplied by the user
                // Default ObjectFactory is the ObjectFactory of the 
                // Siemens SL45i implemetation of the API.
                if (className == null) {
                    className = "em.sm.impl.siemens.sl45i.SiemensObjectFactory";
                }
                
                // Instanciate the object factory
                theObjectFactory = (ObjectFactory)Class.forName(
                        className).newInstance();
            } catch (IllegalAccessException iae) {
                
                // Rethrow the exception
                throw new SecureMessengerException(iae.toString());
            } catch (InstantiationException ie) {
                
                // Rethrow the exception
                throw new SecureMessengerException(ie.toString());
            } catch (ClassNotFoundException cnfe) {
                
                // Rethrow the exception
                throw new SecureMessengerException(cnfe.toString());
            } catch (ClassCastException cce) {
                
                // Rethrow the exception
                throw new SecureMessengerException(cce.toString());
            }
        }
        
        return theObjectFactory;
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Abstract methods to be implemented in the subclass
    // Address-related methods
    //
    
    /** Returns new platform-specific Address instance.
     *
     * @param name The user name to instantiate the address with.
     * @param number The phone number to instantiate the address with.
     * @param key The key to instantiate the address with.
     * @return The address instance.
     * @throws SecureMessengerException If there is a problem instanciating 
     *         the Address.
     */
    public abstract Address getAddress(String name, String number, 
            byte[] key) throws SecureMessengerException;

    /** Returns new platform-specific AddressStore instance.
     *
     * @param storeName The logical name of the store.
     * @return The Address store with the given name.
     * @throws SecureMessengerException In case if store instanciation fails.
     */
    public abstract AddressStore getAddressStore(String storeName) 
            throws SecureMessengerException;

    /** Returns new platform-specific AddressHandler instance.
     *
     * @return The AddressHandler instance.
     * @throws SecureMessengerException In case of AddressHandler 
     *         instanciation problem.
     */
    public abstract AddressHandler getAddressHandler() 
            throws SecureMessengerException;
    
    
    // -----------------------------------------------------------------------
    //
    // Abstract methods to be implemented in the subclass
    // Message-related methods
    //
    
    /** Returns new platform-specific Message instance.
     *
     * @param address The message address.
     * @param text The Message text
     * @param status The message status. 
     *               Use <code>Message.STATUS_MASK_READ</code>
     *               and <code>Message.STATUS_READ</code> variables.
     * @return The new Message instance.
     * @throws SecureMessengerException In case of any Message instanciation 
     *         problem.
     */
    public abstract Message getMessage(Address address, String text, 
            int status) 
                    throws SecureMessengerException;

    /** Returns new platform-specific MessageStore instance.
     *
     * @param storeName The logical name of the store.
     * @return The instance of the MessageStore with the given logical name.
     * @throws SecureMessengerException In case of any problems instantiating 
     *         the MessageStore
     */
    public abstract MessageStore getMessageStore(String storeName) 
            throws SecureMessengerException;

    /** Returns new platform-specific MessageHandler instance
     *
     * @return The instance of the MessageHandler.
     * @throws SecureMessengerException In case of any problems instanciating 
     *         the MessageHandler.
     */
    public abstract MessageHandler getMessageHandler() 
            throws SecureMessengerException;

    
    // -----------------------------------------------------------------------
    //
    // Abstract methods to be implemented in the subclass
    // Transcriptor-related methods
    //
    
    /** Returns new specific Transcriptor instance
     *
     * @return The platform/version-dependent transcriptor.
     * The default implementation is likely to be TripleDES.
     * @throws SecureMessengerException In case of any problems instanciating 
     *         the MessageHandler.
     */
    public abstract Transcriptor getTranscriptor() 
            throws SecureMessengerException;

}
