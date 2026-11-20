package com.gojo.aifitness;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().setStatusBarColor(Color.rgb(9, 6, 17));
        getWindow().setNavigationBarColor(Color.rgb(9, 6, 17));

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER);
        page.setPadding(dp(24), dp(24), dp(24), dp(24));
        page.setBackgroundColor(Color.rgb(9, 6, 17));

        TextView title = new TextView(this);
        title.setText("GOJO AI FITNESS");
        title.setTextColor(Color.rgb(192, 132, 252));
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        TextView note = new TextView(this);
        note.setText("Your fitness journey starts here.\n\nStarter build — features are still being developed.");
        note.setTextColor(Color.WHITE);
        note.setTextSize(16);
        note.setGravity(Gravity.CENTER);
        note.setPadding(0, dp(20), 0, 0);

        page.addView(title);
        page.addView(note);

        setContentView(page);
    }
}
