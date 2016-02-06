/**
 * MessageRecordFilter.java
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

package em.sm.impl.siemens.m50;

import javax.microedition.rms.RecordFilter;
import em.sm.api.*;

/** Implementation of the RMS RecordFilter interface.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageRecordFilter implements RecordFilter {

    // -----------------------------------------------------------------------
    //
    // Member variables
    //
    //
    
    private long id;
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new <code>MessageRecordFilter</code> instance.
     */
    MessageRecordFilter(long messageId) {
        id = messageId;    
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Implementation of the RecordFilter interface
    //
    //
    
    // Javadoc inherited from the interface
    public boolean matches(byte[] values) {

        // XXX To avoid the message instance creation we can analize the part 
        // of the byte array directly
        Message message = ObjectSerializer.deserializeMessage(values);
        if ((id != 0) && (message.getId() == id)) {
            return true;
        } 
        return false;
    }
    
}
