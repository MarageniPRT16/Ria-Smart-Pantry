
package com.ria.smartpantry.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.ria.smartpantry.R;
import com.ria.smartpantry.data.RecipeRepository;
import com.ria.smartpantry.model.Recipe;
import com.ria.smartpantry.model.RecipeIngredient;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeName;
    private TextView recipeDescription;
    private TextView recipeIngredients;
    private TextView recipeInstructions;
    private Button backButton;

    private RecipeRepository recipeRepository;
    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeName = findViewById(R.id.recipeDetailName);
        recipeDescription = findViewById(R.id.recipeDetailDescription);
        recipeIngredients = findViewById(R.id.recipeDetailIngredients);
        recipeInstructions = findViewById(R.id.recipeDetailInstructions);
        backButton = findViewById(R.id.recipeDetailBackButton);

        recipeRepository = new RecipeRepository(this);

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        backButton.setOnClickListener(v -> finish());

        if (recipeId == -1) {
            Toast.makeText(
                    this,
                    "Recipe not found.",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            return;
        }

        loadRecipeDetails();
    }

    private void loadRecipeDetails() {
        try {
            Recipe recipe = recipeRepository.getRecipeById(recipeId);

            if (recipe == null) {
                Toast.makeText(
                        this,
                        "Recipe could not be found.",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
                return;
            }

            recipeName.setText(recipe.getName());

            String description = recipe.getDescription();
            recipeDescription.setText(
                    description == null || description.trim().isEmpty()
                            ? "No description available."
                            : description
            );

            displayIngredients(recipe);
            displayInstructions(recipe);

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Could not load recipe details.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void displayIngredients(Recipe recipe) {
        List<RecipeIngredient> ingredients =
                recipe.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            recipeIngredients.setText(
                    "No ingredient details available."
            );
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {
            builder.append("\u2022 ");

            builder.append(ingredient.getName());

            double quantity = ingredient.getQuantity();

            if (quantity > 0) {
                builder.append(" - ");
                builder.append(quantity);
            }

            String unit = ingredient.getUnit();

            if (unit != null && !unit.trim().isEmpty()) {
                builder.append(" ");
                builder.append(unit);
            }

            builder.append("\n");
        }

        recipeIngredients.setText(builder.toString().trim());
    }

    private void displayInstructions(Recipe recipe) {
        String instructions = recipe.getInstructions();

        if (instructions == null || instructions.trim().isEmpty()) {
            recipeInstructions.setText(
                    "No preparation instructions available."
            );
        } else {
            recipeInstructions.setText(instructions);
        }
    }
}

