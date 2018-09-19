package prof.mo.ed.popularmoviesstageone;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 8/8/2018.
 */
public class DetailFragment extends Fragment implements CustomAsyncTask.OnTaskCompleted{
    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    public ArrayAdapter<MovieEntity> ReviewsAdapter;
    ArrayList<MovieEntity> ReviewsList = new ArrayList<MovieEntity>();

    public JSONObject moviesTrailers;
    public JSONArray moviesTrailersArray;
    public JSONObject oneTrailerData;
    public ArrayAdapter<RoomHelper> trailersAdapter;
    ArrayList<MovieEntity> trailersList = new ArrayList<MovieEntity>();
    public String main_List = "results";

    RatingBar ratingBar;

    ListView listView_trailers, listView_reviews;
    TextView text_author;

    ViewGroup header, footer;

    RoomHelper movieEntity = null;
    String trailersEndPoint;
    String reviewsEndPoint;
    String movieID = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ratingBar = (RatingBar) header.findViewById(R.id.ratingBar);
        TextView txtTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtReleaseDate = (TextView) header.findViewById(R.id.txt_release_date);
        TextView txtVoteAverage = (TextView) header.findViewById(R.id.txt_VoteAverage);
        TextView txtOverView = (TextView) header.findViewById(R.id.txt_overview);
        ImageView imgPoster = (ImageView) header.findViewById(R.id.Image_Poster);

        text_author = (TextView) footer.findViewById(R.id.text_author);

        final Bundle bundle = getArguments();
//        myDB = new DBHelper(getActivity());

