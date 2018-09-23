package prof.mo.ed.popularmoviesstageone.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;

/**
 * Created by Prof-Mohamed Atef on 9/21/2018.
 * Reference : Udacity Course
 */

public class FavoriteMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDatabase;
    private final String MovieID;

    public FavoriteMovieViewModelFactory(AppDatabase mDatabase, String movieID) {
        this.mDatabase = mDatabase;
        MovieID = movieID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CheckMovieIDStatusViewModel( mDatabase, MovieID);
    }
}
