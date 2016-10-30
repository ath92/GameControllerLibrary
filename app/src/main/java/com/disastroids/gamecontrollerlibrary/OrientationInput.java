package  com.disastroids.gamecontrollerlibrary;

/**
 * Created by Daniel on 02/10/2016.
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


    private WindowManager windowManager;
    private SensorManager sensorManager;

    @Nullable
    private Sensor rotationSensor;

    private int lastAccuracy;

    private float pitch, roll;

    public OrientationInput(Activity activity) {
        windowManager = activity.getWindow().getWindowManager();
        sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

        // Can be null if the sensor hardware is not available
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void startListening() {
        if (rotationSensor == null) {
            //LogUtil.w("Rotation vector sensor not available; will not provide orientation data.");
            return;
        }
        sensorManager.registerListener(this, rotationSensor, 50*1000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (lastAccuracy != accuracy) {
            lastAccuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == rotationSensor) {
            updateOrientation(event.values);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.
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

        // Transform rotation matrix into azimuth/pitch/roll
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Convert radians to degrees
        pitch = orientation[1] * -57;
        roll = orientation[2] * -57;
    }

    public String serialize() {
        String strSerialized;
        strSerialized = "{\"type\":\"OrientationInput\",\"x\":"+pitch+",\"y\":"+roll+",\"z\":0}";

        return strSerialized;
    }

}