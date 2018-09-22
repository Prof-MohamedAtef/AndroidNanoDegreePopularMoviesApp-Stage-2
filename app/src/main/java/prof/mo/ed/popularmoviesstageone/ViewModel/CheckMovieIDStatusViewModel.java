package prof.mo.ed.popularmoviesstageone.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;

/**
 * Created by Prof-Mohamed Atef on 9/21/2018.
 */

public class CheckMovieIDStatusViewModel extends ViewModel{

    private LiveData<List<String>> Movie;

    public LiveData<List<String>> getMovie(){
        return Movie;
    }

    public CheckMovieIDStatusViewModel(AppDatabase mDatabase, String movieID) {
        Movie=mDatabase.movieDao().getIsFavoriteMovieID();
    }
}
