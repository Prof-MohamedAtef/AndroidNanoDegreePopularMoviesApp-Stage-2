package prof.mo.ed.popularmoviesstageone;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 8/26/2018.
 */

@Entity(tableName = "Favourites")
public class RoomHelper implements Serializable {


    public RoomHelper() {
    }


//    @Ignore
//    @NonNull
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "ID")
//    public int ID;

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "MovieID")
    public String MovieID;

    @Nullable
    @ColumnInfo(name = "MovieTitle")
    public String MovieTitle;

    @Nullable
    @ColumnInfo(name = "MovieOverView")
    public String MovieOverView;

    @Nullable
    @ColumnInfo(name = "ReleaseDate")
    public String ReleaseDate;

    @Nullable
    @ColumnInfo(name = "Popularity")
    public String Popularity;

    @Nullable
    @ColumnInfo(name = "VoteAverage")
    public String VoteAverage;

    @Nullable
    @ColumnInfo(name = "IS_Favourite")
    public Integer IS_Favourite;

    @Nullable
    @ColumnInfo(name = "PosterPath")
    public String PosterPath;

//    public int getID() {
//        return ID;
//    }
//
//    public void setID(int ID) {
//        this.ID = ID;
//    }

    public String getMovieID() {
        return MovieID;
    }

    public void setMovieID(String MovieID) {
        this.MovieID = MovieID;
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String MovieTitle) {
        this.MovieTitle = MovieTitle;
    }

    public String getMovieOverView() {
        return MovieOverView;
    }

    public void setMovieOverView(String MovieOverView) {
        this.MovieOverView = MovieOverView;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String ReleaseDate) {
        this.ReleaseDate = ReleaseDate;
    }

    public String getPopularity() {
        return Popularity;
    }

    public void setPopularity(String Popularity) {
        this.Popularity = Popularity;
    }

    public String getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(String VoteAverage) {
        this.VoteAverage = VoteAverage;
    }

    public int getIS_Favourite() {
        return IS_Favourite;
    }

    public void setIS_Favourite(int IS_Favourite) {
        this.IS_Favourite = IS_Favourite;
    }

    public String getPosterPath() {
        return Image+ PosterPath;
    }

    public void setPosterPath(String PosterPath) {
        this.PosterPath = PosterPath;
    }

    String IMAGES_BASE_Url = "http://image.tmdb.org";
    String IMAGE_SIZE = "/t/p/w185/";

    String Image = IMAGES_BASE_Url + IMAGE_SIZE;

    @Ignore
    public RoomHelper(String PosterPath, String MovieID, String MovieTitle, String MovieOverView, String ReleaseDate, String Popularity, String VoteAverage) {
        this.PosterPath= PosterPath;
        this.MovieID= MovieID;
        this.MovieTitle= MovieTitle;
        this.MovieOverView= MovieOverView;
        this.ReleaseDate= ReleaseDate;
        this.Popularity= Popularity;
        this.VoteAverage= VoteAverage;
    }

    @Ignore
    public RoomHelper(String AUTHOR_STRING, String CONTENT_STRING) {
        this.AUTHOR_STRING = AUTHOR_STRING;
        this.CONTENT_STRING = CONTENT_STRING;
    }

    public String getAUTHOR_STRING() {
        return AUTHOR_STRING;
    }

    public void setAUTHOR_STRING(String AUTHOR_STRING) {
        this.AUTHOR_STRING = AUTHOR_STRING;
    }

    public String getCONTENT_STRING() {
        return CONTENT_STRING;
    }

    public void setCONTENT_STRING(String CONTENT_STRING) {
        this.CONTENT_STRING = CONTENT_STRING;
    }

    @Ignore
    String AUTHOR_STRING, CONTENT_STRING,TRAILER_ID_STRING, TRAILER_KEY_STRING, TRAILER_NAME_STRING, TRAILER_SITE_STRING, TRAILER_SIZE_STRING;

    @Ignore
    public RoomHelper(String TRAILER_ID_STRING, String TRAILER_KEY_STRING, String TRAILER_NAME_STRING, String TRAILER_SITE_STRING, String TRAILER_SIZE_STRING) {
        this.TRAILER_ID_STRING = TRAILER_ID_STRING;
        this.TRAILER_KEY_STRING = TRAILER_KEY_STRING;
        this.TRAILER_NAME_STRING = TRAILER_NAME_STRING;
        this.TRAILER_SITE_STRING = TRAILER_SITE_STRING;
        this.TRAILER_SIZE_STRING = TRAILER_SIZE_STRING;
    }

    public String getTRAILER_ID_STRING() {
        return TRAILER_ID_STRING;
    }

    public void setTRAILER_ID_STRING(String TRAILER_ID_STRING) {
        this.TRAILER_ID_STRING = TRAILER_ID_STRING;
    }

    public String getYoutubeUrl() {
        return YoutubeUrl;
    }

    public void setYoutubeUrl(String YoutubeUrl) {
        YoutubeUrl = YoutubeUrl;
    }

    @Ignore
    public String YoutubeUrl = "https://www.youtube.com/watch?v=";
    public String getTRAILER_KEY_STRING() {
        return YoutubeUrl + TRAILER_KEY_STRING;
    }

    public void setTRAILER_KEY_STRING(String TRAILER_KEY_STRING) {
        this.TRAILER_KEY_STRING = TRAILER_KEY_STRING;
    }

    public String getTRAILER_NAME_STRING() {
        return TRAILER_NAME_STRING;
    }

    public void setTRAILER_NAME_STRING(String TRAILER_NAME_STRING) {
        this.TRAILER_NAME_STRING = TRAILER_NAME_STRING;
    }

    public String getTRAILER_SITE_STRING() {
        return TRAILER_SITE_STRING;
    }

    public void setTRAILER_SITE_STRING(String TRAILER_SITE_STRING) {
        this.TRAILER_SITE_STRING = TRAILER_SITE_STRING;
    }

    public String getTRAILER_SIZE_STRING() {
        return TRAILER_SIZE_STRING;
    }

    public void setTRAILER_SIZE_STRING(String TRAILER_SIZE_STRING) {
        this.TRAILER_SIZE_STRING = TRAILER_SIZE_STRING;
    }
}