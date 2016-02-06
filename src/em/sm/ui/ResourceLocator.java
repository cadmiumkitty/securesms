/**
 * ResourceLocator.java
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

import java.io.*;
import java.util.*;

/** This class handles the String resource requests. Note that this class is 
 * exception-free, so it can be used in the static variable initialization.
 * The resource file has the following format:
 * [rsrc_id: 1 byte][rsrc_length: 1 byte][rsrc: rsrc_length bytes]
 * The resources are stored in the UTF-8 encoding, so this software can be
 * localized easily and build process can be done with no human intervention.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class ResourceLocator {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //
    
    private static final String SOURCE_NAME = "resources.bin";
    private static final Class resourceClass = (new ResourceLocator()).getClass();

    public static final int FACTORY = 0;

    // Commandly used Ids
    public static final int SAVE = 1;
    public static final int SEND = 2;
    public static final int REPLY = 3;
    public static final int DELETE = 4;
    public static final int ADD = 5;
    public static final int CANCEL = 6;
    public static final int EXIT = 7;
    public static final int SECURE_MESSENGER = 10;
    public static final int INBOX = 11;
    public static final int SEND_NEW = 12;
    public static final int SENT_ITEMS = 13;
    public static final int ADDRESS_BOOK = 14;
    public static final int MESSAGE = 20;
    public static final int ADDRESS = 21;
    public static final int NEW_ADDRESS = 22;
    public static final int NEW_MESSAGE = 23;
    public static final int TEXT = 24;
    public static final int TO = 25;
    public static final int FROM = 26;
    public static final int NUMBER = 27;
    public static final int NAME = 28;
    public static final int KEY = 29;
    public static final int MESSAGE_SENT = 30;
    public static final int MESSAGE_HAS_NOT_BEEN_SENT = 31;
    public static final int MESSAGE_DELETED = 32;
    public static final int MESSAGE_HAS_NOT_BEEN_DELETED = 33;
    public static final int MESSAGE_RECEIVED = 34;
    public static final int ADDRESS_SAVED = 35;
    public static final int ADDRESS_HAS_NOT_BEEN_SAVED = 36;
    public static final int ADDRESS_DELETED = 37;
    public static final int ADDRESS_HAS_NOT_BEEN_DELETED = 38;
    public static final int ERROR = 39;
    public static final int INFO = 40;
    public static final int MESSAGE_STORE_ERROR = 41;
    public static final int ADDRESS_STORE_ERROR = 42;
    public static final int CAN_NOT_START = 43;
    
    
    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //
    
    /** Returns the resource String given the resource Id.
     */
    public static final String getResource(int id) {
        
        InputStream is = null;
        try {
            
            // Get the InputStream
            is = resourceClass.getResourceAsStream(SOURCE_NAME);
        
            // Scan through looking for the resource
            int current_rsrc_id;
            int current_rsrc_id_length;
            byte[] resourceBytes;
            String resourceString;
            while (true) {
                
                // Get the id of the resource
                current_rsrc_id = (int)is.read();
                current_rsrc_id_length = (int)is.read();
                if (current_rsrc_id == id) {
                    resourceBytes= new byte[current_rsrc_id_length];
                    is.read(resourceBytes);
                    try {
                        
                        // Convert to the string using UTF-8
                        resourceString = new String(resourceBytes, "UTF-8");
                    } catch (UnsupportedEncodingException uee) {
                        
                        // Do our best to decode the resource - use the 
                        // default encoding instead of UTF-8
                        resourceString = new String(resourceBytes);
                    }
                    return resourceString;
                } else {
                    
                    // Skip this resource
                    if (is.skip(current_rsrc_id_length) 
                            < current_rsrc_id_length) {
                                
                         // Problems reading the stream - return empty String
                         return new String();
                    }
                }
            }
        } catch (EOFException ioe) {
            
            // Return empty string in case of any problems
            return new String();
        } catch (IOException eofe) {
            
            // Return empty string in case if resource not found and EOF 
            // has been reached
            return new String();
        } finally {
            
            // Do our best to free resources in case of any problems
            if (is != null) {
                try {
                    
                    // Close the Stream 
                    is.close();
                } catch (IOException ioe) {
                    
                    // Silently dispose the exception - we have nothing to do
                    // at this stage anyway
                }
            }
        }
    }
    
}
