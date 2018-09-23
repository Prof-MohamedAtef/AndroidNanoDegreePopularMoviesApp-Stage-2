package prof.mo.ed.popularmoviesstageone.GenericAsyncTasks;

import android.os.AsyncTask;
import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;

/**
 * Created by Prof-Mohamed Atef on 9/22/2018.
 */

public class UpdateAsyncTask extends AsyncTask<Void,Void,Boolean> {

    private int IsFavorite;
    private String MovieID;
    private AppDatabase appDatabase;

    public UpdateAsyncTask(AppDatabase appDatabase, int IsFavorite, String MovieID, OnTaskCompletes onTaskCompletes) {
        super();
        this.appDatabase=appDatabase;
        this.IsFavorite=IsFavorite;
        this.MovieID=MovieID;
        this.onTaskCompletes=onTaskCompletes;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return IsUpdated();
    }

    private Boolean IsUpdated() {
        int x= appDatabase.movieDao().UpdateMovie(IsFavorite,MovieID);
        if (x>0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean!= null) {
            onTaskCompletes.onUpdateTaskCompletes(aBoolean);
        }
    }

    private OnTaskCompletes onTaskCompletes;

    public interface OnTaskCompletes
    {
        void onUpdateTaskCompletes(boolean x);
    }
}