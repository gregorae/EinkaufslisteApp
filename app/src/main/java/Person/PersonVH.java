package Person;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.codeyourapp.einkaufsliste_app.R;

    public class PersonVH extends RecyclerView.ViewHolder{
        public TextView name;
        public Button color;
        public PersonVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name_Listview);
            color = itemView.findViewById(R.id.Color_Listview);
        }
    }




