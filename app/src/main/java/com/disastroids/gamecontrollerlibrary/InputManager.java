package com.disastroids.gamecontrollerlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all the InputMethods and the communication with the NetworkManager. A new instance of InputManager is typically used every time a new activity is created.
 */

public class InputManager {
    /**
     * list of all inputMethods (or implementations thereof) that are controlled but this InputManager.
     * This list is iterated through by the serializeAll method every time the networkManager calls it.
     */
    private ArrayList<InputMethod> inputMethods = new ArrayList<InputMethod>();

    /**
     * empty constructor.
     */
    public InputManager(){
    }

    /**
     * adds a new input method to this manager.
     * @param inputMethod the new inputMethod to be added. The manager will automatically forward the packages of this method to the networkmanager.
     */
    public void addInputMethod(InputMethod inputMethod){
        inputMethods.add(inputMethod);
    }

    /**
     * Check if this inputMethod is already part of this Manager.
     * @param inputMethod the InputMethod you want to check.
     * @return boolean that is true if the inputMethod is already part of this InputManager.
     */
    public boolean hasInputMethod(InputMethod inputMethod){
        for(InputMethod method: inputMethods){
            if(method == inputMethod) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove InputMethod from this InputManager.
     * @param inputMethod the InputMethod to be removed.
     */
    public void removeInputMethod(InputMethod inputMethod){
        inputMethods.remove(inputMethod);
    }

    /**
     * This method loops through all the InputMethods that have been added to this InputManager.
     * It calls the serialize() method on all of its children, each of which returns a string with its latest data.
     * @return arrayList with all the data to be sent by the NetworkManager. This method is usually called only by the NetworkManager.
     */
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
