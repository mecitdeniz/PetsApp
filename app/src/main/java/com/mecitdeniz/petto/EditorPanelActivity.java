package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditorPanelActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    LinearLayout parentTextViewOnayBekleyenIlanlar;
    LinearLayout parentTextViewSikayetler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_panel);

        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        parentTextViewOnayBekleyenIlanlar = findViewById(R.id.parent_onay_bekleyen_ilanlar_tv);
        parentTextViewSikayetler = findViewById(R.id.parent_sikayetler_tv);
        textViewToolbarBaslik.setText("Editör Paneli");

        parentTextViewSikayetler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditorPanelActivity.this,SikayetlerActivity.class);
                startActivity(i);
            }
        });


        parentTextViewOnayBekleyenIlanlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditorPanelActivity.this,OnayBekleyenIlanActivity.class);
                startActivity(i);
            }
        });

    }
}