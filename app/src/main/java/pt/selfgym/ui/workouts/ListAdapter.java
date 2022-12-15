package pt.selfgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.dtos.WorkoutDTO;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<WorkoutDTO> workouts;
    private List<WorkoutDTO> filteredWorkouts;
    private WorkoutsInterface workoutsInterface;

    // RecyclerView recyclerView;
    public ListAdapter(List<WorkoutDTO> workouts, WorkoutsInterface workoutsInterface) {
        this.workouts = workouts;
        this.filteredWorkouts = workouts;
        this.workoutsInterface = workoutsInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem, workoutsInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WorkoutDTO workouts = filteredWorkouts.get(position);
        holder.textView.setText(workouts.getName());
        holder.textView2.setText("(" + workouts.getType() + ")");
    }

    @Override
    public int getItemCount() {
        return filteredWorkouts != null ? filteredWorkouts.size() : 0;
    }

    public void setWorkouts(List<WorkoutDTO> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public  void setFilteredWorkouts(List<WorkoutDTO> filteredList){
        this.filteredWorkouts = filteredList;
        notifyDataSetChanged();
    }

    public List<WorkoutDTO> getFilteredWorkouts() {
        return filteredWorkouts;
    }
    public List<WorkoutDTO> getWorkouts() {
        return workouts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView,textView2;
        public RelativeLayout relativeLayout;
        WorkoutsInterface workoutsInterface;

        public ViewHolder(View itemView, WorkoutsInterface workoutsInterface) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.workoutsInterface = workoutsInterface;

            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::onLongClick);
        }


        @Override
        public void onClick(View view) {
            workoutsInterface.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            workoutsInterface.onLongItemClick(getAdapterPosition(), view);
            return true;
        }
    }
}


