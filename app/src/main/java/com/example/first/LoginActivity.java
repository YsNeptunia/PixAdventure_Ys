package com.example.first;



import static com.example.first.DatabaseHelper.COLUMN_PASSWORD;
import static com.example.first.DatabaseHelper.COLUMN_USERNAME;
import static com.example.first.DatabaseHelper.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.first.ui.LevelSelectionActivity;
import com.example.first.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button exitButton;

    private DatabaseHelper databaseHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ConnectionActivity.class);
//                startActivity(intent);
                // 在这里处理退出游戏的逻辑，例如：
                finish(); // 关闭当前Activity
                // 或者调用 System.exit(0); 来退出应用程序
            }
        });

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        databaseHelper = new DatabaseHelper(this);
        ImageView passwordVisibilityImageView = findViewById(R.id.passwordVisibilityImageView);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);

        // 初始设置密码输入框为密码模式
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordVisibilityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换密码输入框的显示与隐藏
                if (isPasswordVisible) {
                    // 如果当前可见，则切换为密码模式
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordVisibilityImageView.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    // 如果当前不可见，则切换为普通文本模式
                    passwordEditText.setTransformationMethod(null);
                    passwordVisibilityImageView.setImageResource(R.drawable.ic_visibility);
                }

                isPasswordVisible = !isPasswordVisible;

                // 将光标移至末尾
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
    }


    public void onLoginButtonClicked(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 查询数据库
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            // 登录成功，获取用户数据
            cursor.moveToFirst();
            int avatarResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVATAR_RES_ID));
            String completionPercentages = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETION_PERCENTAGES));
// 获取八个关卡的地图数据
            String mapData1 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_1));
            String mapData2 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_2));
            String mapData3 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_3));
            String mapData4 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_4));
            String mapData5 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_5));
            String mapData6 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_6));
            String mapData7 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_7));
            String mapData8 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_8));

// 创建Intent传递数据到LevelSelectionActivity

            Log.d("Tiaozhuan", "username: " + username);
            Log.d("Tiaozhuan", "avatarResId: " + avatarResId);
            Log.d("Tiaozhuan", "completionPercentages: " + completionPercentages);
            Log.d("Tiaozhuan", "mapData1: " + mapData1);
            Log.d("Tiaozhuan", "mapData2: " + mapData2);
            Log.d("Tiaozhuan", "mapData3: " + mapData3);
            Log.d("Tiaozhuan", "mapData4: " + mapData4);
            Log.d("Tiaozhuan", "mapData5: " + mapData5);
            Log.d("Tiaozhuan", "mapData6: " + mapData6);
            Log.d("Tiaozhuan", "mapData7: " + mapData7);
            Log.d("Tiaozhuan", "mapData8: " + mapData8);
            Intent intent = new Intent(this, LevelSelectionActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("avatarResId", avatarResId);
            intent.putExtra("completionPercentages", completionPercentages);
// 将八个关卡的地图数据传递到LevelSelectionActivity
            intent.putExtra("mapData1", mapData1);
            intent.putExtra("mapData2", mapData2);
            intent.putExtra("mapData3", mapData3);
            intent.putExtra("mapData4", mapData4);
            intent.putExtra("mapData5", mapData5);
            intent.putExtra("mapData6", mapData6);
            intent.putExtra("mapData7", mapData7);
            intent.putExtra("mapData8", mapData8);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
        } else {
            // 登录失败，显示错误信息给用户
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }


        cursor.close();
        db.close();
    }

    public void onRegisterButtonClicked(View view) {
        // 点击注册按钮时跳转到注册页面
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}