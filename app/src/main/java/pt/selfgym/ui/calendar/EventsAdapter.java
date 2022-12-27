package pt.selfgym.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.EditAdapter;
import pt.selfgym.ui.workouts.ListAdapter;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolderEvent> {
    private ArrayList<EventDTO> events;
    private ButtonsInterface buttonsInterface;

    // RecyclerView recyclerView;
    public EventsAdapter(ArrayList<EventDTO> eventDTOList, ButtonsInterface buttonsInterface) {
        this.events = events;
        this.buttonsInterface = buttonsInterface;
    }

    @Override
    public ViewHolderEvent onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_event, parent, false);
        return new ViewHolderEvent(listItem, buttonsInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvent holder, int position) {
        holder.textView.setText(events.get(position).getWorkoutName());
        holder.textView2.setText(events.get(position).getWorkoutType());
        holder.startTime.setText(events.get(position).getHour() + ":" + events.get(position).getMinute());
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void setEvents(ArrayList<EventDTO> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public ArrayList<EventDTO> getEvents() {
        return events;
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView,textView2, startTime;
        public RelativeLayout relativeLayout;
        ButtonsInterface buttonsInterface;

        public ViewHolderEvent(View itemView, ButtonsInterface buttonsInterface) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            this.startTime = (TextView) itemView.findViewById(R.id.startTime);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.buttonsInterface = buttonsInterface;

            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::onLongClick);
        }


        @Override
        public void onClick(View view) {
            buttonsInterface.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            buttonsInterface.onLongItemClick(getAdapterPosition(), view);
            return true;
        }
    }
}


