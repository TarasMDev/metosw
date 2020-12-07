package app.mintartdevstudio.com.metosw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    StringBuilder mInfoTemp = new StringBuilder();
    StringBuilder mInfoPress = new StringBuilder();
    StringBuilder mInfoHumi = new StringBuilder();
    StringBuilder mInfoLux = new StringBuilder();
    StringBuilder mInfoALT = new StringBuilder();
    StringBuilder mTemperature = new StringBuilder("");
    StringBuilder mPressure = new StringBuilder("");
    StringBuilder mHumidity = new StringBuilder("");
    StringBuilder mLux = new StringBuilder("");
    StringBuilder mALT = new StringBuilder("");

    public static double mmhg = 1.333;
    public String a = "°C";
    public String b = " mmHG";
    public String c = " %";
    public String dl = " lux";
    public String meters = " m";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText("" + getBatteryTemperature(this));


    }

    @Override
    protected void onResume() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        mSensorManager.registerListener(this, mSensorManager
                        .getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);

        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mInfoTemp = new StringBuilder();
        mInfoPress = new StringBuilder();
        mInfoHumi = new StringBuilder();
        mInfoLux = new StringBuilder();
        mInfoALT = new StringBuilder();
        float alt = 0;


        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            mTemperature = new StringBuilder("");
            mTemperature.append(event.values[0]);
        }
        //try add mmhg
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            mPressure = new StringBuilder("");
            mPressure.append(event.values[0] / mmhg);

        }

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {

            float presure = event.values[0];
            alt = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, presure);

            TextView tv_alt = (TextView) findViewById(R.id.textView9);
            tv_alt.setText(String.format("%.4s", alt) + meters);


        }


        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            mHumidity = new StringBuilder("");
            mHumidity.append(event.values[0]);
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mLux = new StringBuilder("");
            mLux.append(event.values[0]);
        }
        //try add mmhg
        mInfoTemp.append(String.format("%.4s", mTemperature));
        mInfoPress.append(String.format("%.5s", mPressure));
        mInfoHumi.append(String.format("%.4s", mHumidity));
        mInfoLux.append(String.format("%.4s", mLux));
        mInfoALT.append(String.format("%.4s", mALT));


        TextView tv_temp = (TextView) findViewById(R.id.temperatureTextView);
        tv_temp.setText(mInfoTemp.toString() + a);

        TextView tv_press = (TextView) findViewById(R.id.pressureValueTextView);
        tv_press.setText(mInfoPress.toString() + b);

        TextView tv_humi = (TextView) findViewById(R.id.humidityValueTextView);
        tv_humi.setText(mInfoHumi.toString() + c);

        TextView tv_lux = (TextView) findViewById(R.id.textView5);
        tv_lux.setText(mInfoLux.toString() + dl);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public static String getBatteryTemperature(Context c) {
        Intent intent = c.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        float temp = ((float) intent.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return String.valueOf(temp) + "°C";
    }


}