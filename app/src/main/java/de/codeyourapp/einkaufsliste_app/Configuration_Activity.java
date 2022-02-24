package de.codeyourapp.einkaufsliste_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Configuration_Activity extends AppCompatActivity {
    EditText conquant;
    EditText connote;
    DatabaseReference listref;
    String item_short;

    // String Array mit Inhalt des Dropdown-Menüs
    String[] items_dropdown = {"Liter (l)","Gramm (g)","Kilogramm (kg)"};
    String[] items_dropdown_short = {"l","g","kg"};
    AutoCompleteTextView actvUnit;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Button apply = findViewById(R.id.con_apply);
        TextView conproduct = findViewById(R.id.con_product);

        conquant = findViewById(R.id.edit_quantity);
        connote = findViewById(R.id.edit_notice);
        listref = FirebaseDatabase.getInstance().getReference("Entry").child(conproduct.getText().toString());

        conproduct.setText(getIntent().getStringExtra("prod_name"));

        actvUnit = findViewById(R.id.actv_unit);
        adapterItems = new ArrayAdapter<String>(this,R.layout.dropdown_item,items_dropdown);
        adapterItemsShort = new ArrayAdapter<String>(this,R.layout.dropdown_item,items_dropdown_short);

        actvUnit.setAdapter(adapterItems);

        actvUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                item_short = adapterItemsShort.getItem(position);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
                 String quantity = conquant.getText().toString();
                String notice = connote.getText().toString();
                String product_name = conproduct.getText().toString();
                String user_token = "hi";
                String unit = item_short;
                updateData(user_token,product_name, quantity,unit,notice);
            }
        });
    }

    private void updateData(String user_token,String product_name, String quantity,String unit, String notice){
        listref = FirebaseDatabase.getInstance().getReference("Entry").child(product_name);
        DataModel entry = new DataModel(user_token,product_name,quantity,unit,notice);
        listref.setValue(entry);
    }

    private void changeActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
