package com.example.first;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AVATAR_RES_ID = "avatar_res_id";
    public static final String COLUMN_COMPLETION_PERCENTAGES = "completion_percentages";
    // 新增八个列名用于存储各关卡的地图数据
    public static final String COLUMN_MAP_DATA_1 = "map_data_1";
    public static final String COLUMN_MAP_DATA_2 = "map_data_2";
    public static final String COLUMN_MAP_DATA_3 = "map_data_3";
    public static final String COLUMN_MAP_DATA_4 = "map_data_4";
    public static final String COLUMN_MAP_DATA_5 = "map_data_5";
    public static final String COLUMN_MAP_DATA_6 = "map_data_6";
    public static final String COLUMN_MAP_DATA_7 = "map_data_7";
    public static final String COLUMN_MAP_DATA_8 = "map_data_8";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_AVATAR_RES_ID + " INTEGER, " +
                COLUMN_COMPLETION_PERCENTAGES + " TEXT, " +
                COLUMN_MAP_DATA_1 + " TEXT, " +
                COLUMN_MAP_DATA_2 + " TEXT, " +
                COLUMN_MAP_DATA_3 + " TEXT, " +
                COLUMN_MAP_DATA_4 + " TEXT, " +
                COLUMN_MAP_DATA_5 + " TEXT, " +
                COLUMN_MAP_DATA_6 + " TEXT, " +
                COLUMN_MAP_DATA_7 + " TEXT, " +
                COLUMN_MAP_DATA_8 + " TEXT)";
        db.execSQL(createTableQuery);

        // 插入测试数据
        insertUserData(db, "旺", "123", R.drawable.all, new float[]{0, 0, 0, 0, 0, 0, 0, 0}, new int[][]{
                {
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0
                },
                {
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
                }
        });

        insertUserData(db, "公共画板", "184753881ucbiuiqwuqhcuiqwdhs", R.drawable.flag, new float[]{0, 0, 0, 0, 0, 0, 0, 0}, new int[][]{
                {
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0
                },
                {
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,
                },
                {
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0
                },
                {
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
                }
        });

        // 如果你有更多的用户数据要插入，可以继续在这里添加插入操作
    }



    public void insertUserData(SQLiteDatabase db, String username, String password,
                               int avatarResId, float[] completionPercentages, int[][] mapData) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_AVATAR_RES_ID, avatarResId);
        values.put(COLUMN_COMPLETION_PERCENTAGES, floatArrayToString(completionPercentages));
        values.put(COLUMN_MAP_DATA_1, intArrayToString(mapData[0]));
        values.put(COLUMN_MAP_DATA_2, intArrayToString(mapData[1]));
        values.put(COLUMN_MAP_DATA_3, intArrayToString(mapData[2]));
        values.put(COLUMN_MAP_DATA_4, intArrayToString(mapData[3]));
        values.put(COLUMN_MAP_DATA_5, intArrayToString(mapData[4]));
        values.put(COLUMN_MAP_DATA_6, intArrayToString(mapData[5]));
        values.put(COLUMN_MAP_DATA_7, intArrayToString(mapData[6]));
        values.put(COLUMN_MAP_DATA_8, intArrayToString(mapData[7]));
        db.insert(TABLE_NAME, null, values);
    }

    private String floatArrayToString(float[] array) {
        StringBuilder builder = new StringBuilder();
        for (float value : array) {
            builder.append(value).append(",");
        }
        builder.deleteCharAt(builder.length() - 1); // 删除最后一个逗号
        return builder.toString();
    }

    private String intArrayToString(int[] array) {
        StringBuilder builder = new StringBuilder();
        for (int value : array) {
            builder.append(value).append(",");
        }
        builder.deleteCharAt(builder.length() - 1); // 删除最后一个逗号
        return builder.toString();
    }


    public void updateUserLevelData(String username, int level, float completionPercentage, int[] mapData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update completion percentages
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_COMPLETION_PERCENTAGES}, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String completionPercentagesStr = cursor.getString(0);
            float[] completionPercentages = convertStringToFloatArray(completionPercentagesStr);
            completionPercentages[level - 1] = completionPercentage;
            values.put(COLUMN_COMPLETION_PERCENTAGES, floatArrayToString(completionPercentages));
            cursor.close();
        }

        // Update map data for the specific level
        switch (level) {
            case 1:
                values.put(COLUMN_MAP_DATA_1, intArrayToString(mapData));
                break;
            case 2:
                values.put(COLUMN_MAP_DATA_2, intArrayToString(mapData));
                break;
            case 3:
                values.put(COLUMN_MAP_DATA_3, intArrayToString(mapData));
                break;
            case 4:
                values.put(COLUMN_MAP_DATA_4, intArrayToString(mapData));
                break;
            case 5:
                values.put(COLUMN_MAP_DATA_5, intArrayToString(mapData));
                break;
            case 6:
                values.put(COLUMN_MAP_DATA_6, intArrayToString(mapData));
                break;
            case 7:
                values.put(COLUMN_MAP_DATA_7, intArrayToString(mapData));
                break;
            case 8:
                values.put(COLUMN_MAP_DATA_8, intArrayToString(mapData));
                break;
        }

        db.update(TABLE_NAME, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
    }



    private float[] convertStringToFloatArray(String str) {
        if (str == null || str.isEmpty()) {
            return new float[0];
        }
        String[] parts = str.split(",");
        float[] array = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Float.parseFloat(parts[i]);
        }
        return array;
    }

    private int[] convertStringToIntArray(String str) {
        if (str == null || str.isEmpty()) {
            return new int[0];
        }
        String[] parts = str.split(",");
        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Integer.parseInt(parts[i]);
        }
        return array;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 检查用户名是否存在
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // 添加新用户
    public boolean addUser(String username, String password,
                           int avatarResId, float[] completionPercentages, int[][] mapData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_AVATAR_RES_ID, avatarResId);
        values.put(COLUMN_COMPLETION_PERCENTAGES, floatArrayToString(completionPercentages));
        values.put(COLUMN_MAP_DATA_1, intArrayToString(mapData[0]));
        values.put(COLUMN_MAP_DATA_2, intArrayToString(mapData[1]));
        values.put(COLUMN_MAP_DATA_3, intArrayToString(mapData[2]));
        values.put(COLUMN_MAP_DATA_4, intArrayToString(mapData[3]));
        values.put(COLUMN_MAP_DATA_5, intArrayToString(mapData[4]));
        values.put(COLUMN_MAP_DATA_6, intArrayToString(mapData[5]));
        values.put(COLUMN_MAP_DATA_7, intArrayToString(mapData[6]));
        values.put(COLUMN_MAP_DATA_8, intArrayToString(mapData[7]));

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }
}