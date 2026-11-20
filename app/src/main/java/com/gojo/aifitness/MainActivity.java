
package com.gojo.aifitness;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;
import android.content.SharedPreferences;
import java.util.Locale;

public class MainActivity extends Activity {

    final int PURPLE = Color.rgb(170, 80, 255);
    final int WHITE = Color.WHITE;
    final int DARK = Color.rgb(7, 5, 15);
    final int CARD = Color.rgb(25, 17, 40);
    final int MUTED = Color.rgb(180, 165, 200);

    LinearLayout root, page;
    SharedPreferences prefs;
    Handler handler = new Handler();

    TextView timerText;
    Button timerButton;
    int seconds = 60;
    boolean timerRunning = false;

    EditText[] setsInputs = new EditText[3];
    EditText[] repsInputs = new EditText[3];

    EditText foodInput, caloriesInput;
    EditText proteinInput, carbsInput, fatsInput;
    TextView nutritionSummary, waterText;

    String[] exercises = {"Push-ups", "Squats", "Plank"};

    int dp(int n) {
        return (int)(n * getResources()
            .getDisplayMetrics().density);
    }

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(1), Color.rgb(105, 48, 155));
        return d;
    }

    GradientDrawable gradient(int radius) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] {
                Color.rgb(68, 25, 110),
                Color.rgb(25, 12, 42),
                Color.rgb(12, 8, 22)
            });
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(1), PURPLE);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    TextView bold(String value, int size, int color) {
        TextView t = text(value, size, color);
        t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    LinearLayout vertical() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    LinearLayout card() {
        LinearLayout c = vertical();
        c.setPadding(dp(17), dp(16), dp(17), dp(16));
        c.setBackground(shape(CARD, 19));
        return c;
    }

    Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(WHITE);
        b.setTextSize(13);
        b.setAllCaps(false);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackground(gradient(14));
        return b;
    }

    EditText input(String hint, boolean number) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextColor(WHITE);
        e.setHintTextColor(MUTED);
        e.setTextSize(14);
        e.setSingleLine(true);
        e.setPadding(dp(12), 0, dp(12), 0);
        e.setBackground(shape(
            Color.rgb(39, 27, 55), 12));
        if (number) e.setInputType(2);
        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, dp(50));
        lp.setMargins(0, dp(6), 0, dp(6));
        e.setLayoutParams(lp);
        return e;
    }

    String dailyKey(String name) {
        return name + "_" +
            new java.text.SimpleDateFormat(
                "yyyyMMdd", Locale.US).format(new java.util.Date());
    }

    int getDaily(String name) {
        return prefs.getInt(dailyKey(name), 0);
    }

    void addDaily(String name, int amount) {
        prefs.edit().putInt(
            dailyKey(name), getDaily(name) + amount).apply();
    }

    void setupPage(String title, String subtitle) {
        root = vertical();
        root.setBackgroundColor(DARK);

        LinearLayout header = vertical();
        header.setPadding(dp(20), dp(18), dp(20), dp(12));

        TextView brand = bold("GOJO  /  AI FITNESS", 13, PURPLE);
        header.addView(brand);

        TextView heading = bold(title, 25, WHITE);
        heading.setPadding(0, dp(8), 0, dp(3));
        header.addView(heading);

        TextView sub = text(subtitle, 12, MUTED);
        header.addView(sub);

        root.addView(header);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);

        page = vertical();
        page.setPadding(dp(18), dp(8), dp(18), dp(20));

        scroll.addView(page);

        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1));

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(7), dp(8), dp(7), dp(8));
        nav.setBackgroundColor(Color.rgb(17, 11, 27));

        addNav(nav, "⌂\nHOME", () -> showHome());
        addNav(nav, "⚔\nWORKOUT", () -> showWorkout());
        addNav(nav, "◈\nDIET", () -> showDiet());

        root.addView(nav, new LinearLayout.LayoutParams(
            -1, dp(66)));

        setContentView(root);
    }

    void addNav(LinearLayout nav, String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(10);
        b.setTextColor(WHITE);
        b.setAllCaps(false);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setOnClickListener(v -> action.run());

        nav.addView(b, new LinearLayout.LayoutParams(
            0, -1, 1));
    }

    void addCard(LinearLayout c) {
        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(7), 0, dp(7));
        page.addView(c, lp);
    }

    void sectionTitle(String value) {
        TextView t = bold(value, 17, WHITE);
        t.setPadding(0, dp(19), 0, dp(8));
        page.addView(t);
    }

    void showHome() {
        setupPage("THE LIMITLESS ERA",
            "YOUR JOURNEY. YOUR POWER. YOUR RULES.");

        LinearLayout hero = vertical();
        hero.setPadding(dp(20), dp(23), dp(20), dp(23));
        hero.setBackground(gradient(23));

        TextView tag = bold("SPECIAL GRADE FITNESS", 11, PURPLE);
        hero.addView(tag);

        TextView name = bold("UNLEASH YOUR\nLIMITLESS POTENTIAL", 27, WHITE);
        name.setPadding(0, dp(12), 0, dp(10));
        hero.addView(name);

        hero.addView(text(
            "Train harder. Track smarter. Become stronger.",
            13, MUTED));

        TextView power = bold("∞  GOJO MODE", 15, WHITE);
        power.setPadding(0, dp(18), 0, 0);
        hero.addView(power);

        addCard(hero);

        sectionTitle("TODAY'S OVERVIEW");

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);

        addStat(stats, "CALORIES",
            getDaily("calories") + " kcal");
        addStat(stats, "WORKOUTS",
            "" + prefs.getInt("workouts", 0));

        addCard(stats);

        LinearLayout water = card();
        water.addView(bold("WATER INTAKE", 15, PURPLE));
        water.addView(text(
            getDaily("water") + " ml consumed today",
            14, WHITE));
        addCard(water);

        sectionTitle("YOUR TRAINING ZONE");

        addHomeTile("⚔  WORKOUT TRACKER",
            "Sets, reps and rest timer",
            () -> showWorkout());

        addHomeTile("◈  DIET & CALORIES",
            "Meals and daily nutrition",
            () -> showDiet());

        addHomeTile("◎  AI MEAL SCANNER",
            "Camera food scanning",
            () -> comingSoon("AI Meal Scanner"));

        addHomeTile("✦  PROGRESS",
            "Fitness charts and weight tracking",
            () -> comingSoon("Progress"));

        addHomeTile("☯  PROFILE",
            "Personal fitness settings",
            () -> comingSoon("Profile"));

        TextView footer = bold(
            "TRAIN SMART  •  LIVE STRONG",
            12, PURPLE);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(24), 0, dp(15));
        page.addView(footer);
    }

    void addStat(LinearLayout parent,
                 String title, String value) {
        LinearLayout c = card();
        c.addView(bold(title, 11, MUTED));
        TextView v = bold(value, 19, WHITE);
        v.setPadding(0, dp(9), 0, 0);
        c.addView(v);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(0, -2, 1);
        lp.setMargins(dp(4), 0, dp(4), 0);
        parent.addView(c, lp);
    }

    void addHomeTile(String title, String sub, Runnable action) {
        LinearLayout c = card();
        c.setBackground(gradient(17));

        c.addView(bold(title, 16, WHITE));
        TextView s = text(sub, 12, MUTED);
        s.setPadding(0, dp(6), 0, 0);
        c.addView(s);

        c.setOnClickListener(v -> action.run());
        addCard(c);
    }

    void comingSoon(String name) {
        Toast.makeText(this,
            name + " will be added in a future update",
            Toast.LENGTH_SHORT).show();
    }

    void showWorkout() {
        setupPage("WORKOUT ARENA",
            "BUILD YOUR STRENGTH • ONE REP AT A TIME");

        LinearLayout intro = card();
        intro.setBackground(gradient(18));
        intro.addView(bold("TODAY'S TRAINING", 18, WHITE));
        intro.addView(text(
            "Complete your exercises and save your session.",
            12, MUTED));
        addCard(intro);

        for (int i = 0; i < exercises.length; i++) {
            LinearLayout c = card();

            c.addView(bold(
                "⚡  " + exercises[i], 18, PURPLE));

            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView a = text("SETS", 11, MUTED);
            row.addView(a);

            EditText sets = input("3", true);
            sets.setText("3");
            sets.setLayoutParams(new LinearLayout.LayoutParams(
                0, dp(48), 1));
            setsInputs[i] = sets;
            row.addView(sets);

            TextView b = text("REPS", 11, MUTED);
            b.setPadding(dp(8), 0, 0, 0);
            row.addView(b);

            EditText reps = input("10", true);
            reps.setText("10");
            reps.setLayoutParams(new LinearLayout.LayoutParams(
                0, dp(48), 1));
            repsInputs[i] = reps;
            row.addView(reps);

            c.addView(row);
            addCard(c);
        }

        Button save = button("SAVE WORKOUT  ✓");
        save.setOnClickListener(v -> saveWorkout());
        page.addView(save);

        sectionTitle("REST TIMER");

        LinearLayout timer = card();
        timer.setBackground(gradient(20));

        timerText = bold("01:00", 38, WHITE);
        timerText.setGravity(Gravity.CENTER);
        timer.addView(timerText);

        timerButton = button("START REST");
        timerButton.setOnClickListener(v -> startTimer());
        timer.addView(timerButton);

        Button reset = button("RESET TIMER");
        reset.setOnClickListener(v -> resetTimer());
        timer.addView(reset);

        addCard(timer);

        TextView total = text(
            "Completed workouts: " +
            prefs.getInt("workouts", 0), 14, WHITE);
        total.setPadding(0, dp(15), 0, 0);
        page.addView(total);
    }

    void saveWorkout() {
        StringBuilder summary = new StringBuilder();

        for (int i = 0; i < exercises.length; i++) {
            String s = setsInputs[i].getText().toString().trim();
            String r = repsInputs[i].getText().toString().trim();

            try {
                int a = Integer.parseInt(s);
                int b = Integer.parseInt(r);

                if (a < 1 || a > 100 || b < 1 || b > 1000)
                    throw new Exception();

                summary.append(exercises[i])
                    .append(": ").append(a)
                    .append(" x ").append(b).append("\n");

            } catch (Exception e) {
                Toast.makeText(this,
                    "Please enter valid sets and reps",
                    Toast.LENGTH_SHORT).show();
                return;
            }
        }

        prefs.edit()
            .putInt("workouts",
                prefs.getInt("workouts", 0) + 1)
            .putString("last_workout", summary.toString())
            .apply();

        Toast.makeText(this,
            "Workout saved! 💜", Toast.LENGTH_SHORT).show();
        showWorkout();
    }

    Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            if (!timerRunning || timerText == null) return;

            seconds--;

            if (seconds <= 0) {
                seconds = 0;
                timerRunning = false;
                timerText.setText("00:00");
                timerButton.setText("START AGAIN");
                Toast.makeText(MainActivity.this,
                    "REST COMPLETE! LET'S GO!",
                    Toast.LENGTH_LONG).show();
                return;
            }

            updateTimer();
            handler.postDelayed(this, 1000);
        }
    };

    void updateTimer() {
        timerText.setText(String.format(Locale.US,
            "%02d:%02d", seconds / 60, seconds % 60));
    }

    void startTimer() {
        if (timerRunning) return;
        if (seconds <= 0) seconds = 60;

        timerRunning = true;
        timerButton.setText("TIMER RUNNING");
        updateTimer();
        handler.postDelayed(timerTask, 1000);
    }

    void resetTimer() {
        handler.removeCallbacks(timerTask);
        timerRunning = false;
        seconds = 60;
        if (timerText != null) timerText.setText("01:00");
        if (timerButton != null) timerButton.setText("START REST");
    }

    void showDiet() {
        setupPage("NUTRITION CENTER",
            "FUEL YOUR BODY • POWER YOUR TRAINING");

        LinearLayout totals = card();
        totals.setBackground(gradient(20));
        totals.addView(bold("DAILY NUTRITION", 18, WHITE));

        nutritionSummary = text("", 15, WHITE);
        nutritionSummary.setPadding(0, dp(12), 0, 0);
        totals.addView(nutritionSummary);

        addCard(totals);
        updateNutrition();

        sectionTitle("ADD YOUR MEAL");

        LinearLayout form = card();

        foodInput = input("Food name", false);
        form.addView(foodInput);

        caloriesInput = input("Calories (kcal)", true);
        form.addView(caloriesInput);

        proteinInput = input("Protein (grams)", true);
        form.addView(proteinInput);

        carbsInput = input("Carbs (grams)", true);
        form.addView(carbsInput);

        fatsInput = input("Fats (grams)", true);
        form.addView(fatsInput);

        Button save = button("SAVE MEAL  ✓");
        save.setOnClickListener(v -> saveMeal());
        form.addView(save);

        addCard(form);

        sectionTitle("WATER TRACKER");

        LinearLayout water = card();
        waterText = text("", 16, WHITE);
        water.addView(waterText);
        updateWater();

        Button add = button("+ ADD 250 ML");
        add.setOnClickListener(v -> {
            addDaily("water", 250);
            updateWater();
        });
        water.addView(add);

        Button reset = button("RESET WATER");
        reset.setOnClickListener(v -> {
            prefs.edit().putInt(dailyKey("water"), 0).apply();
            updateWater();
        });
        water.addView(reset);

        addCard(water);

        TextView note = text(
            "Nutrition values are estimates. Use food labels " +
            "or trusted nutrition sources for accurate entries.",
            11, MUTED);
        note.setPadding(0, dp(15), 0, dp(10));
        page.addView(note);
    }

    void updateNutrition() {
        if (nutritionSummary == null) return;

        nutritionSummary.setText(
            "🔥 Calories     " + getDaily("calories") + " kcal\n\n" +
            "💪 Protein       " + getDaily("protein") + " g\n\n" +
            "🍚 Carbs         " + getDaily("carbs") + " g\n\n" +
            "🥑 Fats           " + getDaily("fats") + " g");
    }

    void updateWater() {
        if (waterText != null) {
            waterText.setText(
                "💧 Today's water: " +
                getDaily("water") + " ml");
        }
    }

    void saveMeal() {
        String food = foodInput.getText().toString().trim();

        String[] values = {
            caloriesInput.getText().toString().trim(),
            proteinInput.getText().toString().trim(),
            carbsInput.getText().toString().trim(),
            fatsInput.getText().toString().trim()
        };

        if (food.isEmpty()) {
            Toast.makeText(this,
                "Enter the food name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int[] nums = new int[4];

            for (int i = 0; i < 4; i++) {
                nums[i] = Integer.parseInt(values[i]);
                if (nums[i] < 0 || nums[i] > 10000)
                    throw new Exception();
            }

            addDaily("calories", nums[0]);
            addDaily("protein", nums[1]);
            addDaily("carbs", nums[2]);
            addDaily("fats", nums[3]);

            prefs.edit().putString(
                "last_meal", food + " - " +
                nums[0] + " kcal").apply();

            Toast.makeText(this,
                "Meal saved successfully!",
                Toast.LENGTH_SHORT).show();

            showDiet();

        } catch (Exception e) {
            Toast.makeText(this,
                "Enter valid nutrition numbers",
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(DARK);

        prefs = getSharedPreferences(
            "GojoFitnessData", MODE_PRIVATE);

        showHome();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timerTask);
        super.onDestroy();
    }
}
