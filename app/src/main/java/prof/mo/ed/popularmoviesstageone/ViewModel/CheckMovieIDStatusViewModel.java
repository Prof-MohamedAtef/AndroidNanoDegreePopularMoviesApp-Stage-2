package prof.mo.ed.popularmoviesstageone.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 9/21/2018.
 * Reference : Udacity Course
 */

public class CheckMovieIDStatusViewModel extends ViewModel{

    private LiveData<List<MoviesRoomEntity>> Movie;

    public LiveData<List<MoviesRoomEntity>> getMovie(){
        return Movie;
    }

    public CheckMovieIDStatusViewModel(AppDatabase mDatabase, String movieID) {
        Movie=mDatabase.movieDao().getIsFavoriteMovieID(movieID);
    }
}
