package com.dacasa.sdakitidistrict.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dacasa.sdakitidistrict.Activities.PostDetailActivity;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.Commoners.PostItemClickListener;
import com.dacasa.sdakitidistrict.Models.Post;
import com.dacasa.sdakitidistrict.POJOS.Verse;
import com.dacasa.sdakitidistrict.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;
    Bible bible;
    // verse view
    FeedInteractor interactor;
    PostItemClickListener postItemClickListener;

    public PostAdapter(Context mContext,List<Post> mData,PostItemClickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        bible = new Bible(mContext);
        interactor = (FeedInteractor)mContext;
        postItemClickListener = listener;
    }

    public void addFeed(Post feed){
        mData.add(0,feed);
        notifyItemInserted(0);
    }

    public String getScripture(Post f){
        if (!P.bibleAvailable())return "Please download the bible to view the scripture";
        StringBuilder s = new StringBuilder();
        List<Verse> vs = bible.getVerses(f.getBook(),f.getChapter(),f.getFrom_verse(),f.getTo_verse());
        for (Verse ve:vs){
            s.append(ve.getVerse()+"\n");
        }
        return s.toString();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // show verse
        final Post feed = mData.get(position);
        // changed from setText(get(position).getUsername())
        holder.tvTitle.setText(feed.getTitle());
        Glide.with(mContext).load(feed.getPicture()).into(holder.imgPost);
        holder.tvtv.setText(feed.getUsername());
        holder.verse_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactor.onScriptureClicked(feed);
            }
        });
        // show verse
        if (P.bibleAvailable())holder.book.setText(bible.bookName(feed.getBook()) + ": ch: " + feed.getChapter() + " v:" + feed.getFrom_verse() + "-" + feed.getTo_verse());
        holder.verse.setText(getScripture(feed));

        String userImg = mData.get(position).getUserPhoto();
        if (userImg != null) {
            Glide.with(mContext).load(userImg).into(holder.imgPostProfile);
            holder.tvtv.setText(mData.get(position).getUsername());
        }else
            Glide.with(mContext).load(R.mipmap.ic_launcher_h_foreground).into(holder.imgPostProfile);

        holder.TVrowtime.setText(timestampToString((Long)mData.get(position).getTimeStamp()));
        holder.rowpost_desc.setText(mData.get(position).getDescription());
        holder.tvtv.setText(mData.get(position).getUsername());
        // add feed

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,TVrowtime,rowpost_desc,tvtv,verse,book;
        ImageView imgPost;
        ImageView imgPostProfile;
        // add feed
        View verse_view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            TVrowtime = itemView.findViewById(R.id.TVrowtime);
            rowpost_desc = itemView.findViewById(R.id.rowpost_desc);
            tvtv = itemView.findViewById(R.id.tvtv);
            // add verse
            verse = itemView.findViewById(R.id.verse);
            book = itemView.findViewById(R.id.book);
            verse_view = itemView.findViewById(R.id.verse_view);

            imgPost.setOnClickListener(new View.OnClickListener() {
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
                    postDetailActivity.putExtra("userName",mData.get(position).getUsername());

                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestamp) ;
                    mContext.startActivity(postDetailActivity);
                }
            });
        }
    }

    public interface FeedInteractor{
        void onScriptureClicked(Post feed);
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd MMM" ,calendar).toString();
        return date;
    }
}
