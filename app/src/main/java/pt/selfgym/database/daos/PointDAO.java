package pt.selfgym.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import pt.selfgym.database.entities.Point;

import java.util.List;


@Dao
public interface PointDAO {

    @Query("SELECT * FROM points")
    List<Point> getAll();

    @Delete
    void delete(Point point);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Point point);

   /* @Query("SELECT * FROM Point WHERE id_note IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Point WHERE title LIKE :titulo")
    Note findByTitle(String titulo);

    @Query("UPDATE Point SET title = :newTitle WHERE id_note = :id_nota")
    void updateTitle(int id_nota, String newTitle);

    @Query("UPDATE Point SET description = :newNote WHERE id_note = :id_nota")
    void updateNote(int id_nota, String newNote);

    @Insert
    void insertNote(Note note);

    @Insert
    void insertAll(List<Note> notes);



    @Query("DELETE FROM Point WHERE id_note = :id_nota")
    void delete(int id_nota);*/

}