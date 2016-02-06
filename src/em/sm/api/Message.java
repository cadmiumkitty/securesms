/**
 * Message.java
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

/** A message interface. Message object can be stored persistently using the 
 * MessageStore object.
 *
 * @author Eugene Morozov
 * @version 0.9.0
 */
public interface Message {

    /** The read status mask
     * <b>Note:</b> public static variables preferred over the public inner interface
     * static variables to save some resources.
     */    
    public static final int STATUS_MASK_READ = 0x01;
    /** The read status.
     */    
    public static final int STATUS_READ = 0x01;

    /** Returns a unique id of the Message.
     *
     * @return The unique id of the Message.
     */    
    public long getId();
    
    /** Returns a timestamp of the Message.
     *
     * @return The timestamp of the message.
     */    
    public Calendar getTimestamp();
    
    /** Returns the Address of the Message.
     * To save the resources the Message has just one addres. For the outgoing 
     * message it is the recipient address, for the incoming message it is 
     * the originator address.
     *
     * @return The Address of the Message.
     */    
    public Address getAddress();
    
    /** Sets the Address of the Message.
     *
     * @param messageAddress The Address of the Message.
     */    
    public void setAddress(Address messageAddress);
    
    /** Returns the plain text of the message.
     *
     * @return The plain text of the message.
     */    
    public String getText();
    
    /** Sets the plain text of the Message.
     *
     * @param messageText The plain text of the Message.
     */    
    public void setText(String messageText);
    
    /** Returns the status of the message.
     * <b>Note:</b> To detect the status the <code>STATUS_MASK_READ</code>
     * can be used.
     *
     * @return The status of the message.
     */    
    public int getStatus();
    
    /** Sets the status of the message.
     * <b>Note:</b> the <code>STATUS_READ</code> variable can be used.
     *
     * @param status The status of the Message.
     */    
    public void setStatus(int status);
    
}
