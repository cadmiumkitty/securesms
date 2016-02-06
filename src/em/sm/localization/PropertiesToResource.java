/**
 * PropertiesToResource.java
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

package em.sm.localization;

import java.io.*;
import java.util.*;

/** This utility is not a part of the final .JAR.
 * Converts the properties into the internal "resources.bin" file.
 *
 * Localization notes: If you want to make this software available in your 
 * language translate the resources file. Use the the UTF-8 encoding when you 
 * save the properties file.
 * Localization questions? Email me at xonixboy@hotmail.com
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class PropertiesToResource {

    /** Entry point. This is the part of the build process, so implementation 
     * is very thin - no filename/directory checks, etc.
     *
    * @param args the command line arguments.
    */
    public static void main (String args[]) {

        // Check the arguments
        if (args.length != 3) {
            System.out.println("Usage: java PropertiesToResource "
                    + "<encoding name | default> <prop.txt> <resource.bin>");
            System.exit(-1);
        }
        
        // Create and load the properties
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(args[1]));
        
            DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream(args[2]));
        
            // Now get all the keys and save the resources.bin
            Enumeration enum = props.keys();
            while (enum.hasMoreElements()) {
                String keyString = (String)enum.nextElement();
                byte keyByte = Byte.parseByte(keyString);
                String contentString = props.getProperty(keyString);
                byte[] contentBytes;
                
                // Convert to bytes based on the encoding
                try {
                
                    // Try the one selected by user
                    contentBytes = contentString.getBytes(args[0]);
                } catch (UnsupportedEncodingException uee) {
                
                    // Use the default
                    contentBytes = contentString.getBytes();
                }
                int contentBytesLength = contentBytes.length;
            
                dos.writeByte(keyByte);
                dos.writeByte(contentBytesLength);
                for (int i = 0; i < contentBytesLength; i++) {
                    dos.writeByte(contentBytes[i]);
                }
            }
            
            dos.flush();
            dos.close();
        } catch (Exception e) {
            
            System.out.println("Problem converting the properties.");
            e.printStackTrace();
        }
    }

}
