package com.dacasa.sdakitidistrict.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dacasa.sdakitidistrict.Activities.PostDetailActivity;
import com.dacasa.sdakitidistrict.Models.Post;
import com.dacasa.sdakitidistrict.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    Context mContext;
    List<Post> mData;


    public PostAdapter(Context mContext,List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

        String userImg = mData.get(position).getUserPhoto();
        if (userImg != null) {
            Glide.with(mContext).load(userImg).into(holder.imgPostProfile);
        }else
            Glide.with(mContext).load(R.mipmap.ic_launcher_h_foreground).into(holder.imgPostProfile);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgPost;
        ImageView imgPostProfile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("title",mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    // will fix this later i forgot to add user name to post object
                    //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestamp) ;
                    mContext.startActivity(postDetailActivity);

                }
            });


        }


    }
}
