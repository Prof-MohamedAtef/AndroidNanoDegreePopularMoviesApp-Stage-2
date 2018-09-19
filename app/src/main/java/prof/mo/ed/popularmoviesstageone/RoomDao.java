package prof.mo.ed.popularmoviesstageone;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 8/26/2018.
 */

@Dao
public interface RoomDao {

    @Insert
    void InsertMovie(RoomHelper movieEntity);

    @Query("SELECT * From Favourites where IS_Favourite = 1")
    List<RoomHelper> getAllMoviesData();

    @Query("select * from Favourites WHERE MovieID = :MovieID")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<RoomHelper> getMovieID(String MovieID);

    @Query("select * from Favourites WHERE MovieID = :MovieID")
    int getIsFavoriteMovieID(String MovieID);

    @Query("UPDATE Favourites SET IS_Favourite = :IS_Favourite WHERE MovieID LIKE :MovieID")
    void UpdateMovie(int IS_Favourite, String MovieID);
}