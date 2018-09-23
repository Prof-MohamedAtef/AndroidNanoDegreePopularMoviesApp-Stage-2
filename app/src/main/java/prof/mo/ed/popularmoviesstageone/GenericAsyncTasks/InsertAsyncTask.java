package prof.mo.ed.popularmoviesstageone.GenericAsyncTasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 9/22/2018.
 */

public class InsertAsyncTask extends AsyncTask<Void,Void,Boolean> {

    private MoviesRoomEntity moviesRoomEntity;
    private AppDatabase appDatabase;



    public InsertAsyncTask(AppDatabase appDatabase, MoviesRoomEntity moviesRoomEntity, OnTaskCompletes onTaskCompletes) {
        super();
        this.appDatabase=appDatabase;
        this.moviesRoomEntity = moviesRoomEntity;
        this.onTaskCompletes=onTaskCompletes;
    }

    public InsertAsyncTask(OnTaskCompletes onTaskCompletes) {
        this.onTaskCompletes=onTaskCompletes;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return IsInserted();
    }

    @NonNull
    private Boolean IsInserted() {
        long x= appDatabase.movieDao().InsertMovie(moviesRoomEntity);
        if (x>0){
            return true;
        }else{
            return false;
        }
    }

    private OnTaskCompletes onTaskCompletes;

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean!= null) {
            onTaskCompletes.onInsertTaskCompletes(aBoolean);
        }
    }

    public interface OnTaskCompletes
    {
        void onInsertTaskCompletes(boolean x);
    }
}
