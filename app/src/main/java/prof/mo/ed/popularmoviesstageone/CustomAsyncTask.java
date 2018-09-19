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

public class CustomAsyncTask extends AsyncTask <String, Void, ArrayList<RoomHelper>> {

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

    ArrayList<RoomHelper> list = new ArrayList<RoomHelper>();

    private final String LOG_TAG = CustomAsyncTask.class.getSimpleName();

    private ArrayList<RoomHelper> getMovieDataFromJson(String MoviesJsonStr)
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

            RoomHelper entity = new RoomHelper(POSTER_PATH_STRING, VIDEO_ID_STRING, TITLE_STRING, OVERVIEW_STRING, RELEASE_DATE_STRING, POPULARITY_STRING, VOTE_AVERAGE_STRING);
            list.add(entity);
        }

        return list;
    }

    public JSONObject moviesReviews;
    public JSONArray moviesReviewsArray;
    public JSONObject oneReviewsData;

    private ArrayList<RoomHelper> getReviewsDataFromJson(String MoviesJsonStr)
            throws JSONException {


        moviesReviews = new JSONObject(MoviesJsonStr);
        moviesReviewsArray = moviesReviews.getJSONArray(main_List);
        list.clear();
        Log.v("jsonArraySize", Integer.toString(moviesReviewsArray.length()));
        for (int i = 0; i < moviesReviewsArray.length(); i++) {
            String AUTHOR_STRING, CONTENT_STRING;
            String AUTHOR = "author", CONTENT = "content";
            // Get the JSON object representing a movie per each loop
            oneReviewsData = moviesReviewsArray.getJSONObject(i);
            AUTHOR_STRING = oneReviewsData.getString(AUTHOR);
            Log.v("reviewData", AUTHOR_STRING);
            CONTENT_STRING = oneReviewsData.getString(CONTENT);
            Log.v("reviewData", CONTENT_STRING);
            RoomHelper entity = new RoomHelper(AUTHOR_STRING, CONTENT_STRING);
            list.add(entity);
        }
        if (list.size()>0){
            Log.v("auther_name", list.get(0).AUTHOR_STRING);
            Log.v("ArraySize", Integer.toString(list.size()));
        }
        return list;
    }

    public JSONObject moviesTrailers;
    public JSONArray moviesTrailersArray;
    public JSONObject oneTrailerData;

    private ArrayList<RoomHelper> getTrailersDataFromJson(String TrailersJsonStr)
            throws JSONException {

        moviesTrailers = new JSONObject(TrailersJsonStr);
        moviesTrailersArray = moviesTrailers.getJSONArray(main_List);

        list.clear();
        for (int i = 0; i < moviesTrailersArray.length(); i++) {

            // Get the JSON object representing a Trailer per each loop

            String TRAILER_ID_STRING, TRAILER_KEY_STRING, TRAILER_NAME_STRING, TRAILER_SITE_STRING, TRAILER_SIZE_STRING;
            String TRAILER_ID = "id", TRAILER_KEY = "key", TRAILER_NAME = "name", TRAILER_SITE = "site", TRAILER_SIZE = "size";

            oneTrailerData = moviesTrailersArray.getJSONObject(i);

            TRAILER_ID_STRING = oneTrailerData.getString(TRAILER_ID);
            TRAILER_KEY_STRING = oneTrailerData.getString(TRAILER_KEY);
            TRAILER_NAME_STRING = oneTrailerData.getString(TRAILER_NAME);
            TRAILER_SITE_STRING = oneTrailerData.getString(TRAILER_SITE);
            TRAILER_SIZE_STRING = oneTrailerData.getString(TRAILER_SIZE);

            RoomHelper entity = new RoomHelper(TRAILER_ID_STRING, TRAILER_KEY_STRING, TRAILER_NAME_STRING, TRAILER_SITE_STRING, TRAILER_SIZE_STRING);
            list.add(entity);
        }

        return list;
    }

    String s="";
    String Type=null;
    private OnTaskCompleted onTaskCompleted;

    CustomAsyncTask(OnTaskCompleted onTaskCompleted){
        this.onTaskCompleted=onTaskCompleted;
    }

    CustomAsyncTask(String str, OnTaskCompleted onTaskCompleted){
        this.onTaskCompleted=onTaskCompleted;
        this.Type=str;
    }

    @Override
    protected ArrayList<RoomHelper> doInBackground(String... params) {

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
            if (Type!=null){
                   if (Type.equals("review")){
                       return getReviewsDataFromJson(Movies_images_JsonSTR);
                   }else if (Type.equals("trailer")){
                       return getTrailersDataFromJson(Movies_images_JsonSTR);
                   }
            }else if (Type==null) {
                return getMovieDataFromJson(Movies_images_JsonSTR);
            }
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
    protected void onPostExecute(ArrayList<RoomHelper> result) {
        super.onPostExecute(result);
        if (result != null) {
            onTaskCompleted.onTaskCompleted(Type,result);
        }
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(String Type,ArrayList<RoomHelper> result);
    }
}
