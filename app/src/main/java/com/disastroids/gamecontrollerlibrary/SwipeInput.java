package  com.disastroids.gamecontrollerlibrary;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * InputMethod that listens to swipe events.
 */

public class SwipeInput extends GestureDetector.SimpleOnGestureListener implements InputMethod {
    /**
     * Member variables that work like thresholds for the swipes. Not every little touch / drag should be considered a swipe.
     */
    private int swipeThreshold = 100, swipeVelocityThreshold = 100;
    /**
     * boolean flag that tells the system whether a swipe was just registered.
     */
    private boolean swipe;

    /**
     * implements this method so it can use the onFling method.
     * @param event the MotionEvent
     * @return always returns true, so onFling is always called.
     */
    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    /**
     * Checks if a swipe has occurred. If so, change the boolean so that the serialize function will know.
     * @param event1 MotionEvent
     * @param event2 MotionEvent
     * @param velocityX X Movement speed of the touch
     * @param velocityY Y Movement speed of the touch
     * @return always returns true; return type is only a boolean because it's required by the parent interface.
     */
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (Math.abs(event1.getY() - event2.getY()) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold) {
            if (event1.getY() - event2.getY() > 0) {
                System.out.println("swipe");
            } else {
                System.out.println("other swipe");
            }
            swipe = true;
        }
        return true;
    }

    /**
     * serializes the swipes, if the occurred.
     * @return Serialized message.
     */
    public String serialize(){
        String returnString = null;
        if(this.swipe){
            returnString = "{\"type\": \"Swipe\", \"x\":0, \"y\":0, \"z\":0}";
            swipe = false;
        }
        return returnString;
    }
}
