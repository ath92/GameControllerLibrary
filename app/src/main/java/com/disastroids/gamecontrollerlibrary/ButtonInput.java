package  com.disastroids.gamecontrollerlibrary;

import android.view.View;

/**
 * InputMethod that handles the input from a button.
 */

public class ButtonInput implements InputMethod, View.OnClickListener {
    /**
     * true if the button has been pressed.
     */
    private boolean clicked = false;

    /**
     * Constructor. Not much going on here.
     */
    public ButtonInput(){
        System.out.println("Hey");
    }

    /**
     * onClick handler. Sets the clicked flag to true, so we now the button has been pressed.
     * @param v necessary since we're implementing this; we're not using it in this instance.
     */
    @Override
    public void onClick(View v) {
        System.out.println("Clicked!");
        clicked = true;
    }

    /**
     * Checks if the button has been pressed and formats that into a sendable package.
     * @return String with a package for the NetworkManager.
     */
    public String serialize(){
        if(clicked){
            clicked =false;
            return "{\"type\": \"Fire\", \"x\": 0, \"y\": 0, \"z\": 0}";
        } else {
            return null;
        }
    }
}
