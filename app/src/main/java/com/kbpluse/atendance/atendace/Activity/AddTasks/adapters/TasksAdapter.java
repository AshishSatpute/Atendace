package com.kbpluse.atendance.atendace.Activity.AddTasks.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kbpluse.atendance.atendace.Activity.AddTasks.dataModel.AddTasksDataModel;
import com.kbpluse.atendance.atendace.Activity.AddTasks.listeners.OnNoteClickListener;
import com.kbpluse.atendance.atendace.R;

import java.util.ArrayList;



public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private final OnNoteClickListener onNoteClickListener;
    public ArrayList<AddTasksDataModel> addTasksDataModelArrayList;
    private String TAG = TasksAdapter.class.getSimpleName();

    public TasksAdapter(OnNoteClickListener onNoteClickListener, ArrayList<AddTasksDataModel> messageDataModelArrayList) {
        this.onNoteClickListener = onNoteClickListener;
        this.addTasksDataModelArrayList = messageDataModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_for_note, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AddTasksDataModel addTasksDataModel = addTasksDataModelArrayList.get(i);
        viewHolder.cardViewNote.setCardBackgroundColor(ColorUtil.generateRandomColor());
        viewHolder.tvTitle.setText(addTasksDataModel.getTasksName());
        viewHolder.tvMessage.setText(addTasksDataModel.getTasksDecription());
    }

    @Override
    public int getItemCount() {
        return addTasksDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewNote;
        TextView tvTitle;
        TextView tvMessage;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvMessage = view.findViewById(R.id.tvMessage);
            cardViewNote = view.findViewById(R.id.cardViewNote);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "onClick: ");
                    onNoteClickListener.onNoteClick(getLayoutPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onNoteClickListener.onNoteLongClick(getLayoutPosition());
                    return true;
                }
            });
        }
    }

    public void clear() {
        this.addTasksDataModelArrayList.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<AddTasksDataModel> messageDataModelArrayList) {
        this.addTasksDataModelArrayList = messageDataModelArrayList;
        notifyDataSetChanged();
    }
}
