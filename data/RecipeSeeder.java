
package com.ria.smartpantry.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecipeSeeder {

    private RecipeSeeder() {
    }

    public static void seedIfNeeded(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " +
                        DatabaseHelper.TABLE_RECIPES, null);

        try {
            if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                return;
            }
        } finally {
            cursor.close();
        }

        db.beginTransaction();

        try {
            addRecipe(db, "Tomato Omelette",
                    "Beat the eggs, mix with chopped tomato and salt, " +
                    "then cook in a lightly oiled pan.",
                    item("egg", 2, "piece"),
                    item("tomato", 100, "g"),
                    item("oil", 5, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Cheese Omelette",
                    "Beat the eggs, cook in a pan, add cheese and fold.",
                    item("egg", 2, "piece"),
                    item("cheese", 30, "g"),
                    item("oil", 5, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Garlic Tomato Pasta",
                    "Boil pasta. Cook garlic and tomato in oil, " +
                    "then combine with the drained pasta.",
                    item("pasta", 100, "g"),
                    item("tomato", 100, "g"),
                    item("garlic", 5, "g"),
                    item("oil", 10, "ml"));

            addRecipe(db, "Egg Fried Rice",
                    "Scramble the egg, add cooked rice and peas, " +
                    "then stir-fry until heated through.",
                    item("rice", 200, "g"),
                    item("egg", 1, "piece"),
                    item("peas", 50, "g"),
                    item("oil", 10, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Potato Mash",
                    "Boil potatoes until soft, then mash with milk, " +
                    "butter and salt.",
                    item("potato", 250, "g"),
                    item("milk", 50, "ml"),
                    item("butter", 15, "g"),
                    item("salt", 1, "g"));

            addRecipe(db, "Banana Pancakes",
                    "Mash the banana, mix with egg, flour and milk, " +
                    "then cook small pancakes in a pan.",
                    item("banana", 1, "piece"),
                    item("egg", 1, "piece"),
                    item("flour", 50, "g"),
                    item("milk", 50, "ml"));

            addRecipe(db, "Cheese Sandwich",
                    "Place cheese between the bread slices and toast " +
                    "until golden and the cheese melts.",
                    item("bread", 2, "slice"),
                    item("cheese", 30, "g"),
                    item("butter", 5, "g"));

            addRecipe(db, "Garlic Butter Pasta",
                    "Boil pasta. Melt butter, cook garlic briefly " +
                    "and mix with the drained pasta.",
                    item("pasta", 100, "g"),
                    item("garlic", 5, "g"),
                    item("butter", 15, "g"),
                    item("salt", 1, "g"));

            addRecipe(db, "Tomato Cheese Toast",
                    "Place tomato and cheese on bread and toast " +
                    "until the cheese melts.",
                    item("bread", 2, "slice"),
                    item("tomato", 50, "g"),
                    item("cheese", 30, "g"));

            addRecipe(db, "Spinach Omelette",
                    "Cook chopped spinach briefly, add beaten eggs " +
                    "and cook until set.",
                    item("egg", 2, "piece"),
                    item("spinach", 50, "g"),
                    item("oil", 5, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Carrot Soup",
                    "Boil carrots and onion in water until soft, " +
                    "then blend and season.",
                    item("carrot", 200, "g"),
                    item("onion", 50, "g"),
                    item("water", 300, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Onion Fried Potatoes",
                    "Cook sliced potatoes and onion in oil until " +
                    "tender and golden.",
                    item("potato", 200, "g"),
                    item("onion", 50, "g"),
                    item("oil", 10, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Apple Banana Fruit Bowl",
                    "Wash and cut the apple and banana, then mix.",
                    item("apple", 1, "piece"),
                    item("banana", 1, "piece"));

            addRecipe(db, "Peanut Butter Toast",
                    "Toast the bread and spread peanut butter evenly.",
                    item("bread", 2, "slice"),
                    item("peanut butter", 30, "g"));

            addRecipe(db, "Cucumber Tomato Salad",
                    "Chop cucumber and tomato, then mix with oil " +
                    "and salt.",
                    item("cucumber", 100, "g"),
                    item("tomato", 100, "g"),
                    item("oil", 5, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Sweet Corn Rice",
                    "Cook the rice, heat the corn and mix together " +
                    "with salt.",
                    item("rice", 150, "g"),
                    item("corn", 80, "g"),
                    item("salt", 1, "g"));

            addRecipe(db, "Milk Porridge",
                    "Cook oats with milk until soft, stirring regularly.",
                    item("oats", 50, "g"),
                    item("milk", 250, "ml"));

            addRecipe(db, "Garlic Spinach",
                    "Cook garlic briefly in oil, add spinach and " +
                    "stir until wilted.",
                    item("spinach", 100, "g"),
                    item("garlic", 5, "g"),
                    item("oil", 5, "ml"),
                    item("salt", 1, "g"));

            addRecipe(db, "Carrot Potato Mash",
                    "Boil carrot and potato until tender, then mash " +
                    "with butter and salt.",
                    item("carrot", 100, "g"),
                    item("potato", 150, "g"),
                    item("butter", 10, "g"),
                    item("salt", 1, "g"));

            addRecipe(db, "Tomato Egg Rice",
                    "Scramble the egg, cook the tomato, add cooked " +
                    "rice and combine.",
                    item("rice", 150, "g"),
                    item("egg", 1, "piece"),
                    item("tomato", 100, "g"),
                    item("oil", 5, "ml"));

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    private static RecipeItem item(String name,
                                   double quantity, String unit) {
        return new RecipeItem(name, quantity, unit);
    }

    private static void addRecipe(SQLiteDatabase db,
                                  String name,
                                  String instructions,
                                  RecipeItem... ingredients) {

        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("instructions", instructions);

        long recipeId = db.insertOrThrow(
                DatabaseHelper.TABLE_RECIPES,
                null, recipeValues);

        for (RecipeItem ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", ingredient.name);
            ingredientValues.put("required_quantity", ingredient.quantity);
            ingredientValues.put("unit", ingredient.unit);

            db.insertOrThrow(
                    DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                    null, ingredientValues);
        }
    }

    private static class RecipeItem {
        final String name;
        final double quantity;
        final String unit;

        RecipeItem(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}
