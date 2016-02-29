package com.example.pratibha.whattocook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    private Button backButton;
    private Button addButton;

    private EditText nameEditText;
    private EditText timeEditText;
    private Spinner difficultySpinner;
    private EditText ingredientEditText;
    private TextView methodText;
    private EditText methodEditText;

    private ListView ingrListView;
    private ArrayList<String> newIngrArrayList;
    private ArrayAdapter<String> arrayAdapter;

    private DatabaseHandler recipeDB;

    static final private int REMOVE_INGREDIENT = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // get the UI controls
        backButton = (Button) findViewById(R.id.recipeBack);
        addButton = (Button) findViewById(R.id.addNewRecipe);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        //nameEditText.setBackgroundResource(R.drawable.edit_text_shape);

        timeEditText = (EditText) findViewById(R.id.timeEditText);
        //timeEditText.setBackgroundResource(R.drawable.edit_text_shape);

        difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
       // difficultySpinner.setBackgroundResource(R.drawable.edit_text_shape);

        ingredientEditText = (EditText) findViewById(R.id.ingrEditText);
        //ingredientEditText.setBackgroundResource(R.drawable.edit_text_shape);

       // set scrollbar to the Directions Text View
        methodText = (TextView) findViewById(R.id.methodText);
        methodText.setMovementMethod(new ScrollingMovementMethod());

        methodEditText = (EditText) findViewById(R.id.methodEditText);
        methodEditText.setBackgroundResource(R.drawable.edit_text_shape);

        ingrListView = (ListView) findViewById(R.id.addIngredientList);
        registerForContextMenu(ingrListView);

        backButton.setOnClickListener(myListener);
        addButton.setOnClickListener(myListener);

        // add elements to the ListView.
        addIngredientsToListView();

        // create database and table
        recipeDB = new DatabaseHandler(this);
    }

    private void addIngredientsToListView()
    {
        // set the custom layout in AddIngredientView class to the activity
        // set the adapter
        newIngrArrayList = new ArrayList<>();
        int resID = R.layout.add_ingredient_view;
        arrayAdapter = new ArrayAdapter<>(this, resID, newIngrArrayList);
        ingrListView.setAdapter(arrayAdapter);

        ingredientEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String ingredient = ingredientEditText.getText().toString().trim();
                        newIngrArrayList.add(0, ingredient);
                        ingredientEditText.setText("");
                        arrayAdapter.notifyDataSetChanged();
                        return true;
                    }
                return false;
            }
        });

    }

    private View.OnClickListener myListener = new View.OnClickListener()
    {
        StringBuffer sb = null;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.recipeBack:
                    stopService(new Intent(AddRecipeActivity.this, PlaySoundService1.class));
                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(backIntent);
                    break;

                case R.id.addNewRecipe:
                    Log.v("AddRecipeActivity", "inside addNewRecipe-------");
                    // get text field values
                    String name = nameEditText.getText().toString();
                    String time = timeEditText.getText().toString();
                    String method = methodEditText.getText().toString();
                    String diffLevel = difficultySpinner.getSelectedItem().toString();

                    // append all the ingredients in the list to the StringBuffer
                    sb = new StringBuffer();
                    for(int i = 0; i < newIngrArrayList.size(); i++) {
                        String ingredient = newIngrArrayList.get(i) + "\t";
                        sb.append(ingredient);
                    }

                    // validate empty field
                    if(name.length() == 0 || time.length() == 0 || method.length() == 0 || newIngrArrayList.size() == 0)
                    {
                        showMessage("Error", "Please enter all values");
                        return;
                    }
                    else
                    {
                        // create Recipe object
                        Recipe recipe = new Recipe(name, time, diffLevel, sb.toString(), method);

                        // add the recipe to the database
                        boolean success = recipeDB.addRecipe(recipe);

                        if (success == true) {
                            showMessage("**Success**", "Recipe Added!");
                            clearFields();
                        }
                        else
                        {
                            showMessage("Unsuccessful", "Failed to add recipe!");
                            nameEditText.setText("");
                        }
                    }

                    // display table contents
                    //displayRecipes();
                    break;
            }
        }
        /*public void displayRecipes()
        {
            recipes = recipeDB.getAllRecipes();

            sb = new StringBuffer();
            for(int i = 0; i < recipes.size(); i++) {
                Recipe recipe = recipes.get(i);
                sb.append(recipe.getName() + " " + recipe.getTime() + " " + recipe.getLevelOfDifficulty() + " " +
                          recipe.getIngredients() + " " + recipe.getMethod() + "\n");
            }
            showMessage("List of recipes", sb.toString());
        }*/

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.clear)
        {
            clearFields();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, REMOVE_INGREDIENT, Menu.NONE, "Remove Ingredient");
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case REMOVE_INGREDIENT: {
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
        }
        return false;
    }

    private void removeIngredient(int index)
    {
        newIngrArrayList.remove(index);
        arrayAdapter.notifyDataSetChanged();
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

    private void clearFields()
    {
        nameEditText.setText("");
        timeEditText.setText("");
        difficultySpinner.setSelection(0);
        ingredientEditText.setText("");
        newIngrArrayList.clear();
        ingrListView.clearChoices();
        methodEditText.setText("");
        nameEditText.requestFocus();
    }
}
