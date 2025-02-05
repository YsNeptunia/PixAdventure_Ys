package com.example.first.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.first.Constants;
import com.example.first.DatabaseHelper;
import com.example.first.InfoAboutUs;
import com.example.first.MainActivity2;
import com.example.first.PixBoardActivity;
import com.example.first.R;
import com.example.first.RankActivity2;
import com.example.first.RegisterActivity2;
import com.example.first.StoryActivity;
import com.example.first.TutorialPagerAdapter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LevelSelectionActivity2 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private DatabaseHelper databaseHelper;

    private static final int REQUEST_CODE_GAME = 1;
    private List<Integer> imageResIds = Arrays.asList(
            R.drawable.tt1,
            R.drawable.tt2,
            R.drawable.tt3,
            R.drawable.tt4,
            R.drawable.tt5,
            R.drawable.tt6
    );

    private List<String> descriptions = Arrays.asList(
            "每个关卡都有一个谜面。你需要观察谜面上的数字进行填色，最终填满正确的颜色完成关卡\n（滑动查看下一页）\n<1/6>",
            "每个方格中的数字代表它周围共需要填涂多少个黑色色块（手指点击拖动填上黑色，再次点击填上灰色）\n<2/6>",
            "当方块内的数字为9时，即代表它周围的所有方块都要涂上黑色（包括其自身）。同理数字为0时周围全为灰色。\n<3/6>",
            "上一步的填色是你下一步的线索，这是你解谜的关键。\n<4/6>",
            "仔细观察，继续填涂黑色和灰色...\n<5/6>",
            "直到关卡被全部正确完成。\n<6/6>"
    );
    int[] mapData1Array;
    int[] mapData2Array;
    int[] mapData3Array;
    int[] mapData4Array;
    int[] mapData5Array;
    int[] mapData6Array;
    int[] mapData7Array;
    int[] mapData8Array;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection2); // 对应你的布局文件名

        databaseHelper = new DatabaseHelper(this);
        Intent intent2 = getIntent();
        username = intent2.getStringExtra("username");
        int avatarResId = intent2.getIntExtra("avatarResId", 0);
        String completionPercentages = intent2.getStringExtra("completionPercentages");

        TextView textView_user = findViewById(R.id.username);
        textView_user.setText(username);

// 接收八个关卡的地图数据
        String mapData1 = intent2.getStringExtra("mapData1");
        String mapData2 = intent2.getStringExtra("mapData2");
        String mapData3 = intent2.getStringExtra("mapData3");
        String mapData4 = intent2.getStringExtra("mapData4");
        String mapData5 = intent2.getStringExtra("mapData5");
        String mapData6 = intent2.getStringExtra("mapData6");
        String mapData7 = intent2.getStringExtra("mapData7");
        String mapData8 = intent2.getStringExtra("mapData8");


        ImageView imageView2 = findViewById(R.id.button_more);

        // 设置 ImageView 的资源为 avatarResId
        if (imageView2 != null && avatarResId != 0) {
            imageView2.setImageResource(avatarResId);
        } else {
            Log.e("YourActivity", "ImageView not found or avatarResId is invalid!");
        }
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            // 找到头部视图
            View headerView = navigationView.getHeaderView(0);

            // 找到 TextView 对象
            TextView textView = headerView.findViewById(R.id.textView);

            // 检查 TextView 是否为 null
            if (textView != null && username != null) {
                // 设置 TextView 的文本为 username
                textView.setText(username);
            } else {
                // 打印日志或处理异常
                Log.e("YourActivity", "TextView not found or username is null!");
            }

            ImageView imageView = headerView.findViewById(R.id.imageView_user_avatar);

            // 设置 ImageView 的资源为 avatarResId
            if (imageView != null && avatarResId != 0) {
                imageView.setImageResource(avatarResId);
            } else {
                Log.e("YourActivity", "ImageView not found or avatarResId is invalid!");
            }
        } else {
            // 打印日志或处理异常
            Log.e("YourActivity", "NavigationView not found!");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_home){
                    drawer.closeDrawer( GravityCompat.START );
                }
                if (id == R.id.nav_story) {
                    Intent intent = new Intent(LevelSelectionActivity2.this, StoryActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer( GravityCompat.START );
                    return true;
                }
                if (id == R.id.nav_draw) {
                    Intent intent = new Intent(LevelSelectionActivity2.this, PixBoardActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer( GravityCompat.START );
                    return true;
                }
                if(id == R.id.nav_new){
                    weeklynew();
                    drawer.closeDrawer( GravityCompat.START );
                    return true;
                }
                if (id == R.id.nav_rank) {
                    // 点击了排行榜菜单项，启动RankActivity
                    Intent intent = new Intent(LevelSelectionActivity2.this, RankActivity2.class);
                    startActivity(intent);
                    drawer.closeDrawer( GravityCompat.START );
                    return true;
                }
                if (id == R.id.nav_info){
                    Intent intent = new Intent(LevelSelectionActivity2.this, InfoAboutUs.class);
                    startActivity(intent);
                    drawer.closeDrawer( GravityCompat.START );
                    return true;
                }
                // 其他菜单项的处理逻辑
                return false;
            }
        });


