/**

 This class is a fragment that displays a list of nearby facilities. It uses a RecyclerView to display the list of facilities.
 The list of facilities is passed in as an argument to the newInstance method.
 This class also has four interfaces: OnItemLocateClickListener, OnItemDetailsClickListener, OnItemFavoriteClickListener, and IsItemFavorite.
 These interfaces are used to communicate with the Activity that this Fragment is attached to when an item in the list is clicked.
 */
package com.johntoro.myapplication.views;

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

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
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
    private OnItemFavoriteClickListener onItemFavoriteClickListener;
    private IsItemFavorite isItemFavorite;

    /**

     This method creates a new instance of the NearbyFacilitiesListFragment.
     @param results A list of Results objects to display in the fragment.
     @return A new instance of the NearbyFacilitiesListFragment.
     */
    public static NearbyFacilitiesListFragment newInstance(List<Results> results) {
        final NearbyFacilitiesListFragment fragment = new NearbyFacilitiesListFragment();
        final Bundle args = new Bundle();
        args.putSerializable("results", (Serializable) results);
        fragment.setArguments(args);
        return fragment;
    }

    /**

     This method inflates the layout for the fragment and returns the root view.
     {@inheritDoc}
     @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNearbyFacilitiesListListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**

     This method is called after onCreateView and is used to set up the RecyclerView and the adapter for the list of facilities.
     {@inheritDoc}
     @param view The View returned by onCreateView.
     @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
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

    /**

     This method is called when the fragment's view is destroyed.
     {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**

     Sets the OnItemLocateClickListener.
     @param listener The listener to set.
     */
    public void setOnItemClickListener(OnItemLocateClickListener listener) {
        this.onItemLocateClickListener = listener;
    }

    /**

     Sets the OnItemDetailsClickListener.
     @param listener The listener to set.
     */
    public void setOnItemDetailsClickListener(OnItemDetailsClickListener listener) {
        this.onItemDetailsClickListener = listener;
    }

    /**

     Sets the OnItemFavoriteClickListener.
     @param listener The listener to set.
     */
    public void setOnItemFavoriteClickListener(OnItemFavoriteClickListener listener) {
        this.onItemFavoriteClickListener = listener;
    }

    /**

     Sets the IsItemFavorite listener.
     @param listener The listener to set.
     */
    public void setIsItemFavorite(IsItemFavorite listener) {
        this.isItemFavorite = listener;
    }

    /**

     This interface provides a way to communicate with the Activity that this Fragment is attached to when an item in the list is clicked.
     The ItemAdapter class is responsible for populating the RecyclerView with the facility details obtained from the API response.
     It extends the RecyclerView.Adapter class and uses the ViewHolder class to optimize the RecyclerView's performance by caching the views of each list item.
     The class defines the onCreateViewHolder method which inflates the layout for the list item and returns a new ViewHolder object.
     The onBindViewHolder method binds the data to the views of the ViewHolder object and sets the click listeners for each view.
     The class also defines a getItemCount method that returns the number of items in the list.
     */
    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final List<Results> mResults;
        /**

         Constructs an ItemAdapter with the given list of Results.
         @param results The list of Results to be displayed in the RecyclerView.
         */
        ItemAdapter(List<Results> results) {
            this.mResults = results;
        }

        /**

         Called when a new ViewHolder is created.
         @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
         @param viewType The view type of the new View.
         @return A new ViewHolder that holds a View of the given view type.
         */
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentNearbyFacilitiesListListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        /**

         Called when a ViewHolder is bound to a position in the RecyclerView.
         @param holder The ViewHolder to bind data to.
         @param position The position of the item in the list.
         */
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
            holder.favoriteBtn.setFavorite(isItemFavorite.isItemFavorite(facilityDetails));
            holder.favoriteBtn.setOnFavoriteChangeListener((MaterialFavoriteButton buttonView, boolean favorite) -> {
                onItemFavoriteClickListener.onItemFavouriteClickListener(facilityDetails, favorite);
            });
        }

        /**

         Returns the number of items in the list.
         @return The number of items in the list.
         */
        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    /**

     This static class represents a single item view in the RecyclerView. It holds the views and their
     corresponding data for each item in the list.
     The views that are contained within this ViewHolder include facilityName, facilityAddress, locateFacility,
     facilityDetails, and favoriteBtn. These views are initialized in the constructor using the passed-in
     FragmentNearbyFacilitiesListListItemBinding.
     */
    private static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView facilityName, facilityAddress;
        final View facilityDetails, locateFacility;
        final MaterialFavoriteButton favoriteBtn;

        /**
         * Constructs a ViewHolder with the given FragmentNearbyFacilitiesListListItemBinding.
         * @param binding
         */
        ViewHolder(FragmentNearbyFacilitiesListListItemBinding binding) {
            super(binding.getRoot());
            locateFacility = binding.placeImageView;
            facilityDetails = binding.details;
            facilityName = binding.facilityName;
            facilityAddress = binding.facilityAddress;
            favoriteBtn = binding.favoriteButton;
        }
    }

    /**

    An interface used to handle click events when a user clicks on the "Locate" button of an item in the nearby facilities list.
    */
    interface OnItemLocateClickListener {
        /**
        Called when the "Locate" button of an item in the nearby facilities list is clicked.
        @param latLng the latitude and longitude coordinates of the facility to be located
        */
        void onItemLocateClickListener(LatLng latLng);
    }

    /**

     An interface used to handle click events when a user clicks on the "Details" button of an item in the nearby facilities list.
     */
    interface OnItemDetailsClickListener {

        /**
         * Called when the "Details" button of an item in the nearby facilities list is clicked.
         * @param facilityDetails
         */
        void onItemDetailsClickListener(Results facilityDetails);
    }

    /**

     An interface used to handle click events when a user clicks on the "Favorite" button of an item in the nearby facilities list.
     */
    interface OnItemFavoriteClickListener {

        /**
         * Called when the "Favorite" button of an item in the nearby facilities list is clicked.
         * @param facilityDetails
         * @param isFavourite
         */
        void onItemFavouriteClickListener(Results facilityDetails, boolean isFavourite);
    }

    /**

     An interface used to check if an item is a favorite.
     */
    interface  IsItemFavorite {
        /**
         * Called to check if an item is a favorite.
         * @param facilityDetails
         * @return boolean value that indicates if the item is a favorite
         */
        boolean isItemFavorite(Results facilityDetails);
    }
}
