package com.disastroids.gamecontrollerlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 02/10/2016.
 */

public class InputManager {
    private ArrayList<InputMethod> inputMethods = new ArrayList<InputMethod>();

    public InputManager(){
    }

    public void addInputMethod(InputMethod inputMethod){
        inputMethods.add(inputMethod);
    }

    public boolean hasInputMethod(InputMethod inputMethod){
        for(InputMethod method: inputMethods){
            if(method == inputMethod) {
                return true;
            }
        }
        return false;
    }

    public void removeInputMethod(InputMethod inputMethod){
        inputMethods.remove(inputMethod);
    }

    public ArrayList<String> serializeAll() {
        String total = "";
        ArrayList<String> allStrings = new ArrayList<String>();
        //System.out.println(String.valueOf(inputMethods.size()));
        for(InputMethod method: inputMethods){
            String serialized = method.serialize();
            if(serialized != null) allStrings.add(serialized);
        }
        System.out.println(allStrings.size());
        return allStrings;
    }
}
