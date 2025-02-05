package com.example.first;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ImageView avatarImageView;

    private  int flag;
   // private boolean isPasswordVisible = false;

    //private boolean isconfirmPasswordVisible = false;

//    private Uri avatarUri;
//
//    private int[] avatarIds = {
//            R.drawable.a1,
//            R.drawable.a2,
//            R.drawable.a3,
//            // 添加更多头像资源ID
//    };


    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        flag = R.drawable.i21;//默认头像

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        avatarImageView = findViewById(R.id.avatarImageView);

        ImageView passwordVisibilityImageView = findViewById(R.id.passwordVisibilityImageView);
        ImageView confirmPasswordVisibilityImageView = findViewById(R.id.confirmPasswordVisibilityImageView);

        // 初始设置密码输入框为密码模式
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        passwordVisibilityImageView.setOnClickListener(new View.OnClickListener() {
            private boolean isPasswordVisible = false;

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

        confirmPasswordVisibilityImageView.setOnClickListener(new View.OnClickListener() {
            private boolean isConfirmPasswordVisible = false;

            @Override
            public void onClick(View v) {
                // 切换确认密码输入框的显示与隐藏
                if (isConfirmPasswordVisible) {
                    // 如果当前可见，则切换为密码模式
                    confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordVisibilityImageView.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    // 如果当前不可见，则切换为普通文本模式
                    confirmPasswordEditText.setTransformationMethod(null);
                    confirmPasswordVisibilityImageView.setImageResource(R.drawable.ic_visibility);
                }

                isConfirmPasswordVisible = !isConfirmPasswordVisible;

                // 将光标移至末尾
                confirmPasswordEditText.setSelection(confirmPasswordEditText.getText().length());
            }
        });



        Button chooseAvatarButton = findViewById(R.id.chooseAvatarButton);
        chooseAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarChooserDialog();
            }
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void showAvatarChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择头像");

        List<Integer> avatarList = Arrays.asList(
                R.drawable.i1,
                R.drawable.i2,
                R.drawable.i3,
                R.drawable.i4,
                R.drawable.i5,
                R.drawable.i6,
                R.drawable.i7,
                R.drawable.i8,
                R.drawable.i9,
                R.drawable.i10,
                R.drawable.i11,
                R.drawable.i12,
                R.drawable.i13,
                R.drawable.i14,
                R.drawable.i15,
                R.drawable.i16,
                R.drawable.i17,
                R.drawable.i18,
                R.drawable.i19,
                R.drawable.i20
                // 添加更多头像资源ID
        );

        View dialogView = getLayoutInflater().inflate(R.layout.avatar_chooser_dialog, null);
        GridView gridView = dialogView.findViewById(R.id.avatarGridView);

        AvatarAdapter adapter = new AvatarAdapter(this, avatarList);
        gridView.setAdapter(adapter);

        // 创建并显示对话框
        AlertDialog dialog = builder.setView(dialogView)
                .setNegativeButton("取消", null)
                .create();

        // 设置GridView的点击事件监听器
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            int selectedAvatarResId = avatarList.get(position);
            avatarImageView.setImageResource(selectedAvatarResId); // 替换成你的ImageView ID
            flag=selectedAvatarResId;
            dialog.dismiss(); // 选择头像后关闭对话框
        });

        dialog.show();
    }
    private void updateAvatar(int avatarResId) {
        avatarImageView.setImageResource(avatarResId);
    }

    private void register() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查密码是否匹配
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "密码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 检查用户名是否存在
        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "该用户名已存在，请更换名称", Toast.LENGTH_SHORT).show();
            return;
        }



        // 这里假设你已经有了其他需要存储的数据，比如头像资源ID、完成百分比、地图数据等
        int avatarResId = flag; // 默认头像资源ID，可以根据实际情况获取
        float[] completionPercentages = new float[]{0, 0, 0, 0, 0, 0, 0, 0}; // 默认完成百分比
        int[][] mapData=new int[][]{
                {
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0
                },
                {
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0
                },
                {
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                },
                {
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                }
        };


        // 将用户名和密码保存到数据库中
        if (dbHelper.addUser(username, password, avatarResId, completionPercentages,mapData)){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            // 注册成功后返回登录页面
            // Intent intent = new Intent(this, LoginActivity.class);
            // startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}