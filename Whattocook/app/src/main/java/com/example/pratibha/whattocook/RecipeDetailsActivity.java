package com.example.pratibha.whattocook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Button backButton;
    private Button homeButton;
    private TextView nameTextView;
    private TextView timeDifficultyTextView;
    private TextView ingredientsTextView;
    private ListView ingredientsListView;
    private TextView directionsTextView;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        backButton = (Button) findViewById(R.id.recipeDetailsBack);
        backButton.setOnClickListener(myListener);

        homeButton = (Button) findViewById(R.id.recipeHome);
        homeButton.setOnClickListener(myListener);

        // get the UI
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        timeDifficultyTextView = (TextView) findViewById(R.id.timeDifficultyTextView);
        ingredientsTextView = (TextView) findViewById(R.id.ingredientsTextView);
        ingredientsTextView.setMovementMethod(new ScrollingMovementMethod());
        ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        directionsTextView = (TextView) findViewById(R.id.directionsTextView);
        directionsTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        // get intent and values associated with it
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String time = intent.getStringExtra("time");
        String difficulty = intent.getStringExtra("difficulty");

        String ingredients = intent.getStringExtra("ingredients");
        String[] ingredient = ingredients.split("\t");

        String method = intent.getStringExtra("method");

        //initialize the adapter
        int resID = R.layout.add_ingredient_view;
        adapter = new ArrayAdapter<>(this, resID, ingredient);

        nameTextView.setText(name);
        timeDifficultyTextView.setText("Level of Difficulty: " + difficulty + "\nCook Time: " + time + " mins");
        ingredientsTextView.setText("Ingredients: ");
        ingredientsListView.setAdapter(adapter);
        directionsTextView.setText("Directions: \n" + method);
    }

    private View.OnClickListener myListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.recipeDetailsBack:

                    Intent backIntent = new Intent(getApplicationContext(), RecipesListActivity.class);
                    backIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); /* set this flag to get the old instance of RecipeListActivity;
                                                                                   if not set, it would create a new instance every time back button is hit*/
                    startActivity(backIntent);
                    break;

                case R.id.recipeHome:
                    stopService(new Intent(RecipeDetailsActivity.this, PlaySoundService2.class));
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(homeIntent);
                    break;
            }
        }
    };
}
