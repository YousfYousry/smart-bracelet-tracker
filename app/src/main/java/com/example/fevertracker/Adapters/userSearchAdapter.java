package com.example.fevertracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fevertracker.Classes.userSearch;
import com.example.fevertracker.R;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class userSearchAdapter extends ArrayAdapter<userSearch> {
    private final Context mContext;
    private final int mResource;

    private static class ViewHolder {
        TextView userName, userPassport, userId;
        FrameLayout back;
        ImageView UserPic;
    }

    public userSearchAdapter(Context context, int resource, ArrayList<userSearch> users) {
        super(context, resource, users);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName().trim(),
                passport = getItem(position).getPassport().trim(),
                id = getItem(position).getId().trim(),
                ImageUrl = getItem(position).getmImageUrl();
        int status = getItem(position).getStatus();

        int resource = 0;
        if (getItem(position).getStatus() == 1) {
            resource = R.drawable.green_circle;
        } else if (getItem(position).getStatus() == 2) {
            resource = R.drawable.yellow_circle;
        } else if (getItem(position).getStatus() == 3) {
            resource = R.drawable.red_circle;
        }

        userSearch user = new userSearch(name, passport, id, status);
        user.setmImageUrl(ImageUrl);

        final ViewHolder holder;

//        if (convertView == null) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        holder = new ViewHolder();
        holder.userName = convertView.findViewById(R.id.userName);
        holder.userPassport = convertView.findViewById(R.id.userPassport);
        holder.userId = convertView.findViewById(R.id.userId);
        holder.back = convertView.findViewById(R.id.back);
        holder.UserPic = convertView.findViewById(R.id.UserPic);


//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        holder.userName.setText(user.getName());
        holder.userPassport.setText(user.getPassport());
        holder.userId.setText(user.getId());
        holder.back.setBackgroundResource(resource);
        if (user.getmImageUrl().isEmpty()) {
            user.setmImageUrl("avatar");
            getNewImage(user.getId(), holder.UserPic, getItem(position));
        } else if (!user.getmImageUrl().contains("avatar")) {
            getOldImage(holder.UserPic, user.getmImageUrl());
        }

        return convertView;
    }

    public void getNewImage(String id, ImageView pic, userSearch user) {
        FirebaseStorage.getInstance().getReference("uploads").child(id).getDownloadUrl().addOnSuccessListener(downloadUrl -> {
            Picasso.get()
                    .load(downloadUrl)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .fit()
                    .into(pic);
            user.setmImageUrl(downloadUrl.toString());
        });
    }

    public void getOldImage(ImageView pic, String uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .fit()
                .into(pic);
    }

}
