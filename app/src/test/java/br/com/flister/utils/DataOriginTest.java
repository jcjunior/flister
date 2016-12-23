package br.com.flister.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by junior on 22/12/2016.
 */

public class DataOriginTest {

    @Test
    public void testDataOriginEnumOrder(){
        assertEquals(0,DataOrigin.REST_API.ordinal());
        assertEquals(1,DataOrigin.SHARED_PREFERENCES.ordinal());
        assertEquals(2,DataOrigin.DATA_BASE.ordinal());
    }

    @Test
    public void testDataOriginQuantity(){
        assertEquals(3, DataOrigin.values().length);
    }
}
