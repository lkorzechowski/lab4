package com.orzechowski.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawingSurface surface = findViewById(R.id.drawing_surface);

        Button red = findViewById(R.id.kolor_czerwony);
        red.setOnClickListener(v -> surface.setPaintColor(Color.RED));

        Button black = findViewById(R.id.kolor_czarny);
        black.setOnClickListener(v -> surface.setPaintColor(Color.BLACK));

        Button blue = findViewById(R.id.kolor_niebieski);
        blue.setOnClickListener(v -> surface.setPaintColor(Color.BLUE));

        Button green = findViewById(R.id.kolor_zielony);
        green.setOnClickListener(v -> surface.setPaintColor(Color.GREEN));

        ImageButton delete = findViewById(R.id.czysc);
        delete.setOnClickListener(v -> surface.deleteDrawing());
    }
}