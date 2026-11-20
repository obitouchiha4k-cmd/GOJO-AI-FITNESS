
package com.gojo.aifitness;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    int purple = Color.rgb(168, 85, 247);
    int white = Color.WHITE;
    int dark = Color.rgb(9, 6, 17);

    int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density);
    }

    GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16), dp(14), dp(16), dp(14));
        c.setBackground(bg(Color.rgb(28, 19, 42), 18));
        return c;
    }

    void addSection(LinearLayout parent, String name,
                    String subtitle) {
        LinearLayout c = card();
        c.setOnClickListener(v ->
            Toast.makeText(this, name + " section",
                Toast.LENGTH_SHORT).show());

        TextView title = text(name, 18, purple);
        title.setTypeface(null, Typeface.BOLD);
        c.addView(title);

        TextView sub = text(subtitle, 13, white);
        sub.setPadding(0, dp(7), 0, 0);
        c.addView(sub);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(8), 0, dp(8));
        parent.addView(c, lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(dark);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(18), dp(25), dp(18), dp(25));

        TextView logo = text("GOJO AI FITNESS", 27, purple);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setGravity(Gravity.CENTER);
        page.addView(logo);

        TextView welcome = text(
            "WELCOME BACK, WARRIOR", 14, white);
        welcome.setGravity(Gravity.CENTER);
        welcome.setPadding(0, dp(10), 0, dp(20));
        page.addView(welcome);

        LinearLayout stats = card();

        TextView heading = text("YOUR FITNESS STATS", 17, purple);
        heading.setTypeface(null, Typeface.BOLD);
        stats.addView(heading);

        TextView calories = text(
            "🔥  Calories: Start tracking", 16, white);
        calories.setPadding(0, dp(15), 0, dp(8));
        stats.addView(calories);

        TextView workout = text(
            "💪  Workouts: Start your journey", 16, white);
        workout.setPadding(0, dp(8), 0, dp(8));
        stats.addView(workout);

        TextView water = text(
            "💧  Water: Track your daily intake", 16, white);
        water.setPadding(0, dp(8), 0, dp(5));
        stats.addView(water);

        page.addView(stats);

        TextView quick = text("YOUR FITNESS ZONE", 20, white);
        quick.setTypeface(null, Typeface.BOLD);
        quick.setPadding(0, dp(24), 0, dp(10));
        page.addView(quick);

        addSection(page, "💪 WORKOUT TRACKER",
            "Exercises, sets, reps and rest timer");

        addSection(page, "🥗 DIET & CALORIES",
            "Meals, calories, protein, carbs and fats");

        addSection(page, "📸 AI MEAL SCANNER",
            "Scan food and estimate its nutrition");

        addSection(page, "📊 PROGRESS",
            "Track your weight and fitness progress");

        addSection(page, "👤 PROFILE & SETTINGS",
            "Your personal fitness profile");

        TextView footer = text(
            "TRAIN SMART. LIVE STRONG. 🔥", 14, purple);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        page.addView(footer);

        scroll.addView(page);
        setContentView(scroll);
    }
            }
