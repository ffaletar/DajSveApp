package hr.foi.air.webservice;

/**
 * Created by Filip on 13.11.2016..
 */

public interface WebServiceHandler {
    void onDataArrived(Object result, boolean ok);
}
