package prof.mo.ed.popularmoviesstageone.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import prof.mo.ed.popularmoviesstageone.Fragments.DetailFragment;
import prof.mo.ed.popularmoviesstageone.R;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
       if (savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putSerializable("twoPaneExtras", getIntent().getSerializableExtra("movieInfo"));
            detailFragment.setArguments(twoPaneExtras);
            getFragmentManager().beginTransaction()
                    .replace(R.id.panel_two, detailFragment, "detail").commit();
        }
    }
}