
package com.ria.smartpantry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ria.smartpantry.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PantryRepository {

    private final DatabaseHelper dbHelper;

    public PantryRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName().trim());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit().trim());
        values.put("expiry_date", ingredient.getExpiryDate());

        return db.insert(DatabaseHelper.TABLE_PANTRY,
                null, values);
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY,
                null, null, null, null, null, "name ASC")) {

            while (cursor.moveToNext()) {
                ingredients.add(new Ingredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"))
                ));
            }
        }

        return ingredients;
    }

    public boolean updateIngredient(Ingredient ingredient) {
        if (ingredient.getId() <= 0) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName().trim());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit().trim());
        values.put("expiry_date", ingredient.getExpiryDate());

        return db.update(
                DatabaseHelper.TABLE_PANTRY,
                values,
                "id = ?",
                new String[]{String.valueOf(ingredient.getId())}
        ) > 0;
    }

    public boolean deleteIngredient(int id) {
        if (id <= 0) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(
                DatabaseHelper.TABLE_PANTRY,
                "id = ?",
                new String[]{String.valueOf(id)}
        ) > 0;
    }

    public void close() {
        dbHelper.close();
    }
}
