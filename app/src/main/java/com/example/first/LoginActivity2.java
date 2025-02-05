package com.example.first;



import static com.example.first.DatabaseHelper.COLUMN_PASSWORD;
import static com.example.first.DatabaseHelper.COLUMN_USERNAME;
import static com.example.first.DatabaseHelper.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import com.example.first.ui.LevelSelectionActivity2;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity2 extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button exitButton;

    private DatabaseHelper databaseHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity2);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity2.this, ConnectionActivity.class);
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
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "正在登录中", Toast.LENGTH_SHORT).show();
        denlu(username,password);
    }


    public void onRegisterButtonClicked(View view) {
        // 点击注册按钮时跳转到注册页面
        Intent intent = new Intent(this, RegisterActivity2.class);
        startActivity(intent);
    }

    private void denlu(final String thisusername, final String thispassword) {
        // 模拟网络连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder()
                            .url(Constants.USERS_ENDPOINT)
                            .get()
                            .build();

                    client2.newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // 处理请求失败的情况
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 处理请求成功的情况
                            if (response.isSuccessful()) {
                                String responseData = response.body().string();
                                int avatarResId=0;
                                String completionPercentages = null;
                                String mapData1 = null;
                                String mapData2 = null;
                                String mapData3 = null;
                                String mapData4 = null;
                                String mapData5 = null;
                                String mapData6 = null;
                                String mapData7 = null;
                                String mapData8 = null;


                                try {
                                    boolean foundUsername = false;
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject userObject = jsonArray.getJSONObject(i);
                                        String username = userObject.getString("username");
                                        String password = userObject.getString("password");
                                        Log.d("Users", "用户名 " + i + ": " + username);
                                        if (thisusername.equals(username)&&thispassword.equals(password)) {
                                            foundUsername = true;
                                            String avatarResIdStr = userObject.getString("avatarResId");
                                            avatarResId = Integer.parseInt(avatarResIdStr);
                                            String completionPercentageso= userObject.getString("completionPercentages");
                                            completionPercentages = completionPercentageso.replaceAll("[{}]", "");
                                            String mapData1o=userObject.getString("mapData1");
                                            String mapData2o=userObject.getString("mapData2");
                                            String mapData3o=userObject.getString("mapData3");
                                            String mapData4o=userObject.getString("mapData4");
                                            String mapData5o=userObject.getString("mapData5");
                                            String mapData6o=userObject.getString("mapData6");
                                            String mapData7o=userObject.getString("mapData7");
                                            String mapData8o=userObject.getString("mapData8");

// 获取八个关卡的地图数据
                                            mapData1 = mapData1o.replaceAll("[{}]", "");
                                            mapData2 = mapData2o.replaceAll("[{}]", "");
                                            mapData3 = mapData3o.replaceAll("[{}]", "");
                                            mapData4 = mapData4o.replaceAll("[{}]", "");
                                            mapData5 = mapData5o.replaceAll("[{}]", "");
                                            mapData6 = mapData6o.replaceAll("[{}]", "");
                                            mapData7 = mapData7o.replaceAll("[{}]", "");
                                            mapData8 = mapData8o.replaceAll("[{}]", "");
                                            //int avatarResId
                                            break;
                                        }
                                    }
                                    if (foundUsername) {
                                        //connectToServer(thisusername, password, avatarResId);
                                        int finalAvatarResId = avatarResId;
                                        String finalCompletionPercentages = completionPercentages;
                                        String finalMapData1 = mapData1;
                                        String finalMapData2 = mapData2;
                                        String finalMapData3 = mapData3;
                                        String finalMapData4 = mapData4;
                                        String finalMapData5 = mapData5;
                                        String finalMapData6 = mapData6;
                                        String finalMapData7= mapData7;
                                        String finalMapData8 = mapData8;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity2.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                tiaozhuan(thisusername, finalAvatarResId, finalCompletionPercentages, finalMapData1,finalMapData2,finalMapData3,finalMapData4,finalMapData5,finalMapData6,finalMapData7,finalMapData8);
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity2.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("TAG", "请求失败 " + response.code());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("ConnectToServer", "连接失败: " + e.getMessage(), e);
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity2.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void tiaozhuan(final String username, final int avatarResId, final String completionPercentages,
                           final String mapData1, final String mapData2, final String mapData3,
                           final String mapData4, final String mapData5, final String mapData6,
                           final String mapData7, final String mapData8) {
        // 打印所有传递的参数信息
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

        Intent intent = new Intent(this, LevelSelectionActivity2.class);
        intent.putExtra("username", username);
        intent.putExtra("avatarResId", avatarResId);
        intent.putExtra("completionPercentages", completionPercentages);
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
    }
}