package hr.foi.air.dajsve.Helpers;
import android.app.Activity;

/**
 * Created by Filip on 1.2.2017..
 */

public class Factory {

    public static SearchDataInterface Create(int tip){
        SearchDataInterface b = null;
        if(tip == 0){
            b = (SearchDataInterface) new hr.foi.air.dajsve.Helpers.PretrazivanjeKeyword();
        }else{
            b = (SearchDataInterface) new hr.foi.air.dajsve.Helpers.PretrazivanjeLokacija();
        }

        return b;
    }


}
