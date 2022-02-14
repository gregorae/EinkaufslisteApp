package Search;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.codeyourapp.einkaufsliste_app.R;

public class ViewHolder extends RecyclerView.ViewHolder{

    View mview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);    //Inhalt ViewHolder

        mview = itemView;   //zeige das Item auf Screen
        //Initialisiere OnClickListener für View (um Produkt zu Einkaufsliste hinzuzufügen)
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition()); //ermittle angeklickte Position(gezeigtes item)
            }
        });
    }

    public void setDetails(Context ctx, String title){
        TextView mTitle = mview.findViewById(R.id.TV_Title);//TextView der row.XML

        mTitle.setText(title); //Titel Produkt in Zeile eintragen
    }

    private ViewHolder.ClickListener mClickListener;

    public  interface ClickListener {   //Interface für Click-Events initialisieren
        void onItemClick(View view, int position);
       // void onItemLongClick(View view, int position);
    }
    //Initialisiere allgemeinen ClickListener
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
