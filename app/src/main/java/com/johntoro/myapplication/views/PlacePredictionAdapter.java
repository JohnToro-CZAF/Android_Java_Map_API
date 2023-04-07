package com.johntoro.myapplication.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.johntoro.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A RecyclerView.Adapter for a AutocompletePrediction.
 */
public class PlacePredictionAdapter extends RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder> {

    /**
     * The list of predictions to display.
     */
    private final List<AutocompletePrediction> predictions = new ArrayList<>();

    /**
     * Listener for when a place is clicked.
     */
    private OnPlaceClickListener onPlaceClickListener;

    /**
     * Creates and returns a new PlacePredictionViewHolder for the given view type.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new PlacePredictionViewHolder that holds a view for an AutocompletePrediction.
     */
    @NonNull
    @Override
    public PlacePredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlacePredictionViewHolder(
                inflater.inflate(R.layout.place_prediction_item, parent, false));
    }

    /**
     * Updates the contents of the given ViewHolder to display the data at the given position.
     *
     * @param holder The ViewHolder to update.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PlacePredictionViewHolder holder, int position) {
        final AutocompletePrediction prediction = predictions.get(position);
        holder.setPrediction(prediction);
        holder.itemView.setOnClickListener(v -> {
            if (onPlaceClickListener != null) {
                onPlaceClickListener.onPlaceClicked(prediction);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the adapter.
     */
    @Override
    public int getItemCount() {
        return predictions.size();
    }

    /**
     * Sets the list of predictions to display.
     *
     * @param predictions The list of predictions.
     */
    public void setPredictions(List<AutocompletePrediction> predictions) {
        this.predictions.clear();
        this.predictions.addAll(predictions);
        // When the data set changes, notify the adapter so that it can update the RecyclerView -> display the items
        notifyDataSetChanged();
    }

    /**
     * Sets the listener for when a place is clicked.
     *
     * @param onPlaceClickListener The listener to set.
     */
    public void setPlaceClickListener(OnPlaceClickListener onPlaceClickListener) {
        this.onPlaceClickListener = onPlaceClickListener;
    }

    /**
     * A ViewHolder for an AutocompletePrediction.
     */
    public static class PlacePredictionViewHolder extends RecyclerView.ViewHolder {

        /**
         * The title TextView for the prediction.
         */
        private final TextView title;

        /**
         * The address TextView for the prediction.
         */
        private final TextView address;

        /**
         * Creates a new PlacePredictionViewHolder with the given view.
         *
         * @param itemView The view to hold.
         */
        public PlacePredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            address = itemView.findViewById(R.id.text_view_address);
        }

        /**
         * Sets the prediction to display in the ViewHolder.
         *
         * @param prediction The prediction to display.
         */
        public void setPrediction(AutocompletePrediction prediction) {
            title.setText(prediction.getPrimaryText(null));
            address.setText(prediction.getSecondaryText(null));
        }
    }

    /**
     * Interface definition for a callback to be invoked when a place is clicked.
     */
    public interface OnPlaceClickListener {

        /**
         * When a place is clicked.
         * @param place The place that was clicked.
         */
        void onPlaceClicked(AutocompletePrediction place);
    }
}
