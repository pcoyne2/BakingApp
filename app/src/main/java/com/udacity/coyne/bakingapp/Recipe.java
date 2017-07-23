package com.udacity.coyne.bakingapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 7/17/2017.
 */

public class Recipe {
    private int id;
    private String name;
    @SerializedName("ingredients")
    private List<Ingredients> ingredients = new ArrayList<>();
    @SerializedName("steps")
    private List<Steps> steps = new ArrayList<>();
    private int servings;
    private String image;


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

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
