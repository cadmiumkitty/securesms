/**
 * MessageScreenOutgoing.java
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
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/** Extension of the MessageScreen. Doesn't contain the "Reply" command.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageScreenOutgoing extends MessageScreen {

    // -----------------------------------------------------------------------
    //
    // Public class data
    //
    //

    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new MessageScreen */
    public MessageScreenOutgoing(SecureMessenger sm) {
        
        // Create the form
        super(sm, ResourceLocator.getResource(ResourceLocator.MESSAGE),
                ResourceLocator.getResource(ResourceLocator.TO),
                ResourceLocator.getResource(ResourceLocator.TEXT));
    }

    
    // -----------------------------------------------------------------------
    //
    // Public methods - inherited from the MessageScreen
    //
    //
    
}
