package prof.mo.ed.popularmoviesstageone;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 8/14/2018.
 */

public class CustomAsyncTask extends AsyncTask <String, Void, ArrayList<MovieEntity>> {

    public String main_List = "results";


    public String POSTER_PATH = "poster_path";
    public String VIDEO_ID = "id";
    public String TITLE = "title";
    public String OVERVIEW = "overview";
    public String RELEASE_DATE = "release_date";
    public String POPULARITY = "popularity";
    public String VOTE_AVERAGE_ = "vote_average";

    public String POSTER_PATH_STRING;
    public String VIDEO_ID_STRING;
    public String TITLE_STRING;
    public String OVERVIEW_STRING;
    public String RELEASE_DATE_STRING;
    public String POPULARITY_STRING;
    public String VOTE_AVERAGE_STRING;

    public JSONObject MoviesJson;
    public JSONArray moviesDataArray;
    public JSONObject oneMovieData;

    ArrayList<MovieEntity> list = new ArrayList<MovieEntity>();

    private final String LOG_TAG = CustomAsyncTask.class.getSimpleName();

    private ArrayList<MovieEntity> getMovieDataFromJson(String MoviesJsonStr)
            throws JSONException {

        MoviesJson = new JSONObject(MoviesJsonStr);
        moviesDataArray = MoviesJson.getJSONArray(main_List);

        list.clear();
        for (int i = 0; i < moviesDataArray.length(); i++) {

            // Get the JSON object representing a movie per each loop
            oneMovieData = moviesDataArray.getJSONObject(i);

            POSTER_PATH_STRING = oneMovieData.getString(POSTER_PATH);
            VIDEO_ID_STRING = oneMovieData.getString(VIDEO_ID);
            TITLE_STRING = oneMovieData.getString(TITLE);
            OVERVIEW_STRING = oneMovieData.getString(OVERVIEW);
            RELEASE_DATE_STRING = oneMovieData.getString(RELEASE_DATE);
            POPULARITY_STRING = oneMovieData.getString(POPULARITY);
            VOTE_AVERAGE_STRING = oneMovieData.getString(VOTE_AVERAGE_);

            MovieEntity entity = new MovieEntity(POSTER_PATH_STRING, VIDEO_ID_STRING, TITLE_STRING, OVERVIEW_STRING, RELEASE_DATE_STRING, POPULARITY_STRING, VOTE_AVERAGE_STRING);
            list.add(entity);
        }

        return list;
    }
    String s="";
    private OnTaskCompleted onTaskCompleted;

    CustomAsyncTask(OnTaskCompleted onTaskCompleted){
        this.onTaskCompleted=onTaskCompleted;
    }

    @Override
    protected ArrayList<MovieEntity> doInBackground(String... params) {

        String Movies_images_JsonSTR = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        if (params.length == 0) {
            return null;
        }

        try {

            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Movies_images_JsonSTR = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            Movies_images_JsonSTR = buffer.toString();

            Log.v(LOG_TAG, "Movies JSON String: " + Movies_images_JsonSTR);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error here Exactly ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMovieDataFromJson(Movies_images_JsonSTR);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "didn't got Movies Data from getJsonData method", e);

            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            onTaskCompleted.onTaskCompleted(result);
        }
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(ArrayList<MovieEntity> result);
    }
}
