package de.codeyourapp.einkaufsliste_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.EntryViewHolder> {
    private ArrayList<DataModel> usersList;

    public recyclerAdapter(ArrayList<DataModel>usersList){
        this.usersList = usersList;
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder{
        private Button userCTXT;
        public  TextView pnameTXT;
        private TextView quantityTXT;
        private TextView unitTXT;

        public EntryViewHolder(final View view){
            super(view);
            userCTXT = view.findViewById(R.id.userButton);
            pnameTXT = view.findViewById(R.id.tv_product_name);
            quantityTXT = view.findViewById(R.id.tv_quantity);
            unitTXT = view.findViewById(R.id.tv_unit);
        }
    }


    @NonNull
    @Override
    public recyclerAdapter.EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.EntryViewHolder holder, int position) {
        DataModel dataModel = usersList.get(position);
        holder.userCTXT.setText(dataModel.getUserToken());
        holder.pnameTXT.setText(dataModel.getProduct_name());
        holder.quantityTXT.setText("" + dataModel.getQuantity());
        holder.unitTXT.setText(dataModel.getUnit());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
