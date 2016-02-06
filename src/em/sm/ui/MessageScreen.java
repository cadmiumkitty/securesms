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
import java.util.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/** The message screen. Can be extended to create specific message screens 
 * (Incoming, Outgoing).
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageScreen extends Form {

    // -----------------------------------------------------------------------
    //
    // Public class data
    //
    //

    public static final Command DELETE = new Command(
            ResourceLocator.getResource(ResourceLocator.DELETE), 
            Command.SCREEN, 2);
    public static final Command CANCEL = new Command(
            ResourceLocator.getResource(ResourceLocator.CANCEL), 
            Command.CANCEL, 3);
    
    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    protected StringItem address;
    protected StringItem text;
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new MessageScreen 
     */
    protected MessageScreen(SecureMessenger sm, 
            String title, String addressTitle, String textTitle) {
        
        // Create the form
        super(title);
        
        // Create the string items 
        append(address = new StringItem(addressTitle, ""));
        append(text = new StringItem(textTitle, ""));
        
        // Add commands
        addCommand(DELETE);
        addCommand(CANCEL);
        
        // Set the listener
        setCommandListener(sm);
    }

    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Updates the message Form.
     */
    public final void updateMessage(Message message) {
        
        // Set the title
        StringBuffer messageTitle = new StringBuffer();
        Calendar timestamp = message.getTimestamp();
        messageTitle.append(
                timestamp.get(Calendar.DAY_OF_MONTH)).append(
                '-').append(
                timestamp.get(Calendar.MONTH) + 1).append(
                '-').append(
                timestamp.get(Calendar.YEAR));
        setTitle(messageTitle.toString());
        
        // Set the fields
        address.setText(message.getAddress().getName());
        text.setText(message.getText());
    }
    
    /** Clears the message form.
     */
    public final void clear() {
        
        // Clear the title
        setTitle("");
        
        // Clear the fields
        address.setText("");
        text.setText("");
    }
}
