package entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Helena on 24.12.2016..
 */

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private Ponuda dohvacenaPonuda;

    public MyItem(double lat, double lng, Ponuda dohvacena) {
        mPosition = new LatLng(lat, lng);
        dohvacenaPonuda = dohvacena;
        mTitle = null;
        mSnippet = null;
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public MyItem(LatLng pozicija, Ponuda dohvacena) {
        mPosition = pozicija;
        dohvacenaPonuda = dohvacena;
        mTitle = null;
        mSnippet = null;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


    public String getNazivInfo() { return dohvacenaPonuda.getNaziv(); }


    public String getDetalji() { return dohvacenaPonuda.getCijena(); }

    public String getUrlSlike(){
        return dohvacenaPonuda.getUrlSlike();
    }

    public Ponuda getPonuda(){return dohvacenaPonuda;}

    public String getGrad(){return dohvacenaPonuda.getGrad();}

}
