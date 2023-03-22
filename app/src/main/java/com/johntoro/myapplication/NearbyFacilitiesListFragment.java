package com.johntoro.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.johntoro.myapplication.databinding.FragmentNearbyFacilitiesListListBinding;
import com.johntoro.myapplication.databinding.FragmentNearbyFacilitiesListListItemBinding;
import com.johntoro.myapplication.models.Results;

import java.io.Serializable;
import java.util.List;

public class NearbyFacilitiesListFragment extends Fragment {

    protected static final String RESULTS_LIST = "results";
    private static final String TAG = NearbyFacilitiesListFragment.class.getSimpleName();
    private FragmentNearbyFacilitiesListListBinding binding;
    private OnItemLocateClickListener onItemLocateClickListener;
    private OnItemDetailsClickListener onItemDetailsClickListener;

    public static NearbyFacilitiesListFragment newInstance(List<Results> results) {
        final NearbyFacilitiesListFragment fragment = new NearbyFacilitiesListFragment();
        final Bundle args = new Bundle();
        args.putSerializable("results", (Serializable) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNearbyFacilitiesListListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assert getArguments() != null;
        if (getArguments().getSerializable(RESULTS_LIST) == null) {
            Log.d(TAG, "onViewCreated: results is null");
        } else {
            List<Results> results = (List<Results>) getArguments().getSerializable(RESULTS_LIST);
            recyclerView.setAdapter(new ItemAdapter(results));
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setOnItemClickListener(OnItemLocateClickListener listener) {
        this.onItemLocateClickListener = listener;
    }
    public void setOnItemDetailsClickListener(OnItemDetailsClickListener listener) {
        this.onItemDetailsClickListener = listener;
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final List<Results> mResults;
        ItemAdapter(List<Results> results) {
            this.mResults = results;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentNearbyFacilitiesListListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Results facilityDetails = mResults.get(position);
            String nameFacility = mResults.get(position).getName();
            String addressFacility = mResults.get(position).getVicinity();
            holder.facilityName.setText(nameFacility);
            holder.facilityAddress.setText(addressFacility);
            holder.locateFacility.setOnClickListener(v -> {
                // Locate facility on map
                Log.d(TAG, "onClick: " + facilityDetails.toString());
                LatLng latLng = facilityDetails.getGeometry().getLocation().getLatLng();
                onItemLocateClickListener.onItemLocateClickListener(latLng);
            });
            holder.facilityDetails.setOnClickListener(v -> {
                Log.d(TAG, "onClick: " + facilityDetails.toString());
                onItemDetailsClickListener.onItemDetailsClickListener(facilityDetails);
            });
        }
        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView facilityName, facilityAddress;
        final View facilityDetails, locateFacility;
        ViewHolder(FragmentNearbyFacilitiesListListItemBinding binding) {
            super(binding.getRoot());
            locateFacility = binding.placeImageView;
            facilityDetails = binding.details;
            facilityName = binding.facilityName;
            facilityAddress = binding.facilityAddress;
        }
    }
    interface OnItemLocateClickListener {
        void onItemLocateClickListener(LatLng latLng);
    }
    interface OnItemDetailsClickListener {
        void onItemDetailsClickListener(Results facilityDetails);
    }
}