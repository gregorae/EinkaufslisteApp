package Person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.codeyourapp.einkaufsliste_app.MainActivity;
import de.codeyourapp.einkaufsliste_app.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class PersonActivity extends AppCompatActivity  {

    // Variablen
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText person_name;
    private String key =null;
    private Button save_person, cancel_person, choose_color_person;
    private int Default_Color;
    private FloatingActionButton add_person;
    private ImageView backButton;


    // Datenbank
    DatabaseReference databaseReference;

    //Variablen für das Anzeigen der Daten aus Firebase in  RecyclerView
    RecyclerView recyclerView;
    PersonAdapter personAdapter;
    ArrayList<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_person);


        // Beim klicken aus den Floating Button mit den Plus wird die Methode ausgerufen
        // die den Dialogfenster aufruft, wo man eine neuen Person erstellen kann
        add_person = findViewById(R.id.person_add);
        add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPersonDialog();
            }
        });

        backButton = findViewById(R.id.back_icon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Methode zum dauerhaften aktualisieren der Data wird aufgerufen
        retrieve_person_data();
        //setup recycler view
    }




    // Methode für das Anlegen einer neuen Person
    public void createNewPersonDialog(){
        // neuer dialog variable wird erstellt
        dialogBuilder = new AlertDialog.Builder(this);
        // dialog hat das Layout von person_popup.xml
        final View personPopupView = getLayoutInflater().inflate(R.layout.person_popup, null);

        // Variablen im Person Dialog
        person_name = (EditText) personPopupView.findViewById(R.id.newperson_name);
        save_person = (Button) personPopupView.findViewById(R.id.saveButton_person);
        cancel_person = (Button) personPopupView.findViewById(R.id.cancelButton_person);

        //Firebase erstellt eine Tabelle von Typ Person
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Person");


        // Dialog wird geöffnet
        dialogBuilder.setView(personPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        // Choose Color Button ruft einen Color Dialog Methode auf
        Default_Color = ContextCompat.getColor(PersonActivity.this, R.color.purple_500);
        choose_color_person = (Button)  personPopupView.findViewById(R.id.choosecolor_person);
        choose_color_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //  Button um Person zu speichern
        save_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Läd die eingegeben daten in die Firebase
                insertPersonData();
                // schließt Dialog
                dialog.dismiss();
            }
        });

        //Button um den Dialog zu schließen
        cancel_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, Default_Color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            // Die ausgewählte Farbe wird in die Variable Default_color gespeichert
            // und der Button wird in der ausgewählen Farben angezeigt
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Default_Color = color;
                choose_color_person.setBackgroundColor(Default_Color);
            }
        });
        colorPicker.show();
    }


    // Eingegebene Werte für die Person werden in Database hochgeladen
    private void insertPersonData() {

        // Eine Person wird mit der Klasse Person angelegt
        String Person_name = person_name.getText().toString();
        String Color = Integer.toString(Default_Color);
        Person person = new Person(Person_name, Color);
        if(Person_name.length()>0) {
            // Daten werden hochgeladen und der Name wird als key benutzt
            databaseReference.child(Person_name).setValue(person).addOnSuccessListener(suc ->
            {
                Toast.makeText(this, "Person was generated", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er ->
            {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            });
        }
        else{
            Toast.makeText(this, "Person needs minimum 1 letter", Toast.LENGTH_SHORT).show();
        }
    }



    // Methode um die Firebase Daten in der Recycler View anzuzeigen
    private void retrieve_person_data() {

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.person_list);
        // Sucht die Daten aus Person Liste
        databaseReference = FirebaseDatabase.getInstance().getReference("Person");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(recyclerView);

        // Neuer Person Adapter wird erstellt
        personAdapter = new PersonAdapter(this,list);
        recyclerView.setAdapter(personAdapter);


        // Daten werden in die Liste geladen
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Liste muss gecleart werden, sonst werden die Daten doppelt angezeigt
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Person person = dataSnapshot.getValue(Person.class);
                    person.setKey(dataSnapshot.getKey());
                    list.add(person);
                    //key = snapshot.getKey(); unsicher ob das raus darf
                }
                personAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Methode mit der man Personen löscht.
    // wird durch ein swipe auf dem recycler view ausgelöst
    ItemTouchHelper.SimpleCallback itemtouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)  { // Löschen durch Links oder Rechts swipen
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Person per = list.get(viewHolder.getAdapterPosition());     //holt sich die Position des ausgewählen
            key = per.getKey();     //key dieser person wird zwischengespeichert
            databaseReference = FirebaseDatabase.getInstance().getReference("Person/" + key);   // Reference wird auf gewünschten Person gesetzt
            databaseReference.removeValue();    // Diese Reference wird gelöscht

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Entry");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    for(DataSnapshot snapshot : datasnapshot.getChildren()) {
                        snapshot.child("userToken" + key.substring(0, 2).toUpperCase());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    };
}