package prof.mo.ed.popularmoviesstageone;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 8/7/2018.
 */
public class MainFragment extends Fragment {

    public MainFragment() {

    }

    public interface MovieDataListener {
        void onMovieFragmentSelected(MovieEntity movieEntity);
    }

    public ImagesAdapter mAdapter;

    ArrayList<MovieEntity> list = new ArrayList<MovieEntity>();


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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", Integer.toString(Util.type));
        outState.putString("position", Integer.toString(Util.pos));
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mAdapter = null;
            Util.type = Integer.parseInt(savedInstanceState.getString("type"));
        }
    }

    String order;
    // constant variables for any image
    final String IMAGES_BASE_Url = "http://image.tmdb.org";
    final String IMAGE_SIZE = "/t/p/w185/";

    final String Image = IMAGES_BASE_Url + IMAGE_SIZE;
    // EACH INDIVIDUAL IMAGE LINK
    final String Batman_Image = "/vsjBeMPZtyB7yNsYY56XYxifaQZ.jpg";
    final String IMAGE_combine = IMAGES_BASE_Url + IMAGE_SIZE + Batman_Image;
    final String now = "http://api.themoviedb.org/3/movie/popular?api_key=868bebbf76a3831f11b3985c703cf959";
    final String Start = "/3/movie/popular?api_key=868bebbf76a3831f11b3985c703cf959";
    // Json link containing Poster_path, Overview, Videos Id, Release_Date, popularity, Titles,
    //BASE URL
    final String JSON_BASE_URL_START = "http://api.themoviedb.org";
    //START URL
    final String DIR_MOIVE_START = "/3/movie/popular?";
    final String API_KEY_START = "api_key=868bebbf76a3831f11b3985c703cf959";
    final String VIDEO_COMBINE_START = DIR_MOIVE_START + API_KEY_START;
    //SORTING URLS

    final String DIR_MOVIEW_TYPE = "/3/discover/movie?";
    final String SORT_BY = "sort_by=";
    final String POPULARITY_DESC = "popularity.desc";
    final String VOTE_AVERAGE = "vote_average.desc";
    final String API_KEY = "&api_key=868bebbf76a3831f11b3985c703cf959";
    //
    final String VIDEO_COMBINE_POPULARITY = DIR_MOVIEW_TYPE + SORT_BY + POPULARITY_DESC + API_KEY;

    final String VIDEO_COMBINE_VOTE_AVERAGE = DIR_MOVIEW_TYPE + SORT_BY + VOTE_AVERAGE + API_KEY;

    public JSONObject MoviesJson;
    public JSONArray moviesDataArray;
    public JSONObject oneMovieData;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // resource for my overflow icon menu
        // http://stackoverflow.com/questions/21544501/overflow-icon-in-action-bar-invisible
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.smoothScrollToPosition(Util.pos == 0 ? 0 : Util.pos + 4);
        if (savedInstanceState != null) {
            mAdapter = null;
            //savedInstanceState.getSerializable("myList");
            Util.type = Integer.parseInt(savedInstanceState.getString("type"));
            Util.pos = Integer.parseInt(savedInstanceState.getString("position"));
        }

        switch (Util.type) {
            case 0:
                getActivity().setTitle("Popular Movies");
                order = "popular";
                Fetch_Movies_data FetchRating = new Fetch_Movies_data();
                FetchRating.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key=868bebbf76a3831f11b3985c703cf959");
                break;
            case 1:
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                Fetch_Movies_data FetchRating2 = new Fetch_Movies_data();
                FetchRating2.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key=868bebbf76a3831f11b3985c703cf959");
                break;
        }
        Log.v("MainFragment", Integer.toString(Util.pos));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                getActivity().setTitle("Popular Movies");
                order = "popular";
                Util.type = 0;
                Util.pos = 0;
                Fetch_Movies_data FetchRating = new Fetch_Movies_data();
                FetchRating.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key=868bebbf76a3831f11b3985c703cf959");
                break;
            case R.id.highest_rated:
                Util.pos = 0;
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                Fetch_Movies_data FetchRating2 = new Fetch_Movies_data();
                FetchRating2.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key=868bebbf76a3831f11b3985c703cf959");
                Util.type = 1;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        Util.pos = layoutManager.findFirstVisibleItemPosition();
        Log.v("MainFragment", Integer.toString(Util.pos));
    }

    public class Fetch_Movies_data extends AsyncTask<String, Void, ArrayList<MovieEntity>> {

        private final String LOG_TAG = Fetch_Movies_data.class.getSimpleName();

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

                mAdapter = null;
                MovieEntity entity = new MovieEntity(POSTER_PATH_STRING, VIDEO_ID_STRING, TITLE_STRING, OVERVIEW_STRING, RELEASE_DATE_STRING, POPULARITY_STRING, VOTE_AVERAGE_STRING);
                list.add(entity);
            }

            return list;
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
        protected void onPostExecute(ArrayList<MovieEntity> result) {
            if (result != null) {
                mAdapter = null;
                list = result;
                mAdapter = new ImagesAdapter(getActivity(), result);
                recyclerView.setAdapter(mAdapter);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                Log.v("MainFragment", Integer.toString(Util.pos));
                layoutManager.scrollToPosition(Util.pos);
            }
        }
    }
}