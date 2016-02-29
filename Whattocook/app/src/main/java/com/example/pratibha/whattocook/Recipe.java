package com.example.pratibha.whattocook;

/**
 * Created by Pratibha on 11/28/2015.
 */
class Recipe
{
    private String name;
    private String time;
    private String levelOfDifficulty;
    private String ingredients;
    private String method;

    public Recipe(String name, String time, String levelOfDifficulty, String ingredients, String method)
    {
        this.name = name;
        this.time = time;
        this.levelOfDifficulty = levelOfDifficulty;
        this.ingredients = ingredients;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String getTime() { return time; }

    public String getLevelOfDifficulty() {
        return levelOfDifficulty;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMethod() {
        return method;
    }
}
