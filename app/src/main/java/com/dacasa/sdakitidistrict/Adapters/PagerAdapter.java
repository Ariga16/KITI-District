package com.dacasa.sdakitidistrict.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    PageListener pageListener;
    Context context;
    FragmentManager manager;



    public PagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
        pageListener = (PageListener)context;
        this.manager = fm;
    }


    public void addTitle(String title){
        titles.add(title);
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    public void addFrag(Fragment fragment,String title){
        fragments.add(fragment);
        titles.add(title);
    }

    public void addFrag(Fragment fragment,String title,int pos){
        fragments.add(pos,fragment);
        titles.add(pos,title);
    }

    public void addAllFrags(Fragment... frag){
        fragments.addAll(Arrays.asList(frag));
    }

    public void addAll(List<Fragment> f){
        fragments.addAll(f);
    }


    public void addAllT(List<String> f){
        titles.addAll(f);
    }


    public void addAllTitles(String... title){
        titles.addAll(Arrays.asList(title));
    }

    public void removeFrag(int i){
        fragments.remove(i);
        titles.remove(i);
        notifyDataSetChanged();
        try {
            manager.getFragments().remove(i);
        }catch (Exception e){}
    }

    public void clearAll(){
        titles.clear();
        fragments.clear();
        notifyDataSetChanged();
        if (manager.getFragments() != null){
            manager.getFragments().clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        pageListener.pageCount(fragments.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public List<String> getTitles() {
        return titles;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public interface PageListener{
        void pageCount(int size);
    }



}
