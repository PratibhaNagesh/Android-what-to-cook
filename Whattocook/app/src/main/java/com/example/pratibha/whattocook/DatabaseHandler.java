package com.example.pratibha.whattocook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pratibha on 11/26/2015.
 */
class DatabaseHandler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RecipeDB";

    // Table name
    private static final String TABLE_NAME = "Recipe";

    // Recipe table field name
    //private static final String KEY_NAME = "name";


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("DatabaseHandler", "inside constructor -----------");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("DatabaseHandler", "Inside onCreate --------------");
//        db.execSQL("create table if not exists " + TABLE_NAME + "(rname VARCHAR, time VARCHAR, difficulty VARCHAR, ingredients VARCHAR, method VARCHAR, PRIMARY KEY(rname));");
        db.execSQL("create table if not exists " + TABLE_NAME + "(rname VARCHAR, time VARCHAR, difficulty VARCHAR, ingredients VARCHAR, method VARCHAR);");

        // Recipe one
        db.execSQL("insert into " + TABLE_NAME + " values('Asparagus with Lime and Mint', '10', 'Easy', 'Asparagus\tPepper\tSalt\tLime Juice\tMint Leaves\tOnion', " +
                "'1. Heat the oil in a wide sauté or frying pan on medium-high. When the oil is hot, add the asparagus and sprinkle with salt and pepper to taste, " +
                "cooking for about 5 minutes until fragrant.\n\n" +
                "2. Put on a plate and squeeze lime juice over the asparagus and sprinkle on the mint.');");

        // Recipe Two
        db.execSQL("insert into " + TABLE_NAME + " values('Vegetarian Korma', '50', 'Medium', " +
                "'Tomato Sauce\tOnion\tSalt\tGinger\tGarlic\tCarrots\tPotatoes\tGreen Peas\tCloves\tHeavy Cream\tBell Pepper\tCilantro\tVegetable Oil', " +
                "'1. Heat the oil in a skillet over medium heat. Stir in the onion, and cook until tender. Mix in ginger and garlic, and continue cooking 1 minute. " +
                "Mix potatoes, carrots, jalapeno, cashews, and tomato sauce. Season with salt and curry powder. Cook and stir 10 minutes, or until potatoes are tender.\n\n" +
                "2. Stir peas, green bell pepper, red bell pepper, and cream into the skillet. Reduce heat to low, cover, and simmer 10 minutes. Garnish with cilantro to serve.');");

        // Recipe Three
        db.execSQL("insert into " + TABLE_NAME + " values('Chickpea Curry', '30', 'Hard', 'Chickpea\tTomatoes\tCumin Seeds\tOlive Oil\tRice flour\tCilantro\tOnion\tSalt', " +
                "'1. Combine the tomatoes, about half the onion, the ginger, and rice flour in a blender; blend into a paste.\n\n" +
                "2. Heat the oil and cumin seeds in a large skillet over medium-high heat until the cumin swells and turns golden brown. " +
                "Cook the remaining onion in the hot oil for about 3 minutes. Stir the blended tomato mixture, garbanzo beans, curry powder, " +
                "and salt into the onions; cover and cook until hot, about 5 minutes. Garnish with the cilantro to serve.');");

        // Recipe Four
        db.execSQL("insert into " + TABLE_NAME + " values('Vegan Tofu and Spinach Scramble', '30', 'Hard', 'Onion\tSalt\tTomatoes\tSpinach\tOlive Oil\tLemon Juice\tBasil\tTofu\tTurmeric', " +
                "'1. Combine the tofu, turmeric, 1/2 teaspoon black pepper, 1/4 teaspoon salt and cayenne if using in a medium bowl. Toss well to combine and set aside.\n" +
                "\n" + "2. Heat the oil in a large nonstick skillet over medium-high heat. Add the scallion whites and cook, stirring, until soft, about 1 minute. " +
                "Add the tofu mixture and cook, stirring occasionally, until the tofu is lightly browned and resembles scrambled eggs, about 5 minutes.\n" + "\n" +
                "3. Add the spinach, lemon juice and 1/2 teaspoon salt and stir until the spinach is wilted, about 1 minute. " +
                "Add the tomatoes and scallion greens and stir until the tomatoes are just heated through and begin to soften, about 1 minute. " +
                "Remove from the heat, add the basil and stir to combine. Season with salt and pepper.');");

        // Recipe Five
        db.execSQL("insert into " + TABLE_NAME + " values('Spicy Vegan Sloppy Joes', '20', 'Medium', 'Onion\tSalt\tBlack Pepper\tWalnuts\tCabbage or Lettuce\tKetchup\tHamburger Buns\tPickled Jalapeno', " +
                "'1. Pulse the mushrooms in batches in a food processor until finely chopped. Set aside.\n" + "\n" +
                "2. Heat the oil in a large nonstick skillet over medium-high heat. Add the onions, 1 tablespoon beer and 1/4 teaspoon salt and cook, stirring frequently, " +
                "until the onions are lightly browned, about 5 minutes. Add the walnuts and peppers and cook, stirring occasionally, until the peppers are crisp-tender, about 3 minutes. " +
                "Add the mushrooms, 3/4 teaspoon black pepper and chipotle powder and cook, stirring frequently, until the mushrooms are just cooked through, about 5 minutes.\n" + "\n" +
                "3. Add the ketchup, tomato paste and 1/8 teaspoon salt and cook while stirring until the sauce is the consistency that you like, about 2 minutes.\n" + "\n" +
                "Spoon the mixture onto each bun. Serve with toppings if desired.');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // add a recipe
    public boolean addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean addSuccess = true;
        ContentValues values = new ContentValues();
        values.put("rname", recipe.getName()); // Recipe name
        values.put("time", recipe.getTime());
        values.put("difficulty", recipe.getLevelOfDifficulty());
        values.put("ingredients", recipe.getIngredients()); // Recipe ingredients
        values.put("method", recipe.getMethod()); // Preparation method

        try
        {
            // Inserting Row
            db.insert(TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            addSuccess = false;
        }
        db.close(); // Closing database connection
        return addSuccess;
    }

    // get a recipe
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding recipes to recipeList
                String name = cursor.getString(0);
                String time = cursor.getString(1);
                String difficulty = cursor.getString(2);
                String ingredient = cursor.getString(3);
                String method = cursor.getString(4);
                Recipe recipe = new Recipe(name, time, difficulty, ingredient, method);
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        // return recipe list
        return recipeList;
    }
}