        if (bundle != null) {
            movieEntity = (RoomHelper) bundle.getSerializable("twoPaneExtras");
            txtTitle.setText(movieEntity.getMovieTitle());
            txtReleaseDate.setText(movieEntity.getReleaseDate());
            txtVoteAverage.setText(movieEntity.getVoteAverage());
            txtOverView.setText(movieEntity.getMovieOverView());
            Picasso.with(getActivity()).load(movieEntity.getPosterPath()).into(imgPoster);
            movieID = movieEntity.getMovieID();
//            boolean IsFound = myDB.get_Movie_ID(movieID);
            boolean IsFound = IsMovieIDExist(movieID);
            ratingBar.setRating(0);
            if (IsFound == true) {
                int movieState = IsFavoriteMovieID(movieID);
                if (movieState == 0) {
                    ratingBar.setRating(0);
                } else if (movieState == 1) {
                    ratingBar.setRating(1);
                }
            } else if (IsFound == false) {
                ratingBar.setRating(0);
            }
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
//                int movieState = IsFavoriteMovieID(movieID);
//                if (movieState==0){
//
//                }
                if (ratingBar.getRating() == 1) {
                    movieEntity.setIS_Favourite(1);
//                    boolean IsInserted = myDB.insertData(movieEntity.getVIDEO_ID_STRING(), movieEntity.getTITLE_STRING(), movieEntity.getOVERVIEW_STRING(), movieEntity.getRELEASE_DATE_STRING(), movieEntity.getPOPULARITY_STRING(), movieEntity.getVOTE_AVERAGE(), movieEntity.getIs__Favourite(), movieEntity.getPOSTER_PATH_STRING());
                    boolean IsDone= TryInsert(database,movieEntity.getMovieID(), movieEntity.getMovieTitle(), movieEntity.getMovieOverView(), movieEntity.getReleaseDate(), movieEntity.getPopularity(), movieEntity.getVoteAverage(), movieEntity.getIS_Favourite(), movieEntity.getPosterPath());
                    if (IsDone==true) {
                        Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getActivity(), "Movie not Inserted", Toast.LENGTH_LONG).show();
                } else if (ratingBar.getRating() == 0) {
                    movieEntity.setIS_Favourite(0);
//                    boolean IsUpdated = myDB.UpdateData(movieEntity.getVIDEO_ID_STRING(), movieEntity.getIs__Favourite());
                    boolean isUpdated= TryUpdate(database, movieEntity.getMovieID(), movieEntity.getIS_Favourite());
                    if (isUpdated == true) {
                        Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        trailersAdapter = new TrailersAdapter(getActivity(), R.layout.trailer_list_item, new ArrayList<RoomHelper>());
        listView_trailers.addHeaderView(header, null, false);
        listView_trailers.addFooterView(footer, null, false);
        listView_trailers.setAdapter(trailersAdapter);
        listView_trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private boolean IsMovieIDExist(final String movieID) {
        List<RoomHelper> roomHelperList;
        boolean x=false;

//            new AsyncTask<void, void,void>(){
//                @Override
//                protected void doInBackground(void... voids) {

//                }
//            }.execute();

        roomHelperList= database.movieDao().getMovieID(movieID);
        if (roomHelperList.size()>0){
            x=true;
        }else if (roomHelperList.size()==0){
            x=false;
        }
        return x;
    }

    private int IsFavoriteMovieID(String movieID) {
        int movieState= 0;
        movieState= database.movieDao().getIsFavoriteMovieID(movieID);
        if (movieState>0){
            movieState=1;
            return movieState;
        }else
            return movieState;
    }

    private boolean TryUpdate(AppDatabase db, String video_id_string, int is__favourite) {
        try {
            db.movieDao().UpdateMovie(is__favourite,video_id_string);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    RoomHelper helper=new RoomHelper();
    private boolean TryInsert(AppDatabase db, String video_id_string, String title_string, String overview_string, String release_date_string, String popularity_string, String vote_average, int is__favourite, String poster_path_string) {

        helper.setMovieID(video_id_string);
        helper.setMovieTitle(title_string);
        helper.setMovieOverView(overview_string);
        helper.setReleaseDate(release_date_string);
        helper.setPopularity(popularity_string);
        helper.setVoteAverage(vote_average);
        helper.setIS_Favourite(is__favourite);
        helper.setPosterPath(poster_path_string);
        try{
            db.movieDao().InsertMovie(helper);
            return true;
        }catch (Exception e){
            return false;
        }
//        return addMovie(db,helper);
    }

    private boolean addMovie(final AppDatabase db, RoomHelper helper){
        try{
            db.movieDao().InsertMovie(helper);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void startFetchingReviews() {
        try {
            CustomAsyncTask reviewsAsync = new CustomAsyncTask("review",DetailFragment.this);
            reviewsAsync.execute(reviewsEndPoint);
        } catch (Exception e) {
            Log.v(LOG_TAG, "didn't Execute Reviews");
        }
    }

    public void startFetchingTrailers() {
        try {
            CustomAsyncTask fetchTrailers = new CustomAsyncTask("trailer",DetailFragment.this);
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
    String apiKey;
    AppDatabase database;
    RoomDao roomDao;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiKey=BuildConfig.ApiKey;
        database=new AppDatabase() {
            @Override
            public RoomDao movieDao() {
                return null;
            }
        };
        database=AppDatabase.getAppDatabase(getActivity());
    }

    @Override
    public void onTaskCompleted(String Type,ArrayList<RoomHelper> result) {
        if (result != null&&result.size()>0) {
            if (Type!=null){
                if (Type.equals("review")){
                    text_author.setText("");
                    for (RoomHelper mv : result){
                        text_author.append("Author Name : \n" +mv.getAUTHOR_STRING().toString()+"\n\n"+"Review : \n"+mv.getCONTENT_STRING().toString()+"\n\n\n**\n\n\n" );
                    }

                }else if (Type.equals("trailer")){
                    trailersAdapter = new TrailersAdapter(getActivity(), R.layout.trailer_list_item, result);
                    listView_trailers.setAdapter(trailersAdapter);
                }
            }else text_author.setText("No Reviews");
        }
    }
}