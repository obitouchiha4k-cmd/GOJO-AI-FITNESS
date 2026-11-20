
package com.gojo.aifitness;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    int purple = Color.rgb(168, 85, 247);
    int white = Color.WHITE;
    int dark = Color.rgb(9, 6, 17);
    int cardColor = Color.rgb(28, 19, 42);

    LinearLayout page;
    SharedPreferences prefs;
    Handler handler = new Handler();

    TextView timerText;
    Button timerButton;
    int seconds = 60;
    boolean timerRunning = false;

    EditText[] setsInputs = new EditText[3];
    EditText[] repsInputs = new EditText[3];

    String[] exercises = {"Push-ups", "Squats", "Plank"};

    EditText foodInput, caloriesInput;
    EditText proteinInput, carbsInput, fatsInput;
    TextView nutritionSummary, waterText;

    String today() {
        return new SimpleDateFormat(
            "yyyyMMdd", Locale.US).format(new Date());
    }

    String dailyKey(String name) {
        return name + "_" + today();
    }

    int getDaily(String name) {
        return prefs.getInt(dailyKey(name), 0);
    }

    void addDaily(String name, int amount) {
        prefs.edit().putInt(
            dailyKey(name), getDaily(name) + amount).apply();
    }

    int dp(int n) {
        return (int)(n * getResources()
            .getDisplayMetrics().density);
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
        c.setBackground(bg(cardColor, 18));
        return c;
    }

    Button makeButton(String name) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextColor(white);
        b.setAllCaps(false);
        b.setBackground(bg(Color.rgb(105, 45, 170), 14));
        return b;
    }

    EditText input(String hint, boolean number) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextColor(white);
        e.setHintTextColor(Color.LTGRAY);
        e.setTextSize(15);
        e.setPadding(dp(12), 0, dp(12), 0);
        e.setBackground(bg(Color.rgb(48, 35, 62), 10));
        if (number) e.setInputType(2);
        return e;
    }

    EditText numberInput(String value) {
        EditText e = input(value, true);
        e.setText(value);
        return e;
    }

    void setupPage(String title) {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(dark);

        page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(18), dp(24), dp(18), dp(25));

        TextView heading = text(title, 25, purple);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, 0, 0, dp(20));
        page.addView(heading);

        scroll.addView(page);
        setContentView(scroll);
    }

    void addHomeButton(String name, String subtitle,
                       Runnable action) {
        LinearLayout c = card();

        TextView title = text(name, 18, purple);
        title.setTypeface(null, Typeface.BOLD);
        c.addView(title);

        TextView sub = text(subtitle, 13, white);
        sub.setPadding(0, dp(7), 0, 0);
        c.addView(sub);

        c.setOnClickListener(v -> action.run());

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, dp(7), 0, dp(7));
        page.addView(c, lp);
    }

    void backButton() {
        Button home = makeButton("← Back to Home");
        home.setOnClickListener(v -> showHome());
        page.addView(home);
    }

    void showHome() {
        setupPage("GOJO AI FITNESS");

        TextView welcome = text(
            "WELCOME BACK, WARRIOR", 14, white);
        welcome.setGravity(Gravity.CENTER);
        welcome.setPadding(0, 0, 0, dp(20));
        page.addView(welcome);

        LinearLayout stats = card();

        TextView heading = text(
            "YOUR FITNESS STATS", 17, purple);
        heading.setTypeface(null, Typeface.BOLD);
        stats.addView(heading);

        stats.addView(text(
            "Completed Workouts: " +
            prefs.getInt("workouts", 0), 16, white));

        TextView calories = text(
            "Today's Calories: " + getDaily("calories") +
            " kcal", 16, white);
        calories.setPadding(0, dp(10), 0, 0);
        stats.addView(calories);

        page.addView(stats);

        TextView zone = text("YOUR FITNESS ZONE", 20, white);
        zone.setTypeface(null, Typeface.BOLD);
        zone.setPadding(0, dp(24), 0, dp(10));
        page.addView(zone);

        addHomeButton("💪 WORKOUT TRACKER",
            "Exercises, sets, reps and rest timer",
            () -> showWorkout());

        addHomeButton("🥗 DIET & CALORIES",
            "Meals, calories, protein, carbs and fats",
            () -> showDiet());

        addHomeButton("📸 AI MEAL SCANNER",
            "Food scanning and nutrition estimates",
            () -> comingSoon("AI Meal Scanner"));

        addHomeButton("📊 PROGRESS",
            "Weight and fitness progress",
            () -> comingSoon("Progress"));

        addHomeButton("👤 PROFILE & SETTINGS",
            "Personal fitness settings",
            () -> comingSoon("Profile & Settings"));

        TextView footer = text(
            "TRAIN SMART. LIVE STRONG. 🔥", 14, purple);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        page.addView(footer);
    }

    void comingSoon(String name) {
        Toast.makeText(this,
            name + " will be added in a future update",
            Toast.LENGTH_SHORT).show();
    }

    void showWorkout() {
        setupPage("💪 WORKOUT TRACKER");
        backButton();

        TextView intro = text(
            "Enter your sets and reps for each exercise.",
            14, white);
        intro.setPadding(0, dp(18), 0, dp(15));
        page.addView(intro);

        for (int i = 0; i < exercises.length; i++) {
            LinearLayout c = card();

            TextView name = text(exercises[i], 19, purple);
            name.setTypeface(null, Typeface.BOLD);
            c.addView(name);

            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView setsLabel = text("Sets", 14, white);
            row.addView(setsLabel);

            EditText sets = numberInput("3");
            LinearLayout.LayoutParams p1 =
                new LinearLayout.LayoutParams(0, dp(48), 1);
            p1.setMargins(dp(8), 0, dp(12), 0);
            row.addView(sets, p1);
            setsInputs[i] = sets;

            TextView repsLabel = text("Reps", 14, white);
            row.addView(repsLabel);

            EditText reps = numberInput("10");
            LinearLayout.LayoutParams p2 =
                new LinearLayout.LayoutParams(0, dp(48), 1);
            p2.setMargins(dp(8), 0, 0, 0);
            row.addView(reps, p2);
            repsInputs[i] = reps;

            c.addView(row);

            LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, dp(7), 0, dp(7));
            page.addView(c, lp);
        }

        Button save = makeButton("SAVE WORKOUT");
        save.setOnClickListener(v -> saveWorkout());
        page.addView(save);

        LinearLayout timerCard = card();

        TextView timerTitle = text("⏱ REST TIMER", 20, purple);
        timerTitle.setTypeface(null, Typeface.BOLD);
        timerCard.addView(timerTitle);

        timerText = text("01:00", 35, white);
        timerText.setGravity(Gravity.CENTER);
        timerText.setPadding(0, dp(18), 0, dp(18));
        timerCard.addView(timerText);

        timerButton = makeButton("START 60 SECOND REST");
        timerButton.setOnClickListener(v -> startTimer());
        timerCard.addView(timerButton);

        Button reset = makeButton("RESET TIMER");
        reset.setOnClickListener(v -> resetTimer());
        timerCard.addView(reset);

        LinearLayout.LayoutParams timerLp =
            new LinearLayout.LayoutParams(-1, -2);
        timerLp.setMargins(0, dp(15), 0, dp(15));
        page.addView(timerCard, timerLp);

        page.addView(text(
            "Total completed workouts: " +
            prefs.getInt("workouts", 0), 16, white));
    }

    void saveWorkout() {
        StringBuilder summary = new StringBuilder();

        for (int i = 0; i < exercises.length; i++) {
            String sets = setsInputs[i].getText().toString().trim();
            String reps = repsInputs[i].getText().toString().trim();

            if (sets.isEmpty() || reps.isEmpty()) {
                Toast.makeText(this,
                    "Enter sets and reps for all exercises",
                    Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int s = Integer.parseInt(sets);
                int r = Integer.parseInt(reps);

                if (s < 1 || s > 100 || r < 1 || r > 1000) {
                    Toast.makeText(this,
                        "Enter valid sets and reps",
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                summary.append(exercises[i])
                    .append(": ").append(s)
                    .append(" sets x ").append(r)
                    .append(" reps\n");

            } catch (NumberFormatException ex) {
                Toast.makeText(this,
                    "Numbers only please",
                    Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int total = prefs.getInt("workouts", 0) + 1;

        prefs.edit()
            .putInt("workouts", total)
            .putString("last_workout", summary.toString())
            .apply();

        Toast.makeText(this,
            "Workout saved successfully! 💪",
            Toast.LENGTH_LONG).show();

        showWorkout();
    }

    Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            if (!timerRunning || timerText == null) return;

            seconds--;

            if (seconds <= 0) {
                seconds = 0;
                timerText.setText("00:00");
                timerRunning = false;
                timerButton.setText("START AGAIN");
                Toast.makeText(MainActivity.this,
                    "Rest complete! Let's go! 🔥",
                    Toast.LENGTH_LONG).show();
                return;
            }

            updateTimer();
            handler.postDelayed(this, 1000);
        }
    };

    void updateTimer() {
        timerText.setText(String.format(
            Locale.US, "%02d:%02d",
            seconds / 60, seconds % 60));
    }

    void startTimer() {
        if (timerRunning) return;
        if (seconds <= 0) seconds = 60;

        timerRunning = true;
        timerButton.setText("TIMER RUNNING...");
        updateTimer();
        handler.postDelayed(timerTask, 1000);
    }

    void resetTimer() {
        handler.removeCallbacks(timerTask);
        timerRunning = false;
        seconds = 60;

        if (timerText != null) timerText.setText("01:00");
        if (timerButton != null)
            timerButton.setText("START 60 SECOND REST");
    }

    void showDiet() {
        setupPage("🥗 DIET & CALORIES");
        backButton();

        TextView subtitle = text(
            "YOUR DAILY NUTRITION", 18, purple);
        subtitle.setTypeface(null, Typeface.BOLD);
        subtitle.setPadding(0, dp(20), 0, dp(12));
        page.addView(subtitle);

        LinearLayout totals = card();
        nutritionSummary = text("", 16, white);
        totals.addView(nutritionSummary);
        page.addView(totals);
        updateNutrition();

        TextView addTitle = text(
            "ADD YOUR MEAL", 19, purple);
        addTitle.setTypeface(null, Typeface.BOLD);
        addTitle.setPadding(0, dp(22), 0, dp(12));
        page.addView(addTitle);

        LinearLayout form = card();

        foodInput = input("Food name (e.g. Rice)", false);
        form.addView(foodInput);

        caloriesInput = input("Calories (kcal)", true);
        form.addView(caloriesInput);

        proteinInput = input("Protein (grams)", true);
        form.addView(proteinInput);

        carbsInput = input("Carbs (grams)", true);
        form.addView(carbsInput);

        fatsInput = input("Fats (grams)", true);
        form.addView(fatsInput);

        Button saveMeal = makeButton("SAVE MEAL");
        saveMeal.setOnClickListener(v -> saveMeal());
        form.addView(saveMeal);

        page.addView(form);

        TextView waterTitle = text(
            "💧 WATER TRACKER", 19, purple);
        waterTitle.setTypeface(null, Typeface.BOLD);
        waterTitle.setPadding(0, dp(22), 0, dp(12));
        page.addView(waterTitle);

        LinearLayout waterCard = card();

        waterText = text("", 18, white);
        waterCard.addView(waterText);
        updateWater();

        Button addWater = makeButton("+ ADD 250 ML WATER");
        addWater.setOnClickListener(v -> {
            addDaily("water", 250);
            updateWater();
        });
        waterCard.addView(addWater);

        Button resetWater = makeButton("RESET WATER");
        resetWater.setOnClickListener(v -> {
            prefs.edit().putInt(dailyKey("water"), 0).apply();
            updateWater();
        });
        waterCard.addView(resetWater);

        page.addView(waterCard);

        TextView note = text(
            "Nutrition values are estimates. Enter the values " +
            "from your food label or a trusted nutrition source.",
            12, Color.LTGRAY);
        note.setPadding(0, dp(18), 0, dp(10));
        page.addView(note);
    }

    void updateNutrition() {
        if (nutritionSummary == null) return;

        nutritionSummary.setText(
            "Calories: " + getDaily("calories") + " kcal\n\n" +
            "Protein: " + getDaily("protein") + " g\n\n" +
            "Carbs: " + getDaily("carbs") + " g\n\n" +
            "Fats: " + getDaily("fats") + " g"
        );
    }

    void updateWater() {
        if (waterText != null) {
            waterText.setText(
                "Today's water: " + getDaily("water") + " ml");
        }
    }

    void saveMeal() {
        String food = foodInput.getText().toString().trim();

        String cal = caloriesInput.getText().toString().trim();
        String pro = proteinInput.getText().toString().trim();
        String carb = carbsInput.getText().toString().trim();
        String fat = fatsInput.getText().toString().trim();

        if (food.isEmpty() || cal.isEmpty() ||
            pro.isEmpty() || carb.isEmpty() || fat.isEmpty()) {
            Toast.makeText(this,
                "Please fill in all meal details",
                Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int c = Integer.parseInt(cal);
            int p = Integer.parseInt(pro);
            int cb = Integer.parseInt(carb);
            int f = Integer.parseInt(fat);

            if (c < 0 || p < 0 || cb < 0 || f < 0 ||
                c > 10000 || p > 1000 || cb > 1000 || f > 1000) {
                Toast.makeText(this,
                    "Please enter valid nutrition values",
                    Toast.LENGTH_SHORT).show();
                return;
            }

            addDaily("calories", c);
            addDaily("protein", p);
            addDaily("carbs", cb);
            addDaily("fats", f);

            prefs.edit().putString(
                "last_meal",
                food + ": " + c + " kcal").apply();

            Toast.makeText(this,
                "Meal saved successfully! 🥗",
                Toast.LENGTH_LONG).show();

            showDiet();

        } catch (NumberFormatException ex) {
            Toast.makeText(this,
                "Please enter numbers only",
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);

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
                            

                                        
