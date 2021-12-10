package ru.iliavolkov.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.iliavolkov.notes.R;
import ru.iliavolkov.notes.data.NotesData;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private ArrayList<NotesData> notes;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public NotesAdapter(ArrayList<NotesData> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.getTitle().setText(notes.get(position).getTitle());
        holder.getDescription().setText(notes.get(position).getDescription());
        holder.getData().setText(notes.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private TextView data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleList);
            description = itemView.findViewById(R.id.descriptionList);
            data = itemView.findViewById(R.id.titleDataList);
            itemView.setOnClickListener(v->{
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(v->{
                if (itemLongClickListener != null) {
                    itemLongClickListener.onItemLongClick(v,getAdapterPosition());
                    return true;
                } else
                    return false;
            });
        }

        public TextView getTitle() {return title;}
        public TextView getDescription() {return description;}
        public TextView getData() {return  data;}
    }
}
