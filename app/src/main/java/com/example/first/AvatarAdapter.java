package com.example.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class AvatarAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> mAvatarList;

    public AvatarAdapter(Context context, List<Integer> avatarList) {
        this.mContext = context;
        this.mAvatarList = avatarList;
    }

    @Override
    public int getCount() {
        return mAvatarList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAvatarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.avatar_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.avatarImageView);
        imageView.setImageResource(mAvatarList.get(position));

        return convertView;
    }
}