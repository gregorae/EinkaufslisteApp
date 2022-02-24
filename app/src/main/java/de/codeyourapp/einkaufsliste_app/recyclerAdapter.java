package de.codeyourapp.einkaufsliste_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.EntryViewHolder>{
    ArrayList<DataModel> usersList;
    private String key = null;
    private final MainInterface mainInterface;
    FirebaseDatabase fireref = FirebaseDatabase.getInstance();
    DatabaseReference dataref;
    DatabaseReference dataref2;

    boolean isOnTextChanged = false;

    public recyclerAdapter( ArrayList<DataModel>usersList,MainInterface mainInterface){
        this.usersList = usersList;
        this.mainInterface=mainInterface;
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        Button userCTXT;
        TextView pnameTXT;
        TextView quantityTXT;
        TextView noticeTXT;
        TextView unitTXT;
        String color;

        public EntryViewHolder(@NonNull View itemView, MainInterface mainInterface){
            super(itemView);
            rootView = itemView;
            userCTXT = itemView.findViewById(R.id.userButton);
            pnameTXT = itemView.findViewById(R.id.tv_product_name);
            quantityTXT = itemView.findViewById(R.id.tv_quantity);
            noticeTXT = itemView.findViewById(R.id.tv_notice);
            unitTXT = itemView.findViewById(R.id.tv_unit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mainInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            mainInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }

    }


    @NonNull
    @Override
    public recyclerAdapter.EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new EntryViewHolder(itemView, mainInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.EntryViewHolder holder, int position) {
        DataModel dataModel = usersList.get(position);
        holder.userCTXT.setText(dataModel.getUserToken());
        holder.pnameTXT.setText(dataModel.getProduct_name());
        holder.quantityTXT.setText(" " + dataModel.getQuantity());
        holder.noticeTXT.setText(""+dataModel.getNotice());
        holder.unitTXT.setText(dataModel.getUnit());
        holder.userCTXT.setBackgroundColor(Integer.parseInt(dataModel.getColor()));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
