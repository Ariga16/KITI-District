package com.dacasa.sdakitidistrict.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dacasa.sdakitidistrict.Activities.PostDetailActivity;
import com.dacasa.sdakitidistrict.Adapters.PostAdapter;
import com.dacasa.sdakitidistrict.Commoners.PostItemClickListener;
import com.dacasa.sdakitidistrict.Models.Post;
import com.dacasa.sdakitidistrict.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChurchLevelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChurchLevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChurchLevelFragment extends Fragment implements PostItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static  int index = -1;
    public static  int top = -1;
    LinearLayoutManager mLayoutManager;


    private OnFragmentInteractionListener mListener;

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;
    ProgressBar progressBar;





    public ChurchLevelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChurchLevelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChurchLevelFragment newInstance(String param1, String param2) {
        ChurchLevelFragment fragment = new ChurchLevelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_church_level, container, false);
        postRecyclerView = fragmentView.findViewById(R.id.postRV);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        // save firebase data offline
        databaseReference.keepSynced(true);

        // progressbar on data display
        progressBar = fragmentView.findViewById(R.id.progressbar);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get lists posts from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot postsnap: dataSnapshot.getChildren()){

                    Post post = postsnap.getValue(Post.class);
                    // add reverse from top to bottom
                    postList.add(0,post);
                }

                postAdapter = new PostAdapter(getActivity(), postList, new PostItemClickListener() {
                    @Override
                    public void onPostClick(Post post, ImageView postImageView) {

                        Intent intent = new Intent(getActivity(),PostDetailActivity.class);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),postImageView,"SharedName");
                        startActivity(intent,options.toBundle());

                    }
                });
                postRecyclerView.setAdapter(postAdapter);
                // add reverse from top to bottom
                postRecyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onPause() {
        super.onPause();
        //index = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        //View v = postRecyclerView.getChildAt(0);
        //top = (v == null) ? 0: (v.getTop() - postRecyclerView.getPaddingTop());

    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (index != -1){
           // mLayoutManager.scrollToPositionWithOffset(index,top);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPostClick(Post post, ImageView postImageView) {
        Intent intent = new Intent(getActivity(),PostDetailActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),postImageView,"SharedName");
        startActivity(intent,options.toBundle());


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.kk
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
