package com.dacasa.sdakitidistrict.Activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.dacasa.sdakitidistrict.Commoners.LoginListener;
import com.dacasa.sdakitidistrict.POJOS.User;
import com.dacasa.sdakitidistrict.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login_church extends Fragment {

    TextView prompt,prompt2;
    Spinner parish,churches,districts;
    ArrayAdapter parishAdapter;
    ArrayAdapter churchAdapter;
    ArrayAdapter districtAdapter;
    Button proceed;
    LoginListener loginListener;
    LinearLayout checkes;
    SharedPreferences settings;
    User user;

    public Login_church() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginListener = (LoginListener)getActivity();
        View v = inflater.inflate(R.layout.fragment_login_church, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        proceed = (Button)v.findViewById(R.id.proceed);
        checkes = (LinearLayout)v.findViewById(R.id.checkes);

        prompt = (TextView)v.findViewById(R.id.prompt);
        prompt2 = (TextView)v.findViewById(R.id.prompt2);
        parish = (Spinner)v.findViewById(R.id.parish);
        churches = (Spinner)v.findViewById(R.id.church);
        districts = (Spinner)v.findViewById(R.id.district);

        parishAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,new String[]{"Select Local Conference","Central Rift Valley Conference"});
        parishAdapter.setDropDownViewResource(R.layout.spinner_item_list);
        churchAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,new String[]{"Select Church","Kiratina SDA Church","SDA Church Elimu","KITI SDA Church","Seventh-Day Adventist Church KITI-East","WhiteHouse Seventh-day Adventist Church","Free-Area SDA Church"});
        churchAdapter.setDropDownViewResource(R.layout.spinner_item_list);
        districtAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,new String[]{"Select District","KITI District"});
        districtAdapter.setDropDownViewResource(R.layout.spinner_item_list);

        parish.setAdapter(parishAdapter);
        churches.setAdapter(churchAdapter);
        districts.setAdapter(districtAdapter);

        // animation for spinners

        prompt.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        prompt2.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        parish.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        churches.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        districts.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        proceed.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));

        parish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validated())loginListener.pageTwoProceed(user);
            }
        });

        return v;
    }

    public void updateObject(User user){
        this.user = user;
    }

    private boolean validated(){
        boolean ok = true;
        if (parish.getSelectedItem().equals("Select Parish")){
            ok = false;
        }
        if (churches.getSelectedItem().equals("Select Church")){
            ok = false;
        }
        if (districts.getSelectedItem().equals("Select District")){
            ok = false;
        }
        if (!ok) Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();

        if (ok){
            if (user == null)user = new User();
            user.setParish(parish.getSelectedItem().toString());
            user.setChurch(churches.getSelectedItem().toString());
            user.setDistrict(districts.getSelectedItem().toString());
            settings.edit().putString("parish", parish.getSelectedItem().toString());
            settings.edit().putString("church",churches.getSelectedItem().toString());
            settings.edit().putString("district",districts.getSelectedItem().toString());
        }
        return ok;
    }


    public void animate(){
        if (checkes == null)return;
        for (int i = 0; i<checkes.getChildCount();i++){
            View v = checkes.getChildAt(i);
            v.setTranslationX(70);
            v.setAlpha(0f);
        }
        for (int i = 0; i<checkes.getChildCount();i++){
            View v = checkes.getChildAt(i);
            v.animate().alpha(1f).setDuration(700).translationX(1f).setStartDelay(i*100);
        }
    }

}