// 分割字符串


// 分割字符串
        String[] percentagesArray = completionPercentages.split(",");

// 如果你只想要第一个值，可以直接取出第一个元素
        String completionPercentage1Str = percentagesArray[0];

// 将字符串转换为整数
        // int completionPercentage1 = Integer.parseInt(completionPercentage1Str);
        mediaPlayer = MediaPlayer.create(this,R.raw.hh);
        mediaPlayer.setLooping(true);

        ImageButton refreshButton = findViewById(R.id.button_refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示 "正在刷新中" 的提示
                Toast.makeText(LevelSelectionActivity2.this, "正在刷新中", Toast.LENGTH_SHORT).show();

                // 模拟刷新操作
                genxing(username);
            }
        });

        ImageButton buttonmore = findViewById(R.id.button_more);
        buttonmore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_story).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_draw).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_rank).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_new).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_info).setChecked(false);
                drawer.openDrawer( GravityCompat.START );
            }
        });


        findViewById(R.id.button_tutorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTutorialDialog();
            }
        });
        Switch switchMusicControl = findViewById(R.id.switch_music_control);
        switchMusicControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaPlayer.start();
                    // 处理音乐播放
                } else {
                    // 处理音乐暂停
                    mediaPlayer.pause();
                }
            }
        });

        // 添加难度一到难度八的按钮
        Button button1 = findViewById(R.id.button1);


        // 设置完成率文本和背景颜色

        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        TextView completionRate1 = findViewById(R.id.textView_completion_rate_1);
        TextView completionRate2 = findViewById(R.id.textView_completion_rate_2);
        TextView completionRate3 = findViewById(R.id.textView_completion_rate_3);
        TextView completionRate4 = findViewById(R.id.textView_completion_rate_4);
        TextView completionRate5 = findViewById(R.id.textView_completion_rate_5);
        TextView completionRate6 = findViewById(R.id.textView_completion_rate_6);
        TextView completionRate7 = findViewById(R.id.textView_completion_rate_7);
        TextView completionRate8 = findViewById(R.id.textView_completion_rate_8);

        String[] completionArray = completionPercentages.split(",");



        int completionPercentage1 = (int) (Double.parseDouble(completionArray[0]) * 100);
        //int completionPercentage1 = 80; // 示例完成率
        completionRate1.setText(String.valueOf(completionPercentage1));
        setCompletionRateColor(completionRate1, completionPercentage1);
        int completionPercentage2 = (int) (Double.parseDouble(completionArray[1]) * 100); // 示例完成率
        completionRate2.setText(String.valueOf(completionPercentage2));
        setCompletionRateColor(completionRate2, completionPercentage2);
        int completionPercentage3 = (int) (Double.parseDouble(completionArray[2]) * 100); // 示例完成率
        completionRate3.setText(String.valueOf(completionPercentage3));
        setCompletionRateColor(completionRate3, completionPercentage3);
        int completionPercentage4 = (int) (Double.parseDouble(completionArray[3]) * 100); // 示例完成率
        completionRate4.setText(String.valueOf(completionPercentage4));
        setCompletionRateColor(completionRate4, completionPercentage4);
        int completionPercentage5 = (int) (Double.parseDouble(completionArray[4]) * 100); // 示例完成率
        completionRate5.setText(String.valueOf(completionPercentage5));
        setCompletionRateColor(completionRate5, completionPercentage5);
        int completionPercentage6 = (int) (Double.parseDouble(completionArray[5]) * 100); // 示例完成率
        completionRate6.setText(String.valueOf(completionPercentage6));
        setCompletionRateColor(completionRate6, completionPercentage6);
        int completionPercentage7 = (int) (Double.parseDouble(completionArray[6]) * 100); // 示例完成率
        completionRate7.setText(String.valueOf(completionPercentage7));
        setCompletionRateColor(completionRate7, completionPercentage7);
        int completionPercentage8 = (int) (Double.parseDouble(completionArray[7]) * 100); // 示例完成率
        completionRate8.setText(String.valueOf(completionPercentage8));
        setCompletionRateColor(completionRate8, completionPercentage8);
        // ... 其他按钮


        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (username.equals("离线账号")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LevelSelectionActivity2.this);
