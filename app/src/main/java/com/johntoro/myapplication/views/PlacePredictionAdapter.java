// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
 * A {@link RecyclerView.Adapter} for a {@link AutocompletePrediction}.
 */
public class PlacePredictionAdapter extends RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder> {

    private final List<AutocompletePrediction> predictions = new ArrayList<>();
    private OnPlaceClickListener onPlaceClickListener;

    @NonNull
    @Override
    public PlacePredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlacePredictionViewHolder(
            inflater.inflate(R.layout.place_prediction_item, parent, false));
    }

    @Override
    // Called by LayoutManager
    public void onBindViewHolder(@NonNull PlacePredictionViewHolder holder, int position) {
        final AutocompletePrediction prediction = predictions.get(position);
        holder.setPrediction(prediction);
        holder.itemView.setOnClickListener(v -> {
            if (onPlaceClickListener != null) {
                onPlaceClickListener.onPlaceClicked(prediction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void setPredictions(List<AutocompletePrediction> predictions) {
        this.predictions.clear();
        this.predictions.addAll(predictions);
        // When the data set changes, notify the adapter so that it can update the RecyclerView -> display the items
        notifyDataSetChanged();
    }

    public void setPlaceClickListener(OnPlaceClickListener onPlaceClickListener) {
        this.onPlaceClickListener = onPlaceClickListener;
    }

    public static class PlacePredictionViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView address;

        public PlacePredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            address = itemView.findViewById(R.id.text_view_address);
        }

        public void setPrediction(AutocompletePrediction prediction) {
            title.setText(prediction.getPrimaryText(null));
            address.setText(prediction.getSecondaryText(null));
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClicked(AutocompletePrediction place);
    }
}
