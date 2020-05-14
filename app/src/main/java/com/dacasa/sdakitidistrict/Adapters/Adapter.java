package com.dacasa.sdakitidistrict.Adapters;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dacasa.sdakitidistrict.Nyimbo_Details;
import com.dacasa.sdakitidistrict.POJOS.Wimbo;
import com.dacasa.sdakitidistrict.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>implements Filterable {

    private LayoutInflater inflater;
    private List<Wimbo> mData = new ArrayList<>();
    List<Wimbo> mDataFiltered = new ArrayList<>();

    Context mcontext;
    boolean isDark=false;


    public Adapter(Context context, String[] titles, String[] contents){
        this.inflater=LayoutInflater.from(context);
        this.mcontext=context;
        if (titles.length == contents.length){
            for (int i = 0; i < titles.length; i++){
                Wimbo wimboMpya = new Wimbo(titles[i], contents[i]);
                mData.add(wimboMpya);
                mDataFiltered.add(wimboMpya);
            }
        }else {
            Toast.makeText(context, "Number of titles and contents do not match", Toast.LENGTH_LONG).show();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_forrecyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wimbo currentSong = mDataFiltered.get(position);
        holder.songTitle.setText(currentSong.getTitle());
        holder.songContent.setText(currentSong.getContent());

        //animation for song title
        holder.songTitle.setAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.fade_transition_animation));
        //animation for linear layout
        holder.LLcontainer.setAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.fade_transition_animation));
        //anim for cardview
        holder.CVcontainer.setAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.fade_scale_animation));



    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if (Key.isEmpty()) {

                    mDataFiltered.clear();
                    mDataFiltered.addAll(mData);


                }
                else {
                    List<Wimbo> IstFiltered = new ArrayList<>();
                    for (Wimbo row : mData) {

                       if (row.getTitle().toLowerCase().contains(Key.toLowerCase())){
                           IstFiltered.add(row);
                       }
                    }
                    mDataFiltered.clear();
                    mDataFiltered.addAll(IstFiltered);

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;



            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mDataFiltered =( List<Wimbo>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView songTitle,songContent;
        LinearLayout LLcontainer;
        CardView CVcontainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //implement on click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Nyimbo_Details.class);
                    //send song title and contents through recyclerview to detail activity
                    Wimbo wimbo = mDataFiltered.get(getAdapterPosition());
                    i.putExtra("title of song",wimbo.getTitle());
                    i.putExtra("content of song",wimbo.getContent());
                    v.getContext().startActivity(i);
                }
            });


            songTitle=itemView.findViewById(R.id.tvsongTitle);
            songContent=itemView.findViewById(R.id.tvSongContent);
            LLcontainer=itemView.findViewById(R.id.LLcontainer);
            CVcontainer=itemView.findViewById(R.id.CVcontainer);

            if (isDark){
                setDarkTheme();
            }
        }
        private void setDarkTheme(){

            CVcontainer.setBackgroundResource(R.drawable.circle_bg_dark);

        }


    }

}

