package com.johntoro.myapplication.views;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johntoro.myapplication.R;
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

    /**
     * instantiates a new FilterFragment
     * @param res passes in a List of Results to instantiate the fragment
     * @return returns a FilterFragment
     */
    public static FilterFragment newInstance(List<Results> res) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putSerializable("res", (Serializable) res);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        res = (List<Results>) getArguments().getSerializable("res");
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns a view
     */
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

    /**
     * defines OnFilterChangeListener
     */
    interface OnFilterChangeListener {
        void onFilterChange(List<Results> res);
    }

    /**
     * instantiates OnFilterChangeListener by implementing onFilterChangeListener.onFilterChange(res)
     * @param listener passes in the List of Results through a listener
     */
    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.onFilterChangeListener = listener;
    }
}