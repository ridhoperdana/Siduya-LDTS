package net.ridhoperdana.siduya;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HalamanKeamanan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_halaman_keamanan);

        List<Data> data = fill_with_data();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        Adapter adapter = new Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(HalamanKeamanan.this);
//
//                //setting custom layout to dialog
//                dialog.setContentView(R.layout.custom_dialog);
////                dialog.setTitle("Custom Dialog");
//
//                //adding text dynamically
////                TextView txt = (TextView) dialog.findViewById(R.id.textView);
////                txt.setText("Put your dialog text here.");
//
////                ImageView image = (ImageView)dialog.findViewById(R.id.image);
////                image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
//
//                //adding button click event
////                Button dismissButton = (Button) dialog.findViewById(R.id.button);
//                ImageView image = (ImageView)dialog.findViewById(R.id.tombol_cancel);
//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//            }
//        });

    }

    public List<Data> fill_with_data() {
        List<Data> data = new ArrayList<>();

        data.add(new Data("Polsek Sukolilo", "Jl. Manyar Kertoadi 1 No.701, Klampis Ngasem, Sukolilo, Kota SBY, Jawa Timur, Indonesia."));
        data.add(new Data("Polsek Pulogadung", "Jl Cipingan Baru Raya Jakarta Timur, Tlp 021-4892844, Indonesia."));
        data.add(new Data("Polsek Manyar", "Jl. Raya Manyar No. 41, Gresik, Jawa Timur, Indonesia."));

        return data;
    }

}
