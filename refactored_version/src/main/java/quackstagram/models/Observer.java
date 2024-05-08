package quackstagram.models;

/**
 * The Observer interface represents an object that can be notified of changes
 * in a subject.
 * Classes that implement this interface can register themselves as observers
 * and receive updates
 * when the subject's state changes.
 */

public interface Observer {
    /**
     * 
     * This method is called by the subject to notify the observer of a state change.
     */
    void update();
}

//Subject: Picture class
//Observer: Notification class


// If the Picture state changes, a picture is liked
// All of its observers are notified
// ie update the notification UI