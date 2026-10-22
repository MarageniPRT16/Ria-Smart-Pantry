
package com.ria.smartpantry.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ria.smartpantry.R;
import com.ria.smartpantry.data.PantryRepository;
import com.ria.smartpantry.model.Ingredient;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText quantityInput;
    private EditText unitInput;
    private Button saveButton;
    private Button cancelButton;

    private PantryRepository pantryRepository;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        nameInput = findViewById(R.id.ingredientNameInput);
        quantityInput = findViewById(R.id.ingredientQuantityInput);
        unitInput = findViewById(R.id.ingredientUnitInput);
        saveButton = findViewById(R.id.saveIngredientButton);
        cancelButton = findViewById(R.id.cancelIngredientButton);

        pantryRepository = new PantryRepository(this);

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != -1) {
            loadIngredient();
            saveButton.setText("Update Ingredient");
        }

        saveButton.setOnClickListener(v -> saveIngredient());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadIngredient() {
        Ingredient ingredient =
                pantryRepository.getIngredientById(ingredientId);

        if (ingredient == null) {
            Toast.makeText(
                    this,
                    "Ingredient not found.",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            return;
        }

        nameInput.setText(ingredient.getName());
        quantityInput.setText(String.valueOf(ingredient.getQuantity()));
        unitInput.setText(ingredient.getUnit());
    }

    private void saveIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText =
                quantityInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(quantityText)
                || TextUtils.isEmpty(unit)) {

            Toast.makeText(
                    this,
                    "Please complete all fields.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantity = -1;
        }

        if (quantity <= 0 || Double.isInfinite(quantity)
                || Double.isNaN(quantity)) {
            Toast.makeText(
                    this,
                    "Enter a quantity greater than zero.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Ingredient ingredient = new Ingredient(
                name,
                quantity,
                unit
        );

        try {
            if (ingredientId == -1) {
                pantryRepository.addIngredient(ingredient);
                Toast.makeText(
                        this,
                        "Ingredient added successfully.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                ingredient.setId(ingredientId);
                pantryRepository.updateIngredient(ingredient);
                Toast.makeText(
                        this,
                        "Ingredient updated successfully.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            finish();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Could not save ingredient. Please try again.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

