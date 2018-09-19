package prof.mo.ed.popularmoviesstageone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements MainFragment.MovieDataListener {

    boolean mTowPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout panelTwo = (FrameLayout) findViewById(R.id.panel_two);
        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.panel_one, mainFragment)
                .commit();

        if (panelTwo == null) {
            mTowPanel = false;
        } else {
            mTowPanel = true;
            if (savedInstanceState == null) {
                DetailFragment detailFragment = new DetailFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.panel_two, detailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onMovieFragmentSelected(RoomHelper movieEntity) {
        if (mTowPanel) {
            //Two Pane Ui
            DetailFragment detailFragment = new DetailFragment();
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putSerializable("twoPaneExtras", movieEntity);
            detailFragment.setArguments(twoPaneExtras);
            getFragmentManager().beginTransaction()
                    .replace(R.id.panel_two, detailFragment, "detail").commit();
        } else {
            // single pane Ui
            Intent intent = new Intent(this, DetailsActivity.class)
                    .putExtra("movieInfo", movieEntity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }
}