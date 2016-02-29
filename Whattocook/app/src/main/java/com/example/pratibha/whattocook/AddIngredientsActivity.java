package com.example.pratibha.whattocook;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class AddIngredientsActivity extends AppCompatActivity  {

    private static final String TAG = "Add Ingredient Activity";

    static final private int REMOVE_INGREDIENT = Menu.FIRST;
    static final private int EDIT_INGREDIENT = Menu.FIRST + 1;

    private Button backButton;
    private Button searchButton;
    private EditText ingredientEditText;

    private ArrayList<String> ingredientsArrayList;
    private ArrayAdapter<String> aa;

    private DatabaseHandler recipeDB;
    private static final String TABLE_NAME = "Recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the view
        setContentView(R.layout.activity_add_ingredients);

        /*// Loader
        getLoaderManager().initLoader(0, null, this);

        getLoaderManager().restartLoader(0, null, this);*/

        // Database object
        recipeDB = new DatabaseHandler(this);

        // Get references to UI
        ingredientEditText = (EditText) findViewById(R.id.ingredientEditText);
        ingredientEditText.setBackgroundResource(R.drawable.edit_text_shape);

        ListView ingredientListView = (ListView) findViewById(R.id.ingredientList);
        backButton = (Button) findViewById(R.id.ingrBack);
        searchButton = (Button) findViewById(R.id.searchRecipes);

        // set the listener
        backButton.setOnClickListener(myListener);
        searchButton.setOnClickListener(myListener);

        // register for context menu
        registerForContextMenu(ingredientListView);

        // initialize the arraylist
        ingredientsArrayList = new ArrayList<>();

        // set the custom layout in AddIngredientView class to the activity
        // set the adapter
        int resID = R.layout.add_ingredient_view;
        aa = new ArrayAdapter<>(this, resID, ingredientsArrayList);
        ingredientListView.setAdapter(aa);

        ingredientEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String ingredient = ingredientEditText.getText().toString();
                        ingredientsArrayList.add(0, ingredient.trim().toLowerCase());
                        ingredientEditText.setText("");
                        aa.notifyDataSetChanged();
                        return true;
                    }
                return false;
            }
        });
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args)
    {
        List<Recipe> recipeList = new ArrayList<>();
        String rawQuery = "SELECT  * FROM " + TABLE_NAME;
        List queryParams = recipeList;// to substitute placeholders
                SQLiteCursorLoader loader =
            new SQLiteCursorLoader(
                    getApplicationContext(),
                    recipeDB,
                    rawQuery,
                    queryParams);
        return loader;
    }

    @Override
    public void onLoadFinished(
            Loader<Cursor> loader,
            Cursor cursor)
    {

    }*/

    private View.OnClickListener myListener = new View.OnClickListener()
    {
        List<Recipe> recipes = null;

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.ingrBack:
                    stopService(new Intent(AddIngredientsActivity.this, PlaySoundService2.class));
                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(backIntent);
                    break;

                case R.id.searchRecipes:
                    // check for empty search
                    if(ingredientsArrayList.size() == 0)
                    {
                        showMessage("Error ! Empty Search", "Please add at-least one ingredient.");
                        return;
                    }
                    recipes = recipeDB.getAllRecipes(); // get recipes from the database

                    if (recipes.size() == 0) // check for empty database
                    {
                        showMessage("Empty Database!", "No recipes in database.");
                    }

                    ArrayList<Recipe> matchedList = new ArrayList<>();

                    for(int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        String recipeIngredientsDB = recipe.getIngredients();
                        String[] recipeIngredient = recipeIngredientsDB.split("\t");

                        int matchCount = 0;
                        for (int j = 0; j < ingredientsArrayList.size(); j++)
                        {
                            for (int k = 0; k < recipeIngredient.length; k++)
                            {
                                if (ingredientsArrayList.get(j).compareTo(recipeIngredient[k].trim().toLowerCase()) == 0)
                                {
                                    matchCount++;
                                    break;
                                }
                            }
                        }
                        if (matchCount == ingredientsArrayList.size())
                        {
                            matchedList.add(recipe);
                           // showMessage("Match found", "Ingredients match");
                        }
                    }

                    if (matchedList.size() == 0) // check for no recipes found
                    {
                        showMessage("Recipes not found!", "Your ingredients do not match any recipes");
                        return;
                    }

                    int numberOfRecipes = matchedList.size();
                    String [] recipeNames = new String[numberOfRecipes];
                    String [] recipeTimes = new String[numberOfRecipes];
                    String [] recipeDifficulty = new String[numberOfRecipes];
                    String [] recipeIngredients = new String[numberOfRecipes];
                    String [] recipeMethods = new String[numberOfRecipes];

                    for (int i = 0; i < numberOfRecipes; i++)
                    {
                        Recipe matchedRecipe = matchedList.get(i);
                        recipeNames[i] = matchedRecipe.getName();
                        recipeTimes[i] = matchedRecipe.getTime();
                        recipeDifficulty[i] = matchedRecipe.getLevelOfDifficulty();
                        recipeIngredients[i] = matchedRecipe.getIngredients();
                        recipeMethods[i] = matchedRecipe.getMethod();
                    }
                    Intent searchIntent = new Intent(getApplicationContext(), RecipesListActivity.class);
                    searchIntent.putExtra("name", recipeNames);
                    searchIntent.putExtra("time", recipeTimes);
                    searchIntent.putExtra("difficulty", recipeDifficulty);
                    searchIntent.putExtra("ingredient", recipeIngredients);
                    searchIntent.putExtra("method", recipeMethods);
                    startActivity(searchIntent);
                    break;
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
/*        int id = item.getItemId();

        switch (id)
        {
            case ADD_INGREDIENT:
            {
                Log.v(TAG, "Inside switch..");
                addNewIngredient();
                return true;
            }
        }*/

        return false;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, REMOVE_INGREDIENT, Menu.NONE, "Remove Ingredient");
        menu.add(0, EDIT_INGREDIENT, Menu.NONE, "Edit Ingredient");
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        super.onContextItemSelected(item);
        switch (item.getItemId())
        {
            case REMOVE_INGREDIENT:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Remove Ingredient from the list.");
                builder.setMessage("Are you sure you want to proceed? Press Yes or No.");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        int index = menuInfo.position;
                        removeIngredient(index);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();

                return true;
            }

           /* case EDIT_INGREDIENT:
            {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = menuInfo.position;

            }*/
        }
        return false;
    }

    private void removeIngredient(int index)
    {
        ingredientsArrayList.remove(index);
        aa.notifyDataSetChanged();
    }

    private void showMessage(String title, String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }


}
