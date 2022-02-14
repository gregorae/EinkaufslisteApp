package de.codeyourapp.einkaufsliste_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import Person.PersonActivity;
import Search.Produkt;
import Search.SearchActivity;
import Search.ViewHolder;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashMap;

import android.view.Window;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;


public class MainActivity extends AppCompatActivity {
    private ImageButton person_link;
    private Button btnSearch;

    private ArrayList<DataModel> usersList;
    private RecyclerView recyclerView;
    Intent receivedIntent;
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    DatabaseReference listDatabaseReference = fbase.getReference().child("Entry");
    FirebaseRecyclerOptions<DataModel> options;
    FirebaseRecyclerAdapter<DataModel, recyclerAdapter.EntryViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);    //Ansicht Liste von oben nach unten
        mLinearLayoutManager.setStackFromEnd(true);

        btnSearch = findViewById(R.id.btnsearch);
        person_link = findViewById(R.id.person_link);

        recyclerView = findViewById(R.id.recyclerView);
        usersList = new ArrayList<>();


        //Übergebenes Produkt aus Suchfunkton in Listendatenbank anlegen
        receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("id")) {
            String produkt = receivedIntent.getStringExtra("id");
            HashMap<String,String> listMap = new HashMap<>();    //Hashmap von Produktdata-Tweig in Datenbank
            listMap.put("product_name",produkt);    //Beteiligte Größen neuer Datensatz (Produkt)
            listMap.put("user_token","");
            listMap.put("unit","");
            listMap.put("quantity","");
            listMap.put("notice","");
            listDatabaseReference.push().setValue(listMap);
        }

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
        setUserInfo();
        setAdapter();
    }

    private void openPersonActivity() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);

    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

        private void setUserInfo() {
        usersList.add(new DataModel("TI", "Banane",5, "Stk."));
    }
}