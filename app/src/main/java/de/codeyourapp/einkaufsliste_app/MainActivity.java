package de.codeyourapp.einkaufsliste_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Person.PersonActivity;
import Search.SearchActivity;

public class MainActivity extends AppCompatActivity {
    private Button person_link;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnsearch);
        person_link = findViewById(R.id.person_link);

        person_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPersonActivity();
            }
        });

        //L: Button verlinkt zu Suchfunktion
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openPersonActivity() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);

    }
}