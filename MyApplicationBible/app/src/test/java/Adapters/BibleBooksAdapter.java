package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intersofteagles.pcea.Commoners.Bible;
import com.intersofteagles.pcea.POJOS.Book;
import com.intersofteagles.pcea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monroe on 11/27/2016.
 */
public class BibleBooksAdapter extends RecyclerView.Adapter<BibleBooksAdapter.BookHolder>{

    Context context;
    LayoutInflater inflater;
    int res;
    List<Book> books = new ArrayList();
    List<Book> originalList = new ArrayList();
    Bible bible;
    BookListener bookListener;

    public BibleBooksAdapter(Context context, int res) {
        this.context = context;
        this.res = res;
        inflater = LayoutInflater.from(context);
        bible = new Bible(context);
        bookListener = (BookListener)context;
    }

    public void addBook(Book book){
        books.add(book);
        originalList.add(book);
    }

    @Override
    public void onBindViewHolder(BookHolder holder, int position) {
        final Book book = books.get(position);
        holder.book.setText(book.getName());
        holder.chapters.setText(book.getChapters()+" chapters");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookListener.bookClicked(book);
            }
        });
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookHolder(inflater.inflate(res,parent,false));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BookHolder extends RecyclerView.ViewHolder{
        TextView book,chapters;
        public BookHolder(View v) {
            super(v);
            book = (TextView)v.findViewById(R.id.book);
            chapters = (TextView)v.findViewById(R.id.chapters);
        }
    }


    public interface BookListener{
        void bookClicked(Book book);
    }


    public List<Book> getBooks() {
        return books;
    }

    public void reload(){
        books.clear();
        books.addAll(originalList);
        notifyDataSetChanged();
    }

    public Book removeItem(int pos){
        final Book book = books.remove(pos);
        notifyItemRemoved(pos);
        return book;
    }

    public void addItem(int pos, Book book){
        books.add(pos, book);
        notifyItemInserted(pos);
    }

    public void moveItem(int from, int to){
        final Book book =books.remove(from);
        books.add(to,book);
        notifyItemMoved(from, to);
    }

    private void applyRemovals(List<Book> list){
        for(int i = list.size()-1; i>=0 ; i--){
            final Book book = books.get(i);
            if(!list.contains(book)){
                removeItem(i);
            }
        }
    }

    public void applyAdditions(List<Book> list){
        for(int i = 0; i<list.size(); i++){
            final Book book = list.get(i);
            if(!books.contains(book)){
                addItem(i, book);
            }
        }
    }

    public void applyMovements(List<Book> list){
        for(int to = list.size()-1; to >= 0; to--){
            final Book book = list.get(to);
            final int from = books.indexOf(book);
            if(from >= 0 && from != to){
                moveItem(from, to);
            }

        }

    }

    public void animateTo(List<Book> list){
        applyRemovals(list);
        applyAdditions(list);
        applyMovements(list);
    }




}
