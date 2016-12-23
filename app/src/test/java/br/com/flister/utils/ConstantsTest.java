package br.com.flister.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by junior on 22/12/2016.
 */

public class ConstantsTest {

    @Test
    public void testDefautValues(){

        assertEquals(Constants.BASE_GET_IMAGE_URL, "https://image.tmdb.org/t/p/w500/");
        assertEquals(Constants.BASE_TMD_URL, "http://api.themoviedb.org/");
        assertEquals(Constants.DATA_ORIGIN, "dataOrigin");
        assertEquals(Constants.MOVIE_SELECTED, "movieSelected");
    }
}
