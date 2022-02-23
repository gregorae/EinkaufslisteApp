package de.codeyourapp.einkaufsliste_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Configuration_Activity extends AppCompatActivity {

    //EditText conquant = findViewById(R.id.edit_quantity);
    //EditText connote = findViewById(R.id.edit_notice);
    //DatabaseReference noteref;
    //DatabaseReference quantref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Button apply = findViewById(R.id.con_apply);
        TextView conproduct = findViewById(R.id.con_product);

        conproduct.setText(getIntent().getStringExtra("prod_name"));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String quantity = conquant.getText().toString();
                //String notice = connote.getText().toString();
                //noteref = FirebaseDatabase.getInstance().getReference("Entry").child(conproduct.getText().toString()).child("notice");
                //quantref = FirebaseDatabase.getInstance().getReference("Entry").child(conproduct.getText().toString()).child("quantity");
                //noteref.setValue(notice);
                //quantref.setValue(quantity);
                changeActivity();
            }
        });
    }

    private void changeActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}