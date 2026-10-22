
package com.ria.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.ria.smartpantry.R;
import com.ria.smartpantry.adapter.PantryAdapter;
import com.ria.smartpantry.data.PantryRepository;
import com.ria.smartpantry.model.Ingredient;

public class PantryActivity extends AppCompatActivity {

    private ListView pantryListView;
    private TextView emptyMessage;
    private Button addIngredientButton;
    private Button suggestRecipesButton;

    private PantryAdapter pantryAdapter;
    private PantryRepository pantryRepository;
    private final List<Ingredient> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        pantryListView = findViewById(R.id.pantryListView);
        emptyMessage = findViewById(R.id.emptyMessage);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        suggestRecipesButton = findViewById(R.id.suggestRecipesButton);

        pantryRepository = new PantryRepository(this);

        pantryAdapter = new PantryAdapter(this, ingredients);
        pantryListView.setAdapter(pantryAdapter);

        addIngredientButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );
            startActivity(intent);
        });

        suggestRecipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });

        pantryListView.setOnItemClickListener((parent, view, position, id) -> {
            Ingredient ingredient = ingredients.get(position);

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            intent.putExtra("ingredient_id", ingredient.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    private void loadIngredients() {
        try {
            ingredients.clear();

            List<Ingredient> savedIngredients =
                    pantryRepository.getAllIngredients();

            if (savedIngredients != null) {
                ingredients.addAll(savedIngredients);
            }

            pantryAdapter.notifyDataSetChanged();

            if (ingredients.isEmpty()) {
                emptyMessage.setVisibility(View.VISIBLE);
                pantryListView.setVisibility(View.GONE);
            } else {
                emptyMessage.setVisibility(View.GONE);
                pantryListView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Could not load pantry ingredients.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

