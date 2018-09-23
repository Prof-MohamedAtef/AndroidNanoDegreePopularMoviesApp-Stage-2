package prof.mo.ed.popularmoviesstageone.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import prof.mo.ed.popularmoviesstageone.Fragments.MainFragment;
import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;
import prof.mo.ed.popularmoviesstageone.R;

/**
 * Created by Prof-Mohamed on 8/7/2018.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHOlder> implements Serializable {
    private List<MoviesRoomEntity> feedItemList;

    public transient Context mContext;

    public ImagesAdapter(Context context, List<MoviesRoomEntity> feedItemList) {
        this.feedItemList=feedItemList;
        this.mContext= context;
    }

    @Override
    public ViewHOlder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, null);
        RecyclerView.ViewHolder viewHolder=new ViewHOlder(view);
        return (ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHOlder customViewholder, final int i) {
        final MoviesRoomEntity feedItem = feedItemList.get(i);
//        Picasso.with(mContext).load(feedItem.getPosterPath()).into(customViewholder.one_img);
        Picasso.with(mContext).load(feedItem.getPosterPath())
                .error(R.drawable.movie_poster)
                .into(customViewholder.one_img);
        customViewholder.one_text.setText(feedItem.getMovieTitle());
        customViewholder.one_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedItemList != null) {
                    ((MainFragment.MovieDataListener) mContext).onMovieFragmentSelected(feedItemList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=feedItemList?feedItemList.size():0);
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {
        protected ImageView one_img;
        protected TextView one_text;


        public ViewHOlder(View converview) {
            super(converview);
            this.one_img = (ImageView) converview.findViewById(R.id.img_view);
            this.one_text = (TextView) converview.findViewById(R.id.txt_poster_title);
        }
    }
}