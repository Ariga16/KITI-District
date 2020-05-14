package com.dacasa.sdakitidistrict.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dacasa.sdakitidistrict.AuthorDetails;
import com.dacasa.sdakitidistrict.R;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private String[] sTitles;
    private String[] sContents;

    public AuthorAdapter(Context context, String[] titles, String[] contents) {

        this.inflater = LayoutInflater.from(context);
        this.sTitles = titles;
        this.sContents = contents;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_for_authorsrecyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = sTitles[position];
        String content = sContents[position];
        holder.storyTitle.setText(title);
        holder.storyContent.setText(content);


    }


    @Override
    public int getItemCount() {
        return sTitles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView storyTitle,storyContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AuthorDetails.class);
                    //send song title and contents through recyclerview to detail activity
                    i.putExtra("title of story",sTitles[getAdapterPosition()]);
                    i.putExtra("content of story",sContents[getAdapterPosition()]);
                    v.getContext().startActivity(i);
                }
            });


            storyTitle = itemView.findViewById(R.id.tvStoryTitle);
            storyContent = itemView.findViewById(R.id.tvStoryContent);

        }
    }
}
