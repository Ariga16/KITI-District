package com.dacasa.sdakitidistrict;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.Transliterator;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dacasa.sdakitidistrict.Adapters.EventRecyclerAdapter;
import com.dacasa.sdakitidistrict.Adapters.MyGridAdapter;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalenderView extends LinearLayout {
    ImageButton NextButton,PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDER_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);


    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    private DBOpenHelper dbOpenHelper;

    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        InitializeLayout();
        SetUpCalender();

        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalender();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalender();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.add_newevent,null);
                final EditText EventName = addView.findViewById(R.id.eventname);
                final TextView EventsTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.seteventtime);
                Button addEvent = addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY,i);
                                c.set(Calendar.MINUTE,i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a",Locale.ENGLISH);
                                String event_Time = hformat.format(c.getTime());
                                EventsTime.setText(event_Time);


                            }
                        },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });
                // replaced position with i
                final String date = eventDateFormat.format(dates.get(i));
                final String month = monthFormat.format(dates.get(i));
                final String year = yearFormat.format(dates.get(i));




                addEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SaveEvent(EventName.getText().toString(),EventsTime.getText().toString(),date,month,year);
                        SetUpCalender();
                        alertDialog.dismiss();




                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();


            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date = eventDateFormat.format(dates.get(i));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.show_events,null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView .setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext()
                        ,CollectEventsByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();


                return true;
            }
        });

    }
    private ArrayList<Events> CollectEventsByDate(String date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date,database);
        while (cursor.moveToNext()) {

            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event,time,Date,month,Year);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }


    public CustomCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveEvent (String event,String time,String date,String month,String year) {

        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context,"Event Saved", Toast.LENGTH_SHORT).show();
    }



    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calender_layout,this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_date);
        gridView = view.findViewById(R.id.gridview);
    }

    private void SetUpCalender(){
        String currentDate =dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalender = (Calendar) calendar.clone();
        monthCalender.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalender.get(Calendar.DAY_OF_WEEK)-1;
        monthCalender.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDER_DAYS) {
            dates.add(monthCalender.getTime());
            monthCalender.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }

    private void CollectEventsPerMonth (String Month,String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month,year,database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

    }

}
