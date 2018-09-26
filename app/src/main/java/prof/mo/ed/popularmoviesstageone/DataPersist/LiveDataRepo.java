package prof.mo.ed.popularmoviesstageone.DataPersist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 9/20/2018.
 * Reference : github repo from Android Architecture Components
 */

public class LiveDataRepo {

    private static LiveDataRepo liveDataRepoInstance;
    private final AppDatabase mDatabase;

    private MediatorLiveData<List<MoviesRoomEntity>> mObservableMovies;

    public LiveDataRepo(final AppDatabase database) {

        mDatabase = database;
        mObservableMovies=new MediatorLiveData<>();

        mObservableMovies.addSource(mDatabase.movieDao().getAllMoviesData(),
                MoviesEntities -> {
            if (mDatabase.getDatabaseCreated().getValue()!=null){
                mObservableMovies.postValue(MoviesEntities);
            }
        });
    }

    public static LiveDataRepo getLiveDataRepoInstance(final AppDatabase database){
        if(liveDataRepoInstance==null){
            synchronized (LiveDataRepo.class){
                if (liveDataRepoInstance==null){
                    liveDataRepoInstance=new LiveDataRepo(database);
                }
            }
        }
        return liveDataRepoInstance;
    }

    public LiveData<List<MoviesRoomEntity>> LoadAllMovies(){
        return mDatabase.movieDao().getAllMoviesData();
    }

    public LiveData<List<MoviesRoomEntity>> isMoveExist(String movieID){
        LiveData<List<MoviesRoomEntity>> xList=mDatabase.movieDao().getAllMoviesData();
        return xList;
    }
}