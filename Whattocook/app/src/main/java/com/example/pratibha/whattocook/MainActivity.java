package com.example.pratibha.whattocook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button addRecipeButton;
    private Button searchRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addRecipeButton = (Button) findViewById(R.id.addRecipe);
        searchRecipeButton = (Button) findViewById(R.id.searchRecipe);

        addRecipeButton.setOnClickListener(buttonListener);
        searchRecipeButton.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent i;
            switch(v.getId())
            {
                case R.id.addRecipe:

                    // start the music
                    i = new Intent(getApplicationContext(), PlaySoundService1.class);
                    startService(i);

                    // start new activity with layout
                    i = new Intent(getApplicationContext(), AddRecipeActivity.class);
                    startActivity(i);
                    break;

                case R.id.searchRecipe:

                    // start the music
                    i = new Intent(getApplicationContext(), PlaySoundService2.class);
                    startService(i);

                    // start new activity with layout
                    i = new Intent(getApplicationContext(), AddIngredientsActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };
}
