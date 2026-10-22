
package com.ria.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ria.smartpantry.R;
import com.ria.smartpantry.model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnIngredientActionListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientActionListener listener;

    public PantryAdapter(OnIngredientActionListener listener) {
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> newIngredients) {
        ingredients.clear();

        if (newIngredients != null) {
            ingredients.addAll(newIngredients);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder, int position) {

        Ingredient ingredient = ingredients.get(position);

        holder.name.setText(ingredient.getName());

        holder.quantity.setText(String.format(
                Locale.getDefault(), "%s %s",
                formatQuantity(ingredient.getQuantity()),
                ingredient.getUnit()));

        String expiryDate = ingredient.getExpiryDate();

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            holder.expiry.setText("Expiry date: Not specified");
        } else {
            holder.expiry.setText("Expiry date: " + expiryDate);
        }

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(ingredient);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.format(Locale.ROOT, "%.0f", quantity);
        }

        return String.format(Locale.ROOT, "%s", quantity);
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;
        TextView expiry;
        Button editButton;
        Button deleteButton;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvIngredientName);
            quantity = itemView.findViewById(R.id.tvIngredientQuantity);
            expiry = itemView.findViewById(R.id.tvIngredientExpiry);
            editButton = itemView.findViewById(R.id.btnEditIngredient);
            deleteButton = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
