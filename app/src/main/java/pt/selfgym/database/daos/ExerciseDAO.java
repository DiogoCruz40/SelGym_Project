package pt.selfgym.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import pt.selfgym.database.entities.Exercise;
import java.util.List;


@Dao
public interface ExerciseDAO {

    @Query("SELECT * FROM exercises")
    List<Exercise> getAll();

    @Delete
    void delete(Exercise exercise);

    @Insert
    long insert(Exercise exercise);


   /* @Query("SELECT * FROM Point WHERE id_note IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Point WHERE title LIKE :titulo")
    Note findByTitle(String titulo);

    @Query("UPDATE Point SET title = :newTitle WHERE id_note = :id_nota")
    void updateTitle(int id_nota, String newTitle);

    @Query("UPDATE Point SET description = :newNote WHERE id_note = :id_nota")
    void updateNote(int id_nota, String newNote); */
}