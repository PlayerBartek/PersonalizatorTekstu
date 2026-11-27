package com.example.personalizatortekstu;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.ColorUtils;

public class MainActivity extends AppCompatActivity {

    private TextView tvText;
    private Button btnChangeText;
    private Button btnChangeColor;
    private Button btnChangeBg;
    private ImageView bgImage;
    private SwitchCompat switchNight;
    private TextView tvAlphaLabel;
    private SeekBar seekAlpha;

    private int[] images;
    private int[] colors;
    private String[] texts;
    private int[] styles;

    private int imageIndex = 1;
    private int colorIndex = 0;
    private int textIndex = 1;
    private int styleIndex = 1;

    private int textAlphaPercent = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("ustawienia", MODE_PRIVATE);

        tvText = findViewById(R.id.tvText);
        btnChangeText = findViewById(R.id.btnChangeText);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        btnChangeBg = findViewById(R.id.btnChangeBg);
        bgImage = findViewById(R.id.bgImage);
        switchNight = findViewById(R.id.switchNight);
        tvAlphaLabel = findViewById(R.id.tvAlphaLabel);
        seekAlpha = findViewById(R.id.seekAlpha);

        images = new int[]{R.drawable.tlo1, R.drawable.tlo2, R.drawable.tlo3};
        colors = new int[]{R.color.text_color, R.color.text_opt2, R.color.text_opt3};
        texts = getResources().getStringArray(R.array.texts);
        styles = new int[]{Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC};

        colorIndex = Math.floorMod(prefs.getInt("colorIndex", 0), colors.length);

        tvText.setText(texts[0]);
        tvText.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        applyTextColor();

        btnChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvText.setText(texts[textIndex]);
                tvText.setTypeface(Typeface.DEFAULT, styles[styleIndex]);
                textIndex = (textIndex + 1) % texts.length;
                styleIndex = (styleIndex + 1) % styles.length;
            }
        });

        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorIndex = (colorIndex + 1) % colors.length;
                applyTextColor();
                prefs.edit().putInt("colorIndex", colorIndex).apply();
            }
        });

        btnChangeBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgImage.setImageResource(images[imageIndex]);
                imageIndex = (imageIndex + 1) % images.length;
            }
        });

        // stan przełącznika zgodny z aktualnym trybem
        boolean isNight = (getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        switchNight.setChecked(isNight);

        switchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                AppCompatDelegate.setDefaultNightMode(isChecked
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        seekAlpha.setProgress(100);
        seekAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textAlphaPercent = progress;
                applyTextColor();
                tvAlphaLabel.setText("Przezroczystość tekstu: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void applyTextColor() {
        int color = getColor(colors[colorIndex]);
        int alpha = textAlphaPercent * 255 / 100;
        tvText.setTextColor(ColorUtils.setAlphaComponent(color, alpha));
    }
}