package com.example.first;
public class UserData {
    private String username;
    private int avatarResId;
    private String completionPercentages;
    private int score;

    // 构造函数
    public UserData(String username, int avatarResId, String completionPercentages) {
        this.username = username;
        this.avatarResId = avatarResId;
        this.completionPercentages = completionPercentages;
        this.score = calculateScore(completionPercentages); // 计算分数
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 获取头像资源ID
    public int getAvatarResId() {
        return avatarResId;
    }

    // 获取分数
    public int getScore() {
        return score;
    }

    private int calculateScore(String completionPercentages) {
        double score = 0;
        String[] completionArray = completionPercentages.split(",");
        for (String completion : completionArray) {
            try {
                score += Double.parseDouble(completion);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return (int) (score * 100); // 将 score 乘以 100 并转换为整数类型
    }
}

