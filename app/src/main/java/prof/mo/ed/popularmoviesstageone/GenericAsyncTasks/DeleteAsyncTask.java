package prof.mo.ed.popularmoviesstageone.GenericAsyncTasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 9/23/2018.
 */

public class DeleteAsyncTask  extends AsyncTask<Void,Void,Boolean> {

    private String MovieID;
    private AppDatabase appDatabase;

    public DeleteAsyncTask(AppDatabase appDatabase, String movieID, OnTaskCompletes onTaskCompletes) {
        super();
        this.appDatabase=appDatabase;
        this.MovieID= movieID;
        this.onTaskCompletes=onTaskCompletes;
    }

    public DeleteAsyncTask(OnTaskCompletes onTaskCompletes) {
        this.onTaskCompletes=onTaskCompletes;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return IsDeleted();
    }

    private Boolean IsDeleted() {
        int x=appDatabase.movieDao().deleteByMovieId(MovieID);
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
            onTaskCompletes.onDeleteTaskCompletes(aBoolean);
        }
    }

    public interface OnTaskCompletes
    {
        void onDeleteTaskCompletes(boolean x);
    }
}