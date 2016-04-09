package net.ridhoperdana.siduya;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by RIDHO on 3/18/2016.
 */
public class Adapter extends RecyclerView.Adapter<View_Holder> {

    List<Results> list = Collections.emptyList();
//    List<Results> list = new ArrayList<>();
    Context context;
    TextView judul;
    TextView alamat;
    ImageView button;

    HalamanKeamanan halamanKeamanan;

//    List tampung_list = halamanKeamanan.tampung_result;
//    Context contextHalamanKeamanan = null;
//    private Activity activity;


    public Adapter(List<Results> list, Context context) {
        this.list = list;
        this.context = context;
//        contextHalamanKeamanan = getApplicationContext();
//        judul = (TextView)itemView.findViewById(R.id.person_name);
//        Log.d("nama: ", list.get(0).getName());
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
//        Log.d("log judul ----->>>> ",holder.isEmpty());
//        System.console(list.get(position).nama);
//        holder.textview_nama.setText(list.get(position).getResults().get(position).getName());
//        holder.textview_alamat.setText(list.get(position).getResults().get(position).getName());
        holder.textview_nama.setText(list.get(position).getName());
        holder.textview_alamat.setText(list.get(position).getName());
        Log.d("name: ", list.get(position).getName());
        Log.d("alamat: ", list.get(position).getVicinity());
        holder.container.setOnClickListener(onClickListener(holder, position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private View.OnClickListener onClickListener(final View_Holder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_detail);

//                dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog
//                button = (ImageView) dialog.findViewById(R.id.tombol_cancel);
//                button.setOnClickListener( new View.OnClickListener()
//                {
//                    public void onClick(View v)
//                    {
//                        dialog.dismiss();
//                    }
//                });
                // set the custom dialog components - texts and image
                TextView name = (TextView) dialog.findViewById(R.id.nama_tempat);
                name.setText(list.get(position).getName());
                TextView address = (TextView) dialog.findViewById(R.id.alamat_tempat);
                address.setText(list.get(position).getVicinity());
//                ImageView icon = (ImageView) dialog.findViewById(R.id.image);

//                setDataToView(name, job, icon, position);

                dialog.show();
            }
        };
    }
}
