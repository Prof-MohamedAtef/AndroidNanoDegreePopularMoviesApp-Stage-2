package prof.mo.ed.popularmoviesstageone.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import prof.mo.ed.popularmoviesstageone.BasicApp;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;
import prof.mo.ed.popularmoviesstageone.Util;

/**
 * Created by Prof-Mohamed Atef on 9/20/2018.
 * Reference : github repo from Android Architecture Components
 */

public class MoviesViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<MoviesRoomEntity>> mObservableMovies;
    private final MediatorLiveData<List<MoviesRoomEntity>> mObservableMovieExist;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        Util.application=application;
        mObservableMovies=new MediatorLiveData<>();

        mObservableMovieExist=new MediatorLiveData<>();

        mObservableMovies.setValue(null);

        mObservableMovieExist.setValue(null);

        LiveData<List<MoviesRoomEntity>> MoviesList= ((BasicApp)application).getRepository().LoadAllMovies();
        mObservableMovies.addSource(MoviesList,mObservableMovies::setValue);
    }

    String MovieID;

    public void setMovieID(String MovieID){
        this.MovieID=MovieID;
        LiveData<List<MoviesRoomEntity>> MovieIDExist=((BasicApp)Util.application).getRepository().isMoveExist(MovieID);
        mObservableMovieExist.addSource(MovieIDExist,mObservableMovieExist::setValue);

    }
    public String getMovieID(){
        return MovieID;
    }

    public LiveData<List<MoviesRoomEntity>> getFavoriteMovies(){
        return mObservableMovies;
    }

    public LiveData<List<MoviesRoomEntity>> getIsMovieFavorite(){

        return mObservableMovieExist;}

}
