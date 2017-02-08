package hr.foi.air.dajsve.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.MyItem;
import entities.Ponuda;
import hr.foi.air.dajsve.Helpers.Factory;
import hr.foi.air.dajsve.Helpers.PretrazivanjeLokacija;
import hr.foi.air.dajsve.Helpers.SearchDataInterface;
import hr.foi.air.dajsve.R;

/**
 * Created by Filip on 27.11.2016..
 */


public class MapFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    public MapFragment() {
    }

    protected GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private float zoomStatus;

    private ClusterManager<MyItem> mClusterManager;
    private MyItem clickedClusterItem;
    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map = null;
    List<Ponuda> svePonude = null;
    private RecyclerView rv;
    private Context context;
    private String[] arraySpinner;
    public  List <String> hashs =new ArrayList<>();
    public String unos;
    public boolean dobraAdresa=true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(hr.foi.air.dajsve.R.layout.map_fragment, container, false);
        rv = (RecyclerView) v.findViewById(hr.foi.air.dajsve.R.id.rv);
        svePonude = Ponuda.getAll();
        System.out.print("Broj ponuda: " + svePonude.size());
        context = v.getContext();

        mapFragment = new com.google.android.gms.maps.MapFragment();
        mapFragment.getMapAsync(this);
        getActivity().getFragmentManager().beginTransaction().add(hr.foi.air.dajsve.R.id.frame, mapFragment).commit();

        for(Ponuda ponuda : Ponuda.getAll())
        {
            hashs.add(ponuda.getHash());
        }
        arraySpinner = new String[] {
                "10", "30", "60", "100", "200"
        };
        try {
            this.hashs = getArguments().getStringArrayList("AAA");
            this.unos = getArguments().getString("BBB");
            this.dobraAdresa=true;
            System.out.println("ISPISIIII");
        }catch (Exception e){
            this.unos="Turopoljska 17,Lekenik";
        }


        //provjera da li je gps omogućen
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(context, "GPS je omogućen", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Dohvaćanje lokacije...", Toast.LENGTH_SHORT).show();
        }else{
            GPSupozorenje();
        }
        //kraj provjere

        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        zoomStatus = map.getCameraPosition().zoom;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setRotateGesturesEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
        }

        List<Address> address = null;

        System.out.println("ADRESA: "+ this.unos);
        try {
            Geocoder coder = new Geocoder(getActivity());
            address = coder.getFromLocationName(this.unos, 5);
            Address location = address.get(0);
            if(this.unos.equals("Turopoljska 17,Lekenik")) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45, 16), 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
            }
            else {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
                map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Vaša lokacija"));
            }
        } catch (IOException e) {
            System.out.print("GRESKA");

        }
        List<String> hashPonudeLokacija = new ArrayList<>();
        for(Ponuda ponuda : Ponuda.getAll())
        {
            if(!ponuda.getLatitude().equals("nema")){
                if(!ponuda.getLongitude().equals("nema")) {
                    hashPonudeLokacija.add(ponuda.getHash());
                }
            }
        }
        pokreniCluster(map,hashPonudeLokacija,"");

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new detaljniInfoWindow());

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Trenutna lokacija");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(9));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        clickedClusterItem = myItem;
        System.out.println(myItem.getNazivInfo());
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        ArrayList<Ponuda> ponudaTag = new ArrayList<Ponuda>();
        ponudaTag.add(clickedClusterItem.getPonuda());

        AppCompatActivity activity = (AppCompatActivity) getContext();
        Fragment detaljiponude = new DetaljiPonudeFragment();
        Bundle bundle = new Bundle();
        activity.getSupportFragmentManager().beginTransaction();
        bundle.putParcelableArrayList("ponuda", ponudaTag);
        detaljiponude.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(hr.foi.air.dajsve.R.id.linearlayout, detaljiponude).addToBackStack(null).commit();
    }

    class detaljniInfoWindow implements GoogleMap.InfoWindowAdapter {

        private final View infowindowView;

        detaljniInfoWindow() {

            infowindowView = getActivity().getLayoutInflater().inflate(hr.foi.air.dajsve.R.layout.detalji_map_infowindow, null);
            infowindowView.setLayoutParams(new RelativeLayout.LayoutParams(900, RelativeLayout.LayoutParams.WRAP_CONTENT));


        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView) infowindowView
                    .findViewById(hr.foi.air.dajsve.R.id.title));
            TextView tvSnippet = ((TextView) infowindowView
                    .findViewById(hr.foi.air.dajsve.R.id.snippet));

            ImageView slika = ((ImageView) infowindowView.findViewById(hr.foi.air.dajsve.R.id.slika));

            tvTitle.setText(clickedClusterItem.getNazivInfo());
            tvSnippet.setText(clickedClusterItem.getGrad() + ", " +clickedClusterItem.getDetalji()+ " kuna");

            if (slika != null) {
                Picasso.with(context).load(clickedClusterItem.getUrlSlike()).into(slika, new MarkerCallback(marker));
            }

            return infowindowView;

        }

    }

    class MarkerInfo extends DefaultClusterRenderer<MyItem> implements GoogleMap.OnCameraIdleListener{
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(hr.foi.air.dajsve.R.drawable.popust_ic);

        public MarkerInfo(Context context, GoogleMap map,
                          ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            markerOptions.snippet(item.getDetalji());
            markerOptions.title(item.getNazivInfo());
            markerOptions.icon(icon1);

            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            //setMinClusterSize(1);
            /**return cluster.getSize() > 10; // if markers <=10 then not clustering*/
            //return true;
            System.out.println("ZOOMSTATUS:" + zoomStatus + "novi:");
            if (zoomStatus > 18) {
                return cluster.getSize() > 10; //if markers <=10 then not clustering
            } else {
                return cluster.getSize() > 1; //if markers <=1 then not clustering
                //return true;
            }
        }
        @Override
        public void onCameraIdle() {
            zoomStatus = map.getCameraPosition().zoom;
            System.out.println("kamera se mice ++: " + zoomStatus);
        }

    }


    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }
    private void pokreniCluster(GoogleMap map, List<String> aa, String adresa) {

        mClusterManager = new ClusterManager<MyItem>(context, map);
        //mClusterManager.setRenderer(new PersonRenderer());
        mClusterManager.setRenderer(new MarkerInfo(context, map, mClusterManager));
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        map.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        map.setOnInfoWindowClickListener(mClusterManager);

        popuniCluster(aa);
        mClusterManager.cluster();
    }

    private void popuniCluster(List<String> aa) {



        System.out.print("krauc : "+aa.size());
        ArrayList<LatLng> spremljeneKoordinate = new ArrayList<>();
        spremljeneKoordinate.add(new LatLng(1.0,1.0));
        List<String> hashPonude = new ArrayList<>();
        List<Double> latPonude = new ArrayList<>();
        List<Double> longPonude = new ArrayList<>();
        for(Ponuda ponuda : Ponuda.getAll())
        {

            if(!ponuda.getLatitude().equals("nema")){
                if(!ponuda.getLongitude().equals("nema")) {
                    latPonude.add(Double.parseDouble(ponuda.getLatitude()));
                    longPonude.add(Double.parseDouble(ponuda.getLongitude()));
                    hashPonude.add(ponuda.getHash());
                }
            }
        }
        try {
            for (final Ponuda ponuda : svePonude) {
                for (String hash : this.hashs) {
                    if (ponuda.getHash().equals(hash)) {
                        String ponudaLat = ponuda.getLatitude();
                        String ponudaLon = ponuda.getLongitude();
                        LatLng izracunato;
                        LatLng gradKoordinate;


                        /**
                         if(!ponudaLat.contentEquals("nema") && !ponudaLon.contentEquals("nema")){
                         double ponudaLatitude = Double.parseDouble(ponudaLat);
                         double ponudaLongitude = Double.parseDouble(ponudaLon);
                         gradKoordinate = new LatLng(ponudaLatitude, ponudaLongitude);
                         mClusterManager.addItem(new MyItem(gradKoordinate, ponuda));
                         }
                         */

                        if (ponudaLat.contentEquals("nema") || ponudaLon.contentEquals("nema")) {
                            continue;
                        } else {
                            do {
                                double ponudaLatitude = Double.parseDouble(ponudaLat);
                                double ponudaLongitude = Double.parseDouble(ponudaLon);
                                gradKoordinate = new LatLng(ponudaLatitude, ponudaLongitude);

                                izracunato = izracunajOffset(ponudaLatitude, ponudaLongitude);
                                spremljeneKoordinate.add(izracunato);
                            } while (!spremljeneKoordinate.contains(izracunato));

                            mClusterManager.addItem(new MyItem(izracunato, ponuda));
                        }
                    }
                }
            }
        }catch (Exception ex){
            System.out.println("Mapa nije spremna");
        }
    }


    private LatLng izracunajOffset(double lat, double lon){
        //Earth’s radius, sphere
        double R = 6378137;

        Random rand = new Random();
        double n = rand.nextInt(50);

        Random rando = new Random();
        double n1 = rando.nextInt(50);

        //offsets in meters
        double dn = n;
        double de = n1;

        //Coordinate offsets in radians
        double dLat = dn/R;
        double dLon = de/(R*Math.cos(Math.PI*lat/180));

        //OffsetPosition, decimal degrees
        double noviLat = lat + dLat * 180/Math.PI;
        double noviLon = lon + dLon * 180/Math.PI;

        LatLng vrati = new LatLng(noviLat, noviLon);
        return vrati;
    }

    //upozorenej da je iskljucen gps
    private void GPSupozorenje(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS nije omogućen. Želite li ga omogućiti?")
                .setCancelable(false)
                .setPositiveButton("Omogući GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Odustani",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    //kraj alerta
}


