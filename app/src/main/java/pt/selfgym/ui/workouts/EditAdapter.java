package pt.selfgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

import pt.selfgym.R;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;

public class EditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private WorkoutDTO workout;
    private LayoutInflater layoutInflater;
    private ArrayList<RecyclerView.ViewHolder> holdersList;

    public EditAdapter(WorkoutDTO workout) {
        this.workout = workout;
        this.holdersList = new ArrayList<RecyclerView.ViewHolder>();
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    class ViewHolderFixedSetsReps extends RecyclerView.ViewHolder {
        public EditText reps, weight, sets, rest;
        public TextView name;

        public ViewHolderFixedSetsReps(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.reps = (EditText) itemView.findViewById(R.id.repsinput);
            this.sets = (EditText) itemView.findViewById(R.id.setsinput);
            this.weight = (EditText) itemView.findViewById(R.id.weightinput);
            this.rest = (EditText) itemView.findViewById(R.id.restinput);
        }
    }

    class ViewHolderFixedSetsTime extends RecyclerView.ViewHolder {
        public TextView name;
        public EditText duration, weight, sets, rest;

        public ViewHolderFixedSetsTime(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name2);
            this.duration = (EditText) itemView.findViewById(R.id.durationinput2);
            this.sets = (EditText) itemView.findViewById(R.id.setsinput2);
            this.weight = (EditText) itemView.findViewById(R.id.weightinput2);
            this.rest = (EditText) itemView.findViewById(R.id.restinput2);
        }
    }

    class ViewHolderVariableSetsReps extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView sets;

        public ViewHolderVariableSetsReps(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name3);
            this.sets = (RecyclerView) itemView.findViewById(R.id.setList3);
        }
    }

    class ViewHolderVariableSetsTime extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView sets;

        public ViewHolderVariableSetsTime(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name4);
            this.sets = (RecyclerView) itemView.findViewById(R.id.setList4);
        }
    }

    class ViewHolderCircuit extends RecyclerView.ViewHolder {
        public EditText laps, rest;
        public RecyclerView exs;

        public ViewHolderCircuit(View itemView) {
            super(itemView);
            this.laps = (EditText) itemView.findViewById(R.id.lapsinput);
            this.rest = (EditText) itemView.findViewById(R.id.restinputcircuit);
            this.exs = (RecyclerView) itemView.findViewById(R.id.exercisesCircuit);
        }
    }

    class ViewHolderSet extends RecyclerView.ViewHolder {
        public TextView set;
        public EditText weight, variable, rest;

        public ViewHolderSet(View itemView) {
            super(itemView);
            this.set = (TextView) itemView.findViewById(R.id.setSets);
            this.rest = (EditText) itemView.findViewById(R.id.restInputSets);
            this.variable = (EditText) itemView.findViewById(R.id.variableInputSets);
            this.weight = (EditText) itemView.findViewById(R.id.weightInputSets);

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
            case 4:
                listItem = layoutInflater.inflate(R.layout.list_item_circuit, parent, false);
                return new ViewHolderCircuit(listItem);
        }
        listItem = layoutInflater.inflate(R.layout.list_item_set, parent, false);
        return new ViewHolderSet(listItem);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (workout.getWorkoutComposition().get(position) instanceof ExerciseWODTO) {
            ExerciseWODTO exerciseWODTO = (ExerciseWODTO) workout.getWorkoutComposition().get(position);
            if (exerciseWODTO.isFixedSetsReps()) {
                ViewHolderFixedSetsReps viewHolder1 = (ViewHolderFixedSetsReps) holder;
                viewHolder1.rest.setText(exerciseWODTO.getRest() + "");
                viewHolder1.weight.setText(exerciseWODTO.getWeight() + "");
                viewHolder1.reps.setText(exerciseWODTO.getReps() + "");
                viewHolder1.sets.setText(exerciseWODTO.getSets() + "");
                viewHolder1.name.setText(exerciseWODTO.getExercise().getName());
                holdersList.add(viewHolder1);

            } else if (exerciseWODTO.isFixedSetsTime()) {
                ViewHolderFixedSetsTime viewHolder2 = (ViewHolderFixedSetsTime) holder;
                viewHolder2.rest.setText(exerciseWODTO.getRest() + "");
                viewHolder2.weight.setText(exerciseWODTO.getWeight() + "");
                viewHolder2.duration.setText(exerciseWODTO.getDuration() + "");
                viewHolder2.sets.setText(exerciseWODTO.getSets() + "");
                viewHolder2.name.setText(exerciseWODTO.getExercise().getName());

                holdersList.add(viewHolder2);
            } else if (exerciseWODTO.isVariableSetsReps()) {
                ViewHolderVariableSetsReps viewHolder3 = (ViewHolderVariableSetsReps) holder;
                viewHolder3.name.setText(exerciseWODTO.getExercise().getName() + "");

                WorkoutDTO workoutSets = new WorkoutDTO(-1L, "set", "set", "set");
                ArrayList<Object> exList = new ArrayList<Object>();
                for (SetsDTO e : ((ExerciseWODTO) workout.getWorkoutComposition().get(position)).getSetsList()) {
                    exList.add(e);
                }
                workoutSets.setWorkoutComposition(exList);

                EditAdapter adapter = new EditAdapter(workoutSets);
                viewHolder3.sets.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(layoutInflater.getContext()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                viewHolder3.sets.setLayoutManager(linearLayoutManager);
                viewHolder3.sets.setAdapter(adapter);
                holdersList.add(viewHolder3);

            } else if (exerciseWODTO.isVariableSetsTime()) {
                ViewHolderVariableSetsTime viewHolder4 = (ViewHolderVariableSetsTime) holder;
                viewHolder4.name.setText(exerciseWODTO.getExercise().getName() + "");

                WorkoutDTO workoutCircuit = new WorkoutDTO(-1L, "set", "set", "set");
                ArrayList<Object> exList = new ArrayList<Object>();
                for (SetsDTO e : ((ExerciseWODTO) workout.getWorkoutComposition().get(position)).getSetsList()) {
                    exList.add(e);
                }
                workoutCircuit.setWorkoutComposition(exList);

                EditAdapter adapter = new EditAdapter(workoutCircuit);
                viewHolder4.sets.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(layoutInflater.getContext()) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                viewHolder4.sets.setLayoutManager(linearLayoutManager);
                viewHolder4.sets.setAdapter(adapter);
                holdersList.add(viewHolder4);
            }
        } else if (workout.getWorkoutComposition().get(position) instanceof CircuitDTO) {
            CircuitDTO circuitDTO = (CircuitDTO) workout.getWorkoutComposition().get(position);
            ViewHolderCircuit viewHolder5 = (ViewHolderCircuit) holder;
            viewHolder5.laps.setText(circuitDTO.getLaps() + "");
            viewHolder5.rest.setText(circuitDTO.getRest() + "");

            WorkoutDTO workoutCircuit = new WorkoutDTO(-1L, "circuit", "circuit", "circuit");
            ArrayList<Object> exList = new ArrayList<Object>();
            for (ExerciseWODTO e : ((CircuitDTO) workout.getWorkoutComposition().get(position)).getExerciseList()) {
                exList.add(e);
            }
            workoutCircuit.setWorkoutComposition(exList);

            EditAdapter adapter = new EditAdapter(workoutCircuit);
            viewHolder5.exs.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(layoutInflater.getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            viewHolder5.exs.setLayoutManager(linearLayoutManager);
            viewHolder5.exs.setAdapter(adapter);
            holdersList.add(viewHolder5);

        } else {
            ViewHolderSet viewHolder6 = (ViewHolderSet) holder;
            viewHolder6.weight.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getWeight() + "");
            viewHolder6.set.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getOrder_set() + "");
            viewHolder6.rest.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getRest() + "");
            viewHolder6.variable.setText(((SetsDTO) workout.getWorkoutComposition().get(position)).getVariable() + "");
            holdersList.add(viewHolder6);
        }
    }

    public WorkoutDTO saveWorkoutChanges() {
        ArrayList<Object> composition = workout.getWorkoutComposition();
        WorkoutDTO newWorkout = new WorkoutDTO(workout.getId(), workout.getName(), workout.getObservation(), workout.getType());
        for (int i = 0; i < holdersList.size(); i++) {
            if (holdersList.get(i) instanceof ViewHolderFixedSetsReps) {
                Long id = ((ExerciseWODTO) composition.get(i)).getId();
                int order = ((ExerciseWODTO) composition.get(i)).getOrder();
                double weight = Double.parseDouble(((ViewHolderFixedSetsReps) holdersList.get(i)).weight.getText().toString());
                int sets = Integer.parseInt(((ViewHolderFixedSetsReps) holdersList.get(i)).sets.getText().toString());
                int reps = Integer.parseInt(((ViewHolderFixedSetsReps) holdersList.get(i)).reps.getText().toString());
                int rest = Integer.parseInt(((ViewHolderFixedSetsReps) holdersList.get(i)).rest.getText().toString());
                ExerciseDTO exercise = ((ExerciseWODTO) composition.get(i)).getExercise();
                ExerciseWODTO ex = new ExerciseWODTO(id, order, weight, sets, reps, rest, exercise);
                newWorkout.addToWorkoutComposition(ex);

            } else if (holdersList.get(i) instanceof ViewHolderFixedSetsTime) {
                Long id = ((ExerciseWODTO) composition.get(i)).getId();
                int order = ((ExerciseWODTO) composition.get(i)).getOrder();
                double weight = Double.parseDouble(((ViewHolderFixedSetsTime) holdersList.get(i)).weight.getText().toString());
                int sets = Integer.parseInt(((ViewHolderFixedSetsTime) holdersList.get(i)).sets.getText().toString());
                int rest = Integer.parseInt(((ViewHolderFixedSetsTime) holdersList.get(i)).rest.getText().toString());
                ExerciseDTO exercise = ((ExerciseWODTO) composition.get(i)).getExercise();
                int duration = Integer.parseInt(((ViewHolderFixedSetsTime) holdersList.get(i)).duration.getText().toString());
                ExerciseWODTO ex = new ExerciseWODTO(id, order, weight, sets, rest, exercise, duration);
                newWorkout.addToWorkoutComposition(ex);

            } else if (holdersList.get(i) instanceof ViewHolderVariableSetsReps) {
                Long id = ((ExerciseWODTO) composition.get(i)).getId();
                int order = ((ExerciseWODTO) composition.get(i)).getOrder();
                int reps = ((ExerciseWODTO) composition.get(i)).getReps();
                ExerciseDTO exercise = ((ExerciseWODTO) composition.get(i)).getExercise();
                EditAdapter setsAdapter = (EditAdapter) ((ViewHolderVariableSetsReps) holdersList.get(i)).sets.getAdapter();
                WorkoutDTO setsWorkout = setsAdapter.saveWorkoutChanges();
                ArrayList <SetsDTO> setsList = new ArrayList<SetsDTO>();
                for(Object o: setsWorkout.getWorkoutComposition()){
                    SetsDTO s = (SetsDTO) o;
                    setsList.add(s);
                }
                ExerciseWODTO ex = new ExerciseWODTO(id, order, reps, exercise, setsList);
                newWorkout.addToWorkoutComposition(ex);

            } else if (holdersList.get(i) instanceof ViewHolderVariableSetsTime) {
                Long id = ((ExerciseWODTO) composition.get(i)).getId();
                int order = ((ExerciseWODTO) composition.get(i)).getOrder();
                int duration = ((ExerciseWODTO) composition.get(i)).getDuration();
                ExerciseDTO exercise = ((ExerciseWODTO) composition.get(i)).getExercise();
                EditAdapter setsAdapter = (EditAdapter) ((ViewHolderVariableSetsTime) holdersList.get(i)).sets.getAdapter();
                WorkoutDTO setsWorkout = setsAdapter.saveWorkoutChanges();
                ArrayList <SetsDTO> setsList = new ArrayList<SetsDTO>();
                for(Object o: setsWorkout.getWorkoutComposition()){
                    SetsDTO s = (SetsDTO) o;
                    setsList.add(s);
                }
                ExerciseWODTO ex = new ExerciseWODTO(id, order, exercise,duration, setsList);
                newWorkout.addToWorkoutComposition(ex);

            } else if (holdersList.get(i) instanceof ViewHolderCircuit) {
                Long id = ((CircuitDTO) composition.get(i)).getId();
                int rest = Integer.parseInt(((ViewHolderCircuit) holdersList.get(i)).rest.getText().toString());
                int laps = Integer.parseInt(((ViewHolderCircuit) holdersList.get(i)).laps.getText().toString());
                EditAdapter exsAdapter = (EditAdapter) ((ViewHolderCircuit) holdersList.get(i)).exs.getAdapter();
                WorkoutDTO exsWorkout = exsAdapter.saveWorkoutChanges();
                ArrayList <ExerciseWODTO> exerciseList = new ArrayList<ExerciseWODTO>();
                for(Object o: exsWorkout.getWorkoutComposition()){
                    ExerciseWODTO e = (ExerciseWODTO) o;
                    exerciseList.add(e);
                }
                CircuitDTO ex = new CircuitDTO(id,laps,rest,exerciseList);
                newWorkout.addToWorkoutComposition(ex);

            } else {
                Long id = ((SetsDTO) composition.get(i)).getId();
                int order = ((SetsDTO) composition.get(i)).getOrder_set();
                double weight = Double.parseDouble(((ViewHolderSet) holdersList.get(i)).weight.getText().toString());
                int rest = Integer.parseInt(((ViewHolderSet) holdersList.get(i)).rest.getText().toString());
                int variable = Integer.parseInt(((ViewHolderSet) holdersList.get(i)).variable.getText().toString());
                SetsDTO set = new SetsDTO(id, variable, rest, weight, order);
                newWorkout.addToWorkoutComposition(set);
            }
        }
        return newWorkout;
    }
}

