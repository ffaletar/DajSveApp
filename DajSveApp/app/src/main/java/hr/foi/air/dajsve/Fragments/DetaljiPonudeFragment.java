package hr.foi.air.dajsve.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Favorit;
import entities.Ponuda;
import hr.foi.air.dajsve.Adapters.RVAdapter;
import hr.foi.air.dajsve.Helpers.Baza;
import hr.foi.air.dajsve.R;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * Created by Helena on 23.11.2016..
 */

public class DetaljiPonudeFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = false;

    public Ponuda ponudaDohvacena;
    private ImageView ponudaSlika;
    private TextView ponudaNaziv;
    private TextView ponudaDescription;
    private TextView ponudaCijena;
    private TextView ponudaPopust;
    private TextView ponudaOriginal;
    private LinearLayout linkNaStranicu;
    private FrameLayout mapaPrikaz;
    private String name = "Map view";
    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map = null;
    public LinearLayout gumbDodajUFavorite;
    public boolean ponudaJeFavorit;
    public TextView dodajUFavoriteTekst;
    public ImageView dodajUFavoriteSlika;
    public LinearLayout detaljiPonudeStatistikaLayout;
    Context context;
    private ImageView prozirnaSlika;
    private ScrollView scroll;
    private TextView brojPregledaPonude;
    private TextView brojOmiljenihPonude;
    private TextView brojOtvaranjaNaWebuPonude;
    private LinearLayout grafDetalji;
    private ImageView ponudivacLogo;
    private BottomNavigationView bnw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(hr.foi.air.dajsve.R.layout.detalji_ponude_fragment, container, false);
        context = rootView.getContext();
        ponudaSlika=(ImageView)rootView.findViewById(hr.foi.air.dajsve.R.id.ponuda_image);
        ponudivacLogo=(ImageView)rootView.findViewById(R.id.ponudivacLogo);
        ponudaNaziv=(TextView) rootView.findViewById(hr.foi.air.dajsve.R.id.ponuda_name);
        linkNaStranicu = (LinearLayout) rootView.findViewById(hr.foi.air.dajsve.R.id.link_na_stranicu);
        ponudaCijena=(TextView)rootView.findViewById(hr.foi.air.dajsve.R.id.ponuda_cijena);
        ponudaPopust=(TextView)rootView.findViewById(hr.foi.air.dajsve.R.id.ponuda_popust);
        dodajUFavoriteTekst = (TextView) rootView.findViewById(hr.foi.air.dajsve.R.id.dodaj_u_favorite_text);
        dodajUFavoriteSlika = (ImageView) rootView.findViewById(hr.foi.air.dajsve.R.id.dodaj_brisi_favorita_slika);
        ponudaOriginal=(TextView)rootView.findViewById(hr.foi.air.dajsve.R.id.ponuda_original);
        gumbDodajUFavorite = (LinearLayout) rootView.findViewById(hr.foi.air.dajsve.R.id.dodaj_brisi_favorita);
        mapaPrikaz=(FrameLayout)rootView.findViewById(hr.foi.air.dajsve.R.id.mapa_prikaz);

        Bundle bundle = getArguments();
        List<Favorit> favoriti= Favorit.getAll();
        ArrayList<Ponuda> listaDohvacena = bundle.getParcelableArrayList("ponuda");
        ponudaDohvacena = listaDohvacena.get(0);
        ponudaDohvacena.getKategorija();
        Picasso.with(context).load(ponudaDohvacena.getURL()).into(ponudaSlika);
        ponudaNaziv.setText(ponudaDohvacena.getNaziv());
        ponudaCijena.setText(ponudaDohvacena.getCijena() + " kuna");
        ponudaPopust.setText((Integer.toString(ponudaDohvacena.getPopust()) + "%"));
        Picasso.with(context).load(ponudaDohvacena.getUrlLogo()).into(ponudivacLogo);

        //strike preko stare cijene
        ponudaOriginal.setPaintFlags(ponudaOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        ponudaOriginal.setText((Integer.toString(ponudaDohvacena.getCijenaOriginal()) + " kuna"));

        prozirnaSlika = (ImageView) rootView.findViewById(hr.foi.air.dajsve.R.id.prozirnaslika);
        scroll = (ScrollView) rootView.findViewById(hr.foi.air.dajsve.R.id.skrolanje);

        //dohvaca se id androida i upisuje se zapis u dnevnik korisnika
        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);
        String tekstPonude = ponudaDohvacena.getTekstPonude();
        baza.ZapisiUDnevnik(6, android_id, "Otvorena ponuda", ponudaDohvacena.getHash(), 1);

        Picasso.with(context).load(R.drawable.addfavorite).into(dodajUFavoriteSlika);

        prozirnaSlika.setOnTouchListener(new View.OnTouchListener() {@Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    scroll.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    scroll.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    scroll.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        }
        });

        linkNaStranicu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(ponudaDohvacena.getUrlWeba());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
                String android_id = prefs.getString("android_id", null);
                Baza baza = new Baza(android_id);
                String tekstPonude = ponudaDohvacena.getTekstPonude();
                baza.ZapisiUDnevnik(7, android_id, "Otvorena ponuda na webu", ponudaDohvacena.getHash(), 1);

                startActivity(intent);

            }
        });

        //provjera je li ponuda favorit, ako je mjenja boju u crveno i mijenja tekst gumba, ako nije onda u zeleno
        ponudaJeFavorit = false;
        dodajUFavoriteTekst.setText("Spremi ponudu");
        dodajUFavoriteSlika.setRotation(0);
