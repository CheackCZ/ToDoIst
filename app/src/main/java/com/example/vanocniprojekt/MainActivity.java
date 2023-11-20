package com.example.vanocniprojekt;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vanocniprojekt.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//Main
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    androidx.appcompat.widget.Toolbar toolbar;

    //Task Arraylist
    private ArrayList<Task> list;

    Date CurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // That is for to set the title of the first fragment
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Falist");
        toolbar.setTitleTextColor(Color.BLACK);

        CurrentDate = Calendar.getInstance().getTime();

    }

    //Method that get the arraylist from the FirstFragment
    public void dispatchInformations(ArrayList<Task> list) {
        this.list = list;
    }

    //Method that saves the data (when you closes app)
    private void SaveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("task list", json);
        editor.apply();
    }

    //Method that loads the data (when you start the app)
    private void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null) {
            list = new ArrayList<>();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Saves data when the app is stopped/Paused
    @Override
    protected void onStop() {
        super.onStop();
        SaveData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

        Date today = calendar.getTime();
        String stringToday = dateFormat.format(today);

        for (int i = 0; i < list.size(); i++) {
            String taskDate = list.get(i).getDate();

            if (stringToday.equals(taskDate)) {
                Toast.makeText(this, "Some tasks should be done today!", Toast.LENGTH_SHORT).show();
            }
        }



    }
}