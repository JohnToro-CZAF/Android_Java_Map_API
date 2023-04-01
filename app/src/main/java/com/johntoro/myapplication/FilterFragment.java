package com.johntoro.myapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.johntoro.myapplication.models.FilterControl;
import com.johntoro.myapplication.models.Results;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FilterFragment extends Fragment {

    private static final String TAG = "FilterFragment";

    protected static final String RESULTS_LIST = "res";

    private AdapterView.OnItemClickListener onItemClickListener;
    private Button ratingbtn;
    private Button openingbtn;
    private OnFilterSelectedListener mRatingListener;
    private List<Results> res;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(List<Results> res) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putSerializable("res", (Serializable) res);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        getArguments().getSerializable("res");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        ratingbtn = view.findViewById(R.id.rating_filter_button);
        openingbtn = view.findViewById(R.id.is_open_button);
        ratingbtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked rating button");
            mRatingListener.onFilterSelected(res);

            FilterControl fc = new FilterControl(true, false);
            Log.d(TAG, "Filter control created");
            List<Results> results = null;
            if (getArguments().getSerializable(RESULTS_LIST) == null) {
                Log.d(TAG, "passList: results is null");
            } else {
                results = (List<Results>) getArguments().getSerializable(RESULTS_LIST);
                List<Results> rescopy = new ArrayList<>(results);
                Log.d(TAG, String.valueOf(rescopy.size()));
                Log.d(TAG, "before sort rating copy");
                fc.sort(rescopy);
                Log.d(TAG, "can i get here Rating?");
                Log.d(TAG, rescopy.toString());
                NearbyFacilitiesListFragment nearbyFacilitiesListFragment = NearbyFacilitiesListFragment.newInstance(rescopy);
            }


        });
        openingbtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: opening button");
            mRatingListener.onFilterSelected(res);

            FilterControl fc = new FilterControl(false, true);
            Log.d(TAG, "Filter control created");
            List<Results> results = null;
            if (getArguments().getSerializable(RESULTS_LIST) == null) {
                Log.d(TAG, "passList: results is null");
            } else {
                results = (List<Results>) getArguments().getSerializable(RESULTS_LIST);
                List<Results> rescopy = new ArrayList<>(results);
                Log.d(TAG, String.valueOf(rescopy.size()));
                Log.d(TAG, "before sort hour copy");
                fc.sort(rescopy);
                Log.d(TAG, "can i get here? Hour");
                NearbyFacilitiesListFragment nearbyFacilitiesListFragment = NearbyFacilitiesListFragment.newInstance(rescopy);
            }

        });
        return view;
    }

    public interface OnFilterSelectedListener{
        void onFilterSelected(List<Results> res);
    }
    public void setOnFilterSelectedListener(OnFilterSelectedListener listener){
        mRatingListener = listener;
    }
}