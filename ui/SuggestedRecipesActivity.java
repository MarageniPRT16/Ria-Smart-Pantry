
package com.ria.smartpantry.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.ria.smartpantry.R;
import com.ria.smartpantry.adapter.RecipeAdapter;
import com.ria.smartpantry.data.PantryRepository;
import com.ria.smartpantry.data.RecipeRepository;
import com.ria.smartpantry.logic.RecipeMatcher;
import com.ria.smartpantry.model.Ingredient;
import com.ria.smartpantry.model.Recipe;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private ListView recipesListView;
    private TextView emptyMessage;

    private RecipeAdapter recipeAdapter;
    private PantryRepository pantryRepository;
    private RecipeRepository recipeRepository;
    private RecipeMatcher recipeMatcher;

    private final List<Recipe> suggestedRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recipesListView = findViewById(R.id.suggestedRecipesListView);
        emptyMessage = findViewById(R.id.emptyRecipesMessage);

        pantryRepository = new PantryRepository(this);
        recipeRepository = new RecipeRepository(this);
        recipeMatcher = new RecipeMatcher();

        recipeAdapter = new RecipeAdapter(this, suggestedRecipes);
        recipesListView.setAdapter(recipeAdapter);

        recipesListView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Recipe recipe = suggestedRecipes.get(position);

                    android.content.Intent intent = new android.content.Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );

                    intent.putExtra("recipe_id", recipe.getId());
                    startActivity(intent);
                }
        );

        loadSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        try {
            List<Ingredient> pantryIngredients =
                    pantryRepository.getAllIngredients();

            List<Recipe> allRecipes =
                    recipeRepository.getAllRecipes();

            suggestedRecipes.clear();

            if (pantryIngredients != null && allRecipes != null) {
                List<Recipe> matches = recipeMatcher.findMatchingRecipes(
                        allRecipes,
                        pantryIngredients
                );

                if (matches != null) {
                    suggestedRecipes.addAll(matches);
                }
            }

            recipeAdapter.notifyDataSetChanged();

            if (suggestedRecipes.isEmpty()) {
                emptyMessage.setText(
                        "No matching recipes found. Add more ingredients to your pantry."
                );
                emptyMessage.setVisibility(View.VISIBLE);
                recipesListView.setVisibility(View.GONE);
            } else {
                emptyMessage.setVisibility(View.GONE);
                recipesListView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            emptyMessage.setText("Could not load suggested recipes.");
            emptyMessage.setVisibility(View.VISIBLE);
            recipesListView.setVisibility(View.GONE);

            Toast.makeText(
                    this,
                    "An error occurred while loading recipes.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

