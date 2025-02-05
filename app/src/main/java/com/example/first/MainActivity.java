package com.example.first;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private int[] clickCount; // 记录每个格子的点击次数
    int n=5;
    int hang;
    int lie;
    int tu;
    int[] daan;
    int[] mimian;
    int[] greycount;

    int[] ditu;

    String username;
    private MediaPlayer mediaPlayer;
    private MediaPlayer click;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchErrorDisplay;
    boolean tipsmode;//提示开关
    boolean screen;//是否多指操作
    private Set<TextView> touchedTextViews = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        hang = intent.getIntExtra("hang", 0); // 默认值为 0，可以根据需要修改
        lie = intent.getIntExtra("lie", 0); // 默认值为 0，可以根据需要修改
        daan = intent.getIntArrayExtra("daan");
        mimian = intent.getIntArrayExtra("mimian");
        ditu = intent.getIntArrayExtra("mapDataArray");
        tu = intent.getIntExtra("tu", 1);//后面存储数据有大用
        username = intent.getStringExtra("username");
        gridLayout = findViewById(R.id.gridLayout);
        clickCount = new int[hang * lie]; // 初始化点击次数数组

        // 设置GridLayout为4*4，并居中显示
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

        SetNumbers(); // 在随机的格子上显示0~9的随机数字


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

        mediaPlayer = MediaPlayer.create(this,R.raw.hh2);
        mediaPlayer.setLooping(true);


        if (checkGameCompleted()) {
            showGameCompletedDialog();
        }

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchMusicControl = findViewById(R.id.switch_music_control);
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
        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回到LevelSelectionActivity
                writesql();
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        Button hintButton = findViewById(R.id.button_hint);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHintDialog(); // 点击提示按钮时显示提示对话框
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress", "UseSwitchCompatOrMaterialCode"}) Switch tishiButton = findViewById(R.id.switch_tishi);
        tishiButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    know(); // 点击提示按钮时显示提示对话框
                    tipsmode = true;
                }
                else{
                    @SuppressLint("UseSwitchCompatOrMaterialCode") Switch hintswitch = findViewById(R.id.switch_error_display);
                    Button hintbutton = findViewById(R.id.button_hint);

                    hintswitch.setVisibility(View.INVISIBLE);
                    hintbutton.setVisibility(View.INVISIBLE);

                    switchErrorDisplay.setChecked(false);
                    doSomethingWhenSwitchIsOff();

                    tipsmode = false;
                }
            }
        });
        // 获取返回按钮并设置点击事件
        Button backButton = findViewById(R.id.button_back);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定返回吗？\n所有未保存记录都将丢失！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 执行退出登录的逻辑，例如跳转到登录界面
