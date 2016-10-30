package  com.disastroids.gamecontrollerlibrary;

/**
 * InpuMethod for orientation. Implements both inputMethod and SensorEventListener.
 * Some code to deal with angles and matrices done by Keith Platfoot - https://github.com/kplatfoot/android-rotation-sensor-sample
 *
 */

import android.hardware.SensorEventListener;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;

public class OrientationInput implements InputMethod, SensorEventListener  {

    /**
     * needed to get the rotation.
     */
    private WindowManager windowManager;
    /**
     * needed to get the rotation.
     */
    private SensorManager sensorManager;

    /**
     * the actual rotation sensor.
     */
    @Nullable
    private Sensor rotationSensor;
    /**
     * only track the rotation if the sensor data is accurate enough.
     */
    private int lastAccuracy;
    /*
    variables for pitch and roll.
     */
    private float pitch, roll;

    /**
     * constructor. Sets up some general variables so we can read the sensor.
     * @param activity
     */
    public OrientationInput(Activity activity) {
        windowManager = activity.getWindow().getWindowManager();
        sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

        // Can be null if the sensor hardware is not available
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    /**
     * add listener for the rotationSensor.
     */
    public void startListening() {
        if (rotationSensor == null) {
            return;
        }
        sensorManager.registerListener(this, rotationSensor, 50*1000);
    }

    /**
     * when accuracy changes, make sure it's not too low.
     * @param sensor the sensor to be checked.
     * @param accuracy accuracy integer that is injected into the params.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (lastAccuracy != accuracy) {
            lastAccuracy = accuracy;
        }
    }

    /**
     * When the sensor gives us a new value, check if the value is accurate. If so, use it to update our variables.
     * @param event the SensorEvent to work with.
     */

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == rotationSensor) {
            updateOrientation(event.values);
        }
    }

    /**
     * when the orientation has changed, this function is called. It uses the sensor data to compute workable values:
     * angles that represent the pitch and roll. These are a little easier to work with for anyone implementing things
     * on the game end.
     * @param rotationVector
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            default:
                worldAxisForDeviceAxisX = SensorManager.AXIS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
                break;
            case Surface.ROTATION_90:
                worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
                break;
            case Surface.ROTATION_270:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_X;
                break;
        }

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix);

        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        pitch = orientation[1] * -57;
        roll = orientation[2] * -57;
    }

    /**
     * Serialize the data. Puts the latest pitch and roll in a nice JSON object.
     * @return String of package.
     */

    public String serialize() {
        String strSerialized;
        strSerialized = "{\"type\":\"OrientationInput\",\"x\":"+pitch+",\"y\":"+roll+",\"z\":0}";

        return strSerialized;
    }

}