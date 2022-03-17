package de.codeyourapp.einkaufsliste_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Configuration_Activity extends AppCompatActivity {
    EditText conquant;
    EditText connote;
    DatabaseReference listref;
    String item_short, name_short, color_button;
    Button button;

    // String Array mit Inhalt des Dropdown-Menüs
    String[] items_dropdown = {"Liter (l)","Gramm (g)","Kilogramm (kg)","Stücke (Stk)","Packungen (Pkg)","Flaschen (Fl)","Kästen (Kasten)","Dosen (Dose)","Tuben (Tube)","Gläser (Glas)"};
    String[] items_dropdown_short = {"l","g","kg","Stk","Pkg","Fl","Kasten","Dosen","Tube","Glas"};
    AutoCompleteTextView actvUnit, persontextview;

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsShort;


    ArrayList<String> personlist;
    ArrayAdapter<String> personadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_configuration);

        Button apply = findViewById(R.id.con_apply);
        TextView conproduct = findViewById(R.id.con_product);

        button = (Button) findViewById(R.id.userButton);
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

        persontextview = findViewById(R.id.actv_person);
        personlist = new ArrayList<String>();
        personadapter = new ArrayAdapter<>(this, R.layout.dropdown_item,personlist);
        persontextview.setAdapter(personadapter);
        getpersondata();

        persontextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = personadapter.getItem(position);
                listref = FirebaseDatabase.getInstance().getReference("Person/" + name + "/short_name");
                listref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name_short = snapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                listref = FirebaseDatabase.getInstance().getReference("Person/" + name + "/color");
                listref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        color_button = snapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });





        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
                String quantity = conquant.getText().toString();
                String notice = connote.getText().toString();
                String product_name = conproduct.getText().toString();
                String user_token = name_short;
                String unit = item_short;
                String color = color_button;
                if (color == null){
                    color = "-10354450";
                }
                updateData(user_token,product_name, quantity,unit,notice, color);
            }
        });
    }

    private void getpersondata() {
        listref = FirebaseDatabase.getInstance().getReference().child("Person");
        listref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                String name = null;
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {
                    name = snapshot.child("name").getValue().toString();
                    personlist.add(name);
                }
                personadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData( String user_token, String product_name, String quantity, String unit, String notice, String color){
        listref = FirebaseDatabase.getInstance().getReference("Entry").child(product_name);
        DataModel entry = new DataModel(user_token,product_name,quantity,unit,notice, color);
        listref.setValue(entry);
    }

    private void changeActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
