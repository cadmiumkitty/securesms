/**
 * SecureMessengerException.java
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

/** This is a superclass for all exceptions in SecureMessenger API.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class SecureMessengerException extends Exception {

    /** Creates new <code>SecureMessengerException</code> without detail 
     * message.
     */
    public SecureMessengerException() {
    }
    
    /** Constructs an <code>SecureMessengerException</code> with the specified 
     * detail message.
     * @param msg the detail message.
     */
    public SecureMessengerException(String msg) {
        super(msg);
    }
}
