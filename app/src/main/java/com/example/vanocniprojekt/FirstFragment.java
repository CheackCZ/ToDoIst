package com.example.vanocniprojekt;

import static android.content.Context.APP_OPS_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vanocniprojekt.databinding.FragmentFirstBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

//Class for the first window that shows up
public class FirstFragment extends Fragment implements rvInterface{

    private FragmentFirstBinding binding;

    //Declaration of the arraylist and the recyclerView and recyclerView Adapter
    private ArrayList<Task> list;
    private RecyclerView recyclerView;
    private T_recyclerViewAdapter recyclerViewAdapter;

    private View view;

    EditText editText_titleBS;
    EditText editText_descriptionBS;
    TextView textView_dateBS;
    TextView textView_timeBS;

    Button buttonEdit;
    Button buttonStartEdit;

    //Declaration of the rating Bar for the bottom sheet menu
    RatingBar ratingBar_BS;

    //Declaration of the Calendar
    MaterialDatePicker materialDatePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        list = new ArrayList<>();

        //RecyclerView
        recyclerView = (RecyclerView)view.findViewById(R.id.mRecycleView);

        SetUp();

        //Putting together recyclerView Adapter and recyclerView
        recyclerViewAdapter = new T_recyclerViewAdapter(getActivity(), list, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Method - when you click on the task in the recyclerView it will remove it
        recyclerViewAdapter.setOnItemClickListener(new T_recyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                list.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(list);
                editor.putString("task list", json);
                editor.commit();


            }
        });
        return binding.getRoot();
    }


    //Method that loads and adds the created tasks to the firstFragment
    private void SetUp() {
        //SharedPreferences - This loads the data when you open the app after you close her
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null) {
            list = new ArrayList<>();
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Sharing list to the MainActivity class
        ((MainActivity)getActivity()).dispatchInformations(list);

        //Method - when you click on the button in the first fragment it will navigate the app to the FragmentTaskFullFillment fragment
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.getBoolean("editable", true);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_fragmentTask, bundle);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Method - When you click on the task in the recyclerView it will show you what it contains
    @SuppressLint("MissingInflatedId")
    @Override
    public void onItemClick(int position) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);

        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));

        editText_titleBS = bottomSheetView.findViewById(R.id.editTextTitle_bottomSheet);
        editText_descriptionBS = bottomSheetView.findViewById(R.id.etDescription_bottomSheet);
        textView_dateBS = bottomSheetView.findViewById(R.id.tvDate_bottomSheet);
        textView_timeBS = bottomSheetView.findViewById(R.id.tvTime_bottomSheet);
        ratingBar_BS = bottomSheetView.findViewById(R.id.ratingBar_bottomSheet);

        editText_titleBS.setText(list.get(position).getTitle());
        editText_descriptionBS.setText(list.get(position).getDescription());
        textView_dateBS.setText(list.get(position).getDate());
        textView_timeBS.setText(list.get(position).getTime());
        ratingBar_BS.setRating(list.get(position).getPriority());

        buttonStartEdit = bottomSheetView.findViewById(R.id.buttonEditTask);
        buttonStartEdit.setVisibility(View.VISIBLE);

        buttonEdit = bottomSheetView.findViewById(R.id.buttonEdit);
        buttonEdit.setVisibility(View.GONE);


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


        //Calendar window
        //This makes the calendar window, that pops up when u wanna set the date
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        //Changes the color of the calendar
        materialDatePicker = MaterialDatePicker.Builder.datePicker().setTheme(R.style.ThemeOverlay_App_MaterialCalendar).build();

        //Method - When you click on the calendar button the calendar pops up
        textView_dateBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "Tag Picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textView_dateBS.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });


        //Timer window
        //Method - When you click on the time button the timer will pops up
        textView_timeBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY + 1);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10 && hourOfDay >= 10) {
                                    textView_timeBS.setText(hourOfDay + ":0" + minute);
                                } else if (minute < 10 && hourOfDay < 10) {
                                    textView_timeBS.setText("0" + hourOfDay + ":0" + minute);
                                } else if (hourOfDay < 10 && minute > 10) {
                                    textView_timeBS.setText("0" + hourOfDay + ":" + minute);
                                } else {
                                    textView_timeBS.setText(hourOfDay + ":" + minute);
                                }
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        buttonStartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonStartEdit.setVisibility(View.GONE);
                buttonEdit.setVisibility(View.VISIBLE);

                editText_titleBS.setEnabled(true);
                editText_descriptionBS.setEnabled(true);
                textView_dateBS.setEnabled(true);
                textView_timeBS.setEnabled(true);
                ratingBar_BS.setIsIndicator(false);

                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonStartEdit.setVisibility(View.VISIBLE);
                        buttonEdit.setVisibility(View.GONE);

                        editText_titleBS.setEnabled(false);
                        editText_descriptionBS.setEnabled(false);
                        textView_dateBS.setEnabled(false);
                        textView_timeBS.setEnabled(false);
                        ratingBar_BS.setIsIndicator(true);

                        String editedTitle = "", editedDescription = "", editedDate = "", editedTime = "";
                        float priority = 0f;

                        //Checks if it is not empty
                        if (!editText_titleBS.getText().toString().equals("")) {
                            editedTitle = editText_titleBS.getText().toString();
                        }
                        if (!editText_descriptionBS.getText().toString().equals("")) {
                            editedDescription = editText_descriptionBS.getText().toString();
                        }
                        if (!textView_dateBS.getText().toString().equals("")) {
                            editedDate = textView_dateBS.getText().toString();
                        }
                        if (!textView_timeBS.getText().toString().equals("")) {
                            editedTime = textView_timeBS.getText().toString();
                        }
                        if (ratingBar_BS.getRating() != 0f) {
                            priority = ratingBar_BS.getRating();
                        }

                        list.set(position, new Task(editedTitle, editedDescription, priority, editedDate, editedTime));
                        recyclerViewAdapter.notifyItemChanged(position);

                    }
                });

            }
        });
    }

    //Method - When you click and hold the item in recyclerView it will delte it
    @Override
    public void onItemLongClick(int postition) {
        list.remove(postition);
        recyclerViewAdapter.notifyItemRemoved(postition);
    }
}