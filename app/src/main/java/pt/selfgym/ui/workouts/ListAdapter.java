package pt.selfgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.dtos.WorkoutDTO;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<WorkoutDTO> workouts;
    private List<WorkoutDTO> filteredWorkouts;
    private ButtonsInterface buttonsInterface;

    // RecyclerView recyclerView;
    public ListAdapter(List<WorkoutDTO> workouts, ButtonsInterface buttonsInterface) {
        this.workouts = workouts;
        this.filteredWorkouts = workouts;
        this.buttonsInterface = buttonsInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem, buttonsInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WorkoutDTO workouts = filteredWorkouts.get(position);
        holder.textView.setText(workouts.getName());
        holder.textView2.setText('\u27A4' +" " + workouts.getType());

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
        ButtonsInterface buttonsInterface;

        public ViewHolder(View itemView, ButtonsInterface buttonsInterface) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.name);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
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


