package de.codeyourapp.einkaufsliste_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configuration_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Button apply = findViewById(R.id.con_apply);
        EditText conquant = findViewById(R.id.edit_quantity);
        EditText connote = findViewById(R.id.edit_notice);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = conquant.getText().toString();
                String notice = connote.getText().toString();
                changeActivity();
            }
        });
    }

    private void changeActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}