package prof.mo.ed.popularmoviesstageone;

import android.app.Application;

import java.io.Serializable;
import java.util.List;

import prof.mo.ed.popularmoviesstageone.Entities.MoviesRoomEntity;

/**
 * Created by Prof-Mohamed Atef on 8/7/2018.
 */
public class Util implements Serializable{
    public static int type=0;
    public static int pos = 0;
    public static String FragmentType = null;
    public static Application application;
    public static List<MoviesRoomEntity> UtilMovies;
}
