package com.dacasa.sdakitidistrict.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dacasa.sdakitidistrict.Commoners.LoginListener;
import com.dacasa.sdakitidistrict.POJOS.User;
import com.dacasa.sdakitidistrict.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDepartment extends Fragment {

    CheckBox youth,guild,pcmf,brigade,ce,welfare,hospitality,jprc,evangelism,mission,publishing,ministerial,children,sabbathsch;
    CheckBox[] checkBoxes;
    Button proceed;
    LoginListener loginListener;
    LinearLayout checkes;
    List<String> groups = new ArrayList<>();

    User user;


    public LoginDepartment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginListener = (LoginListener)getActivity();
        View v = inflater.inflate(R.layout.fragment_login_department, container, false);
        checkes = (LinearLayout)v.findViewById(R.id.checkes);

        youth = (CheckBox)v.findViewById(R.id.youth);
        guild = (CheckBox)v.findViewById(R.id.guild);
        pcmf = (CheckBox)v.findViewById(R.id.pcmf);
        brigade = (CheckBox)v.findViewById(R.id.brigade);
        ce = (CheckBox)v.findViewById(R.id.ce);
        welfare = (CheckBox)v.findViewById(R.id.welfare);
        hospitality = (CheckBox)v.findViewById(R.id.hospitality);
        jprc = (CheckBox)v.findViewById(R.id.jprc);
        evangelism = (CheckBox)v.findViewById(R.id.evangelism);
        mission = (CheckBox)v.findViewById(R.id.nendeni);

        publishing = (CheckBox)v.findViewById(R.id.publishing);
        ministerial = (CheckBox)v.findViewById(R.id.ministerial);
        children = (CheckBox)v.findViewById(R.id.children);
        sabbathsch = (CheckBox)v.findViewById(R.id.sabbathSch);

        proceed = (Button)v.findViewById(R.id.proceed);

        checkBoxes = new CheckBox[]{youth,guild,pcmf,brigade,ce,welfare,hospitality,jprc,evangelism,mission,publishing,ministerial,children,sabbathsch};

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.pageThreeProceed(validated(),getGroups());
            }
        });

        return v;
    }

    public List<String> getGroups(){
        groups.clear();
        for (CheckBox c:checkBoxes){
            if (c.isChecked()){
                groups.add(c.getText().toString());
            }
        }
        return groups;
    }

    public void updateObject(User user,List<String> groups){
        this.user = user;
        this.groups.clear();
        this.groups.addAll(groups);
        for (String s:groups){
            for (CheckBox c:checkBoxes){
                if (c.getText().toString().equals(s)){
                    c.setChecked(true);
                }
            }
        }
    }

    private boolean validated(){
        for (CheckBox c:checkBoxes){
            if (c.isChecked()){
                return true;
            }
        }
        Toast.makeText(getActivity(), "Please select at least one group", Toast.LENGTH_SHORT).show();
        return false;
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
