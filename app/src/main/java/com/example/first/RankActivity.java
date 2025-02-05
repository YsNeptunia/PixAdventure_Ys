package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TableLayout tableLayout; // 表格布局对象，用于显示数据

    private static final int ROWS_PER_PAGE = 20;
    private int currentPage = 0;
    private List<TableRow> allRows = new ArrayList<>();
    private Button prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

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
        refreshData2(); // 传入指定的用户名进行查询

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前Activity或Fragment
            }
        });
    }

    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = databaseHelper.getReadableDatabase();



                // 查询所有用户
                Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tableLayout.removeAllViews(); // 清空现有的表格内容
                        }
                    });

                    int rank = 1;

                    do {
                        String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                        int avatarResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVATAR_RES_ID));
                        String completionPercentages = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETION_PERCENTAGES));

                        // 计算分数
                        double score = calculateScore(completionPercentages);

                        // 在 UI 线程更新表格，确保操作在主线程执行
                        final int finalRank = rank;
                        final String finalUsername = username;
                        final double finalScore = score;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 创建新的行
                                TableRow row = new TableRow(RankActivity.this);

                                // 添加排名
                                TextView rankTextView = new TextView(RankActivity.this);
                                rankTextView.setText(String.valueOf(finalRank));
                                rankTextView.setBackgroundResource(R.drawable.cell_border);
                                rankTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                rankTextView.setPadding(20, 20, 20, 20);
                                row.addView(rankTextView);

                                // 添加头像
                                ImageView avatarImageView = new ImageView(RankActivity.this);
                                avatarImageView.setImageResource(avatarResId);
                                avatarImageView.setBackgroundResource(R.drawable.cell_border);
                                TableRow.LayoutParams avatarParams = new TableRow.LayoutParams(100,100);
                                avatarImageView.setPadding(20, 20, 20, 20);
                                avatarImageView.setLayoutParams(avatarParams);
                                avatarImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                avatarImageView.setAdjustViewBounds(true);
                                row.addView(avatarImageView);

                                // 添加用户名
                                TextView usernameTextView = new TextView(RankActivity.this);
                                usernameTextView.setText(finalUsername);
                                usernameTextView.setBackgroundResource(R.drawable.cell_border);
                                usernameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                usernameTextView.setPadding(20, 20, 20, 20);
                                row.addView(usernameTextView);

                                // 添加分数
                                TextView scoreTextView = new TextView(RankActivity.this);
                                scoreTextView.setText(String.valueOf(finalScore));
                                scoreTextView.setBackgroundResource(R.drawable.cell_border);
                                scoreTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                                scoreTextView.setPadding(20, 20, 20, 20);
                                row.addView(scoreTextView);

                                // 将新行添加到表格布局中
                                tableLayout.addView(row);
                            }
                        });

                        rank++; // 增加排名
                    } while (cursor.moveToNext());

                    // 关闭游标
                    cursor.close();
                }
            }
        }).start();
    }


    private void refreshData2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = databaseHelper.getReadableDatabase();



                List<UserData> userDataList = new ArrayList<>();

                String query = "SELECT " + DatabaseHelper.COLUMN_USERNAME + ", "
                        + DatabaseHelper.COLUMN_AVATAR_RES_ID + ", "
                        + DatabaseHelper.COLUMN_COMPLETION_PERCENTAGES + " "
                        + "FROM " + DatabaseHelper.TABLE_NAME;

                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                        int avatarResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVATAR_RES_ID));
                        String completionPercentages = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETION_PERCENTAGES));

                        // 创建 UserData 对象并添加到列表中
                        UserData userData = new UserData(username, avatarResId, completionPercentages);
                        userDataList.add(userData);

                    } while (cursor.moveToNext());
                }

// 关闭游标
                cursor.close();


                // 查询所有用户

                // 根据分数对 userDataList 进行排序
                Collections.sort(userDataList, new Comparator<UserData>() {
                    @Override
                    public int compare(UserData userData1, UserData userData2) {
                        // 降序排列
                        return Double.compare(userData2.getScore(), userData1.getScore());
                    }
                });

// 在 UI 线程更新表格，确保操作在主线程执行
                // 清空所有行
                allRows.clear();

                int rank2 = 1;
                for (UserData userData : userDataList) {
                    final String username = userData.getUsername();
                    final int avatarResId = userData.getAvatarResId();
                    final int score = userData.getScore();

                    int finalRank = rank2;
                    TableRow row = new TableRow(RankActivity.this);

                    TextView rankTextView = new TextView(RankActivity.this);
                    rankTextView.setText(String.valueOf(finalRank));
                    rankTextView.setBackgroundResource(R.drawable.cell_border);
                    rankTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    rankTextView.setPadding(20, 20, 20, 20);
                    row.addView(rankTextView);

                    ImageView avatarImageView = new ImageView(RankActivity.this);
                    avatarImageView.setImageResource(avatarResId);
                    avatarImageView.setBackgroundResource(R.drawable.cell_border);
                    TableRow.LayoutParams avatarParams = new TableRow.LayoutParams(100, 100);
                    avatarImageView.setPadding(20, 20, 20, 20);
                    avatarImageView.setLayoutParams(avatarParams);
                    avatarImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    avatarImageView.setAdjustViewBounds(true);
                    row.addView(avatarImageView);

                    TextView usernameTextView = new TextView(RankActivity.this);
                    usernameTextView.setText(username);
                    usernameTextView.setBackgroundResource(R.drawable.cell_border);
                    usernameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    usernameTextView.setPadding(20, 20, 20, 20);
                    row.addView(usernameTextView);

                    TextView scoreTextView = new TextView(RankActivity.this);
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

        // 更新分页按钮的可见性
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