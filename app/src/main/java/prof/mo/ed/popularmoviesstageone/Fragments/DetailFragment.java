package prof.mo.ed.popularmoviesstageone.Fragments;

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
import java.util.List;

import prof.mo.ed.popularmoviesstageone.Adapters.TrailersAdapter;
import prof.mo.ed.popularmoviesstageone.BuildConfig;
import prof.mo.ed.popularmoviesstageone.DataPersist.AppDatabase;
import prof.mo.ed.popularmoviesstageone.DataPersist.Dao.RoomMoviesDao;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.InsertAsyncTask;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.MoviesAPIAsyncTask;
import prof.mo.ed.popularmoviesstageone.GenericAsyncTasks.UpdateAsyncTask;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;
import prof.mo.ed.popularmoviesstageone.R;
import prof.mo.ed.popularmoviesstageone.ViewModel.FavoriteMovieViewModelFactory;
import prof.mo.ed.popularmoviesstageone.ViewModel.CheckMovieIDStatusViewModel;

/**
 * Created by Prof-Mohamed Atef on 8/8/2018.
 */
public class DetailFragment extends Fragment implements MoviesAPIAsyncTask.OnTaskCompleted,
        InsertAsyncTask.OnTaskCompletes,
        UpdateAsyncTask.OnTaskCompletes{

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

            factory = new FavoriteMovieViewModelFactory(database, movieID);
            checkMovieIDStatusViewModel =
                    ViewModelProviders.of((FragmentActivity) getActivity(), factory).get(CheckMovieIDStatusViewModel.class);

            getMovieID(checkMovieIDStatusViewModel);

            String DefaultUri = "http://api.themoviedb.org/3";
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
                        TryUpdate(database, movieEntity.getMovieID(), movieEntity.getIS_Favourite());
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

    private void getMovieID(CheckMovieIDStatusViewModel checkMovieIDStatusViewModel) {
        checkMovieIDStatusViewModel.getMovie().observe((LifecycleOwner) getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                checkMovieIDStatusViewModel.getMovie().removeObserver(this);
                if (strings != null) {
                    String x = null;
                    for (int i = 0; i < strings.size(); i++) {
                        x = strings.get(i);
                        if (movieID.equals(x)){
                            break;
                        }
                    }
                    if (x != null) {
                        if (x.equals(movieID)) {
                            ratingBar.setRating(1);
                        } else if (!x.equals(movieID)) {
                            ratingBar.setRating(0);
                        }
                    } else if (x == null) {
                        ratingBar.setRating(0);
                    }
                }
            }
        });
    }

    private void TryUpdate(AppDatabase db, String video_id_string, int is__favourite) {
        UpdateAsyncTask updateAsyncTask=new UpdateAsyncTask(db,is__favourite,video_id_string,this);
        updateAsyncTask.execute();
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
            MoviesAPIAsyncTask reviewsAsync = new MoviesAPIAsyncTask("review", DetailFragment.this);
            reviewsAsync.execute(reviewsEndPoint);
        } catch (Exception e) {
            Log.v(LOG_TAG, "didn't Execute Reviews");
        }
    }

    public void startFetchingTrailers() {
        try {
            MoviesAPIAsyncTask fetchTrailers = new MoviesAPIAsyncTask("trailer", DetailFragment.this);
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiKey = BuildConfig.ApiKey;
        database = new AppDatabase() {
            @Override
            public void clearAllTables() {
            }

            @Override
            public RoomMoviesDao movieDao() {
                return null;
            }
        };
        database = AppDatabase.getAppDatabase(getActivity());
        helper = new MoviesRoomEntity();
    }

    @Override
    public void onTaskCompleted(String Type, ArrayList<MoviesRoomEntity> result) {
        if (result != null && result.size() > 0) {
            if (Type != null) {
                if (Type.equals("review")) {
                    if (getActivity() != null && result.size() > 0) {
                        text_author.setText("");
                        for (MoviesRoomEntity mv : result) {
                            text_author.append("Author Name : \n" + mv.getAUTHOR_STRING().toString() + "\n\n" + "Review : \n" + mv.getCONTENT_STRING().toString() + "\n\n\n**\n\n\n");
                        }
                    }

                } else if (Type.equals("trailer")) {
                    if (getActivity() != null && result.size() > 0) {
                        trailersAdapter = new TrailersAdapter(getActivity(), R.layout.trailer_list_item, result);
                        listView_trailers.setAdapter(trailersAdapter);
                    }
                }
            } else text_author.setText("No Reviews");
        }
    }

    @Override
    public void onInsertTaskCompletes(boolean x) {
        if (x == true) {
            Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Movie not Inserted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateTaskCompletes(boolean x) {
        if (x == true) {
            Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_LONG).show();
        }
    }
}