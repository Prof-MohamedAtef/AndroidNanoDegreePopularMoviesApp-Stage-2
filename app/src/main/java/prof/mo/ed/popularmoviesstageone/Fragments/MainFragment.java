package prof.mo.ed.popularmoviesstageone.Fragments;

import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import java.util.List;

import prof.mo.ed.popularmoviesstageone.Adapters.ImagesAdapter;
import prof.mo.ed.popularmoviesstageone.BuildConfig;
import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.DataPersist.Dao.RoomMoviesDao;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.MoviesAPIAsyncTask;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;
import prof.mo.ed.popularmoviesstageone.R;
import prof.mo.ed.popularmoviesstageone.Util;
import prof.mo.ed.popularmoviesstageone.ViewModel.MoviesViewModel;

/**
 * Created by Prof-Mohamed Atef on 8/7/2018.
 */
public class MainFragment extends Fragment implements MoviesAPIAsyncTask.OnTaskCompleted{

    public MainFragment() {

    }

    @Override
    public void onTaskCompleted(String Type, ArrayList<MoviesRoomEntity> result) {
        if (result != null) {
            mAdapter = null;
            mAdapter = new ImagesAdapter(getActivity(), result);
            recyclerView.setAdapter(mAdapter);
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            Log.v("MainFragment", Integer.toString(Util.pos));
            layoutManager.scrollToPosition(Util.pos);
        }
    }


    public interface MovieDataListener {
        void onMovieFragmentSelected(MoviesRoomEntity movieEntity);
    }

    public ImagesAdapter mAdapter;

    LiveData<List<MoviesRoomEntity>> list = new LiveData<List<MoviesRoomEntity>>() {
        @Override
        public void observeForever(@NonNull Observer<List<MoviesRoomEntity>> observer) {
            super.observeForever(observer);
        }
    };

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
    RecyclerView recyclerView;
    AppDatabase database;
    MoviesViewModel moviesViewModel;

    private void subscribeUi(MoviesViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFavoriteMovies().observe((LifecycleOwner) getActivity(), new Observer<List<MoviesRoomEntity>>() {
            @Override
            public void onChanged(@Nullable List<MoviesRoomEntity> Movies) {
                viewModel.getFavoriteMovies().removeObserver(this);
                if (Movies!= null) {
                    mAdapter = null;
                    mAdapter = new ImagesAdapter(getActivity(), Movies);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // resource for my overflow icon menu
        // http://stackoverflow.com/questions/21544501/overflow-icon-in-action-bar-invisible
        apiKey= BuildConfig.ApiKey;
        database=new AppDatabase() {
            @Override
            public void clearAllTables() {

            }

            @Override
            public RoomMoviesDao movieDao() {
                return null;
            }
        };
        moviesViewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(MoviesViewModel.class);
        subscribeUi(moviesViewModel);
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
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(MainFragment.this);
                    moviesAPIAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(MainFragment.this);
                    moviesAPIAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
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
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(MainFragment.this);
                    moviesAPIAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.highest_rated:
                Util.pos = 0;
                getActivity().setTitle("Top Rated Movies");
                order = "top_rated";
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(MainFragment.this);
                    moviesAPIAsyncTask.execute("http://api.themoviedb.org/3/movie/" + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                Util.type = 1;
                break;
            case R.id.favorites:
                Util.pos = 0;
                getActivity().setTitle("Favorite Movies");
                subscribeUi(moviesViewModel);
                Util.type = 2;
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