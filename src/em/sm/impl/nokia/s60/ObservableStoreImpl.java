/**
 * ObservableStore.java
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

package em.sm.impl.nokia.s60;

import java.util.Vector;
import java.util.Enumeration;
import em.sm.api.*;

/** 
 * This is the superclass for all storage implementations.
 *
 * @author  Eugene Morozov
 * @version 1.1.0
 */
public class ObservableStoreImpl implements ObservableStore {

    // -----------------------------------------------------------------------
    //
    // Member variables
    //
    //
    
    Vector observers = new Vector();
    
    
    // -----------------------------------------------------------------------
    //
    // Methods to add the observers
    //
    //
    
    // Javadoc inherited from the interface
    public void registerObserver(StoreObserver observer) {
        observers.addElement(observer);
    }
    
    // Javadoc inherited from the interface
    public void unregisterObserver(StoreObserver observer) {
        observers.removeElement(observer);
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Notification methods for the subclasses
    //
    //
    
    /** Notifies all observers of the store about addition of the object.
     *
     * @param obj The added object.
     */
    protected void notifyAdded(Object obj) {
        
        // Notify each of the observers
        Enumeration enum = observers.elements();
        while (enum.hasMoreElements()) {
            ((StoreObserver)enum.nextElement()).objectAdded(obj, this);
        }
    }
    
    /** Notifies all observers of the store about removing of the object.
     *
     * @param obj The removed object.
     */
    protected void notifyRemoved(Object obj) {
        
        // Notify each of the observers
        Enumeration enum = observers.elements();
        while (enum.hasMoreElements()) {
            ((StoreObserver)enum.nextElement()).objectRemoved(obj, this);
        }
    }
    
}
