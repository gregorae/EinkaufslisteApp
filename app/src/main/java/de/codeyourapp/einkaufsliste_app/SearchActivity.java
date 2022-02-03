package de.codeyourapp.einkaufsliste_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//Klasse für die zweite Oberfläche die sich öffnet wenn ein neues Produkt gesucht und hinzugefügt werden soll
public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);   //Initialisierung Button

        //Wenn der Button geklickt wird, switcht es wieder zurück zum Startbildschirm(Main_Activity.xml)
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
