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

    Intent recIntent;
    Button apply = findViewById(R.id.con_apply);
    EditText conquant = findViewById(R.id.edit_quantity);
    EditText connote = findViewById(R.id.edit_notice);
    TextView conproduct = findViewById(R.id.con_product);
    DatabaseReference quantref;
    DatabaseReference noteref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        recIntent = getIntent();
        if (recIntent != null && recIntent.hasExtra("prodname")){
            conproduct.setText(recIntent.toString());
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = conquant.getText().toString();
                String notice = connote.getText().toString();
                quantref = FirebaseDatabase.getInstance().getReference("Entry").child(recIntent.toString()).child(quantity);
                noteref = FirebaseDatabase.getInstance().getReference("Entry").child(recIntent.toString()).child(notice);
                quantref.setValue(quantity);
                noteref.setValue(notice);
                changeActivity();
            }
        });
    }

    private void changeActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        /*String quantity = conquant.getText().toString();
        String notice = connote.getText().toString();
        intent.putExtra("notice",notice);
        intent.putExtra("quantity",quantity);*/
        startActivity(intent);
    }
}