
package com.ria.smartpantry.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ria.smartpantry.R;
import com.ria.smartpantry.data.PantryRepository;

public class SettingsActivity extends AppCompatActivity {

    private Button clearPantryButton;
    private Button aboutButton;
    private Button backButton;
    private TextView appVersionText;

    private PantryRepository pantryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        clearPantryButton = findViewById(R.id.clearPantryButton);
        aboutButton = findViewById(R.id.aboutButton);
        backButton = findViewById(R.id.settingsBackButton);
        appVersionText = findViewById(R.id.appVersionText);

        pantryRepository = new PantryRepository(this);

        appVersionText.setText("Recipe and Pantry Manager");

        clearPantryButton.setOnClickListener(
                v -> confirmClearPantry()
        );

        aboutButton.setOnClickListener(
                v -> showAboutDialog()
        );

        backButton.setOnClickListener(v -> finish());
    }

    private void confirmClearPantry() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Pantry")
                .setMessage(
                        "Are you sure you want to remove all ingredients " +
                        "from your pantry? This action cannot be undone."
                )
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.dismiss()
                )
                .setPositiveButton("Clear", (dialog, which) ->
                        clearPantry()
                )
                .show();
    }

    private void clearPantry() {
        try {
            pantryRepository.clearAllIngredients();

            Toast.makeText(
                    this,
                    "Pantry cleared successfully.",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Could not clear pantry. Please try again.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage(
                        "Recipe and Pantry Manager\n\n" +
                        "Manage your pantry ingredients and discover " +
                        "recipes you can prepare with what you have."
                )
                .setPositiveButton("OK", (dialog, which) ->
                        dialog.dismiss()
                )
                .show();
    }
}

