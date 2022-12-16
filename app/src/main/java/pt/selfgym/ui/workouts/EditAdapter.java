package pt.selfgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.selfgym.R;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;

public class EditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private WorkoutDTO workout;

    public EditAdapter(WorkoutDTO workout) {
        this.workout = workout;
    }

    class ViewHolderFixedSetsReps extends RecyclerView.ViewHolder {
        public ViewHolderFixedSetsReps(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderFixedSetsTime extends RecyclerView.ViewHolder {
        public ViewHolderFixedSetsTime(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderVariableSetsReps extends RecyclerView.ViewHolder {
        public ViewHolderVariableSetsReps(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderVariableSetsTime extends RecyclerView.ViewHolder {
        public ViewHolderVariableSetsTime(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderCircuit extends RecyclerView.ViewHolder {
        public ViewHolderCircuit(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return workout.getWorkoutComposition() != null ? workout.getWorkoutComposition().size() : 0;
    }

    @Override
    public int getItemViewType(int position){
        if ( workout.getWorkoutComposition().get(position) instanceof ExerciseWODTO) {
            ExerciseWODTO exerciseWODTO = (ExerciseWODTO) workout.getWorkoutComposition().get(position);
            if (exerciseWODTO.isFixedSetsReps()) {
                return 0;
            } else if (exerciseWODTO.isFixedSetsTime()) {
                return 1;
            } else if (exerciseWODTO.isVariableSetsReps()) {
                return 2;
            } else if (exerciseWODTO.isVariableSetsTime()) {
                return 3;
            }
        }
        return 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        switch (viewType) {
            case 0:
                listItem = layoutInflater.inflate(R.layout.list_item_fixed_sets_reps_exercise, parent, false);
                return new ViewHolderFixedSetsReps(listItem);
            case 1:
                listItem = layoutInflater.inflate(R.layout.list_item_fixed_sets_time_exercise, parent, false);
                return new ViewHolderFixedSetsTime(listItem);
            case 2:
                listItem = layoutInflater.inflate(R.layout.list_item_variable_sets_reps_exercise, parent, false);
                return new ViewHolderVariableSetsReps(listItem);
            case 3:
                listItem = layoutInflater.inflate(R.layout.list_item_variable_sets_time_exercise, parent, false);
                return new ViewHolderVariableSetsTime(listItem);
        }
        listItem = layoutInflater.inflate(R.layout.list_item_circuit, parent, false);
        return new ViewHolderCircuit(listItem);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (workout.getWorkoutComposition().get(position).getClass().isInstance(ExerciseWODTO.class)) {
            ExerciseWODTO exerciseWODTO = (ExerciseWODTO) workout.getWorkoutComposition().get(position);
            if (exerciseWODTO.isFixedSetsReps()){
                //ViewHolderFixedSetsReps viewHolder1 = (ViewHolderFixedSetsReps) holder;
                //TODO:atribuir as variáveis ao layout

            } else if (exerciseWODTO.isFixedSetsReps()){
                //ViewHolderFixedSetsTime viewHolder2 = (ViewHolderFixedSetsTime) holder;
            } else if (exerciseWODTO.isVariableSetsReps()){
                //ViewHolderVariableSetsReps viewHolder3 = (ViewHolderVariableSetsReps) holder;
            } else if (exerciseWODTO.isVariableSetsTime()){
                //ViewHolderVariableSetsTime viewHolder4 = (ViewHolderVariableSetsTime) holder;
            }
        } else {
            //ViewHolderCircuit viewHolder5 = (ViewHolderCircuit) holder;
        }
    }
}

