package net.ridhoperdana.siduya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.view.View;

public class HalamanDepan extends Activity {

    private CardView cardKeamanan, cardKesehatan, cardTransportasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_halaman_depan);

        cardKeamanan = (CardView) findViewById(R.id.card_keamanan);

        cardKeamanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanDepan.this, HalamanKeamanan.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.class, R.anim.left_out);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        HalamanDepan.this,

                        // For each shared element, add to this method a new Pair item,
                        // which contains the reference of the view we are transitioning *from*,
                        // and the value of the transitionName attribute
                        new Pair<View, String>(view.findViewById(R.id.card_keamanan),getString(R.string.transition_name_name))
                );
                ActivityCompat.startActivity(HalamanDepan.this, intent, options.toBundle());
            }
        });

    }

}
