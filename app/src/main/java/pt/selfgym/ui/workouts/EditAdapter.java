package pt.selfgym.ui.workouts;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.selfgym.dtos.ExerciseWODTO;

public class EditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> workoutComposition;

    public EditAdapter(ArrayList<Object> workoutComposition) {
        this.workoutComposition = workoutComposition;
    }

    class ViewHolderFixedSetsReps extends RecyclerView.ViewHolder {
        public ViewHolderFixedSetsReps(View itemView) {
            super(null);
        }
    }

    class ViewHolderFixedSetsTime extends RecyclerView.ViewHolder {
        public ViewHolderFixedSetsTime(View itemView) {
            super(null);
        }
    }

    class ViewHolderVariableSetsReps extends RecyclerView.ViewHolder {
        public ViewHolderVariableSetsReps(View itemView) {
            super(null);
        }
    }

    class ViewHolderVariableSetsTime extends RecyclerView.ViewHolder {
        public ViewHolderVariableSetsTime(View itemView) {
            super(null);
        }
    }

    class ViewHolderCircuit extends RecyclerView.ViewHolder {
        public ViewHolderCircuit(View itemView) {
            super(null);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @Override
    public int getItemCount() {
        return workoutComposition != null ? workoutComposition.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolderFixedSetsReps(null);
            case 1:
                return new ViewHolderFixedSetsTime(null);
            case 2:
                return new ViewHolderVariableSetsReps(null);
            case 3:
                return new ViewHolderVariableSetsTime(null);
        }
        return new ViewHolderCircuit(null);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (workoutComposition.get(position).getClass().isInstance(ExerciseWODTO.class)) {
            ExerciseWODTO exerciseWODTO = (ExerciseWODTO) workoutComposition.get(position);
            if (exerciseWODTO.isFixedSetsReps()){
                ViewHolderFixedSetsReps viewHolder = (ViewHolderFixedSetsReps) holder;
            } else if (exerciseWODTO.isFixedSetsReps()){
                ViewHolderFixedSetsTime viewHolder = (ViewHolderFixedSetsTime) holder;
            } else if (exerciseWODTO.isVariableSetsReps()){
                ViewHolderVariableSetsReps viewHolder = (ViewHolderVariableSetsReps) holder;
            } else if (exerciseWODTO.isVariableSetsTime()){
                ViewHolderVariableSetsTime viewHolder = (ViewHolderVariableSetsTime) holder;
            }
        } else {
            ViewHolderCircuit viewHolder = (ViewHolderCircuit) holder;
        }
    }
}

