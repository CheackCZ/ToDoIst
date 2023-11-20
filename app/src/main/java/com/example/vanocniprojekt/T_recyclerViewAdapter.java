package com.example.vanocniprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

//Class - adapter for the recyclerView
public class T_recyclerViewAdapter extends RecyclerView.Adapter<T_recyclerViewAdapter.MyViewHolder> {


    //Interface for the methods - onClickListener and onLongClickListener
    private final rvInterface rvInterface;

    //Declaration of the variables
    Context context;
    ArrayList<Task> list;

    private OnItemClickListener listener;


    //Constructor for the RecyclerViewAdapter
    public T_recyclerViewAdapter(Context context, ArrayList<Task> list, rvInterface rvInterface) {
        this.context = context;
        this.list = list;
        this.rvInterface = rvInterface;
    }


    @NonNull
    @Override
    public T_recyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new T_recyclerViewAdapter.MyViewHolder(view, listener, rvInterface);
    }


    //Adapter method - that sets the recyclerView values
    @Override
    public void onBindViewHolder(@NonNull T_recyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tv_Title.setText(list.get(position).getTitle());
        holder.tv_Description.setText(list.get(position).getDescription());
        holder.tv_Time.setText(list.get(position).getTime());
        holder.tv_Date.setText(list.get(position).getDate());

        holder.checkBox.setChecked(list.get(position).isCompleted());
        holder.ratingBar.setRating(list.get(position).getPriority());
    }


    //This is RecyclerView Adapter method, that returns the number of the objects in the recyclerView
    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Title, tv_Description, tv_Date, tv_Time;
        RatingBar ratingBar;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener, rvInterface rvInterface) {
            super(itemView);

            tv_Title = itemView.findViewById(R.id.recyclerView_title);
            tv_Description = itemView.findViewById(R.id.recyclerView_description);
            tv_Date = itemView.findViewById(R.id.recyclerView_date);
            tv_Time = itemView.findViewById(R.id.recyclerView_time);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            checkBox = itemView.findViewById(R.id.checkBox);


            //Method - Do something when you click on the task in the arraylist
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            rvInterface.onItemClick(pos);
                        }
                    }
                }
            });


            //Method - Do something when you hold the task in the arraylist
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (rvInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            rvInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });


            //Method for deleting the task when checkbox is clicked (returning the position of the task in the recyclerView)
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    //Implementation for deleting the task when checkbox is clicked
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

}
