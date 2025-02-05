package com.example.first;

import static com.example.first.DatabaseHelper.COLUMN_USERNAME;
import static com.example.first.DatabaseHelper.TABLE_NAME;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.first.ui.LevelSelectionActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class PixBoardActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private int[] clickCount; // 记录每个格子的点击次数
    int hang = 23;
    int lie = 15;
    int tu;

    private DatabaseHelper databaseHelper;

    int[] greycount;

    int[] ditu = {
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
    };

    String username;
    boolean screen;
    private MediaPlayer mediaPlayer;
    private MediaPlayer click;

    private Set<TextView> touchedTextViews = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pix_board);

        mediaPlayer = MediaPlayer.create(this,R.raw.hh3);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        //username="公共画板";

        gridLayout = findViewById(R.id.gridLayout);
        clickCount = new int[hang * lie]; // 初始化点击次数数组

        // 设置GridLayout为4*4，并居中显示
        chushihua(username);
        gridLayout.setColumnCount(lie);
        gridLayout.setRowCount(hang);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        gridLayout.setRowOrderPreserved(false);
        gridLayout.setColumnOrderPreserved(false);

        greycount = new int[hang * lie];
        // 循环创建4*4的格子
        int width = getResources().getDisplayMetrics().widthPixels / lie; // 计算每个格子的宽度，假设屏幕宽度平均分配给每个格子
        for (int i = 0; i < hang * lie; i++) {
            TextView textView = createTextView(i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = width;
            params.height = width;
            textView.setLayoutParams(params); // 设置每个格子的宽高相等
            gridLayout.addView(textView);
        }


        for (int i = 0; i < hang * lie; i++) {
            clickCount[i] = ditu[i];
        }

        // 根据clickCount的值设置格子背景颜色
        for (int i = 0; i < hang * lie; i++) {
            TextView textView = (TextView) gridLayout.getChildAt(i);
            switch (clickCount[i]) {
                case 0:
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setTextColor(Color.BLACK);
                    break;
                case 1:
                    textView.setBackgroundColor(Color.BLACK);
                    textView.setTextColor(Color.WHITE);
                    break;
                case 2:
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setTextColor(Color.BLACK);
                    break;
                default:
                    textView.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
            Drawable[] layers = new Drawable[2];

            // 第一个层是背景
            layers[0] = textView.getBackground();

            // 第二个层是边框
            ShapeDrawable border = new ShapeDrawable(new RectShape());
            border.getPaint().setColor(Color.DKGRAY);
            border.getPaint().setStyle(Paint.Style.STROKE);
            border.getPaint().setStrokeWidth(2);
            layers[1] = border;

            LayerDrawable layerDrawable = new LayerDrawable(layers);

            // 设置 LayerDrawable 为视图的背景
            textView.setBackground(layerDrawable);
        }

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回到LevelSelectionActivity
                writesql();
                //Toast.makeText(PixBoardActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 获取返回按钮并设置点击事件
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PixBoardActivity.this);
                builder.setMessage("确定返回吗？\n所有未保存记录都将丢失！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        });
        Button deleteButton = findViewById(R.id.button_redo);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PixBoardActivity.this);
                builder.setMessage("确定清空吗？\n地图将恢复为初始未涂色状态！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<hang * lie;i++) {
                            clickCount[i]=0;
                        }
                        for (int i = 0; i < gridLayout.getChildCount(); i++) {
                            View child = gridLayout.getChildAt(i);
                            if (child instanceof TextView) {
                                TextView textView = (TextView) child;
                                textView.setBackgroundColor(Color.WHITE);
                                textView.setTextColor(Color.BLACK);
                                Drawable[] layers = new Drawable[2];

                                // 第一个层是背景
                                layers[0] = textView.getBackground();

                                // 第二个层是边框
                                ShapeDrawable border = new ShapeDrawable(new RectShape());
                                border.getPaint().setColor(Color.DKGRAY);
                                border.getPaint().setStyle(Paint.Style.STROKE);
                                border.getPaint().setStrokeWidth(2);
                                layers[1] = border;

                                LayerDrawable layerDrawable = new LayerDrawable(layers);

                                // 设置 LayerDrawable 为视图的背景
                                textView.setBackground(layerDrawable);
                            }
                        }
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
        });
    }

    @Override
    public void onBackPressed() {//返回键触发
        AlertDialog.Builder builder = new AlertDialog.Builder(PixBoardActivity.this);
        builder.setMessage("确定返回吗？\n所有未保存记录都将丢失！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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


    @SuppressLint("ClickableViewAccessibility")
    private TextView createTextView(final int index) {
        final TextView textView = new TextView(this);
        textView.setTag(index);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(index % lie, 1f);
        params.rowSpec = GridLayout.spec(index / lie, 1f);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);

        // 设置黑色边框
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.BLACK);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(2);
        textView.setBackground(shape);

        textView.setOnTouchListener(new View.OnTouchListener() {
            private final Handler longPressHandler = new Handler(Looper.getMainLooper());
            private boolean isLongPressTriggered = false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!screen)
                    return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        playClickSound();
                        isLongPressTriggered = false;
                        touchedTextViews.clear(); // 清空touchedTextViews列表
                        handleTouch(textView, event); // 处理按下事件
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isLongPressTriggered) {
                            handleTouch(textView, event); // 只有在非长按情况下处理滑动事件
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        longPressHandler.removeCallbacksAndMessages(null);
                        if (!isLongPressTriggered) {
                            // 处理触摸抬起事件，遍历touchedTextViews设置颜色
                            for (TextView touchedTextView : touchedTextViews) {
                                setNextBackgroundColor(touchedTextView, gridLayout.indexOfChild(touchedTextView));
                            }
                            touchedTextViews.clear();
                        }
                        break;
                }
                return true;
            }
        });
        return textView;
    }

    private void setNextBackgroundColor(TextView textView, int index) {
        int count = clickCount[index];
        int bgcolor;
        int txcolor;//背景颜色和字体颜色

        switch (count % 3) {
            case 0:
                bgcolor = Color.BLACK;
                txcolor = Color.WHITE;
                break;
            case 1:
                bgcolor = Color.GRAY;
                txcolor = Color.BLACK;
                break;
            case 2:
                bgcolor = Color.WHITE;
                txcolor = Color.BLACK;
                break;
            default:
                bgcolor = Color.BLACK;
                txcolor = Color.WHITE;
                break;
        }
        //动画
        ObjectAnimator bgAnimator = ObjectAnimator.ofArgb(textView,"backgroundColor",Color.MAGENTA,bgcolor);
        bgAnimator.setDuration(600);
        ObjectAnimator txAnimator = ObjectAnimator.ofArgb(textView,"textColor",Color.MAGENTA,txcolor);
        txAnimator.setDuration(600);
        //0.6秒
        @SuppressLint("Recycle") AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(bgAnimator).with(txAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 设置背景颜色后再设置边框
                Drawable[] layers = new Drawable[2];

                // 第一个层是背景
                layers[0] = textView.getBackground();

                // 第二个层是边框
                ShapeDrawable border = new ShapeDrawable(new RectShape());
                border.getPaint().setColor(Color.DKGRAY);
                border.getPaint().setStyle(Paint.Style.STROKE);
                border.getPaint().setStrokeWidth(2);
                layers[1] = border;

                LayerDrawable layerDrawable = new LayerDrawable(layers);

                // 设置 LayerDrawable 为视图的背景
                textView.setBackground(layerDrawable);
            }
        });
        // 启动动画
        animatorSet.start();


        clickCount[index]=(clickCount[index]+1)%3;
    }

    private void handleTouch(TextView textView, MotionEvent event) {
//        textView.clearAnimation();;//清除所有动画效果
        // 获取手指所在的坐标
        float x = event.getRawX();
        float y = event.getRawY();

        // 获取当前触摸到的格子
        TextView touchedTextView = findTouchedTextView(x, y);

        // 设置触摸到的格子颜色为红色
        if (touchedTextView != null) {
            touchedTextView.setBackgroundColor(Color.MAGENTA);
            touchedTextViews.add(touchedTextView);
        }
    }
    private TextView findTouchedTextView(float x, float y) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof TextView) {
                int[] location = new int[2];
                child.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + child.getWidth();
                int bottom = top + child.getHeight();
                if (x >= left && x <= right && y >= top && y <= bottom) {
                    return (TextView) child;
                }
            }
        }
        return null;
    }

    private void playClickSound() {//播放点击音效
        // 释放上一个MediaPlayer实例（如果有的话）
        if (click != null) {
            if (click.isPlaying()) {
                click.stop();
            }
            click.release();
            click = null;
        }

        // 创建一个新的MediaPlayer实例并播放音效
        click = MediaPlayer.create(this, R.raw.click);
        click.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 音效播放完毕后释放资源
                mp.release();
                click = null;
            }
        });
        click.start();
    }

    private void writesql() {
        float lv;
        int sum = hang * lie;
        int cot = 0;

        // 输出 clickCount 数组的值
        for (int i = 0; i < hang * lie; i++) {
            Log.d("MyActivity", "嗡嗡嗡clickCount[" + i + "] = " + clickCount[i]);
            // 如果需要对 clickCount 数组进行操作，可以在这里添加代码
        }

        // 强制转换其中一个操作数为浮点数类型
        for (int i = 0; i < hang * lie; i++) {
            // 这里应该有操作，你可能需要根据具体的逻辑来填充这里的代码
        }

        lv = (float) cot / sum;

        // 使用 BigDecimal 来保留三位小数
        BigDecimal bd = new BigDecimal(Float.toString(lv));
        bd = bd.setScale(3, RoundingMode.HALF_UP);

        Log.d("MyActivity", "Lv: " + bd.toString());
        int[] mapData = new int[hang * lie];
        System.arraycopy(clickCount, 0, mapData, 0, hang * lie);

        for (int i = 0; i < hang * lie; i++) {
            Log.d("MyActivity", "嗡嗡嗡mapData[" + i + "] = " + mapData[i]);
            // 如果需要对 clickCount 数组进行操作，可以在这里添加代码
        }
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.updateUserLevelData("公共画板", 8, bd.floatValue(), mapData);
        Toast.makeText(PixBoardActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void chushihua(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                String selection = COLUMN_USERNAME + " = ?";
                String[] selectionArgs = {"公共画板"};
                Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                boolean hasData = false;
                if (cursor != null && cursor.moveToFirst()) {
                    // 获取用户数据



                    String mapData8 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAP_DATA_8));

                    ditu = parseMapData(mapData8);


                    hasData = true;
                }

                if (cursor != null) {
                    cursor.close();
                }
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

        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchMusicControl = findViewById(R.id.switch_music_control);
        // 在进入Activity后开始播放音乐
        if (switchMusicControl.isChecked() && mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int pointerCount = ev.getPointerCount();
        if (pointerCount > 1)// 忽略多指触摸事件
            screen = false;
        else
            screen = true;
        return super.dispatchTouchEvent(ev);
    }
}