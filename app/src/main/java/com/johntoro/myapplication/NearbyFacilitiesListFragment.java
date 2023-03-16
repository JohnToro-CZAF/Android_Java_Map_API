package com.johntoro.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.johntoro.myapplication.databinding.FragmentNearbyFacilitiesListListBinding;
import com.johntoro.myapplication.databinding.FragmentNearbyFacilitiesListListItemBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     NearbyFacilitiesListFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class NearbyFacilitiesListFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    protected static final String ARG_ITEM_COUNT = "item_count";
    private final String TAG = this.getClass().getSimpleName();
    FragmentNearbyFacilitiesListListBinding binding;
    // TODO: Customize parameters
    public static NearbyFacilitiesListFragment newInstance(int itemCount) {
        final NearbyFacilitiesListFragment fragment = new NearbyFacilitiesListFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        binding = FragmentNearbyFacilitiesListListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: " + view.toString());
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        // factility info is a text view item in recycler view
        final TextView facilityInfo;
        ViewHolder(FragmentNearbyFacilitiesListListItemBinding binding) {
            super(binding.getRoot());
            facilityInfo = binding.facilityInfo;
        }
    }
    // Bind data to each item
    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final int mItemCount;
        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }
        @NonNull
        @Override
        // When ever a recycler (container) created then this method will be called -> inflate the item out
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentNearbyFacilitiesListListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        @Override
        // Bind data to each item
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.facilityInfo.setText(String.valueOf(position));
        }
        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }
}