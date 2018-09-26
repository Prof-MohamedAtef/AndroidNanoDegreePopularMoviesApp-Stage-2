package prof.mo.ed.popularmoviesstageone.DataPersist.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import java.util.List;

import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 8/26/2018.
 */

@Dao
public interface RoomMoviesDao {

    @Insert
    long InsertMovie(MoviesRoomEntity movieEntity);

    @Query("SELECT * From Favourites where IS_Favourite = 1")
    LiveData<List<MoviesRoomEntity>> getAllMoviesData();

    @Query("select * from Favourites WHERE MovieID = :MovieID")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<MoviesRoomEntity>> getMovieID(String MovieID);

    @Query("select * from Favourites WHERE MovieID LIKE :MovieID")
    LiveData<List<MoviesRoomEntity>> getIsFavoriteMovieID(String MovieID);

    @Query("UPDATE Favourites SET IS_Favourite = :IS_Favourite WHERE MovieID LIKE :MovieID")
    int UpdateMovie(int IS_Favourite, String MovieID);

    @Query("DELETE FROM Favourites WHERE MovieID LIKE :MovieID")
    abstract int deleteByMovieId(String MovieID);
}