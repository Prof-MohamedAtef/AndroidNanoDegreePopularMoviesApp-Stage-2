package prof.mo.ed.popularmoviesstageone;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed on 8/6/2016.
 */

//public class MovieEntity {
public class MovieEntity extends AndroidViewModel implements Serializable {

    String POSTER_PATH_STRING,VIDEO_ID_STRING,TITLE_STRING,OVERVIEW_STRING,RELEASE_DATE_STRING,POPULARITY_STRING,VOTE_AVERAGE,
    TRAILER_ID_STRING,TRAILER_KEY_STRING,TRAILER_NAME_STRING,TRAILER_SITE_STRING,TRAILER_SIZE_STRING,AUTHOR_STRING,CONTENT_STRING;
    private String YoutubeUrl="https://www.youtube.com/watch?v=";

    public MovieEntity(@NonNull Application application) {
        super(application);
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



        public String getTRAILER_ID_STRING() {
        return TRAILER_ID_STRING;
    }

    public void setTRAILER_ID_STRING(String TRAILER_ID_STRING) {
        this.TRAILER_ID_STRING = TRAILER_ID_STRING;
    }

    public String getTRAILER_KEY_STRING() {        return YoutubeUrl+ TRAILER_KEY_STRING;    }

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

    int Is__Favourite;

    public int getIs__Favourite() {
        return Is__Favourite;
    }

    public void setIs__Favourite(int is__Favourite) {
        Is__Favourite = is__Favourite;
    }

    final String IMAGES_BASE_Url = "http://image.tmdb.org";
    final String IMAGE_SIZE = "/t/p/w185/";

    final String Image=IMAGES_BASE_Url+IMAGE_SIZE;




    public String getPOSTER_PATH_STRING() {
        return Image+POSTER_PATH_STRING;
    }

    public void setPOSTER_PATH_STRING(String POSTER_PATH_STRING) {
        this.POSTER_PATH_STRING = POSTER_PATH_STRING;
    }

    public String getVIDEO_ID_STRING() {
        return VIDEO_ID_STRING;
    }

    public void setVIDEO_ID_STRING(String VIDEO_ID_STRING) {
        this.VIDEO_ID_STRING = VIDEO_ID_STRING;
    }

    public String getTITLE_STRING() {
        return TITLE_STRING;
    }

    public void setTITLE_STRING(String TITLE_STRING) {
        this.TITLE_STRING = TITLE_STRING;
    }

    public String getOVERVIEW_STRING() {
        return OVERVIEW_STRING;
    }

    public void setOVERVIEW_STRING(String OVERVIEW_STRING) {
        this.OVERVIEW_STRING = OVERVIEW_STRING;
    }

    public String getRELEASE_DATE_STRING() {
        return RELEASE_DATE_STRING;
    }

    public void setRELEASE_DATE_STRING(String RELEASE_DATE_STRING) {
        this.RELEASE_DATE_STRING = RELEASE_DATE_STRING;
    }

    public String getPOPULARITY_STRING() {
        return POPULARITY_STRING;
    }

    public void setPOPULARITY_STRING(String POPULARITY_STRING) {
        this.POPULARITY_STRING = POPULARITY_STRING;
    }


    public String getVOTE_AVERAGE() {
        return VOTE_AVERAGE;
    }

    public void setVOTE_AVERAGE(String VOTE_AVERAGE) {
        this.VOTE_AVERAGE = VOTE_AVERAGE;
    }

}