/**
 * StoreListener.java
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

/** The <code>StoreObserver</code> interface defines the way for the store 
 * observers to be notified of any of change in the store content (objects 
 * added/removed).
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public interface StoreObserver { 

    /** Notifies the Store observer that the object has been added to the 
     * store.
     * 
     * @param obj Object that has been added to the store.
     * @param store The store the object has been added to.
     */
    public void objectAdded(Object obj, ObservableStore store);

    /** Notifies the Store observer that the object has been removed from the 
     * store.
     * 
     * @param obj Object that has been removed from the store.
     * @param store The store the object has been removed from.
     */
    public void objectRemoved(Object obj, ObservableStore store);
    
}
