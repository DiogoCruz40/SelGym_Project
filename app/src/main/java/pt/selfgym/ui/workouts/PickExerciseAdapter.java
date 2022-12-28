package pt.selfgym.ui.workouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.dtos.ExerciseDTO;

public class PickExerciseAdapter extends RecyclerView.Adapter<PickExerciseAdapter.ExerciseViewHolder> {
    private List<ExerciseDTO> exerciseList;
    private ButtonsInterface buttonsInterface;
    private LayoutInflater layoutInflater;

    // RecyclerView recyclerView;
    public PickExerciseAdapter(List<ExerciseDTO> exerciseList, ButtonsInterface buttonsInterface) {
        this.exerciseList = exerciseList;
        this.buttonsInterface = buttonsInterface;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_grid_exercise, parent, false);
        return new ExerciseViewHolder(listItem, buttonsInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        final ExerciseDTO exercise = exerciseList.get(position);
        holder.textView.setText(exercise.getName());
        String imageUrl = exercise.getUrlImage();

        Glide.with(layoutInflater.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.place_holder_foreground)
                .error(R.drawable.error_foreground)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return exerciseList != null ? exerciseList.size() : 0;
    }

    public void setExerciseList(List<ExerciseDTO> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public ImageView imageView;
        ButtonsInterface buttonsInterface;

        public ExerciseViewHolder(View itemView, ButtonsInterface buttonsInterface) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textViewPicker);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageViewPicker);
            this.buttonsInterface = buttonsInterface;

            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View view) {
            buttonsInterface.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public List<ExerciseDTO> getExerciseList() {
        return exerciseList;
    }
}
