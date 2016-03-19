package net.ridhoperdana.siduya;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by RIDHO on 3/18/2016.
 */
public class View_Holder extends RecyclerView.ViewHolder {

//    CardView cv;
    TextView textview_nama;
    TextView textview_alamat;
    public View container;
//    ImageView imageView;

    View_Holder(View itemView) {
        super(itemView);
        textview_nama = (TextView) itemView.findViewById(R.id.nama_keamanan);
        textview_alamat = (TextView) itemView.findViewById(R.id.alamat_singkat_keamanan);
        container = itemView.findViewById(R.id.custom_listview);
//        description = (TextView) itemView.findViewById(R.id.description);
//        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
