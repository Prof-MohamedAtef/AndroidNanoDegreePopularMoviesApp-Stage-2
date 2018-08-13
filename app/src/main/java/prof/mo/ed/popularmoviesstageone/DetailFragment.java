package prof.mo.ed.popularmoviesstageone;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Prof-Mohamed Atef on 8/8/2018.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    MovieEntity movieEntity = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        TextView txtReleaseDate = (TextView) rootView.findViewById(R.id.txt_release_date);
        TextView txtVoteAverage = (TextView) rootView.findViewById(R.id.txt_VoteAverage);
        TextView txtOverView = (TextView) rootView.findViewById(R.id.txt_overview);
        ImageView imgPoster = (ImageView) rootView.findViewById(R.id.Image_Poster);
        final Bundle bundle = getArguments();

        if (bundle != null) {
            movieEntity = (MovieEntity) bundle.getSerializable("twoPaneExtras");
            txtTitle.setText(movieEntity.getTITLE_STRING());
            txtReleaseDate.setText(movieEntity.getRELEASE_DATE_STRING());
            txtVoteAverage.setText(movieEntity.getVOTE_AVERAGE());
            txtOverView.setText(movieEntity.getOVERVIEW_STRING());
            Picasso.with(getActivity()).load(movieEntity.getPOSTER_PATH_STRING()).into(imgPoster);
        }
        return rootView;
    }
}