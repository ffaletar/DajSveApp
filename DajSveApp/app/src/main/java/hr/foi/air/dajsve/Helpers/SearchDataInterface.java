package hr.foi.air.dajsve.Helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 30.1.2017..
 */

public interface SearchDataInterface {

//    void onDataArrived(Object result, boolean ok);

    List<String> DohvatiSve(String search, List<String> nazivPonude, List<String> hash, Context context, List<Double> allElementsLat, List<Double> allElementsLong,  int km);
}
