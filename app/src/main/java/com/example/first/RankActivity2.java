package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.ui.LevelSelectionActivity2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RankActivity2 extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TableLayout tableLayout; // 表格布局对象，用于显示数据
    private static final int ROWS_PER_PAGE = 20;
    private int currentPage = 0;
    private List<TableRow> allRows = new ArrayList<>();
    private Button prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank2);

        // 获取数据库帮助类实例
        databaseHelper = new DatabaseHelper(this);

        // 获取表格布局对象的引用
        tableLayout = findViewById(R.id.tableLayout);

        prevButton = findViewById(R.id.prevbutton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    currentPage--;
                    showPage(currentPage);
                }
            }
        });

        nextButton = findViewById(R.id.nextbutton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((currentPage + 1) * ROWS_PER_PAGE < allRows.size()) {
                    currentPage++;
                    showPage(currentPage);
                }
            }
        });

        // 加载数据
        genxing(); // 传入指定的用户名进行查询

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前Activity或Fragment
            }
        });
    }

    private void genxing() {
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
                                    Toast.makeText(RankActivity2.this, "服务器连接异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 处理请求成功的情况
                            if (response.isSuccessful()) {
                                List<UserData2> userDataList = new ArrayList<>();
                                String responseData = response.body().string();
                                try {
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject userObject = jsonArray.getJSONObject(i);
                                        String username = userObject.getString("username");
                                        int avatarResId = Integer.parseInt(userObject.getString("avatarResId"));
                                        String completionPercentages = userObject.getString("completionPercentages").replaceAll("[{}]", "");
                                        UserData2 userData2 = new UserData2(username, avatarResId, completionPercentages);
                                        userDataList.add(userData2);
                                    }

                                    Collections.sort(userDataList, new Comparator<UserData2>() {
                                        @Override
                                        public int compare(UserData2 userData1, UserData2 userData2) {
                                            return Double.compare(userData2.getScore(), userData1.getScore());
                                        }
                                    });

                                    // 清空所有行
                                    allRows.clear();

                                    int rank2 = 1;
                                    for (UserData2 userData : userDataList) {
                                        final String username = userData.getUsername();
                                        final int avatarResId = userData.getAvatarResId();
                                        final int score = userData.getScore();

                                        int finalRank = rank2;
                                        TableRow row = new TableRow(RankActivity2.this);

                                        TextView rankTextView = new TextView(RankActivity2.this);
                                        rankTextView.setText(String.valueOf(finalRank));
                                        rankTextView.setBackgroundResource(R.drawable.cell_border);
                                        rankTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                        rankTextView.setPadding(20, 20, 20, 20);
                                        row.addView(rankTextView);

                                        ImageView avatarImageView = new ImageView(RankActivity2.this);
                                        avatarImageView.setImageResource(avatarResId);
                                        avatarImageView.setBackgroundResource(R.drawable.cell_border);
                                        TableRow.LayoutParams avatarParams = new TableRow.LayoutParams(100, 100);
                                        avatarImageView.setPadding(20, 20, 20, 20);
                                        avatarImageView.setLayoutParams(avatarParams);
                                        avatarImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                        avatarImageView.setAdjustViewBounds(true);
                                        row.addView(avatarImageView);

                                        TextView usernameTextView = new TextView(RankActivity2.this);
                                        usernameTextView.setText(username);
                                        usernameTextView.setBackgroundResource(R.drawable.cell_border);
                                        usernameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                        usernameTextView.setPadding(20, 20, 20, 20);
                                        row.addView(usernameTextView);

                                        TextView scoreTextView = new TextView(RankActivity2.this);
                                        scoreTextView.setText(String.valueOf(score));
                                        scoreTextView.setBackgroundResource(R.drawable.cell_border);
                                        scoreTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                        scoreTextView.setPadding(20, 20, 20, 20);
                                        row.addView(scoreTextView);

                                        allRows.add(row);
                                        rank2++;
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showPage(currentPage);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RankActivity2.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RankActivity2.this, "读取失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void showPage(int page) {
        tableLayout.removeAllViews();
        int start = page * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, allRows.size());

        for (int i = start; i < end; i++) {
            tableLayout.addView(allRows.get(i));
        }

        prevButton.setEnabled(page > 0);
        nextButton.setEnabled(end < allRows.size());
    }


    private double calculateScore(String completionPercentages) {
        double score = 0;
        String[] completionArray = completionPercentages.split(",");
        for (String completion : completionArray) {
            try {
                score += Double.parseDouble(completion);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return score;
    }
}