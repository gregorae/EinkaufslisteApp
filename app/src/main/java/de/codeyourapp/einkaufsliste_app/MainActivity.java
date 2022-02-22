package de.codeyourapp.einkaufsliste_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import Person.PersonActivity;
import Search.SearchActivity;


public class MainActivity extends AppCompatActivity implements MainInterface{
    private ImageButton person_link;
    private Button btnSearch;
    private EditText notice;
    private EditText quantity;

    private ArrayList<DataModel> usersList;
    private RecyclerView recyclerView;
    private String key =null;
    Intent receivedIntent;
    FirebaseDatabase fbase = FirebaseDatabase.getInstance();
    DatabaseReference listDatabaseReference = fbase.getReference().child("Entry");
    FirebaseRecyclerOptions<DataModel> options;
    FirebaseRecyclerAdapter<DataModel, recyclerAdapter.EntryViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    recyclerAdapter adapter;


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
        notice = findViewById(R.id.tv_notice);
        quantity = findViewById(R.id.tv_quantity);

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
        //setUserInfo();
        setAdapter();
        retrieve_entry_data();
    }

    private void openPersonActivity() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);

    }
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(usersList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void retrieve_entry_data() {

        usersList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        // Sucht die Daten aus Person Liste
        listDatabaseReference = FirebaseDatabase.getInstance().getReference("Entry");
        //Kein Plan was das ist
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(recyclerView);

        // Neuer Person Adapter wird erstellt
        adapter = new recyclerAdapter(this.usersList,this);
        recyclerView.setAdapter(adapter);


        // Daten werden in die Liste geladen
        listDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Liste muss gecleart werden, sonst werden die Daten doppelt angezeigt
                usersList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataModel model = dataSnapshot.getValue(DataModel.class);
                    model.setKey(dataSnapshot.getKey());
                    usersList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    ItemTouchHelper.SimpleCallback itemtouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT)  { // Löschen durch Links oder Rechts swipen
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            DataModel ent = usersList.get(viewHolder.getAdapterPosition());     //holt sich die Position des ausgewählen
            key = ent.getKey();     //key dieser person wird zwischengespeichert
            listDatabaseReference = FirebaseDatabase.getInstance().getReference("Entry/" + key);   // Reference wird auf gewünschten Person gesetzt
            listDatabaseReference.removeValue();    // Diese Reference wird gelöscht
        }
    };

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,Configuration_Activity.class);
        startActivity(intent);
    }
}