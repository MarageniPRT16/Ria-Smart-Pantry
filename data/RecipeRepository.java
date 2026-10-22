
package com.ria.smartpantry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ria.smartpantry.model.Recipe;
import com.ria.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private final DatabaseHelper dbHelper;

    public RecipeRepository(Context context) {
        dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        RecipeSeeder.seedIfNeeded(db);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null, null, null, null, null, "name ASC")) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id"));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow("instructions"));

                recipes.add(new Recipe(
                        id, name, getIngredientsForRecipe(db, id),
                        instructions));
            }
        }

        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                "id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow("instructions"));

                return new Recipe(
                        recipeId, name,
                        getIngredientsForRecipe(db, recipeId),
                        instructions);
            }
        }

        return null;
    }

    private List<RecipeIngredient> getIngredientsForRecipe(
            SQLiteDatabase db, int recipeId) {

        List<RecipeIngredient> ingredients = new ArrayList<>();

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, "ingredient_name ASC")) {

            while (cursor.moveToNext()) {
                ingredients.add(new RecipeIngredient(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("recipe_id")),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("ingredient_name")),
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("required_quantity")),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit"))
                ));
            }
        }

        return ingredients;
    }

    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long recipeId = -1;

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put("name", recipe.getName().trim());
            values.put("instructions", recipe.getInstructions());

            recipeId = db.insertOrThrow(
                    DatabaseHelper.TABLE_RECIPES, null, values);

            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put("recipe_id", recipeId);
                ingredientValues.put("ingredient_name",
                        ingredient.getIngredientName().trim());
                ingredientValues.put("required_quantity",
                        ingredient.getRequiredQuantity());
                ingredientValues.put("unit", ingredient.getUnit().trim());

                db.insertOrThrow(
                        DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                        null, ingredientValues);
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        return recipeId;
    }

    public void close() {
        dbHelper.close();
    }
}
