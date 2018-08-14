package prof.mo.ed.popularmoviesstageone;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 8/7/2018.
 */
public class MainFragment extends Fragment implements CustomAsyncTask.OnTaskCompleted{

    public MainFragment() {

    }

    @Override
    public void onTaskCompleted(ArrayList<MovieEntity> result) {
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


    public interface MovieDataListener {
        void onMovieFragmentSelected(MovieEntity movieEntity);
    }

    public ImagesAdapter mAdapter;

    ArrayList<MovieEntity> list = new ArrayList<MovieEntity>();

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

    String apiKey;
    String order;
    // constant variables for any image
    final String IMAGES_BASE_Url = "http://image.tmdb.org";
    final String IMAGE_SIZE = "/t/p/w185/";

    final String Image = IMAGES_BASE_Url + IMAGE_SIZE;
    // EACH INDIVIDUAL IMAGE LINK
    final String Batman_Image = "/vsjBeMPZtyB7yNsYY56XYxifaQZ.jpg";
    final String IMAGE_combine = IMAGES_BASE_Url + IMAGE_SIZE + Batman_Image;
    final String now = "http://api.themoviedb.org/3/movie/popular?api_key="+apiKey;
    final String Start = "/3/movie/popular?api_key="+apiKey;
    // Json link containing Poster_path, Overview, Videos Id, Release_Date, popularity, Titles,
    //BASE URL
    final String JSON_BASE_URL_START = "http://api.themoviedb.org";
    //START URL
    final String DIR_MOIVE_START = "/3/movie/popular?";
    final String API_KEY_START = "api_key="+apiKey;
    final String VIDEO_COMBINE_START = DIR_MOIVE_START + API_KEY_START;
    //SORTING URLS

    final String DIR_MOVIEW_TYPE = "/3/discover/movie?";
    final String SORT_BY = "sort_by=";
    final String POPULARITY_DESC = "popularity.desc";
    final String VOTE_AVERAGE = "vote_average.desc";
    final String API_KEY = "&api_key="+apiKey;
    //
    final String VIDEO_COMBINE_POPULARITY = DIR_MOVIEW_TYPE + SORT_BY + POPULARITY_DESC + API_KEY;

    final String VIDEO_COMBINE_VOTE_AVERAGE = DIR_MOVIEW_TYPE + SORT_BY + VOTE_AVERAGE + API_KEY;

    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // resource for my overflow icon menu
        // http://stackoverflow.com/questions/21544501/overflow-icon-in-action-bar-invisible
        apiKey=BuildConfig.ApiKey;
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

        checkConnection();
            switch (Util.type) {
            case 0:
                getActivity().setTitle("Popular Movies");
                order = "popular";
                if (isConnected()) {
                    CustomAsyncTask customAsyncTask=new CustomAsyncTask(MainFragment.this);
                    customAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                if (isConnected()) {
                    CustomAsyncTask customAsyncTask=new CustomAsyncTask(MainFragment.this);
                    customAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        Log.v("MainFragment", Integer.toString(Util.pos));
        return rootView;
    }

    boolean isInternetConnected;

    private boolean checkConnection() {
        return isInternetConnected=isConnected();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); //NetworkApplication.getInstance().getApplicationContext()/
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork!=null){
            return isInternetConnected= activeNetwork.isConnected();
        }else
            return isInternetConnected=false;
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
                if (isConnected()) {
                    CustomAsyncTask customAsyncTask=new CustomAsyncTask(MainFragment.this);
                    customAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.highest_rated:
                Util.pos = 0;
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                if (isConnected()) {
                    CustomAsyncTask customAsyncTask=new CustomAsyncTask(MainFragment.this);
                    customAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
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
}