package com.example.king.lock;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;

public class MainActivity extends Activity implements SensorEventListener {

    float xc,yc,zc,size,xg,yg,zg;
    float [] xa = new float[100];
    float [] ya = new float[100];
    float [] za = new float[100];
    float [] xgy = new float[100];
    float [] ygy = new float[100];
    float [] zgy = new float[100];
    int a=0,g=0;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private SensorManager gyroscopeManager;
    private Sensor senGyrosope;
    LockDbHelper mDbHelper = new LockDbHelper(this);
    public final static String EXTRA_MESSAGE = "com.example.king.lock.MESSAGE";


    ContentValues values = new ContentValues();

    int[] org = {1,2,3,6,5,4,7,8,9};
    int[] boo = {0,0,0,0,0,0,0,0,0};
    int[] com = new int[9];
    int x,y;
    int i=0;
    int j=0;
    int flag=1;
    Long firstTime,lastTime,duration;
    int entry=0;
    int click=0;
    int check=0;
    int xco[]=new int[100];
    int yco[]=new int [100];
    public static Vibrator v;
    Rect[] buttons = new Rect[9];
    Rect[] buttons2 = new Rect[9];
    final int[] BTN = {R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,};
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        gyroscopeManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyrosope = gyroscopeManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeManager.registerListener(this,
                gyroscopeManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
        SQLiteDatabase dbw = mDbHelper.getWritableDatabase();
        SQLiteDatabase dbr = mDbHelper.getReadableDatabase();
        long newCount = dbw.insert(LockContract.LockEntry.Table_Name, null, values);
        String [] projection = {LockContract.LockEntry._COUNT, LockContract.LockEntry.Column_Touch_x,
                LockContract.LockEntry.Column_Touch_y, LockContract.LockEntry.Column_Touch_size, LockContract.LockEntry.Column_Touch_time,
                LockContract.LockEntry.Column_Accelerometer_x, LockContract.LockEntry.Column_Accelerometer_y,
                LockContract.LockEntry.Column_Accelerometer_z, LockContract.LockEntry.Column_Gyroscope_x, LockContract.LockEntry.Column_Gyroscope_y,
                LockContract.LockEntry.Column_Gyroscope_z};
        String sortorder = LockContract.LockEntry._COUNT+" DESC";
        Cursor c = dbr.query(LockContract.LockEntry.Table_Name,projection, null,null,null,null,sortorder);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        for(i=0;i<9;i++)
        {
            String buttonID = "button" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button)findViewById(resID);
            Rect out = new Rect();
            button.getGlobalVisibleRect(out);
            buttons[i]=out;

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        x=(int)event.getX();
        y=(int)event.getY();
        size=event.getSize();

        v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        for(i=0;i<9;i++)
        {
           if((!buttons[i].contains(x,y)))
           {
               if(j==0) {
                   xco[j] = x;
                   yco[j] = y;
                   j++;
                   values.put(LockContract.LockEntry.Column_Touch_x,x);
                   values.put(LockContract.LockEntry.Column_Touch_y,y);
               }
               else
               {
                   if(x!=xco[j-1] && y!=yco[j-1])
                   {
                       xco[j] = x;
                       yco[j] = y;
                       j++;
                       values.put(LockContract.LockEntry.Column_Touch_x,x);
                       values.put(LockContract.LockEntry.Column_Touch_y,y);
                   }
               }
           }
            if(buttons[i].contains(x,y)&&boo[i]==0)
            {
                v.vibrate(20);
                if(entry==0)
                {
                   firstTime=System.currentTimeMillis();
                    entry=1;
                }
                click++;
                boo[i]=1;
                com[click-1]=i+1;

                if(click==9)
                {
                    lastTime=System.currentTimeMillis();
                    int k;
                    for(k=0;k<9;k++)
                    {
                        if(com[k]!=org[k])
                        {
                            check=1;
                        }

                    }
                    if(check==0)
                        done();
                }

            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //senSensorManager.unregisterListener(this);
        gyroscopeManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        //senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        gyroscopeManager.registerListener(this,
                gyroscopeManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void done() {

       duration=lastTime-firstTime;
       // String dur = xco.toString();
        if (flag == 1) {
            Intent intent = new Intent(MainActivity.this, Display.class);
            String successMessage ="xa = "+String.valueOf(xc)+"  ya = "
                    +String.valueOf(yc)+" za = "+String.valueOf(zc)+" size = "+String.valueOf(size)
                    +" xg = "+String.valueOf(xg)+" yg = "
                    +String.valueOf(yg)+" zg = "+String.valueOf(zg);
            String failureMessage = "Onne podo";

            if(duration<=2500)
            intent.putExtra(EXTRA_MESSAGE,successMessage);
            else {
                intent.putExtra(EXTRA_MESSAGE, failureMessage);
            }
            startActivity(intent);


        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xc=event.values[0];
            yc=event.values[1];
            zc=event.values[2];
            xa[a]=xc;
            ya[a]=yc;
            za[a]=zc;
            values.put(LockContract.LockEntry.Column_Accelerometer_x,xc);
            values.put(LockContract.LockEntry.Column_Accelerometer_y,yc);
            values.put(LockContract.LockEntry.Column_Accelerometer_z,zc);
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                xg=event.values[0];
                yg=event.values[1];
                zg=event.values[2];
                xgy[g]=xg;
                ygy[g]=yg;
                zgy[g]=zg;
               values.put(LockContract.LockEntry.Column_Gyroscope_x,xg);
               values.put(LockContract.LockEntry.Column_Gyroscope_y,yg);
               values.put(LockContract.LockEntry.Column_Gyroscope_z,zg);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
