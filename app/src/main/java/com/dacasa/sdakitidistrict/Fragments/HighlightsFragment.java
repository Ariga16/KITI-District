package com.dacasa.sdakitidistrict.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dacasa.sdakitidistrict.Activities.BibleView;
import com.dacasa.sdakitidistrict.Adapters.BibleBooksAdapter;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.POJOS.Book;
import com.dacasa.sdakitidistrict.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pitt.library.fresh.FreshDownloadView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HighlightsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HighlightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HighlightsFragment extends Fragment implements BibleBooksAdapter.BookListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View downloader,download_prompt,downloading_view,quick_nav,go,cover;
    FreshDownloadView progressBar;
    TextView percent_download,progress;
    ImageButton download_button;
    RecyclerView recycler;
    Bible bible;
    StorageReference storage;
    BibleBooksAdapter bibleBooksAdapter;
    ArrayAdapter<String> booksAdapter,chaptersAdapter,verseAdapter;
    DatabaseReference database;
    FileDownloadTask downloadTask;
    Spinner books,chapters,verses;
    private  boolean downloading = false;





    public HighlightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HighlightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HighlightsFragment newInstance(String param1, String param2) {
        HighlightsFragment fragment = new HighlightsFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_highlights, container,false);


        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sda-kiti-district-9a0cf.appspot.com");
        database = FirebaseDatabase.getInstance().getReference();
        bible = new Bible(getActivity());

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void bookClicked(Book book) {
        Intent intent = new Intent(getActivity(), BibleView.class);
        intent.putExtra("book",book.getIndex());
        startActivity(intent);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
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
