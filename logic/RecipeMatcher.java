
package com.ria.smartpantry.logic;

import com.ria.smartpantry.model.Ingredient;
import com.ria.smartpantry.model.Recipe;
import com.ria.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeMatcher {

    public List<Recipe> getMatchingRecipes(
            List<Recipe> recipes,
            List<Ingredient> pantry) {

        List<Recipe> matchingRecipes = new ArrayList<>();

        if (recipes == null || pantry == null) {
            return matchingRecipes;
        }

        Map<String, Double> available = new HashMap<>();

        for (Ingredient ingredient : pantry) {
            if (ingredient == null
                    || ingredient.getName() == null
                    || ingredient.getUnit() == null
                    || ingredient.getQuantity() < 0) {
                continue;
            }

            String name = normalizeName(ingredient.getName());
            String unit = normalizeUnit(ingredient.getUnit());

            String key = name + "|" + unit;

            double baseQuantity = toBaseQuantity(
                    ingredient.getQuantity(), unit);

            available.merge(key, baseQuantity, Double::sum);
        }

        for (Recipe recipe : recipes) {
            if (recipe != null && canMake(recipe, available)) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    private boolean canMake(
            Recipe recipe,
            Map<String, Double> available) {

        List<RecipeIngredient> requiredIngredients =
                recipe.getIngredients();

        if (requiredIngredients == null
                || requiredIngredients.isEmpty()) {
            return false;
        }

        for (RecipeIngredient required : requiredIngredients) {

            if (required == null
                    || required.getIngredientName() == null
                    || required.getUnit() == null
                    || required.getRequiredQuantity() <= 0) {
                return false;
            }

            String name = normalizeName(
                    required.getIngredientName());

            String unit = normalizeUnit(required.getUnit());

            String key = name + "|" + unit;

            double requiredQuantity = toBaseQuantity(
                    required.getRequiredQuantity(), unit);

            double availableQuantity = available.containsKey(key)
                    ? available.get(key) : 0.0;

            if (availableQuantity + 0.000001 < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private String normalizeName(String name) {
        String normalized = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        // Handle common simple plural forms.
        if (normalized.endsWith("ies")
                && normalized.length() > 3) {
            return normalized.substring(
                    0, normalized.length() - 3) + "y";
        }

        if (normalized.endsWith("oes")
                && normalized.length() > 3) {
            return normalized.substring(
                    0, normalized.length() - 2);
        }

        if (normalized.endsWith("s")
                && !normalized.endsWith("ss")
                && normalized.length() > 2) {
            return normalized.substring(
                    0, normalized.length() - 1);
        }

        return normalized;
    }

    private String normalizeUnit(String unit) {
        String normalized = unit.trim()
                .toLowerCase(Locale.ROOT)
                .replace(".", "");

        switch (normalized) {
            case "g":
            case "gram":
            case "grams":
                return "g";

            case "kg":
            case "kilogram":
            case "kilograms":
                return "kg";

            case "ml":
            case "milliliter":
            case "milliliters":
            case "millilitre":
            case "millilitres":
                return "ml";

            case "l":
            case "liter":
            case "liters":
            case "litre":
            case "litres":
                return "l";

            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
                return "piece";

            case "slice":
            case "slices":
                return "slice";

            default:
                return normalized;
        }
    }

    private double toBaseQuantity(double quantity, String unit) {
        switch (unit) {
            case "kg":
                return quantity * 1000.0;

            case "l":
                return quantity * 1000.0;

            default:
                return quantity;
        }
    }
}
