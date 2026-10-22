
package com.ria.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ria.smartpantry.R;
import com.ria.smartpantry.model.Recipe;
import com.ria.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes.clear();

        if (newRecipes != null) {
            recipes.addAll(newRecipes);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);

        holder.name.setText(recipe.getName());

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        StringBuilder summary = new StringBuilder("Requires: ");

        if (ingredients != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (i > 0) {
                    summary.append(", ");
                }

                summary.append(ingredients.get(i).getIngredientName());
            }
        }

        holder.ingredients.setText(summary.toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ingredients;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvRecipeName);
            ingredients = itemView.findViewById(R.id.tvRecipeIngredients);
        }
    }
}
