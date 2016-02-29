package com.example.pratibha.whattocook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class RecipesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] name;
    private String[] time;
    private String[] difficulty;
    private String[] ingredients;
    private String[] method;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // get UI reference
        ListView recipeListView = (ListView) findViewById(R.id.displayRecipeList);
        recipeListView.setBackgroundResource(R.drawable.list_custom_shape);
        recipeListView.setOnItemClickListener(this);
        Button backButton = (Button) findViewById(R.id.recipeListBack);
        backButton.setOnClickListener(myListener);

        Log.e("RecipeListActivity", " state is --- " + savedInstanceState);
            // get intent and values associated with it
        Intent intent = getIntent();
        name = intent.getStringArrayExtra("name");
        time = intent.getStringArrayExtra("time");
        difficulty = intent.getStringArrayExtra("difficulty");

        /*boolean[] arrowsRequired = new boolean[name.length]; // set image arrow for every item of the list
        for (int i = 0; i < name.length; i++)
        {
            arrowsRequired[i] = true;
        }*/

        ingredients = intent.getStringArrayExtra("ingredient");
        method = intent.getStringArrayExtra("method");

        String[] displayArray = new String[name.length];
        for (int i = 0; i < displayArray.length; i++)
        {
            displayArray[i] = name[i] + "\nCook Time: " + time[i] + "\t \t \t \t \t Level Of Difficulty: " + difficulty[i];
        }
        // adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.add_ingredient_view, displayArray);
        recipeListView.setAdapter(adapter);
        //recipeListView.setAdapter(new ImageAdapter(this, R.layout.activity_recipes_list, R.id.text1, R.id.arrow, displayArray, arrowsRequired));

    }

    private View.OnClickListener myListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent backIntent = new Intent(getApplicationContext(), AddIngredientsActivity.class);
            startActivity(backIntent);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        Intent displayIntent = new Intent(getApplicationContext(), RecipeDetailsActivity.class);
        displayIntent.putExtra("name", name[position]);
        displayIntent.putExtra("time", time[position]);
        displayIntent.putExtra("difficulty", difficulty[position]);
        displayIntent.putExtra("ingredients", ingredients[position]);
        displayIntent.putExtra("method", method[position]);
        startActivity(displayIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.v("RecipesListActivity", "Inside onSaveInstanceState......");
        savedInstanceState.putStringArray("name", name);
        savedInstanceState.putStringArray("time", time);
        savedInstanceState.putStringArray("difficulty", difficulty);
        savedInstanceState.putStringArray("ingredients", ingredients);
        savedInstanceState.putStringArray("method", method);
    }
}
