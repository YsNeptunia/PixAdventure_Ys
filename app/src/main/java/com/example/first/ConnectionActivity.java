package com.example.first;

import static com.example.first.DatabaseHelper.COLUMN_PASSWORD;
import static com.example.first.DatabaseHelper.COLUMN_USERNAME;
import static com.example.first.DatabaseHelper.TABLE_NAME;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.first.ui.LevelSelectionActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionActivity extends AppCompatActivity {

    private Button connectButton;
    private Button exitButton;

    public DatabaseHelper databaseHelper2;

    private Button singlePlayerButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        connectButton = findViewById(R.id.connectButton);
        exitButton = findViewById(R.id.exitButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectToServer();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }



    private void connectToServer() {
        // 模拟网络连接
        Intent intent = new Intent(ConnectionActivity.this, LoginActivity2.class);
        startActivity(intent);
    }


    public void onClearUsersButtonClick(View view) {
        // 执行清除用户操作的逻辑
        clearUsers();

        // 可选：显示一个Toast提示
        Toast.makeText(this, "正在清除用户", Toast.LENGTH_SHORT).show();
    }

    // 清除用户的逻辑方法
    private void clearUsers() {
        OkHttpClient client = new OkHttpClient();

        // 构建DELETE请求
        Request request = new Request.Builder()
                .url(Constants.USERS_ENDPOINT + "/deleteAll")  // 替换成实际的API端点
                .delete()
                .build();

        // 异步执行DELETE请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnectionActivity.this, "清空用户数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理请求成功的情况
                if (response.isSuccessful()) {
                    // 清空操作成功的逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConnectionActivity.this, "成功清空用户数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // 清空操作失败的逻辑
                    Log.e("TAG", "清空操作失败 " + response.code());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ConnectionActivity.this, "清空用户数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void onsinglePlayerButton(View view) {
        Intent intent = new Intent(ConnectionActivity.this, LoginActivity.class);
        startActivity(intent);
//        finish();
    }
}