package  com.disastroids.gamecontrollerlibrary;

import android.view.View;

/**
 * Created by Tom on 12-10-2016.
 */

public class ButtonInput implements InputMethod, View.OnClickListener {
    private boolean clicked = false;

    public ButtonInput(){
        System.out.println("Hey");
    }

    @Override
    public void onClick(View v) {
        System.out.println("Clicked!");
        clicked = true;
    }

    public String serialize(){
        if(clicked){
            clicked =false;
            return "{\"type\": \"Fire\", \"x\": 0, \"y\": 0, \"z\": 0}";
        } else {
            return null;
        }
    }
}