//                        Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
//                        startActivity(intent);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

        switchErrorDisplay = findViewById(R.id.switch_error_display);

        switchErrorDisplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 执行开启状态下的函数
                    doSomethingWhenSwitchIsOn();
                } else {
                    // 执行关闭状态下的函数
                    doSomethingWhenSwitchIsOff();
                }
            }
        });
    }

    private void doSomethingWhenSwitchIsOn() {
        // Switch打开时执行的函数逻辑
        for (int i = 0; i < hang * lie; i++) {
            if(clickCount[i]==1||clickCount[i]==2)
            {
                if(clickCount[i]!=daan[i])
                {
                    TextView textView = (TextView) gridLayout.getChildAt(i);
                    textView.setBackgroundColor(Color.RED);
                }
            }
        }
    }

    private void doSomethingWhenSwitchIsOff() {
        // Switch关闭时执行的函数逻辑
        for (int i = 0; i < hang * lie; i++) {
            if(clickCount[i]==1||clickCount[i]==2)
            {
                if(clickCount[i]!=daan[i])
                {
                    TextView textView = (TextView) gridLayout.getChildAt(i);
                    if(clickCount[i]==2) {
                        textView.setBackgroundColor(Color.GRAY);
                        textView.setTextColor(Color.BLACK);
                    } else if (clickCount[i]==1) {
                        textView.setBackgroundColor(Color.BLACK);
                        textView.setTextColor(Color.WHITE);
                    }
                }
            }
        }
    }
    private void setenv() {
        // Switch关闭时执行的函数逻辑
        for (int i = 0; i < hang * lie; i++) {
            if(clickCount[i]==1||clickCount[i]==2)
            {

                TextView textView = (TextView) gridLayout.getChildAt(i);
                if(clickCount[i]==2) {
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setTextColor(Color.BLACK);
                } else if (clickCount[i]==1) {
                    textView.setBackgroundColor(Color.BLACK);
                    textView.setTextColor(Color.WHITE);
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
        }
        if (checkGameCompleted()) {
            showGameCompletedDialog();
        }
    }
    private void SetNumbers() {
        // 设置部分格子上的数字
        for (int i = 0; i < hang * lie; i++) {
            TextView textView = (TextView) gridLayout.getChildAt(i);
            int row = i / lie;
            int column = i % lie;
            int index = row * lie + column;
            if (mimian[index] != 10) {
                textView.setText(String.valueOf(mimian[index]));
            }
        }
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
        shape.getPaint().setColor(Color.DKGRAY);
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
                        startLongPressCheck(textView, longPressHandler, 1000);
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
            @SuppressLint("ClickableViewAccessibility")
            private void startLongPressCheck(final TextView textView, final Handler handler, final long timeThreshold) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //setNextBackgroundColor(textView, gridLayout.indexOfChild(textView), Color.GREEN);
                        if(tipsmode) {
                            isLongPressTriggered = true;
                            setNineBackgroundColor(textView);
                        }
                    }
                }, timeThreshold); // 设置长按时间阈值
            }
        });
        return textView;
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
            touchedTextView.clearAnimation();//清除所有动画效果
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
        bgAnimator.setDuration(300);
        ObjectAnimator txAnimator = ObjectAnimator.ofArgb(textView,"textColor",Color.MAGENTA,txcolor);
        txAnimator.setDuration(300);
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



        //getIndexForNumber();
        //if(greyhound[index]==1)
        //{
        // textView.setTextColor(Color.YELLOW);
        //}

        if (checkGameCompleted()) {
            showGameCompletedDialog();
        }
    }

    private void setNineBackgroundColor(TextView textView) {
        // 重载的方法，用于设置指定颜色，例如长按设置为绿色
        int touchedIndex = (int) textView.getTag(); // 获取存储的index
        int h=touchedIndex/lie;
        int l=touchedIndex%lie;
        clickCount[touchedIndex]=daan[touchedIndex];//自身
        if(l>0&&l<lie-1&&h>0&&h<hang-1)//内圈格子.知道坐标求值：h*lie+l
        {
            clickCount[(h-1)*lie+(l-1)]=daan[(h-1)*lie+(l-1)];//左上角
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[(h-1)*lie+(l+1)]=daan[(h-1)*lie+(l+1)];//右上角
            clickCount[h*lie+(l-1)]=daan[h*lie+(l-1)];//左边
            clickCount[h*lie+(l+1)]=daan[h*lie+(l+1)];//右边
            clickCount[(h+1)*lie+(l-1)]=daan[(h+1)*lie+(l-1)];//左下角
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(h+1)*lie+(l+1)]=daan[(h+1)*lie+(l+1)];//右下角
        }
        else if(l==0&&h>0&&h<hang-1)//左侧格子不包括角落
        {
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[(h-1)*lie+(l+1)]=daan[(h-1)*lie+(l+1)];//右上角
            clickCount[h*lie+(l+1)]=daan[h*lie+(l+1)];//右边
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(h+1)*lie+(l+1)]=daan[(h+1)*lie+(l+1)];//右下角
        }
        else if(l==lie-1&&h>0&&h<hang-1)//右侧格子不包括角落
        {
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[(h-1)*lie+(l-1)]=daan[(h-1)*lie+(l-1)];//左上角
            clickCount[h*lie+(l-1)]=daan[h*lie+(l-1)];//左边
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(h+1)*lie+(l-1)]=daan[(h+1)*lie+(l-1)];//左下角
        }
        else if(l>0&&l<lie-1&&h==0)//上方格子不包括角落
        {
            clickCount[(l-1)]=daan[(l-1)];//左边
            clickCount[(l+1)]=daan[(l+1)];//右边
            clickCount[(h+1)*lie+(l-1)]=daan[(h+1)*lie+(l-1)];//左下角
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(h+1)*lie+(l+1)]=daan[(h+1)*lie+(l+1)];//右下角
        }
        else if(l>0&&l<lie-1&&h==hang-1)//下方格子不包括角落
        {
            clickCount[(h-1)*lie+(l-1)]=daan[(h-1)*lie+(l-1)];//左上角
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[(h-1)*lie+(l+1)]=daan[(h-1)*lie+(l+1)];//右上角
            clickCount[h*lie+(l-1)]=daan[h*lie+(l-1)];//左边
            clickCount[h*lie+(l+1)]=daan[h*lie+(l+1)];//右边
        }
        else if(h==0&&l==0)//左上角落
        {
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(l+1)]=daan[(l+1)];//右边
            clickCount[(h+1)*lie+(l+1)]=daan[(h+1)*lie+(l+1)];//右下角
        }
        else if(h==0&&l==lie-1)//右上角落
        {
            clickCount[(h+1)*lie+l]=daan[(h+1)*lie+l];//下方
            clickCount[(l-1)]=daan[(l-1)];//左边
            clickCount[(h+1)*lie+(l-1)]=daan[(h+1)*lie+(l-1)];//左下角
        }
        else if(h==hang-1&&l==0)//左下角落
        {
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[h*lie+(l+1)]=daan[h*lie+(l+1)];//右边
            clickCount[(h-1)*lie+(l+1)]=daan[(h-1)*lie+(l+1)];//右上角
        }
        else if(h==hang-1&&l==lie-1)//右下角落
        {
            clickCount[(h-1)*lie+l]=daan[(h-1)*lie+l];//上方
            clickCount[h*lie+(l-1)]=daan[h*lie+(l-1)];//左边
            clickCount[(h-1)*lie+(l-1)]=daan[(h-1)*lie+(l-1)];//左上角
        }
        setenv();
    }

    private boolean checkGameCompleted() {
        //int centerIndex = 5; // 中心格子的索引（从0开始计数）
        for(int i=0;i<hang * lie;i++) {
            if (clickCount[i] != daan[i]) {
                return false; // 如果有任一对应位置的元素不相等，返回 false
            }
        }
        return true;
    }

    private void showGameCompletedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("恭喜！已通关！");
        builder.setPositiveButton("保存并返回主菜单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writesql();
                finish(); // 结束当前Activity


            }
        });
        builder.setNegativeButton("继续当前游戏", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private TextView createSquareTextView(int position) {
        TextView textView = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        // 设置正方形格子的大小
        int size = 200; // 正方形格子的边长，可以根据需要修改
        params.width = size;
        params.height = size;

        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }


    private void showHintDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("答案");

        // 创建一个 ScrollView 作为对话框的内容视图
        ScrollView scrollView = new ScrollView(this);
        GridLayout dialogGridLayout = new GridLayout(this);
        scrollView.addView(dialogGridLayout);

        dialogGridLayout.setColumnCount(lie);
        dialogGridLayout.setRowCount(hang);

        // 计算每个格子的大小，屏幕宽度的一半作为基准
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels; // 获取屏幕高度
        int gridSize = screenWidth / 2 / Math.max(lie, hang); // 根据行列数动态计算格子的大小，取行列数的较大值作为基准

        // 增大格子的高度
        gridSize *= 1.5; // 可根据需要调整比例来增大格子的高度

        for (int i = 0; i < hang * lie; i++) {
            TextView textView = createSquareTextView(i); // 创建一个正方形的TextView
            if (daan[i] == 2) {
                textView.setBackgroundColor(Color.GRAY); // 如果 daan 值为2，则设置背景颜色为灰色
            } else if (daan[i] == 1) {
                textView.setBackgroundColor(Color.BLACK); // 如果 daan 值为1，则设置背景颜色为黑色
            }
            GridLayout.Spec rowSpec = GridLayout.spec(i / lie, 1f); // 行位置，跨越1行
            GridLayout.Spec columnSpec = GridLayout.spec(i % lie, 1f); // 列位置，跨越1列
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.width = gridSize;
            params.height = gridSize;
            textView.setLayoutParams(params);

            dialogGridLayout.addView(textView);
        }

        // 增大弹窗的高度
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (screenHeight * 0.8)); // 设置为屏幕高度的80%，可以根据需要调整比例

        scrollView.setLayoutParams(layoutParams);

        builder.setView(scrollView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在这里可以添加点击确定按钮后的逻辑
                // 比如关闭对话框或者执行其他操作
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @SuppressLint("SetTextI18n")
    private void know() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题

        // 创建一个布局容器来放置内容
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // 设置内边距

        // 添加一个 TextView 来显示信息
        TextView textView = new TextView(this);
        textView.setText("注意，已开启辅助游戏模式！\n(为了更好的游戏体验，我们不建议您打开该选项)\n\n打开提示开关后，长按某一个格子1s以上，自动将以其为中心的九宫格涂上正确颜色。\n点击错误按钮后，将以红色显示目前您的填涂中的错误格子。\n点击答案会显示当前地图的正确解。");
        textView.setPadding(10, 10, 10, 10); // 设置内边距
        layout.addView(textView);

        builder.setView(layout);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch hintswitch = findViewById(R.id.switch_error_display);
        Button hintbutton = findViewById(R.id.button_hint);

        hintswitch.setVisibility(View.VISIBLE);
        hintbutton.setVisibility(View.VISIBLE);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮的逻辑

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
    public void onBackPressed() {//返回键触发
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定返回吗？\n所有未保存记录都将丢失！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行退出登录的逻辑，例如跳转到登录界面


                //Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
                //startActivity(intent);
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

    private void writesql() {
        float lv;
        int sum = hang * lie;
        int cot = 0;

        for (int i = 0; i < hang * lie; i++) {
            if (clickCount[i] == daan[i]) {
                cot++;
            }
        }

        // 强制转换其中一个操作数为浮点数类型
        lv = (float) cot / sum;

        // 使用 BigDecimal 来保留三位小数
        BigDecimal bd = new BigDecimal(Float.toString(lv));
        bd = bd.setScale(3, RoundingMode.HALF_UP);

        Log.d("MyActivity", "Lv: " + bd.toString());
        int[] mapData = new int[hang * lie];
        System.arraycopy(clickCount, 0, mapData, 0, hang * lie);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.updateUserLevelData(username, tu, bd.floatValue(), mapData);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int pointerCount = ev.getPointerCount();
        if(tipsmode && ((ev.getAction() == MotionEvent.ACTION_MOVE) || (pointerCount > 1)))
            return false;
        if (pointerCount > 1)// 忽略多指触摸事件
            screen = false;
        else
            screen = true;
        return super.dispatchTouchEvent(ev);
    }

}