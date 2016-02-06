/**
 * SecureMessenger.java
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

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import em.sm.api.*;

/** This is the main application class.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class SecureMessenger extends MIDlet 
        implements CommandListener, StoreObserver {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //

    // System-independent store names - this will stay constant across the
    // releases to achive the backward compatibility
    private static final String ADDRESS_BOOK_STORE_NAME = "ab";
    private static final String INBOX_STORE_NAME = "ib";
    private static final String OUTBOX_STORE_NAME = "ob";
    
    
    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
    
    // Messenger objects - stores and handlers
    private MessageStore inbox;
    private MessageStore sentItems;
    private AddressStore addressBook;
    private MessageHandler messageHandler;

    // Midlet's display
    private Display display;

    // Temporary message and address objects
    private Message message;
    private Address address;
    
    // Messenger screens 
    private MenuScreen menuScreen;
    private AddressScreen addressScreen;
    private AddressStoreScreen addressStoreScreen;
    private MessageScreenIncoming messageScreenIncoming;
    private MessageScreenOutgoing messageScreenOutgoing;
    private MessageStoreScreenInbox messageStoreScreenInbox;
    private MessageStoreScreenOutbox messageStoreScreenOutbox;
    private NewMessageScreen newMessageScreen;

    // Alert information
    private String alertTitle;
    private String alertMessage;
    private AlertType alertType;
    private Displayable alertNext;
    
    
    // -----------------------------------------------------------------------
    //
    // Interface implementation
    //
    //
    
    /** Start the MIDlet.
     */
    public void startApp() throws MIDletStateChangeException {

        try {

            // Initialize everything
            ObjectFactory factory = ObjectFactory.getObjectFactory(
                    ResourceLocator.getResource(ResourceLocator.FACTORY));
            
            // Initialize the address book and two message stores
            addressBook = factory.getAddressStore(ADDRESS_BOOK_STORE_NAME);
            inbox = factory.getMessageStore(INBOX_STORE_NAME);
            sentItems = factory.getMessageStore(OUTBOX_STORE_NAME);
            messageHandler = factory.getMessageHandler();
            messageHandler.setIncomingMessageStore(inbox);
            messageHandler.setOutgoingMessageStore(sentItems);
            messageHandler.setIncomingAddressStore(addressBook);
            messageHandler.setTranscriptor(factory.getTranscriptor());
            addressBook.registerObserver(messageHandler);
            inbox.registerObserver(this);
            messageHandler.init();

            // Show the main menu
            display = Display.getDisplay(this);
            menuScreen = new MenuScreen(this);
            display.setCurrent(menuScreen);
        } catch (SecureMessengerException sme) {

            // Exit without any messages
            destroyApp(true);
        }
    }

    /** Pause the MIDlet
     */
    public void pauseApp() {
    }

    /** Destroy the MIDlet
     */
    public void destroyApp(boolean unconditional) {
        
        // Notification
        notifyDestroyed();
    }

    
    // -----------------------------------------------------------------------
    //
    // Implementation of the command listener interface
    //
    //
    
    /** To save the resources it is decidede to keep all event processing in 
     * one place. It also siplifies the state diagram - we know exactly which 
     * element of the app is shown.
     */
    public synchronized void commandAction(
            Command command, Displayable displayable) {
        
        // Process the events from all of the components
        if (displayable == menuScreen) {
            
            // ---------------------------------------------------------------
            // 
            // Main menu 
            //
            //
            
            if (command == MenuScreen.EXIT) {
                destroyApp(false);
            } else if (command == List.SELECT_COMMAND) {
              
                // Null all references we don't need here
                nullReferences();
                
                switch(menuScreen.getSelectedMenuItem()) {
                    case MenuScreen.INBOX:
                        try {
                            
                            // Create the message store screen
                            // and display
                            messageStoreScreenInbox = 
                                    new MessageStoreScreenInbox(this);
                            messageStoreScreenInbox.updateMessageList(inbox);
                            display.setCurrent(messageStoreScreenInbox);
                            return;
                        } catch (SecureMessengerException sme) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.MESSAGE_STORE_ERROR);
                        } catch (Throwable th) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.MESSAGE_STORE_ERROR);
                        }
                        break;
                    case MenuScreen.SEND_NEW:
                        try {
                            
                            // Get the object factory
                            ObjectFactory factory = 
                                    ObjectFactory.getObjectFactory(
                                            ResourceLocator.getResource(
                                                    ResourceLocator.FACTORY));
                            
                            // Create new message
                            message = factory.getMessage(null, "", 0);
                            
                            // Create the new message screen and display
                            newMessageScreen = new NewMessageScreen(this);
                            newMessageScreen.updateMessage(
                                    message, addressBook);
                            
                            // Display the new message screen
                            display.setCurrent(newMessageScreen);
                            return;
                        } catch (Throwable th) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.MESSAGE_STORE_ERROR);
                        }
                        break;
                    case MenuScreen.SENT_ITEMS:
                        try {
                            
                            // Create the message store screen
                            // and display
                            messageStoreScreenOutbox = 
                                    new MessageStoreScreenOutbox(this);
                            messageStoreScreenOutbox.updateMessageList(
                                    sentItems);
                            display.setCurrent(messageStoreScreenOutbox);
                            return;
                        } catch (SecureMessengerException sme) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.MESSAGE_STORE_ERROR);
                        } catch (Throwable th) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.MESSAGE_STORE_ERROR);
                        }
                        break;
                    case MenuScreen.ADDRESS_BOOK:
                        try {
                            
                            // Create the message store screen
                            // and display
                            addressStoreScreen = new AddressStoreScreen(this);
                            addressStoreScreen.updateAddressList(addressBook);
                            display.setCurrent(addressStoreScreen);
                            return;
                        } catch (SecureMessengerException sme) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.ADDRESS_STORE_ERROR);
                        } catch (Throwable th) {
                            // Fall through
                            alertMessage = ResourceLocator.getResource(
                                    ResourceLocator.ADDRESS_STORE_ERROR);
                        }
                        break;
                }
                
                // Null references
                nullReferences();
                            
                // Set the alert type and next displayable and show the alert
                alertTitle = ResourceLocator.getResource(ResourceLocator.ERROR);
                alertType = AlertType.ERROR;
                alertNext = menuScreen;
                showAlert();
            }
        } else if (displayable == messageStoreScreenInbox) {
            
            // ---------------------------------------------------------------
            // 
            // Inbox
            //
            //
            
            try {
                if (command == List.SELECT_COMMAND) {
                
                    // Get the message Id
                    long id = messageStoreScreenInbox.getSelectedMessageId();
                
                    // Get message from the store
                    message = inbox.getById(id);
                    
                    // Create the incoming message screen, update the message 
                    messageScreenIncoming = new MessageScreenIncoming(this);
                    messageScreenIncoming.updateMessage(message);
                    
                    // Show the screen
                    display.setCurrent(messageScreenIncoming);
                } else if (command == MessageStoreScreenInbox.REPLY) {
                    
                    // Reply to the incoming message
                    replyIncomingMessage();
                } else if (command == MessageStoreScreen.DELETE) {
                    
                    // Delete the incoming message
                    deleteIncomingMessage();
                } else if (command == MessageStoreScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(menuScreen);
                } 
            } catch (Throwable th) {
                    
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = messageStoreScreenInbox;
                showAlert();
            }
        } else if (displayable == messageStoreScreenOutbox) {
            
            // ---------------------------------------------------------------
            // 
            // Sent Items
            //
            //
            
            try {
                if (command == List.SELECT_COMMAND) {

                    // Get the message Id
                    long id = messageStoreScreenOutbox.getSelectedMessageId();
                
                    // Get message from the store
                    message = sentItems.getById(id);
                    
                    // Create the incoming message screen, update the message 
                    messageScreenOutgoing = new MessageScreenOutgoing(this);
                    messageScreenOutgoing.updateMessage(message);
                    
                    // Show the screen
                    display.setCurrent(messageScreenOutgoing);
                } else if (command == MessageStoreScreen.DELETE) {
                    
                    // Delete outgoing message
                    deleteOutgoingMessage();
                } else if (command == MessageStoreScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(menuScreen);
                }
            } catch (Throwable th) {
                
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = messageStoreScreenOutbox;
                showAlert();
            }
        } else if (displayable == addressStoreScreen) {
            
            // ---------------------------------------------------------------
            // 
            // Address book
            //
            //
            
            try {
                if (command == List.SELECT_COMMAND) {

                    // Get the message Id
                    long id = addressStoreScreen.getSelectedAddressId();
                    
                    // Get message from the store
                    address = addressBook.getById(id);
                    
                    // Create the incoming message screen, update the message 
                    addressScreen = new AddressScreenExisting(this);
                    addressScreen.updateAddress(address);
                    
                    // Show the screen
                    display.setCurrent(addressScreen);
                } else if (command == AddressStoreScreen.ADD) {
                
                    // Initialize everything
                    ObjectFactory factory = ObjectFactory.getObjectFactory(
                            ResourceLocator.getResource(
                                    ResourceLocator.FACTORY));
                    
                    // Create the address
                    address = factory.getAddress("", "", new byte[0]);
                
                    // Create the new address screen
                    addressScreen = new AddressScreenNew(this);
                    addressScreen.updateAddress(address);
                    
                    // Show the screen
                    display.setCurrent(addressScreen);
                } else if (command == AddressStoreScreen.DELETE) {

                    // Delete the address marked as current on the
                    // address store screen
                    deleteAddress();
                } else if (command == AddressStoreScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(menuScreen);
                }
            } catch (Throwable th) {
                
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.ADDRESS_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = addressStoreScreen;
                showAlert();
            }
        } else if (displayable == messageScreenIncoming) {
            
            // ---------------------------------------------------------------
            // 
            // Incoming message
            //
            //

            try {
                if (command == MessageScreen.DELETE) {
                
                    // Delete the incoming message
                    deleteIncomingMessage();
                
                    // Clean the reference
                    messageScreenIncoming = null;
                } else if (command == MessageScreenIncoming.REPLY) {
                
                    // Reply to incoming message
                    replyIncomingMessage();
                
                    // Clean the reference
                    messageScreenIncoming = null;
                } else if (command == MessageScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(messageStoreScreenInbox);
                }
            } catch (Throwable th) {
                
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = messageStoreScreenInbox;
                showAlert();
            }
        } else if (displayable == messageScreenOutgoing) {
            
            // ---------------------------------------------------------------
            // 
            // Outgoing message
            //
            //

            try {
                if (command == MessageScreen.DELETE) {
                
                    // Delete the outgoing message
                    deleteOutgoingMessage();
                
                    // Clean the reference
                    messageScreenOutgoing = null;
                } else if (command == MessageScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(messageStoreScreenOutbox);
                }
            } catch (Throwable th) {
                
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = messageStoreScreenOutbox;
                showAlert();
            }
        } else if (displayable == newMessageScreen) {
            
            // ---------------------------------------------------------------
            // 
            // Send new
            //
            //

            try {
                if (command == NewMessageScreen.SEND) {
                    
                    // Get the address id
                    long id = newMessageScreen.getSelectedAddressId();
                    
                    // Set the message address
                    message.setAddress(addressBook.getById(id));
                    message.setText(newMessageScreen.getMessageText());
                    
                    // Send the message
                    messageHandler.send(message);
                    
                    // Show the alert
                    alertTitle = ResourceLocator.getResource(
                            ResourceLocator.INFO);
                    alertMessage = ResourceLocator.getResource(
                            ResourceLocator.MESSAGE_SENT);
                    alertType = AlertType.INFO;
                    alertNext = menuScreen;
                    showAlert();
                } else if (command == NewMessageScreen.CANCEL) {
                    
                    // Switch to the main menu screen
                    display.setCurrent(menuScreen);
                }
            } catch (SecureMessengerIOException smioe) {
                
                // Note: catch this exception specifically
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.INFO);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_HAS_NOT_BEEN_SENT);
                alertType = AlertType.INFO;
                alertNext = newMessageScreen;
                showAlert();
            } catch (Throwable th) {
                
                // Catch all. Assume it is some store problems
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = menuScreen;
                showAlert();
            }
        } else if (displayable == addressScreen) {
            
            // ---------------------------------------------------------------
            // 
            // Address
            //
            //
            
            try {
                if (command == AddressScreen.SAVE) {
                
                    // Set the address parameters
                    address.setName(addressScreen.getAddressName());
                    address.setNumber(addressScreen.getAddressNumber());
                    address.setKey(addressScreen.getAddressKey());
                
                    // Store the address
                    addressBook.store(address);
                    
                    // Show the address store screen
                    addressStoreScreen.updateAddressList(addressBook);
                    
                    // Show the alert
                    alertTitle = ResourceLocator.getResource(
                            ResourceLocator.INFO);
                    alertMessage = ResourceLocator.getResource(
                            ResourceLocator.ADDRESS_SAVED);
                    alertType = AlertType.INFO;
                    alertNext = addressStoreScreen;
                    showAlert();
                    
                    // Clean the references
                    address = null;
                    addressScreen = null;
                } else if (command == AddressScreenExisting.DELETE) {
                    
                    // Delete the address
                    deleteAddress();
                } else if (command == AddressScreen.CANCEL) {
                
                    // Jump to main menu screen
                    display.setCurrent(addressStoreScreen);
                }
            } catch (Throwable th) {
                
                // Catch all
                alertTitle = ResourceLocator.getResource(
                        ResourceLocator.ERROR);
                alertMessage = ResourceLocator.getResource(
                        ResourceLocator.MESSAGE_STORE_ERROR);
                alertType = AlertType.ERROR;
                alertNext = addressStoreScreen;
                showAlert();
            }
        }
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Private helper methods
    //
    //

    private final void deleteAddress() throws SecureMessengerException {

        // Get the message Id
        long id = addressStoreScreen.getSelectedAddressId();
                
        // Remove message from the store
        addressBook.remove(id);
        addressStoreScreen.updateAddressList(addressBook);
                    
        // Show the alert and proceed to the Inbox screen
        alertTitle = ResourceLocator.getResource(ResourceLocator.INFO);
        alertMessage = ResourceLocator.getResource(
                ResourceLocator.ADDRESS_DELETED);
        alertType = AlertType.INFO;
        alertNext = addressStoreScreen;
        showAlert();
    } 
    
    private final void deleteIncomingMessage() throws SecureMessengerException {

        // Get the message Id
        long id = messageStoreScreenInbox.getSelectedMessageId();
                
        // Remove message from the store
        inbox.remove(id);
        messageStoreScreenInbox.updateMessageList(inbox);
                    
        // Show the alert and proceed to the Inbox screen
        alertTitle = ResourceLocator.getResource(ResourceLocator.INFO);
        alertMessage = ResourceLocator.getResource(
                ResourceLocator.MESSAGE_DELETED);
        alertType = AlertType.INFO;
        alertNext = messageStoreScreenInbox;
        showAlert();
    }
    
    private final void deleteOutgoingMessage() throws SecureMessengerException {

        // Get the message Id
        long id = messageStoreScreenOutbox.getSelectedMessageId();
                
        // Remove message from the store
        sentItems.remove(id);
        messageStoreScreenOutbox.updateMessageList(sentItems);
                    
        // Show the alert and proceed to the Inbox screen
        alertTitle = ResourceLocator.getResource(ResourceLocator.INFO);
        alertMessage = ResourceLocator.getResource(
                ResourceLocator.MESSAGE_DELETED);
        alertType = AlertType.INFO;
        alertNext = messageStoreScreenOutbox;
        showAlert();
    }
    
    private final void replyIncomingMessage() throws SecureMessengerException {
    
        // Get the message Id
        long id = messageStoreScreenInbox.getSelectedMessageId();
                
        // Get message from the store
        message = inbox.getById(id);
                    
        // Create the new message screen
        newMessageScreen = new NewMessageScreen(this);
        newMessageScreen.updateMessage(message, addressBook);
                    
        // Show the screen
        display.setCurrent(newMessageScreen);
    }
    
    /** Displays the alert.
     */
    private final void showAlert() {
    
        Alert alert = new Alert(alertTitle, alertMessage, null, alertType);
        alert.setTimeout(Alert.FOREVER);
        
        // Check if we have the next screen to show. If not use the system 
        // default
        if (alertNext != null) {
            display.setCurrent(alert, alertNext);
        } else {
            display.setCurrent(alert);
        }
    }
    
    /** Nulls all references except for menuScreen to improove GC
     */
    private final void nullReferences() {
        
        address = null;
        message = null;
        addressScreen = null;
        addressStoreScreen = null;
        messageScreenIncoming = null;
        messageScreenOutgoing = null;
        messageStoreScreenInbox = null;
        messageStoreScreenOutbox = null;
        newMessageScreen = null;
        
        alertTitle = null;
        alertMessage = null;
        alertType = null;
        alertNext = null;
    }

    // -----------------------------------------------------------------------
    //
    // Implementation of the StoreObserver interface
    //
    //
    
    /** Notifies the Store observer that the object has been removed from the
     * store.
     *
     * @param obj Object that has been removed from the store.
     * @param store The store the object has been removed from.
     */
    public synchronized void objectRemoved(Object obj, ObservableStore store) {
        
        // This should be ignored.
    }
    
    /** Notifies the Store observer that the object has been added to the
     * store.
     *
     * @param obj Object that has been added to the store.
     * @param store The store the object has been added to.
     */
    public synchronized void objectAdded(Object obj, ObservableStore store) {

        // Note: just catch all throwables and dispose them...
        // In case of this notification there is not much we can do about it
        try {
            
            // Note: As we register to listen on the incoming message store
            // we should not check the store reference
            
            // Check if we need to update the current inbox view
            // Note: display.getCurrent() may return null.
            if (messageStoreScreenInbox != null 
                    && display.getCurrent() == messageStoreScreenInbox) {
                messageStoreScreenInbox.updateMessageList(inbox);
            }
            
            // Show the alert
            // Left the alertNext as is. In case of overlapping Alerts the
            // screen to be shown next remains correct.
            alertTitle = ResourceLocator.getResource(ResourceLocator.INFO);
            alertMessage = ResourceLocator.getResource(
                    ResourceLocator.MESSAGE_RECEIVED);
            alertType = AlertType.INFO;
            showAlert();
        } catch (Throwable th) {
            
            // Ignored
        }
        
    }
    
}
