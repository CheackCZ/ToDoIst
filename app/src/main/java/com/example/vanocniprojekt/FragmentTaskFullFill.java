package com.example.vanocniprojekt;

import static android.content.Context.MODE_PRIVATE;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.vanocniprojekt.databinding.FragmentTaskFullFillBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

//Fragment - window where u fill in the information
public class FragmentTaskFullFill extends Fragment {

    private FragmentTaskFullFillBinding binding;

    //The declaration of text for users to write an title of the task
    EditText editText_title;
    EditText editText_describtion;

    //The declaration of text for users to see selected date and time
    public EditText editText_selectedDate;
    public EditText editText_selectedTime;

    //Spinner and the array with the values of the spinner
    String[] prioritiesList = {"Very important", "Important", "Less important"};
    Spinner spinner;

    //The declaration of calendar
    MaterialDatePicker materialDatePicker;

    //Recycler view
    ArrayList<Task>list;

    //RatingBar
    RatingBar ratingBar;

    //Task
    Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaskFullFillBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        task = new Task();
        list = new ArrayList<>();

        //The description text for user to know what is the selected time
        editText_selectedTime = getActivity().findViewById(R.id.editTextTime);
        editText_selectedTime.setHint("Selected Time");
        editText_selectedTime.setHintTextColor(Color.GRAY);
        editText_selectedTime.setEnabled(false);

        //The description text for user to know what is the selected date
        editText_selectedDate = getActivity().findViewById(R.id.editTextDate);
        editText_selectedDate.setHint("Selected Date");
        editText_selectedDate.setHintTextColor(Color.GRAY);
        editText_selectedDate.setEnabled(false);

        //The describtion text for user to know where to write the title of the task
        editText_title = getActivity().findViewById(R.id.titleEditText);
        editText_title.setHint("Title");
        editText_title.setHintTextColor(Color.GRAY);

        //The description text for users to know where to write
        editText_describtion = getActivity().findViewById(R.id.describtionEditText);
        editText_describtion.setHint("Description");
        editText_describtion.setHintTextColor(Color.GRAY);


        ratingBar = getActivity().findViewById(R.id.ratingBar);


        //Action that happens when you click on the "add" button in the task "FragmentTaskFullFill" window
        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {

            String text;
            String textDate;
            String textTime;

            @Override
            public void onClick(View view) {

                text = editText_title.getText().toString();
                textDate = editText_selectedDate.getText().toString();
                textTime = editText_selectedTime.getText().toString();

                //It checkes if is it clear or not
                if (text.matches("") && (textDate.matches("")) && (textTime.matches(""))) {
                    editText_title.setError("Title cannot be empty!");
                    editText_selectedDate.setError("Date should be selected!");
                    editText_selectedTime.setError("Time should be selected");

                } else if (textDate.matches("") && (textTime.matches("")) && (!text.matches(""))) {
                    editText_selectedDate.setError("Date should be selected!");
                    editText_selectedTime.setError("Time should be selected");
                    editText_title.setError(null);
                } else if (textDate.matches("") && (text.matches("")) && (!textTime.matches(""))) {
                    editText_selectedDate.setError("Date should be selected!");
                    editText_title.setError("Title cannot be empty!");
                    editText_selectedTime.setError(null);

                } else if (textTime.matches("") && (text.matches("")) && (!textDate.matches(""))) {
                    editText_title.setError("Title cannot be empty!");
                    editText_selectedTime.setError("Time should be selected");
                    editText_selectedDate.setError(null);

                } else if (textTime.matches("") && (!text.matches("") && (!textDate.matches("")))) {
                    editText_selectedTime.setError("Time should be selected");
                    editText_title.setError(null);
                    editText_selectedDate.setError(null);
                } else if (text.matches("") && (!textDate.matches("")) && (!textTime.matches(""))) {
                    editText_title.setError("Title cannot be empty!");
                    editText_selectedDate.setError(null);
                    editText_selectedTime.setError(null);
                } else if (textDate.matches("") && (!text.matches("") && (!textTime.matches("")))) {
                    editText_selectedDate.setError("Date should be selected!");
                    editText_selectedTime.setError(null);
                    editText_title.setError(null);

                } else {
                    task.setTitle(editText_title.getText().toString());
                    task.setDescription(editText_describtion.getText().toString());
                    task.setDate(editText_selectedDate.getText().toString());
                    task.setTime(editText_selectedTime.getText().toString());

                    //Load all the data to save them later on
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("task list", null);
                    Type type = new TypeToken<ArrayList<Task>>() {}.getType();
                    list = gson.fromJson(json, type);

                    //Add created task to the list
                    list.add(task);

                    //Save all the data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(list);
                    editor.putString("task list", json);
                    editor.commit();

                    Bundle bundle = new Bundle();
                        bundle.putSerializable("Task", task);

                    NavHostFragment.findNavController(FragmentTaskFullFill.this)
                            .navigate(R.id.action_fragmentTask_to_FirstFragment, bundle);
                }
            }
        });


        //Action that happens when you click on the "cancel" button in the task "FragmentTaskFullFill" window
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentTaskFullFill.this)
                        .navigate(R.id.action_fragmentTask_to_FirstFragment);
            }
        });


        //Calendar window

        //This makes the calendar window, that pops up when u wanna set the date
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        //Changes the color of the calendar
        materialDatePicker = MaterialDatePicker.Builder.datePicker().setTheme(R.style.ThemeOverlay_App_MaterialCalendar).build();

        //Method - When you click on the calendar button the calendar pops up
        binding.setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "Tag Picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        editText_selectedDate.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });


        //Timer window
        //Method - When you click on the time button the timer will pops up
        binding.setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY - 1);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10 && hourOfDay >= 10) {
                                    editText_selectedTime.setText(hourOfDay + ":0" + minute);
                                } else if (minute < 10 && hourOfDay < 10) {
                                    editText_selectedTime.setText("0" + hourOfDay + ":0" + minute);
                                } else if (hourOfDay < 10 && minute > 10) {
                                    editText_selectedTime.setText("0" + hourOfDay + ":" + minute);
                                } else {
                                    editText_selectedTime.setText(hourOfDay + ":" + minute);
                                }
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        //Spinner window
        spinner = getActivity().findViewById(R.id.spinnerPriorities);

        //Array of choices in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_selected_item, prioritiesList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Setting the default index of spinner
        int spinnerPosition = adapter.getPosition(prioritiesList[1]);
        spinner.setSelection(spinnerPosition);

        //Method - When you click on the spinner
        binding.spinnerPriorities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    task.setPriority(3);
                } else if (i == 1) {
                    task.setPriority(2);
                } else if (i == 2) {
                    task.setPriority(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}