//        gumbDodajUFavorite.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blaga_tamno_zelena));

        // Provjerava da li je trenutna ponuda favorit, ako je mjenja dizajn kao da je ponuda favorit
        for(Favorit favorit : favoriti){
            if(favorit.getHash().equals(ponudaDohvacena.getHash())){
                ponudaJeFavorit = true;
                dodajUFavoriteTekst.setText("Briši iz spremljenih ponuda");
                Picasso.with(context).load(R.drawable.deletefavorite).into(dodajUFavoriteSlika);
//                gumbDodajUFavorite.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blaga_crvena));
            }
        }

        // klik na gumb dodaj/ukloni favorit
        gumbDodajUFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Favorit> favoriti= Favorit.getAll();
                //ako je vec favorit,brišemo ga iz tablice Favorit
                if(ponudaJeFavorit){
                    Picasso.with(context).load(R.drawable.addfavorite).into(dodajUFavoriteSlika);

                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                    fadeIn.setDuration(1500);

                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                    fadeOut.setStartOffset(1500);
                    fadeOut.setDuration(1500);

                    AnimationSet animation = new AnimationSet(false); //change to false
                    animation.addAnimation(fadeOut);
                    animation.addAnimation(fadeIn);
                    dodajUFavoriteSlika.setAnimation(animation);
                    dodajUFavoriteTekst.setAnimation(animation);

                    Favorit.deleteFromHash(ponudaDohvacena.getHash());
                    ponudaJeFavorit = false;

                    Toast.makeText(getActivity(), "Ponuda izbrisana iz Omiljenih ponuda", Toast.LENGTH_LONG).show();

                    dodajUFavoriteTekst.setText("Spremi ponudu");

                    SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
                    String android_id = prefs.getString("android_id", null);
                    Baza baza = new Baza(android_id);
                    String tekstPonude = ponudaDohvacena.getTekstPonude();
                    baza.ZapisiUDnevnik(1, android_id, "Izbrisana omiljena ponuda", ponudaDohvacena.getHash(), 0);

                }else {
                    Picasso.with(context).load(R.drawable.deletefavorite).into(dodajUFavoriteSlika);

                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                    fadeIn.setDuration(1500);

                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                    fadeOut.setStartOffset(1500);
                    fadeOut.setDuration(1500);

                    AnimationSet animation = new AnimationSet(false); //change to false
                    animation.addAnimation(fadeOut);
                    animation.addAnimation(fadeIn);
                    dodajUFavoriteSlika.setAnimation(animation);
                    dodajUFavoriteTekst.setAnimation(animation);


                    Favorit novi = new Favorit(favoriti.size(),ponudaDohvacena.getHash(), true, ponudaDohvacena.getId(), ponudaDohvacena.getTekstPonude(),
                            Integer.parseInt(ponudaDohvacena.getCijena()), ponudaDohvacena.getPopust()
                            , ponudaDohvacena.getCijenaOriginal(),ponudaDohvacena.getUrlSlike(), ponudaDohvacena.getUrlLogo(), ponudaDohvacena.getUrlWeba(),
                            ponudaDohvacena.getUsteda(), ponudaDohvacena.getKategorija(), ponudaDohvacena.getGrad(), ponudaDohvacena.getDatumPonude());
                    novi.save();
                    Toast.makeText(getActivity(), "Ponuda spremljena u Omiljene ponude", Toast.LENGTH_LONG).show();


                    SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
                    String android_id = prefs.getString("android_id", null);
                    Baza baza = new Baza(android_id);
                    String tekstPonude = ponudaDohvacena.getTekstPonude();
                    baza.ZapisiUDnevnik(1, android_id, "Dodana omiljena ponuda", ponudaDohvacena.getHash(), 1);


                    ponudaJeFavorit = true;
                    dodajUFavoriteTekst.setText("Briši iz spremljenih ponuda");
                }
            }
        });

        //dodavanje google karte u layout
        View v = inflater.inflate(hr.foi.air.dajsve.R.layout.map_fragment, container, false);
        mapFragment = new com.google.android.gms.maps.MapFragment();
        mapFragment.getMapAsync(this);
        getActivity().getFragmentManager().beginTransaction().add(hr.foi.air.dajsve.R.id.frame, mapFragment).commit();
        mapaPrikaz.setFocusable(true);
        mapaPrikaz.addView(v);



        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(hr.foi.air.dajsve.R.drawable.popust_ic);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(hr.foi.air.dajsve.R.drawable.grad_ic);

        try {
            if (ponudaDohvacena.getLongitude().contentEquals("nema") || ponudaDohvacena.getLatitude().contentEquals("nema")){
                //Ako nema koordinata v xml-u onda ime grada u kojem je ponuda pretvara u koordinate
                Geocoder geocoder = new Geocoder(context);
                String grad = ponudaDohvacena.getGrad();
                List<Address> adresa;
                adresa = geocoder.getFromLocationName(grad, 1);
                double latitude= adresa.get(0).getLatitude();
                double longitude= adresa.get(0).getLongitude();
                LatLng gradKoordinate = new LatLng(latitude, longitude);

                Marker marker1=map.addMarker(new MarkerOptions()
                        .title("Lokacija ponude:")
                        .snippet("Grad:" +grad)
                        .position(gradKoordinate)
                        .icon(icon2)
                );


                marker1.showInfoWindow();

                CameraPosition cameraPosition = new CameraPosition.Builder().target(gradKoordinate).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }else{
                //Uzima koordinate koje su dostupne v xml-u
                double ponudaLatitude = Double.parseDouble(ponudaDohvacena.getLatitude());
                double ponudaLongitude = Double.parseDouble(ponudaDohvacena.getLongitude());
                LatLng gradKoordinate = new LatLng(ponudaLatitude, ponudaLongitude);

                Geocoder geocoder = new Geocoder(context);
                List<Address> adresa;
                adresa = geocoder.getFromLocation(ponudaLatitude, ponudaLongitude,1);
                String cityName = adresa.get(0).getAddressLine(0);
                String stateName = adresa.get(0).getAddressLine(1);
                String countryName = adresa.get(0).getAddressLine(2);
                String Locality = adresa.get(0).getLocality();

                Marker marker2=map.addMarker(new MarkerOptions()
                        .title("Lokacija: " + Locality)
                        .snippet("Adresa:" +cityName +" "+ stateName+ " " + countryName)
                        .position(gradKoordinate)
                        .icon(icon1));


                marker2.showInfoWindow();

                //Pozicionira i zumirana lokaciju od koordinata
                CameraPosition cameraPosition = new CameraPosition.Builder().target(gradKoordinate).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //funkcija koja prima generiranu liniju i stupce i radi graf
    private void generateData() {
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Dan");
                axisY.setName("Odnos pregleda ponude i otvaranja ponude na webu");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setComboLineColumnChartData(data);
    }

    //generiranje charta s podacima od ovog tjedna
    private ColumnChartData generateColumnData() {

        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);

        List<Integer> lista = baza.DohvatiStatistikuZaDetaljnuPonudu(ponudaDohvacena.getHash(), 6);

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < lista.size(); i++) {

            values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(lista.get(i), ChartUtils.COLOR_GREEN));

            columns.add(new Column(values));
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }

    //generiranje charta s podacima od proslog tjedna
    private LineChartData generateLineData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);

        List<Integer> lista = baza.DohvatiStatistikuZaDetaljnuPonudu(ponudaDohvacena.getHash(), 7);


        List<Line> lines = new ArrayList<Line>();

        for(int b=0; b<lista.size(); b++){
            List<PointValue> values = new ArrayList<PointValue>();

            values.add(new PointValue(b, lista.get(b)));

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_BLUE);
            line.setCubic(isCubic);
            line.setHasLabels(hasLabels);
            line.setHasLines(true);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }


        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;
    }
}
