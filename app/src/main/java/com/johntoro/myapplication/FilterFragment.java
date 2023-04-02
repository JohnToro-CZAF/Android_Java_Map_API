package com.johntoro.myapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.johntoro.myapplication.models.FilterControl;
import com.johntoro.myapplication.models.Results;

import java.io.Serializable;
import java.util.List;

public class FilterFragment extends Fragment {

    private static final String TAG = "FilterFragment";
    private OnFilterChangeListener onFilterChangeListener;
    private Button ratingbtn;
    private Button openingbtn;
    private List<Results> res;

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
        res = (List<Results>) getArguments().getSerializable("res");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        FilterControl filterControl = new FilterControl(false, false);
        ratingbtn = view.findViewById(R.id.rating_filter_button);
        openingbtn = view.findViewById(R.id.is_open_button);
        ratingbtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked rating button");
            filterControl.toggleRating();
            if (filterControl.getRating()){
                List<Results> sorted = filterControl.sort(res);
                onFilterChangeListener.onFilterChange(sorted);
            } else {
                onFilterChangeListener.onFilterChange(res);
            }
        });
        openingbtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: opening button");
            filterControl.toggleActiveHours();
            if (filterControl.getActiveHours()) {
                List<Results> sorted = filterControl.sort(res);
                onFilterChangeListener.onFilterChange(sorted);
            } else {
                onFilterChangeListener.onFilterChange(res);
            }
        });
        return view;
    }
    interface OnFilterChangeListener {
        void onFilterChange(List<Results> res);
    }
    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.onFilterChangeListener = listener;
    }
}