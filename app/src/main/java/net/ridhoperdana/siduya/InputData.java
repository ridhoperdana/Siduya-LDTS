package net.ridhoperdana.siduya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputData extends AppCompatActivity {

    private Double latitude, longitude;
    private String nama, namautf, alamat, alamatutf;
    private EditText nama_tempat, alamat_tempat, nomor_tempat;
    private Button tombol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        latitude = getIntent().getDoubleExtra("value_lat", 1);
        longitude = getIntent().getDoubleExtra("value_longt", 1);
        nama = getIntent().getExtras().getString("value_nama");
        alamat = getIntent().getExtras().getString("value_alamat");

        nama_tempat = (EditText)findViewById(R.id.input_nama_tempat);
        nama_tempat.setText(nama);

        alamat_tempat = (EditText)findViewById(R.id.input_alamat_tempat);
        alamat_tempat.setText(alamat);

        nomor_tempat = (EditText)findViewById(R.id.input_nomor_tempat);
        nomor_tempat.getText().toString();

//        tombol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
}
