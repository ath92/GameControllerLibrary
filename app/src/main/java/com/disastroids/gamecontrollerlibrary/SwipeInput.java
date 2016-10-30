package  com.disastroids.gamecontrollerlibrary;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Tom on 12-10-2016.
 */

public class SwipeInput extends GestureDetector.SimpleOnGestureListener implements InputMethod {

    private int swipeThreshold = 100, swipeVelocityThreshold = 100;
    private boolean swipe;

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }


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

    public String serialize(){
        String returnString = null;
        if(this.swipe){
            returnString = "{\"type\": \"Swipe\", \"x\":0, \"y\":0, \"z\":0}";
            swipe = false;
        }
        return returnString;
    }
}
