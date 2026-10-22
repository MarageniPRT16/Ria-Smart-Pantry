
package com.ria.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private List<RecipeIngredient> ingredients;
    private String instructions;

    public Recipe(int id, String name,
                  List<RecipeIngredient> ingredients,
                  String instructions) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients != null
                ? new ArrayList<>(ingredients)
                : new ArrayList<>();
        this.instructions = instructions;
    }

    public Recipe(String name,
                  List<RecipeIngredient> ingredients,
                  String instructions) {
        this(0, name, ingredients, instructions);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients != null
                ? new ArrayList<>(ingredients)
                : new ArrayList<>();
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
