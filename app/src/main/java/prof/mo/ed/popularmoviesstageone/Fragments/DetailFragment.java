package prof.mo.ed.popularmoviesstageone.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import prof.mo.ed.popularmoviesstageone.Adapters.TrailersAdapter;
import prof.mo.ed.popularmoviesstageone.AppExecutors;
import prof.mo.ed.popularmoviesstageone.BuildConfig;
import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.DataPersist.Dao.RoomMoviesDao;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.DeleteAsyncTask;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.InsertAsyncTask;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.MoviesAPIAsyncTask;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.UpdateAsyncTask;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;
import prof.mo.ed.popularmoviesstageone.R;
import prof.mo.ed.popularmoviesstageone.SessionManagement;
import prof.mo.ed.popularmoviesstageone.Util;
import prof.mo.ed.popularmoviesstageone.ViewModel.FavoriteMovieViewModelFactory;
import prof.mo.ed.popularmoviesstageone.ViewModel.CheckMovieIDStatusViewModel;
import prof.mo.ed.popularmoviesstageone.ViewModel.MoviesViewModel;

/**
 * Created by Prof-Mohamed Atef on 8/8/2018.
 */
public class DetailFragment extends Fragment implements MoviesAPIAsyncTask.OnTaskCompleted,
        InsertAsyncTask.OnTaskCompletes,
        DeleteAsyncTask.OnTaskCompletes{

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    public ArrayAdapter<MoviesRoomEntity> trailersAdapter;
    RatingBar ratingBar;
    ListView listView_trailers, listView_reviews;
    TextView text_author, txtTitle, txtReleaseDate, txtVoteAverage, txtOverView;
    ImageView imgPoster;
    ViewGroup header, footer;
    MoviesRoomEntity movieEntity = null;
    String trailersEndPoint;
    String reviewsEndPoint;
    String movieID = null;
    MoviesRoomEntity helper ;
    FavoriteMovieViewModelFactory factory;
    CheckMovieIDStatusViewModel checkMovieIDStatusViewModel;
    String apiKey;
    AppDatabase database;
    private AppExecutors mAppExecutors;
    MoviesViewModel moviesViewModel;
    public static final String KEY_POPULAR = "popular";
    public static final String KEY_Favorite = "favorite";
    public static final String KEY_TOP_RATED = "top_rated";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ratingBar = (RatingBar) header.findViewById(R.id.ratingBar);
        txtTitle = (TextView) header.findViewById(R.id.txt_title);
        txtReleaseDate = (TextView) header.findViewById(R.id.txt_release_date);
        txtVoteAverage = (TextView) header.findViewById(R.id.txt_VoteAverage);
        txtOverView = (TextView) header.findViewById(R.id.txt_overview);
        imgPoster = (ImageView) header.findViewById(R.id.Image_Poster);
        text_author = (TextView) footer.findViewById(R.id.text_author);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            movieEntity = (MoviesRoomEntity) bundle.getSerializable("twoPaneExtras");
            txtTitle.setText(movieEntity.getMovieTitle());
            txtReleaseDate.setText(movieEntity.getReleaseDate());
            txtVoteAverage.setText(movieEntity.getVoteAverage());
            txtOverView.setText(movieEntity.getMovieOverView());
            Picasso.with(getActivity()).load(movieEntity.getPosterPath()).into(imgPoster);
            movieID = movieEntity.getMovieID();
            ratingBar.setRating(0);
            moviesViewModel= ViewModelProviders.of((FragmentActivity) getActivity()).get(MoviesViewModel.class);
            moviesViewModel.setMovieID(movieID);
            getMovieID(moviesViewModel);

            String DefaultUri = getString(R.string.default_uri);
            trailersEndPoint = DefaultUri + "/movie/" + movieID + "/videos?api_key=" + apiKey;
            reviewsEndPoint = DefaultUri + "/movie/" + movieID + "/reviews?api_key=" + apiKey;

            try {
                startFetchingTrailers();
            } catch (Exception e) {
                Log.v(LOG_TAG, "didn't Execute Trailers");
            }
            try {
                startFetchingReviews();
            } catch (Exception e) {
                Log.v(LOG_TAG, "didn't Execute Reviews");
            }
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    if (ratingBar.getRating() == 1) {
                        movieEntity.setIS_Favourite(1);
                        TryInsert(database, movieEntity.getMovieID(), movieEntity.getMovieTitle(), movieEntity.getMovieOverView(), movieEntity.getReleaseDate(), movieEntity.getPopularity(), movieEntity.getVoteAverage(), movieEntity.getIS_Favourite(), movieEntity.getPosterPath());
                    } else if (ratingBar.getRating() == 0) {
                        movieEntity.setIS_Favourite(0);
                        TryDelete(database,movieEntity.getMovieID());
                    }
                }
            }
        });
        trailersAdapter = new TrailersAdapter(getActivity(), R.layout.trailer_list_item, new ArrayList<MoviesRoomEntity>());
        listView_trailers.addHeaderView(header, null, false);
        listView_trailers.addFooterView(footer, null, false);
        listView_trailers.setAdapter(trailersAdapter);
        listView_trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private void getMovieID(MoviesViewModel moviesViewModel) {
        moviesViewModel.getIsMovieFavorite().observe((LifecycleOwner) getActivity(), new Observer<List<MoviesRoomEntity>>() {
            @Override
            public void onChanged(@Nullable List<MoviesRoomEntity> moviesRoomEntities) {
                if (moviesRoomEntities != null) {
                    if (moviesRoomEntities.size()>0){
                        String x = null;
                        for (int i = 0; i < moviesRoomEntities.size(); i++) {
                            x = moviesRoomEntities.get(i).getMovieID();
                            if (movieID.equals(x)){
                                break;
                            }
                        }
                        if (x != null) {
                            if (x.equals(movieID)) {
                                ratingBar.setRating(1);
                                moviesViewModel.getIsMovieFavorite().removeObserver(this);
                            } else if (!x.equals(movieID)) {
                                ratingBar.setRating(0);
                                moviesViewModel.getIsMovieFavorite().removeObserver(this);
                            }
                        } else if (x == null) {
                            ratingBar.setRating(0);
                        }
                    }else if (moviesRoomEntities.size()==0){
                    }
                }
            }
        });
    }

    private void TryDelete (AppDatabase database, String MovieID){
        DeleteAsyncTask deleteAsyncTask=new DeleteAsyncTask(database,MovieID,this);
        deleteAsyncTask.execute();
    }

    private void TryInsert(AppDatabase db, String video_id_string, String title_string, String overview_string, String release_date_string, String popularity_string, String vote_average, int is__favourite, String poster_path_string) {

        helper.setMovieID(video_id_string);
        helper.setMovieTitle(title_string);
        helper.setMovieOverView(overview_string);
        helper.setReleaseDate(release_date_string);
        helper.setPopularity(popularity_string);
        helper.setVoteAverage(vote_average);
        helper.setIS_Favourite(is__favourite);
        helper.setPosterPath(poster_path_string);

        InsertAsyncTask insertAsyncTask=new InsertAsyncTask(db,helper,this);
        insertAsyncTask.execute();
    }

    public void startFetchingReviews() {
        try {

            MoviesAPIAsyncTask reviewsAsync = new MoviesAPIAsyncTask("",KEY_REVIEW, DetailFragment.this);
            reviewsAsync.execute(reviewsEndPoint);
        } catch (Exception e) {
            Log.v(LOG_TAG, "didn't Execute Reviews");
        }
    }

    public static final String KEY_REVIEW = "review";
    public static final String KEY_TRAILER = "trailer";
    public void startFetchingTrailers() {
        try {
            MoviesAPIAsyncTask fetchTrailers = new MoviesAPIAsyncTask("",KEY_TRAILER, DetailFragment.this);
            fetchTrailers.execute(trailersEndPoint);
        } catch (Exception e) {
            Log.v(LOG_TAG, "didn't Execute Trailers");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        header = (ViewGroup) inflater.inflate(R.layout.header, listView_trailers,
                false);

        footer = (ViewGroup) inflater.inflate(R.layout.footer, listView_reviews,
                false);

        listView_trailers = (ListView) rootView.findViewById(R.id.listview_trailers);
        registerForContextMenu(listView_trailers);
        return rootView;
    }



    SessionManagement sessionManagement;
    HashMap<String, Integer> user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiKey = BuildConfig.ApiKey;
        sessionManagement=new SessionManagement(getActivity());
        database =new AppDatabase() {
            @Override
            public RoomMoviesDao movieDao() {
                return null;
            }

            @Override
            public void clearAllTables() {

            }
        };
        mAppExecutors = new AppExecutors();
        database = AppDatabase.getAppDatabase(getActivity(),mAppExecutors);
        helper = new MoviesRoomEntity();
    }

    @Override
    public void onTaskCompleted(String Type_1,String Type, ArrayList<MoviesRoomEntity> result) {
        if (result != null && result.size() > 0) {
            if (Type != null) {
                if (Type.equals(KEY_REVIEW)) {
                    if (getActivity() != null && result.size() > 0) {
                        text_author.setText("");
                        for (MoviesRoomEntity mv : result) {
                            text_author.append(getString(R.string.author_name)+" \n" + mv.getAUTHOR_STRING().toString() + "\n\n" + getString(R.string.review)+" \n" + mv.getCONTENT_STRING().toString() + "\n\n\n**\n\n\n");
                        }
                    }
                } else if (Type.equals(KEY_TRAILER)) {
                    if (getActivity() != null && result.size() > 0) {
                        trailersAdapter = new TrailersAdapter(getActivity(), R.layout.trailer_list_item, result);
                        listView_trailers.setAdapter(trailersAdapter);
                    }
                }
            } else text_author.setText(getString(R.string.no_review));
        }
    }

    @Override
    public void onInsertTaskCompletes(boolean x, String FragType) {
        if (x == true) {
            if (getActivity()!=null){
                Toast.makeText(getActivity(), getString(R.string.addede_to_fav), Toast.LENGTH_LONG).show();
            }
        } else {
            if (getActivity()!=null){
                Toast.makeText(getActivity(), getString(R.string.movie_not_inserted), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDeleteTaskCompletes(boolean x) {
      if (x==true){
          if (getActivity()!=null){
              Toast.makeText(getActivity(), getString(R.string.removed_from_favs), Toast.LENGTH_LONG).show();
          }
      }else if (x=false){
          if (getActivity()!=null){
              Toast.makeText(getActivity(), getString(R.string.removed_from_favs), Toast.LENGTH_LONG).show();
          }
      }
    }

    public DetailFragment() {
    }
}