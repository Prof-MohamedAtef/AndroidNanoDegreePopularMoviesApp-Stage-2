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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
    public void onTaskCompleted(String Type_1, String Type, ArrayList<MoviesRoomEntity> result) {
        if (result != null) {
            mAdapter = null;
            mAdapter = new ImagesAdapter(getActivity(), result,"");
            recyclerView.setAdapter(mAdapter);
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            Log.v("MainFragment", Integer.toString(Util.pos));
            layoutManager.scrollToPosition(Util.pos);
            if (Type_1!=null){
                if (Type_1.equals(KEY_POPULAR)){
                    Util.FragmentType=KEY_POPULAR;
                    if (getActivity()!=null)
                    {
                        getActivity().setTitle(getString(R.string.popular_movies));
                    }
                }else if (Type_1.equals(KEY_TOP_RATED)){
                    Util.FragmentType=KEY_TOP_RATED;
                    if (getActivity()!=null){
                        getActivity().setTitle(getString(R.string.top_rated_m));
                    }
                }
            }
        }
    }

    public interface MovieDataListener {
        void onMovieFragmentSelected(MoviesRoomEntity movieEntity);
    }

    public ImagesAdapter mAdapter;

    List<MoviesRoomEntity> list = new List<MoviesRoomEntity>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<MoviesRoomEntity> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(MoviesRoomEntity moviesRoomEntity) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends MoviesRoomEntity> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends MoviesRoomEntity> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public MoviesRoomEntity get(int index) {
            return null;
        }

        @Override
        public MoviesRoomEntity set(int index, MoviesRoomEntity element) {
            return null;
        }

        @Override
        public void add(int index, MoviesRoomEntity element) {

        }

        @Override
        public MoviesRoomEntity remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<MoviesRoomEntity> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<MoviesRoomEntity> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<MoviesRoomEntity> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public static final String KEY_POSITION = "position";
    public static final String KEY_TYPE = "type";
    public static final String KEY_POPULAR = "popular";
    public static final String KEY_Favorite = "favorite";
    public static final String KEY_TOP_RATED = "top_rated";
    public static final String KEY_FavoriteList = "FavoriteList";
    public static final String KEY_FavoriteFragmentIdentifier = "FavoriteFragmentIdentifier";
    public static final String KEY_ORIGINAL_URL = "http://api.themoviedb.org/3/movie/";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TYPE, Integer.toString(Util.type));
        outState.putString(KEY_POSITION, Integer.toString(Util.pos));
        outState.putSerializable(KEY_FavoriteList, (Serializable) Util.UtilMovies);
        outState.putString(KEY_FavoriteFragmentIdentifier,Util.FragmentType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.FragmentType!=null){
            //if (Util.UtilMovies!=null&&Util.UtilMovies.size()>0&&Util.FragmentType.equals(KEY_Favorite)){
              if (Util.FragmentType.equals(KEY_Favorite)){
                moviesViewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(MoviesViewModel.class);
                Util.FragmentType=KEY_Favorite;
                subscribeUi(Util.FragmentType,moviesViewModel);
                getActivity().setTitle(getString(R.string.favorite_movies));
            }else if (Util.FragmentType.equals(KEY_TOP_RATED)){
                getActivity().setTitle(getString(R.string.top_rated_m));
            }else if (Util.FragmentType.equals(KEY_POPULAR)){
                getActivity().setTitle(getString(R.string.popular_movies));
            }
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Util.type = Integer.parseInt(savedInstanceState.getString(KEY_TYPE));
            Util.UtilMovies= (List<MoviesRoomEntity>) savedInstanceState.getSerializable(KEY_FavoriteList);
            Util.FragmentType=savedInstanceState.getString(KEY_FavoriteFragmentIdentifier);
        }
    }

    String apiKey;
    String order;
    RecyclerView recyclerView;
    AppDatabase database;
    MoviesViewModel moviesViewModel;

    private void subscribeUi(String FragType,MoviesViewModel viewModel) {
        viewModel.getFavoriteMovies().observe((LifecycleOwner) getActivity(), new Observer<List<MoviesRoomEntity>>() {
            @Override
            public void onChanged(@Nullable List<MoviesRoomEntity> Movies) {
                if (Movies!= null) {
                    //list=Movies;
                    if (Movies.size()>0){
                        viewModel.getFavoriteMovies().removeObserver(this);
                        mAdapter = null;
                        mAdapter = new ImagesAdapter(getActivity(), Movies,FragType);
                        Util.UtilMovies=Movies;
                        Util.FragmentType=FragType;
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                        getActivity().setTitle(getString(R.string.favorite_movies));
                    }else if (Movies.size()==0){
                    }
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
            public RoomMoviesDao movieDao() {
                return null;
            }

            @Override
            public void clearAllTables() {
            }
        };

//        moviesViewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(MoviesViewModel.class);
//        subscribeUi(moviesViewModel);
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
            Util.type = Integer.parseInt(savedInstanceState.getString(KEY_TYPE));
            Util.pos = Integer.parseInt(savedInstanceState.getString(KEY_POSITION));
        }
        checkConnection();
            switch (Util.type) {
            case 0:
                getActivity().setTitle(getString(R.string.popular_movies));
                order = KEY_POPULAR;
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(KEY_POPULAR,KEY_POPULAR,MainFragment.this);
                    moviesAPIAsyncTask.execute(KEY_ORIGINAL_URL + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                getActivity().setTitle(getString(R.string.top_rated));
                order = KEY_TOP_RATED;
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(KEY_TOP_RATED,KEY_TOP_RATED,MainFragment.this);
                    moviesAPIAsyncTask.execute(KEY_ORIGINAL_URL + order + "?api_key="+apiKey);
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
                Util.FragmentType=KEY_POPULAR;
                getActivity().setTitle(getString(R.string.popular_movies));
                order = KEY_POPULAR;
                Util.type = 0;
                Util.pos = 0;
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(KEY_POPULAR,KEY_POPULAR,MainFragment.this);
                    moviesAPIAsyncTask.execute(KEY_ORIGINAL_URL + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.highest_rated:
                Util.pos = 0;
                Util.FragmentType=KEY_TOP_RATED;
                getActivity().setTitle(getString(R.string.top_rated));
                order = KEY_TOP_RATED;
                if (isConnected()) {
                    MoviesAPIAsyncTask moviesAPIAsyncTask =new MoviesAPIAsyncTask(KEY_TOP_RATED, KEY_TOP_RATED,MainFragment.this);
                    moviesAPIAsyncTask.execute(KEY_ORIGINAL_URL + order + "?api_key="+apiKey);
                }else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.pending_connection), Toast.LENGTH_SHORT).show();
                }
                Util.type = 1;
                break;
            case R.id.favorites:
                Util.pos = 0;
                moviesViewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(MoviesViewModel.class);
//                LiveData<List<MoviesRoomEntity>> moviesRoomList=database.movieDao().getAllMoviesData();
//                moviesRoomList.observe((LifecycleOwner) getActivity(), formList->{
//                    if (formList!=null&&!formList.isEmpty()){
//
//                    }
//                });
                Util.FragmentType=KEY_Favorite;
                subscribeUi(Util.FragmentType,moviesViewModel);

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