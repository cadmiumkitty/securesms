/**
 * MessageScreen.java
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

package em.sm.ui;

import em.sm.api.*;
import em.sm.util.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/** This is the message screen. Message can not be updated and saved into the
 * storage. 
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class AddressScreen extends Form {

    // -----------------------------------------------------------------------
    //
    // Public class data
    //
    //

    public static final Command SAVE = new Command(
            ResourceLocator.getResource(ResourceLocator.SAVE), 
            Command.OK, 1);
    public static final Command CANCEL = new Command(
            ResourceLocator.getResource(ResourceLocator.CANCEL), 
            Command.CANCEL, 3);
    
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_NUMBER_LENGTH = 30;
    public static final int MAX_KEY_LENGTH = 32;
    
    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    private TextField name;
    private TextField number;
    private TextField key;

    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new <code>MessageScreen</code> instance.
     */
    protected AddressScreen(SecureMessenger sm) {
        
        // Create the form
        super(ResourceLocator.getResource(ResourceLocator.NEW_ADDRESS));
        
        // Create and add the form items 
        append(name = new TextField(
                ResourceLocator.getResource(ResourceLocator.NAME), 
                "", MAX_NAME_LENGTH, TextField.ANY));
        append(number = new TextField(
                ResourceLocator.getResource(ResourceLocator.NUMBER),
                "", MAX_NUMBER_LENGTH, TextField.PHONENUMBER));
        append(key = new TextField(
                ResourceLocator.getResource(ResourceLocator.KEY), 
                "", MAX_KEY_LENGTH, TextField.ANY));
        
        // Add commands
        addCommand(SAVE);
        addCommand(CANCEL);
        
        // Set the listener
        setCommandListener(sm);
    }

    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Updates the fields of the Form
     */
    public final void updateAddress(Address address) 
            throws SecureMessengerException {

        // Set the title
        setTitle(ResourceLocator.getResource(ResourceLocator.ADDRESS));
                
        // Set the field values
        name.setString(address.getName());
        number.setString(address.getNumber());
        key.setString(EncDec.encodeToHexString(address.getKey()));
    }
    
    /** Clears the Form.
     */
    public final void clear() {
        
        // Set the title
        setTitle(ResourceLocator.getResource(ResourceLocator.NEW_ADDRESS));
        
        // Clear the content of each field
        name.setString("");
        number.setString("");
        key.setString("");
    }
    
    /** Returns the value of the name field.
     */
    public final String getAddressName() {
        return name.getString();
    }
    
    /** Returns the value of the number field.
     */
    public final String getAddressNumber() {
        return number.getString();
    }

    /** Returns the value of the key field converted into the byte[].
     */
    public final byte[] getAddressKey() {
        byte[] result = new byte[16];
        EncDec.decodeHexString(key.getString(), result);
        return result;
    }
}
