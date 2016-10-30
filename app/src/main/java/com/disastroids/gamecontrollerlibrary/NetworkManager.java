package  com.disastroids.gamecontrollerlibrary;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daniel on 02/10/2016.
 */
public class NetworkManager {
    //TODO: Use serializeAll from InputManager
    //TODO: Send UDP Command with InputManager via SendCommand
    //TODO: reset all booleans to false (e.g. Jolts)

    public static NetworkManager networkManager;

    private InputManager inputManager;

    private Timer timer;
    private TimerTask timerTask;
    private int interval = 100;

    private DatagramSocket dsocket;
    private String host;
    private int port;
    private InetAddress address;

    public static NetworkManager getInstance(){
        if(networkManager == null){
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    public void setInputManager(InputManager inputManager){
        this.inputManager = inputManager;
    }

    private NetworkManager(){

        try {
            dsocket = new DatagramSocket();

            timerTask = new TimerTask() {

                @Override
                public void run() {
                    try {
                        if(address != null) {
                            for(String messageString: inputManager.serializeAll()){
                                byte[] message = messageString.getBytes();
                                // Initialize a datagram packet with data and address
                                DatagramPacket packet = new DatagramPacket(message, message.length,
                                        address, port);
                                // Create a datagram socket, send the packet through it, close it.
                                dsocket.send(packet);
                                System.out.println("Sent: " + messageString);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            };

        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void stop(){
        if(!dsocket.isClosed()) {
            dsocket.close();
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void start(){
        try{
            if(dsocket.isClosed()){
                dsocket = new DatagramSocket();
            }

            if(timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(timerTask, 0, interval);
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void setInterval(int interval){
        this.interval = interval;
        if(timer != null){
            timer.scheduleAtFixedRate(timerTask, 0, interval);
        }
    }

    public void setConnection(String host, int port){
        //to do: check if variables are valid.
        try{
            this.port = port;
            address = InetAddress.getByName(host);
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public String getHost(){
        return host;
    }

    public int getPort(){
        return port;
    }

}
