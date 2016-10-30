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
 * The NetworkManager class does all the communication between the InputManager and the game server that runs
 * within Unity. It's based around a timed loop, that checks for new input through the InputManager and sends it to
 * the Unity server if there is anything there.
 */
public class NetworkManager {
    //TODO: Use serializeAll from InputManager
    //TODO: Send UDP Command with InputManager via SendCommand
    //TODO: reset all booleans to false (e.g. Jolts)
    /**
     * static singleton instance of the networkManager.
     */
    public static NetworkManager networkManager;
    /**
     * inputmanager which the networkmanager gets its data from.
     */
    private InputManager inputManager;
    /**
     * central timer that controls the loop that sends the data.
     */
    private Timer timer;
    /**
     * timer task that controls the loop.
     */
    private TimerTask timerTask;
    /**
     * interval, in milliseconds, between sending packages.
     */
    private int interval = 100;
    /**
     * socket server for UDP messages; connects to the unity server.
     */
    private DatagramSocket dsocket;
    /**
     * Host (ip-address) of the server.
     */
    private String host;
    /**
     * Port of the server
     */
    private int port;
    /**
     * InetAddress representation of the server.
     */
    private InetAddress address;

    /**
     * This method is used to get the singleton instance.
     * @return singleton instance of the networkmanager; We only need one.
     */
    public static NetworkManager getInstance(){
        if(networkManager == null){
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    /**
     * sets inputmanager; usually called when creating a new activity.
     * @param inputManager the new inputManager that the networkmanager needs to listen to.
     */
    public void setInputManager(InputManager inputManager){
        this.inputManager = inputManager;
    }

    /**
     * constructor. Sets up a timed loop that will be called repeatedly. In that loop, the current InputManager will be called and messages will be sent if they are available.
     */
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


    /**
     * stops the timer, closes the socket.
     */
    public void stop(){
        if(!dsocket.isClosed()) {
            dsocket.close();
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * opens the socket, starts the timer with the interval coming from a class member.
     */

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

    /**
     * changes the interval of the timed loop for sending messages.
     * @param interval the new interval, in miliseconds.
     */
    public void setInterval(int interval){
        this.interval = interval;
        if(timer != null){
            timer.scheduleAtFixedRate(timerTask, 0, interval);
        }
    }

    /**
     * sets up the address and port for the server (runs on unity, normally within the same network).
     * @param host the IP-address.
     * @param port the port number.
     */

    public void setConnection(String host, int port){
        //to do: check if variables are valid.
        try{
            this.port = port;
            address = InetAddress.getByName(host);
        } catch (Exception e){
            System.err.println(e);
        }
    }

    /**
     * get the host.
     * @return IP-address.
     */
    public String getHost(){
        return host;
    }

    /**
     * get the port number.
     * @return port number.
     */
    public int getPort(){
        return port;
    }

}
