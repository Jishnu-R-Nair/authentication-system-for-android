package com.example.king.lock;

import android.provider.BaseColumns;

/**
 * Created by king on 21/1/16.
 */
public final class LockContract {

        public LockContract(){}

    public static abstract class LockEntry implements BaseColumns {

        public static final String Table_Name = "LockTable";
        public static final String Column_Touch_x = "Touch_x";
        public static final String Column_Touch_y = "Touch_y";
        public static final String Column_Touch_size = "Touch_size";
        public static final String Column_Touch_time = "Touch_time";
        public static final String Column_Accelerometer_x = "Accelerometer_x";
        public static final String Column_Accelerometer_y = "Accelerometer_y";
        public static final String Column_Accelerometer_z = "Accelerometer_z";
        public static final String Column_Gyroscope_x = "Gyroscope_x";
        public static final String Column_Gyroscope_y = "Gyroscope_y";
        public static final String Column_Gyroscope_z = "Gyroscope_z";



    }
}