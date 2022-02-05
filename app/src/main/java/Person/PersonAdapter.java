package Person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeyourapp.einkaufsliste_app.R;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {

    Context context;
    ArrayList<Person> list;
    private PersonListener mPersonListener;



    public PersonAdapter(Context context, ArrayList<Person> personArrayList, PersonListener personListener) {
        this.context = context;
        list = personArrayList;
        this.mPersonListener = personListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // neuen View erstellen
        View v = LayoutInflater.from(context).inflate(R.layout.person,parent,false);
        return new MyViewHolder(v, mPersonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Werte werden in die Liste eingetragen mit den getter aus der Person klasse
        Person person = list.get(position);
        holder.name.setText(person.getName());
        holder.color.setBackgroundColor(Integer.parseInt(person.getColor()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Variablen werden angelegt
        TextView name;
        Button color;
        PersonListener personListener;


        public MyViewHolder(@NonNull View itemView, PersonListener personListener) {
            super(itemView);
            //Variablen werden der xml zugeordnet
            name = itemView.findViewById(R.id.Name_Listview);
            color = itemView.findViewById(R.id.Color_Listview);
            this.personListener = personListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            personListener.personListener(getAdapterPosition());
        }
    }
    public interface PersonListener{
        void personListener(int position);
    }
}
