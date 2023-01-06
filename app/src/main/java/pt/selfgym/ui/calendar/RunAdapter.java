package pt.selfgym.ui.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.EditWorkoutInterface;
import pt.selfgym.R;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;

public class RunAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private WorkoutDTO workout;
    private LayoutInflater layoutInflater;
    private ActivityInterface activityInterface;

    public RunAdapter(WorkoutDTO workout, ActivityInterface activityInterface) {
        this.workout = workout;
        this.activityInterface = activityInterface;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
        notifyDataSetChanged();
    }

    class ViewHolderFixedSetsReps extends RecyclerView.ViewHolder {
        public TextView reps, weight, sets, rest;
        public TextView name;
        public ImageView imageView;


        public ViewHolderFixedSetsReps(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.reps = (TextView) itemView.findViewById(R.id.repsinput);
            this.sets = (TextView) itemView.findViewById(R.id.setsfield);
            this.weight = (TextView) itemView.findViewById(R.id.weightfield);
            this.rest = (TextView) itemView.findViewById(R.id.restfield);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewPicker);
        }
    }

    class ViewHolderFixedSetsTime extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView duration, weight, sets, rest;
        public ImageView imageView;


        public ViewHolderFixedSetsTime(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name2);
            this.duration = (TextView) itemView.findViewById(R.id.durationfield2);
            this.sets = (TextView) itemView.findViewById(R.id.setsfield2);
            this.weight = (TextView) itemView.findViewById(R.id.weightfield2);
            this.rest = (TextView) itemView.findViewById(R.id.restfield2);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewPicker);
        }
    }

    class ViewHolderVariableSetsReps extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView sets;
        public ImageView imageView;


        public ViewHolderVariableSetsReps(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name3);
            this.sets = (RecyclerView) itemView.findViewById(R.id.setList3);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewPicker);
        }
    }

    class ViewHolderVariableSetsTime extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView sets;
        public ImageView imageView;


        public ViewHolderVariableSetsTime(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name4);
            this.sets = (RecyclerView) itemView.findViewById(R.id.setList4);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewPicker);
        }
    }

    class ViewHolderCircuit extends RecyclerView.ViewHolder {
        public TextView laps, rest;
        public RecyclerView exs;
        public ViewHolderCircuit(View itemView) {
            super(itemView);
            this.laps = (TextView) itemView.findViewById(R.id.lapsinput);
            this.rest = (TextView) itemView.findViewById(R.id.restfieldcircuit);
            this.exs = (RecyclerView) itemView.findViewById(R.id.exercisesCircuit);
        }
    }

    class ViewHolderSet extends RecyclerView.ViewHolder {
        public TextView set;
        public TextView weight, variable, rest;

        public ViewHolderSet(View itemView) {
            super(itemView);
            this.set = (TextView) itemView.findViewById(R.id.setSets);
            this.rest = (TextView) itemView.findViewById(R.id.restFieldSets);
            this.variable = (TextView) itemView.findViewById(R.id.variableFieldSets);
            this.weight = (TextView) itemView.findViewById(R.id.weightFieldSets);

        }
    }

    @Override
    public int getItemCount() {
        return workout.getWorkoutComposition() != null ? workout.getWorkoutComposition().size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (workout.getWorkoutComposition().get(position) instanceof ExerciseWODTO) {
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
        } else if (workout.getWorkoutComposition().get(position) instanceof CircuitDTO) {
            return 4;
        }
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        switch (viewType) {
            case 0:
                listItem = layoutInflater.inflate(R.layout.list_item_fixed_sets_reps_exercise_run, parent, false);
                return new ViewHolderFixedSetsReps(listItem);
            case 1:
                listItem = layoutInflater.inflate(R.layout.list_item_fixed_sets_time_exercise_run, parent, false);
                return new ViewHolderFixedSetsTime(listItem);
            case 2:
                listItem = layoutInflater.inflate(R.layout.list_item_variable_sets_reps_exercise_run, parent, false);
                return new ViewHolderVariableSetsReps(listItem);
            case 3:
                listItem = layoutInflater.inflate(R.layout.list_item_variable_sets_time_exercise_run, parent, false);
                return new ViewHolderVariableSetsTime(listItem);
            case 4:
                listItem = layoutInflater.inflate(R.layout.list_item_circuit_run, parent, false);
                return new ViewHolderCircuit(listItem);
        }
        listItem = layoutInflater.inflate(R.layout.list_item_set_run, parent, false);
        return new ViewHolderSet(listItem);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (workout.getWorkoutComposition().get(position) instanceof ExerciseWODTO) {
            ExerciseWODTO exerciseWODTO = (ExerciseWODTO) workout.getWorkoutComposition().get(position);
            if (exerciseWODTO.isFixedSetsReps()) {
                ViewHolderFixedSetsReps viewHolder1 = (ViewHolderFixedSetsReps) holder;
                viewHolder1.rest.setText(exerciseWODTO.getRest() + "");
                viewHolder1.weight.setText(exerciseWODTO.getWeight() + "");
                viewHolder1.reps.setText(exerciseWODTO.getReps() + "");
                viewHolder1.sets.setText(exerciseWODTO.getSets() + "");
                viewHolder1.name.setText(exerciseWODTO.getExercise().getName());
                Glide.with(layoutInflater.getContext())
                        .load(exerciseWODTO.getExercise().getUrlImage())
                        .placeholder(R.drawable.place_holder_foreground)
                        .error(R.drawable.error_foreground)
                        .centerCrop()
                        .into(viewHolder1.imageView);

            } else if (exerciseWODTO.isFixedSetsTime()) {
                ViewHolderFixedSetsTime viewHolder2 = (ViewHolderFixedSetsTime) holder;
                viewHolder2.rest.setText(exerciseWODTO.getRest() + "");
                viewHolder2.weight.setText(exerciseWODTO.getWeight() + "");
                viewHolder2.duration.setText(exerciseWODTO.getDuration() + "");
                viewHolder2.sets.setText(exerciseWODTO.getSets() + "");
                viewHolder2.name.setText(exerciseWODTO.getExercise().getName());
                Glide.with(layoutInflater.getContext())
                        .load(exerciseWODTO.getExercise().getUrlImage())
                        .placeholder(R.drawable.place_holder_foreground)
                        .error(R.drawable.error_foreground)
                        .centerCrop()
                        .into(viewHolder2.imageView);

            } else if (exerciseWODTO.isVariableSetsReps()) {
                ViewHolderVariableSetsReps viewHolder3 = (ViewHolderVariableSetsReps) holder;
                viewHolder3.name.setText(exerciseWODTO.getExercise().getName() + "");

                Glide.with(layoutInflater.getContext())
                        .load(exerciseWODTO.getExercise().getUrlImage())
                        .placeholder(R.drawable.place_holder_foreground)
                        .error(R.drawable.error_foreground)
                        .centerCrop()
                        .into(viewHolder3.imageView);

                WorkoutDTO workoutSets = new WorkoutDTO();
                ArrayList<Object> exList = new ArrayList<Object>();
                for (SetsDTO e : ((ExerciseWODTO) workout.getWorkoutComposition().get(position)).getSetsList()) {
                    exList.add(e);
                }
                workoutSets.setWorkoutComposition(exList);

                RunAdapter adapter = new RunAdapter(workoutSets, activityInterface);
                viewHolder3.sets.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
                viewHolder3.sets.setAdapter(adapter);

            } else if (exerciseWODTO.isVariableSetsTime()) {
                ViewHolderVariableSetsTime viewHolder4 = (ViewHolderVariableSetsTime) holder;
                viewHolder4.name.setText(exerciseWODTO.getExercise().getName() + "");
                Glide.with(layoutInflater.getContext())
                        .load(exerciseWODTO.getExercise().getUrlImage())
                        .placeholder(R.drawable.place_holder_foreground)
                        .error(R.drawable.error_foreground)
                        .centerCrop()
                        .into(viewHolder4.imageView);

                WorkoutDTO workoutSets = new WorkoutDTO("set", "set", "set");
                ArrayList<Object> exList = new ArrayList<Object>();
                for (SetsDTO e : ((ExerciseWODTO) workout.getWorkoutComposition().get(position)).getSetsList()) {
                    exList.add(e);
                }
                workoutSets.setWorkoutComposition(exList);

                RunAdapter adapter = new RunAdapter(workoutSets, activityInterface);
                viewHolder4.sets.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
                viewHolder4.sets.setAdapter(adapter);
            }
        } else if (workout.getWorkoutComposition().get(position) instanceof CircuitDTO) {
            CircuitDTO circuitDTO = (CircuitDTO) workout.getWorkoutComposition().get(position);
            ViewHolderCircuit viewHolder5 = (ViewHolderCircuit) holder;
            try {
            viewHolder5.laps.setText(circuitDTO.getLaps() + "");
            viewHolder5.rest.setText(circuitDTO.getRest() + "");
            }
            catch (Exception e) {
                Log.w("error",e.getMessage());
            };

            WorkoutDTO workoutCircuit = new WorkoutDTO("circuit", "circuit", "circuit");
            ArrayList<Object> exList = new ArrayList<Object>();
            if(((CircuitDTO) workout.getWorkoutComposition().get(position)).getExerciseList() != null)
            {
            for (ExerciseWODTO e : ((CircuitDTO) workout.getWorkoutComposition().get(position)).getExerciseList()) {
                exList.add(e);
            }
            }
            workoutCircuit.setWorkoutComposition(exList);

            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 15;
                }
            };
            viewHolder5.exs.addItemDecoration(itemDecoration);
            RunAdapter adapter = new RunAdapter(workoutCircuit, activityInterface);
            viewHolder5.exs.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
            viewHolder5.exs.setAdapter(adapter);

        } else {
            ViewHolderSet viewHolder6 = (ViewHolderSet) holder;
            viewHolder6.weight.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getWeight() + "");
            viewHolder6.set.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getOrder_set() + "");
            viewHolder6.rest.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getRest() + "");
            viewHolder6.variable.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getVariable() + "");
        }
    }
}