//                    builder.setMessage("确定要返回模式选择界面吗？");
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // 执行退出登录的逻辑，例如跳转到登录界面
//                            Intent intent = new Intent(LevelSelectionActivity2.this, ConnectionActivity.class);
//                            startActivity(intent);
//                            finish(); // 结束当前Activity
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss(); // 取消对话框
//                        }
//                    });
//                    builder.show();
//                }
//                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LevelSelectionActivity2.this);
                    builder.setMessage("确定要返回登录界面吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 执行退出登录的逻辑，例如跳转到登录界面
//                            Intent intent = new Intent(LevelSelectionActivity2.this, LoginActivity2.class);
//                            startActivity(intent);
                            finish(); // 结束当前Activity
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // 取消对话框
                        }
                    });
                    builder.show();
                }
//            }
        });

        mapData1Array = parseMapData(mapData1);
        mapData2Array = parseMapData(mapData2);
        mapData3Array = parseMapData(mapData3);
        mapData4Array = parseMapData(mapData4);
        mapData5Array = parseMapData(mapData5);
        mapData6Array = parseMapData(mapData6);
        mapData7Array = parseMapData(mapData7);
        mapData8Array = parseMapData(mapData8);

// 打印数组内容
        //Log.d("MapDataArray", "mapData1Array: " + Arrays.toString(mapData1Array));
        // 设置按钮点击事件


        button1.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=1;
            int hang = 5;
            int lie = 5;
            int[] daan = {
                    2, 1, 1, 2, 2,
                    1, 1, 1, 2, 1,
                    2, 2, 1, 1, 1,
                    2, 2, 1, 1, 1,
                    2, 2, 2, 1, 2
            };
            int[] mimian = {
                    10, 5, 4, 10, 10,
                    10, 10, 10, 10, 3,
                    10, 5, 6, 10, 5,
                    0, 2, 5, 10, 5,
                    10,10,10,10,10
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData1Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=2;
            int hang = 8;
            int lie = 5;
            int[] daan = {
                    1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,1,2,2,2,1,2,2,2,2,2,2,1,1,1,2,2,2,2,2,2,1,1,1,1,1
            };
            int[] mimian = {
                    10,6,10,10,4,4,10, 10, 6,10,10,10,10, 10, 10, 1,10,0,10,1, 10, 10,3,10,10,10,2,10, 2,10,10,10,10,10,10,10,3,10,10,2
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData2Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);

            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button3.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=3;
            int hang = 11;
            int lie = 7;
            int[] daan = {
                    2,2,2,2,1,2,1,2,2,2,2,1,1,1,2,2,2,1,2,1,1,1,2,2,1,1,1,1,2,1,2,1,1,1,1,2,1,2,2,2,1,1,1,2,2,2,1,1,1,1,2,2,2,1,1,1,1,2,2,1,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,1
            };
            int[] mimian = {
                    10,10,0,10,3,10,10,0,10,10,3,10,10,5,10,10,2,10,10,10,6,2,10,10,10,8,8,10,10,10,4,4,10,8,10,10,3,3,10,10,10,10,10,10,10,10,10,8,6,3,10,10,10,10,9,10,4,10,3,5,10,10,10,10,10,10,10,9,10,6,10,4,5,10,10,10,10
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData3Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            // finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button4.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=4;
            int hang = 11;
            int lie = 7;
            int[] daan = {
                    1,1,1,1,2,2,2,1,1,2,1,2,2,2,1,1,1,1,1,2,2,1,1,1,1,1,2,2,1,2,2,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1,2,2,1,2,1,1,1,1,1,1,1,1,1,1,1,2,1,1,1,2,1,2,1,2,2,2,1,2
            };
            int[] mimian = {
                    4,10,5,10,10,0,10,10,10,10,10,10,10,10,6,8,10,10,10,10,0,5,10,7,7,5,10,10,10,10,10,10,3,10,10,10,10,10,4,4,3,10,0,10,10,10,5,10,10,10,10,6,10,10,9,10,10,6,6,8,7,10,5,10,10,6,10,10,10,10,10,3,10,3,3,3,10
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData4Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button5.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=5;
            int hang = 12;
            int lie = 8;
            int[] daan = {
                    2,2,2,1,2,2,2,2,2,2,1,1,1,2,2,2,2,1,1,1,1,1,2,2,2,2,1,2,1,2,2,2,2,1,1,1,1,1,2,2,2,2,1,2,1,2,2,2,2,1,1,1,1,1,2,2,1,1,1,1,1,1,1,2,2,1,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,1,2,1,2,1,2,2,1,1,1,1,1,1,1,1
            };
            int[] mimian = {
                    10,10,10,10,10,10,0,10,10,3,6,7,10,10,10,10,10,10,10,8,10,10,10,0,10,10,7,10,7,10,10,10,10,4,10,10,5,4,1,10,10,10,7,10,7,10,10,0,10,10,7,8,7,6,3,10,4,10,7,10,10,6,10,10,10,5,10,10,10,10,10,10,10,3,10,1,10,3,3,0,10,10,6,10,10,10,10,10,10,4,10,10,5,10,4,10
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData5Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button6.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=6;
            int hang = 15;
            int lie = 10;
            int[] daan = {
                    1,2,2,2,2,2,2,2,2,1,  2,1,2,1,1,2,2,2,1,2,  2,2,2,2,1,1,2,2,2,2,  1,2,2,2,1,1,1,2,2,1,  1,1,2,1,1,1,1,1,2,2,  2,1,1,1,1,1,1,2,1,2,  1,1,2,1,2,2,2,1,2,2,  1,2,2,1,1,1,1,2,2,1,  2,2,1,1,1,2,2,2,1,2,  2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,1,1,2,1,2,  2,1,2,2,1,2,2,1,2,2,  2,2,1,2,2,1,1,2,1,2,  1,2,1,1,2,2,2,2,2,2,  1,1,2,1,1,1,1,1,1,1
            };
            int[] mimian = {
                    10,10,2,10,10,10,0,1,10,2,  10,2,10,3,10,10,10,10,10,10,  2,10,2,10,6,10,10,2,10,2,  3,10,10,4,10,8,10,3,10,10,  4,10,5,6,10,9,10,5,3,10,  10,10,7,10,7,6,6,10,3,10,  4,10,10,6,10,6,5,4,10,10,  3,10,10,6,6,10,3,3,10,2,  10,10,3,10,10,10,2,2,10,10,  0,10,10,3,10,3,2,3,10,2,  10,10,1,1,10,3,3,10,2,1,  1,2,10,2,10,5,10,5,10,10,  10,10,4,4,10,3,3,10,10,1,  10,10,10,10,10,10,10,10,10,10,  3,10,10,4,4,10,3,10,10,2
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData6Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button7.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=7;
            int hang = 15;
            int lie = 10;
            int[] daan = {
                    2,2,1,1,1,1,2,2,2,2,  2,1,2,1,1,1,2,2,2,2,  2,1,2,2,1,2,2,2,2,2,  2,2,1,2,1,1,1,1,2,2,  2,2,1,1,2,1,2,2,1,2,  1,1,1,2,2,2,1,1,2,1,  1,2,2,1,2,2,1,2,1,1,  2,1,1,1,2,2,1,2,2,1,  2,2,2,1,1,1,1,2,2,2,  2,2,1,1,1,1,1,1,1,1,  2,1,1,1,1,1,1,2,2,1,  2,1,2,1,2,2,1,1,1,1,  2,1,2,1,2,2,2,2,1,1,  2,1,1,2,2,2,2,2,2,2,  1,1,1,2,2,2,2,2,2,2
            };
            int[] mimian = {
                    10,10,10,5,6,10,10,10,0,10,  10,3,10,10,10,10,2,10,10,10,  10,3,4,5,6,6,10,10,10,0,  10,10,10,10,5,10,10,3,10,10,  10,10,5,5,4,10,10,10,10,10,  3,5,5,4,10,10,4,5,10,4,  4,10,10,4,10,10,4,10,10,4,  10,10,5,10,5,5,10,4,3,3,  10,3,6,10,7,7,10,5,10,10,  10,10,6,10,9,9,10,10,10,3,  10,4,10,7,10,10,7,10,10,5,  10,10,7,10,5,4,10,5,10,5,  3,10,10,3,10,10,10,4,10,10,  10,10,10,10,10,10,0,10,10,10,  10,5,4,10,0,10,10,10,10,0
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData7Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });

        button8.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            int tu=8;
            int hang = 23;
            int lie = 15;
            int[] daan = {
                    2,1,2,2,1,2,2,1,2,1,2,2,2,2,1,  1,2,2,1,1,1,2,2,1,2,2,2,1,1,2,  2,2,1,2,2,1,2,2,2,2,2,1,1,2,1,  2,1,1,2,1,2,1,2,2,2,1,1,2,1,2,  2,1,2,2,2,1,1,1,2,2,1,2,2,2,1,  2,1,2,1,1,2,2,1,2,2,1,2,1,2,2,  1,2,2,2,2,2,1,2,1,2,1,1,1,1,1,  1,2,1,1,1,1,2,2,1,2,1,1,2,1,1,  1,1,2,2,2,2,2,1,1,2,2,1,1,2,1,  1,1,1,1,1,1,1,1,1,2,2,1,1,2,1,  1,1,1,1,1,1,1,2,1,2,2,2,1,1,1,  2,1,1,1,1,1,2,1,2,2,1,2,2,1,1,  2,1,1,1,1,2,2,1,2,2,1,1,2,1,1,  2,2,1,1,2,2,1,2,2,1,2,1,2,2,1,  2,2,2,1,1,1,2,2,2,1,1,2,1,2,1,  2,1,2,2,1,2,2,2,1,2,1,1,1,1,2,  1,2,1,2,1,2,2,1,1,1,1,1,1,1,1,  2,2,2,2,1,2,2,1,2,2,2,2,2,2,1,  2,2,2,2,1,2,2,1,1,1,1,1,2,1,2,  1,2,2,2,1,2,2,2,1,2,2,2,1,1,2,  2,1,2,2,1,2,2,2,1,1,1,2,1,1,2,  1,2,2,1,1,1,2,2,1,1,2,1,2,1,2,  2,2,1,1,1,1,1,2,2,1,1,1,1,2,2
            };
            int[] mimian = {
                    10,10,2,3,10,10,10,10,10,2,1,1,10,10,2,  2,10,10,4,10,4,3,10,3,10,10,10,4,10,10,  10,4,4,10,5,10,3,2,10,10,3,10,6,10,3,  10,4,10,10,3,10,5,3,10,2,10,5,4,10,3,  10,10,5,10,10,5,5,4,2,10,4,5,3,10,2,  10,3,10,2,10,10,5,10,10,10,10,10,4,10,3,  10,10,10,10,5,10,3,10,3,5,10,7,10,6,4,  4,10,3,3,3,3,10,10,4,5,5,7,10,10,5,  10,10,10,10,6,10,5,6,5,10,4,10,6,10,10,  6,10,7,10,10,6,10,7,10,3,2,5,6,10,10,  10,8,9,10,9,8,10,10,10,10,10,10,5,10,5,  10,10,9,9,10,10,5,10,10,10,3,10,10,7,6,  10,10,8,10,6,4,4,3,3,10,5,10,10,10,10,  1,10,10,10,6,10,10,10,10,4,6,10,10,10,4,  1,10,10,5,10,4,2,10,3,10,10,10,5,10,10,  10,3,10,10,10,10,2,3,10,7,10,8,10,10,10,  10,10,2,10,10,10,10,4,5,10,10,6,6,10,4,  10,2,1,4,3,3,10,10,7,10,6,5,10,10,4,  10,1,0,3,10,3,2,10,5,10,3,10,4,10,10,  2,10,10,10,10,10,10,10,6,7,10,10,6,10,3,  10,3,2,4,10,10,1,3,5,6,4,4,6,10,10,  2,10,10,10,10,6,10,10,5,10,10,10,10,10,10,  10,10,3,5,6,10,10,2,10,10,5,4,10,2,10
            };
            intent.putExtra("hang", hang);
            intent.putExtra("lie", lie);
            intent.putExtra("daan", daan);
            intent.putExtra("mimian", mimian);
            intent.putExtra("mapDataArray", mapData8Array); // 将解析后的数组传递过去
            intent.putExtra("tu", tu);
            intent.putExtra("username", username);
            startActivity(intent);
            //finish(); // 关闭当前的登录界面
            // 处理难度一按钮点击事件，比如跳转到难度一的游戏关卡
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 在进入Activity后开始播放音乐


        NavigationView navigationView = findViewById(R.id.nav_view);

        // 在 onResume 方法中取消 nav_rank 菜单项的选中状态
        MenuItem rankMenuItem = navigationView.getMenu().findItem(R.id.nav_rank);
        rankMenuItem.setChecked(false);
        // 获取当前用户的用户名，这里假设你有方法获取用户名
        // 刷新数据
        genxing2(username);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchMusicControl = findViewById(R.id.switch_music_control);
        // 在进入Activity后开始播放音乐
        if (switchMusicControl.isChecked() && mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        if(username.equals("离线账号"))
        {
            Button logoutButton = findViewById(R.id.button_logout);
            logoutButton.setText("返回");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在暂停Activity时暂停音乐
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时释放MediaPlayer资源
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showTutorialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_tutorial, null);
        builder.setView(dialogLayout);

        TextView textViewDescription = dialogLayout.findViewById(R.id.textView_description);
        ViewPager viewPager = dialogLayout.findViewById(R.id.viewPager_tutorial);

        TutorialPagerAdapter adapter = new TutorialPagerAdapter(this, imageResIds);
        viewPager.setAdapter(adapter);

        textViewDescription.setText(descriptions.get(0)); // Set initial description

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                textViewDescription.setText(descriptions.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        builder.setNegativeButton("关闭", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setCompletionRateColor(TextView textView, int completionPercentage) {
        if (completionPercentage == 100) {
            // 完成率为100%，设置背景颜色为绿色并显示勾号

            int greenColor = getResources().getColor(R.color.greenDark);
            int whiteColor = getResources().getColor(R.color.white);
            int blendedColor = blendColors(greenColor, whiteColor, completionPercentage / 100f);

            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                    new int[]{greenColor, blendedColor});
            drawable.setShape(GradientDrawable.OVAL);
            textView.setBackground(drawable);

            textView.setText("✔");
        } else {
            // 根据完成率动态设置背景颜色
            int greenColor = getResources().getColor(R.color.greenDark);
            int whiteColor = getResources().getColor(R.color.white);
            int blendedColor = blendColors(greenColor, whiteColor, completionPercentage / 100f);

            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                    new int[]{greenColor, blendedColor});
            drawable.setShape(GradientDrawable.OVAL);
            textView.setBackground(drawable);

            textView.setText(String.valueOf(completionPercentage) + "%");
        }
    }

    // 将两种颜色混合的方法
    private int blendColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private int[] parseMapData(String mapData) {
        if (mapData == null || mapData.isEmpty()) {
            return new int[0];
        }
        String[] strArray = mapData.split(",");
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i].trim());
        }
        return intArray;
    }



    private void genxing(final String thisusername) {
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
                                    Toast.makeText(LevelSelectionActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
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
                                        Log.d("Users", "用户名 " + i + ": " + username);
                                        if (thisusername.equals(username)) {
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
                                                //Toast.makeText(LevelSelectionActivity2.this, "刷新成功", Toast.LENGTH_SHORT).show();
                                                refreshData(thisusername, finalAvatarResId, finalCompletionPercentages, finalMapData1,finalMapData2,finalMapData3,finalMapData4,finalMapData5,finalMapData6,finalMapData7,finalMapData8);
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LevelSelectionActivity2.this, "刷新失败", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(LevelSelectionActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LevelSelectionActivity2.this, "刷新失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void genxing2(final String thisusername) {
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
                                    Toast.makeText(LevelSelectionActivity2.this, "服务器连接异常", Toast.LENGTH_SHORT).show();
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
                                        Log.d("Users", "用户名 " + i + ": " + username);
                                        if (thisusername.equals(username)) {
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
                                                //Toast.makeText(LevelSelectionActivity2.this, "刷新成功", Toast.LENGTH_SHORT).show();
                                                refreshData2(thisusername, finalAvatarResId, finalCompletionPercentages, finalMapData1,finalMapData2,finalMapData3,finalMapData4,finalMapData5,finalMapData6,finalMapData7,finalMapData8);
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LevelSelectionActivity2.this, "刷新失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LevelSelectionActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.e("TAG", "请求失败 " + response.code());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("ConnectToServer", "连接失败: " + e.getMessage(), e);
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LevelSelectionActivity2.this, "刷新失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    private void refreshData(final String username, final int avatarResId, final String completionPercentages,
                             final String mapData1, final String mapData2, final String mapData3,
                             final String mapData4, final String mapData5, final String mapData6,
                             final String mapData7, final String mapData8) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean hasData = false;

                    // 获取用户数据
                    TextView completionRate1 = findViewById(R.id.textView_completion_rate_1);
                    TextView completionRate2 = findViewById(R.id.textView_completion_rate_2);
                    TextView completionRate3 = findViewById(R.id.textView_completion_rate_3);
                    TextView completionRate4 = findViewById(R.id.textView_completion_rate_4);
                    TextView completionRate5 = findViewById(R.id.textView_completion_rate_5);
                    TextView completionRate6 = findViewById(R.id.textView_completion_rate_6);
                    TextView completionRate7 = findViewById(R.id.textView_completion_rate_7);
                    TextView completionRate8 = findViewById(R.id.textView_completion_rate_8);

                    mapData1Array = parseMapData(mapData1);
                    mapData2Array = parseMapData(mapData2);
                    mapData3Array = parseMapData(mapData3);
                    mapData4Array = parseMapData(mapData4);
                    mapData5Array = parseMapData(mapData5);
                    mapData6Array = parseMapData(mapData6);
                    mapData7Array = parseMapData(mapData7);
                    mapData8Array = parseMapData(mapData8);
                    String[] completionArray = completionPercentages.split(",");
                    int completionPercentage1 = (int) (Double.parseDouble(completionArray[0]) * 100);
                    //int completionPercentage1 = 80; // 示例完成率

                    completionRate1.setText(String.valueOf(completionPercentage1));
                    setCompletionRateColor(completionRate1, completionPercentage1);
                    int completionPercentage2 = (int) (Double.parseDouble(completionArray[1]) * 100); // 示例完成率
                    completionRate2.setText(String.valueOf(completionPercentage2));
                    setCompletionRateColor(completionRate2, completionPercentage2);
                    int completionPercentage3 = (int) (Double.parseDouble(completionArray[2]) * 100); // 示例完成率
                    completionRate3.setText(String.valueOf(completionPercentage3));
                    setCompletionRateColor(completionRate3, completionPercentage3);
                    int completionPercentage4 = (int) (Double.parseDouble(completionArray[3]) * 100); // 示例完成率
                    completionRate4.setText(String.valueOf(completionPercentage4));
                    setCompletionRateColor(completionRate4, completionPercentage4);
                    int completionPercentage5 = (int) (Double.parseDouble(completionArray[4]) * 100); // 示例完成率
                    completionRate5.setText(String.valueOf(completionPercentage5));
                    setCompletionRateColor(completionRate5, completionPercentage5);
                    int completionPercentage6 = (int) (Double.parseDouble(completionArray[5]) * 100); // 示例完成率
                    completionRate6.setText(String.valueOf(completionPercentage6));
                    setCompletionRateColor(completionRate6, completionPercentage6);
                    int completionPercentage7 = (int) (Double.parseDouble(completionArray[6]) * 100); // 示例完成率
                    completionRate7.setText(String.valueOf(completionPercentage7));
                    setCompletionRateColor(completionRate7, completionPercentage7);
                    int completionPercentage8 = (int) (Double.parseDouble(completionArray[7]) * 100); // 示例完成率
                    completionRate8.setText(String.valueOf(completionPercentage8));
                    setCompletionRateColor(completionRate8, completionPercentage8);
                    hasData = true;

                final boolean dataFetched = hasData;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataFetched) {
                            Toast.makeText(LevelSelectionActivity2.this, "刷新成功", Toast.LENGTH_SHORT).show();

                            // 在这里更新UI或其他逻辑
                            // 例如，更新文本视图、进度条等
                        } else {
                            Toast.makeText(LevelSelectionActivity2.this, "刷新失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
    private void refreshData2(final String username, final int avatarResId, final String completionPercentages,
                             final String mapData1, final String mapData2, final String mapData3,
                             final String mapData4, final String mapData5, final String mapData6,
                             final String mapData7, final String mapData8) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean hasData = false;

                // 获取用户数据
                TextView completionRate1 = findViewById(R.id.textView_completion_rate_1);
                TextView completionRate2 = findViewById(R.id.textView_completion_rate_2);
                TextView completionRate3 = findViewById(R.id.textView_completion_rate_3);
                TextView completionRate4 = findViewById(R.id.textView_completion_rate_4);
                TextView completionRate5 = findViewById(R.id.textView_completion_rate_5);
                TextView completionRate6 = findViewById(R.id.textView_completion_rate_6);
                TextView completionRate7 = findViewById(R.id.textView_completion_rate_7);
                TextView completionRate8 = findViewById(R.id.textView_completion_rate_8);

                mapData1Array = parseMapData(mapData1);
                mapData2Array = parseMapData(mapData2);
                mapData3Array = parseMapData(mapData3);
                mapData4Array = parseMapData(mapData4);
                mapData5Array = parseMapData(mapData5);
                mapData6Array = parseMapData(mapData6);
                mapData7Array = parseMapData(mapData7);
                mapData8Array = parseMapData(mapData8);
                String[] completionArray = completionPercentages.split(",");
                int completionPercentage1 = (int) (Double.parseDouble(completionArray[0]) * 100);
                //int completionPercentage1 = 80; // 示例完成率

                completionRate1.setText(String.valueOf(completionPercentage1));
                setCompletionRateColor(completionRate1, completionPercentage1);
                int completionPercentage2 = (int) (Double.parseDouble(completionArray[1]) * 100); // 示例完成率
                completionRate2.setText(String.valueOf(completionPercentage2));
                setCompletionRateColor(completionRate2, completionPercentage2);
                int completionPercentage3 = (int) (Double.parseDouble(completionArray[2]) * 100); // 示例完成率
                completionRate3.setText(String.valueOf(completionPercentage3));
                setCompletionRateColor(completionRate3, completionPercentage3);
                int completionPercentage4 = (int) (Double.parseDouble(completionArray[3]) * 100); // 示例完成率
                completionRate4.setText(String.valueOf(completionPercentage4));
                setCompletionRateColor(completionRate4, completionPercentage4);
                int completionPercentage5 = (int) (Double.parseDouble(completionArray[4]) * 100); // 示例完成率
                completionRate5.setText(String.valueOf(completionPercentage5));
                setCompletionRateColor(completionRate5, completionPercentage5);
                int completionPercentage6 = (int) (Double.parseDouble(completionArray[5]) * 100); // 示例完成率
                completionRate6.setText(String.valueOf(completionPercentage6));
                setCompletionRateColor(completionRate6, completionPercentage6);
                int completionPercentage7 = (int) (Double.parseDouble(completionArray[6]) * 100); // 示例完成率
                completionRate7.setText(String.valueOf(completionPercentage7));
                setCompletionRateColor(completionRate7, completionPercentage7);
                int completionPercentage8 = (int) (Double.parseDouble(completionArray[7]) * 100); // 示例完成率
                completionRate8.setText(String.valueOf(completionPercentage8));
                setCompletionRateColor(completionRate8, completionPercentage8);
                hasData = true;

                final boolean dataFetched = hasData;

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LevelSelectionActivity2.this);
        builder.setMessage("确定要返回登录界面吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行退出登录的逻辑，例如跳转到登录界面
                finish(); // 结束当前Activity
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 取消对话框
            }
        });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void weeklynew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("每周关卡"); // 设置标题

        // 创建一个布局容器来放置内容
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // 设置内边距

        // 添加一个 TextView 来显示信息
        TextView textView = new TextView(this);
        textView.setText("正在开发中，敬请期待！");
        textView.setPadding(10, 10, 10, 10); // 设置内边距
        layout.addView(textView);

        builder.setView(layout);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮的逻辑

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}