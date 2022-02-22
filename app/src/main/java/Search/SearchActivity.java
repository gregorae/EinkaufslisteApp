package Search;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import de.codeyourapp.einkaufsliste_app.MainActivity;
import de.codeyourapp.einkaufsliste_app.R;

//Suchfunktions-Seite Lukas
public class SearchActivity extends AppCompatActivity {
    private Button btnSea;

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Produkt, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Produkt> options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        btnSea = (Button) findViewById(R.id.btnSea);

        //Sorgt für Ansicht als List --> Generiert RecyclerViewListe
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);    //Ansicht Liste von oben nach unten
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recyclerView);    //Zugriff auf RecyclerView Searchxml
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Produktdata"); //Wähle Datenbankzweig aus

        showProduktdata();  //Methode aufrufen bei onCreate()


        btnSea.setOnClickListener(new View.OnClickListener() {   //Button neues Produkt hinzufügen(Datenbank und Einkaufsliste)
            @Override
            public void onClick(View v) {
                SearchView searchView = (SearchView) findViewById(R.id.search);
                String msearchview = searchView.getQuery().toString();  //Konvertiere Sucheingabe zu String und speichere

                Toast.makeText(SearchActivity.this, "test2", Toast.LENGTH_SHORT).show();
                if(msearchview.length() == 0){
                    Toast.makeText(SearchActivity.this, "Fehler, keine Eingabe die hinzugefügt werden kann", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(SearchActivity.this, "test", Toast.LENGTH_SHORT).show();
                    String produkt = msearchview.substring(0,1).toUpperCase(Locale.ROOT) + msearchview.substring(1);    //1.Buchstabe groß, Rest klein
                    String search = produkt.toLowerCase();  //Gesamter Suchtext klein geschrieben in String speichern
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);    //Wechsel von Search zu MainActivity
                    intent.putExtra("id",produkt);  //Bei Klassenwechsel String mitübergeben
                    Toast.makeText(SearchActivity.this, "Produkt hinzugefügt", Toast.LENGTH_SHORT).show();  //Hinweis
                    startActivity(intent);  //Wechsel der Klasse starten

                    HashMap<String,String> produktmap = new HashMap<>();    //Hashmap von Produktdata-Tweig in Datenbank
                    produktmap.put("search",search);    //Beteiligte Größen neuer Datensatz (Produkt)
                    produktmap.put("title",produkt);
                    mDatabaseReference.push().setValue(produktmap);     //Neuen Datensatz zur Datenbank Hinzufügen/neues Produkt erstellen
                }
            }
        });
    }

    private void showProduktdata(){
        options = new FirebaseRecyclerOptions.Builder<Produkt>().setQuery(mDatabaseReference,Produkt.class).build();//Zeiger der durch Datenbank bewegt wird
        new ItemTouchHelper(itHelperCallback).attachToRecyclerView(mRecyclerView);  //Per Swipe löschen Methode anlegen

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Produkt, ViewHolder>(options) {   //Adapter zwischen Zeiger Datenbank und Viewholder der RecycleViewListe
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull Produkt produkt) {
                holder.setDetails(getApplicationContext(), produkt.getTitle());     //Gibt ViewHolder die Daten aus Datenbank
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Erstelle neuen ViewHolder
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);    //Definiere Ansicht/aufbau einzelner Item
                ViewHolder viewHolder = new ViewHolder(itemView); //Viewholder mit Ihnalt ItemVIEW
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String produkt = firebaseRecyclerAdapter.getItem(position).getTitle().toString();  //Nimm Titel der Position und konvertiere zu String
                        Toast.makeText(SearchActivity.this, "Produkt hinzugefügt", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SearchActivity.this,MainActivity.class); //Zum Main_Bildschirm wechseln
                        intent.putExtra("id",produkt);
                        startActivity(intent);
                    }
                });
                return viewHolder;  //Gebe Viewholder bestehend aus ItemView zurück
            }
        };

        mRecyclerView.setLayoutManager(mLinearLayoutManager);   //RecyclerView als Ansicht Liste
        firebaseRecyclerAdapter.startListening();       //Adapterzeiger auf Clicks hören
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);      //Adapterzeiger auf RecycleViewListe implementieren
    }

    private void firebaseSearch(String searchText){ //Methode bei Sucheingabe konvertiere weitergegebenen String in String searchText(einheitlich)
        String quary = searchText.toLowerCase();    //Konvertiere Sucheingabe in KleinBuchstaben
        new ItemTouchHelper(itHelperCallback).attachToRecyclerView(mRecyclerView);  //Per Swipe löschen Methode anlegen

        Query firebaseSearchQuery = mDatabaseReference.orderByChild("search").startAt(quary).endAt(quary + "\uf8ff");   //Suche in DAtenzweig "search" von Produktdata
        options = new FirebaseRecyclerOptions.Builder<Produkt>().setQuery(firebaseSearchQuery,Produkt.class).build();   //Zeiger der durch Datenbank bewegt wird

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Produkt, ViewHolder>(options) {   //Adapter zwischen Zeiger Datenbank und Viewholder der RecycleViewListe
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull Produkt produkt) {
                holder.setDetails(getApplicationContext(),produkt.getTitle());  //Siehe datashowprodukt()
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
                ViewHolder viewHolder = new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String produkt = firebaseRecyclerAdapter.getItem(position).getTitle().toString();  //?
                        Toast.makeText(SearchActivity.this, "Produkt hinzugefügt", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SearchActivity.this,MainActivity.class); //Zum Main_Bildschirm wechseln
                        intent.putExtra("id",produkt);
                        startActivity(intent);
                    }
                });

                return viewHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    protected void onStart(){   //Wenn SearchActivity gestartet wird wird auch Adapterzeiger gestartet
        super.onStart();
        if(firebaseRecyclerAdapter != null){    //wenn nicht bereits gestartet
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Erstelle Menu
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);    //Zeige Sucheingabe direkt
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {    //Füllt Sucher für weitere Funktionen
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);  //Bei bestätigter Texteingabe Weitergabe an Methode firebaseSearch
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);    //Bei Textänderung/Texteingabe Weitergabe an Methode firebaseSearch
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Methode um beim Swipen zu löschen
    ItemTouchHelper.SimpleCallback itHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String currentTitle = firebaseRecyclerAdapter.getItem(viewHolder.getAdapterPosition()).getTitle();
            String currentSearch = firebaseRecyclerAdapter.getItem(viewHolder.getAdapterPosition()).getSearch();
            Query mQuery = mDatabaseReference.orderByChild("title").equalTo(currentTitle);  //Suche betroffenes Produkt in Datenbank
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){  //für alle entsprechenden Datensätze in Realtime Database
                        ds.getRef().removeValue();  //entferne/lösche Datensatz aus Datenbank
                        Toast.makeText(SearchActivity.this, "Produkt erfolgreich aus Vorschläge gelöscht", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchActivity.this, "Fehler", Toast.LENGTH_SHORT).show();
                }
            });
            Query nQuery = mDatabaseReference.orderByChild("search").equalTo(currentSearch);    //Gleiches für UNtergruppe Search
            nQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        ds.getRef().removeValue();
                        Toast.makeText(SearchActivity.this, "Produkt erfolgreich aus Vorschläge gelöscht", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchActivity.this, "Fehler", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
}
