package Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intersofteagles.pcea.Commoners.Bible;
import com.intersofteagles.pcea.POJOS.Verse;
import com.intersofteagles.pcea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monroe on 11/27/2016.
 */
public class BibleVerseAdapter extends RecyclerView.Adapter<BibleVerseAdapter.VerseHolder>{

    Context context;
    LayoutInflater inflater;
    List<Verse> verses = new ArrayList();
    List<Verse> originalList = new ArrayList();
    List<Verse> selection = new ArrayList();
    Bible bible;
    VerseListener VerseListener;
    Resources r;
    private boolean nightMode = false;
    String query = null;

    public BibleVerseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        bible = new Bible(context);
        VerseListener = (VerseListener)context;
        r = context.getResources();
    }

    public void addVerse(Verse Verse){
        verses.add(Verse);
        notifyItemInserted(verses.size()-1);
    }


    public void addAllVerses(List<Verse> verses){
        this.verses.addAll(verses);
        this.originalList.addAll(verses);
        notifyDataSetChanged();
    }

    public void highlight(int from,int to){
        for (int i = from-1; i<to;i++){
            selection.add(verses.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearSelection(){
        selection.clear();
        notifyDataSetChanged();
    }

    public void clearVerses(){
        verses.clear();
        notifyDataSetChanged();
    }

    public void reload(){
        verses.clear();
        verses.addAll(originalList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VerseHolder holder, int position) {
        final Verse verse = verses.get(position);
        holder.verse.setText(verse.getVerse());
        holder.verse_no.setText(verse.getVerse_no() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerseListener.verseClicked(verse);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                VerseListener.verseLongClicked(verse);
                return true;
            }
        });
        if (nightMode){
            holder.verse_no.setTextColor(context.getResources().getColor(R.color.night_mode_text));
        }else {
            holder.verse_no.setTextColor(Color.BLACK);
        }
        holder.verse.setTextColor(selection.contains(verse) ? Color.parseColor("#FF9900") : !nightMode ? Color.BLACK : context.getResources().getColor(R.color.night_mode_text));

        if (query != null)span(holder.verse,verse.getVerse());
    }


    public void span(TextView textView, String text){
        String find = text.toLowerCase();
        if (!find.contains(query))return;
        int start = find.indexOf(query);
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(text);
        spanText.setSpan(new ForegroundColorSpan(Color.GREEN), start, query.length()+start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanText);
    }



    @Override
    public VerseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerseHolder(inflater.inflate(R.layout.row_verse,parent,false));
    }

    public void toggleNightMode(boolean nightMode){
        this.nightMode = nightMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    class VerseHolder extends RecyclerView.ViewHolder{
        TextView verse,verse_no;
        public VerseHolder(View v) {
            super(v);
            verse = (TextView)v.findViewById(R.id.verse);
            verse_no = (TextView)v.findViewById(R.id.verse_no);
        }
    }

    public void noQuery(){
        query = null;
    }

    public interface VerseListener{
        public void verseClicked(Verse verse);
        public void verseLongClicked(Verse verse);
    }

    public void toggleSelection(Verse verse){
        if (selection.contains(verse)){
            selection.remove(verse);
        }else {
            selection.add(verse);
        }
        notifyItemChanged(verses.indexOf(verse));
    }

    public List<Verse> getSelection() {
        return selection;
    }

    public List<Verse> getVerses() {
        return verses;
    }


    public Verse removeItem(int pos){
        final Verse Verse = verses.remove(pos);
        notifyItemRemoved(pos);
        return Verse;
    }

    public void addItem(int pos, Verse Verse){
        verses.add(pos, Verse);
        notifyItemInserted(pos);
    }

    public void moveItem(int from, int to){
        final Verse Verse =verses.remove(from);
        verses.add(to,Verse);
        notifyItemMoved(from, to);
    }

    private void applyRemovals(List<Verse> list){
        for(int i = list.size()-1; i>=0 ; i--){
            final Verse Verse = verses.get(i);
            if(!list.contains(Verse)){
                removeItem(i);
            }
        }
    }

    public void applyAdditions(List<Verse> list){
        for(int i = 0; i<list.size(); i++){
            final Verse Verse = list.get(i);
            if(!verses.contains(Verse)){
                addItem(i, Verse);
            }
        }
    }

    public void applyMovements(List<Verse> list){
        for(int to = list.size()-1; to >= 0; to--){
            final Verse Verse = list.get(to);
            final int from = verses.indexOf(Verse);
            if(from >= 0 && from != to){
                moveItem(from, to);
            }
        }
        notifyDataSetChanged();
    }

    public void animateTo(List<Verse> list, String query){
        this.query = query;
        applyRemovals(list);
        applyAdditions(list);
        applyMovements(list);
    }




}
