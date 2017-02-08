package hr.foi.air.dajsve.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import entities.Grad;
import entities.Kategorija;
import entities.Ponuda;
import hr.foi.air.core.DataLoadedListener;
import hr.foi.air.core.DataLoader;
import hr.foi.air.dajsve.Fragments.FavoritiFragment;
import hr.foi.air.dajsve.Fragments.MapFragment;
import hr.foi.air.dajsve.Fragments.MojeKategorijeFragment;
import hr.foi.air.dajsve.Fragments.PocetnaFragment;
import hr.foi.air.dajsve.Fragments.PretraživanjeFragment;
import hr.foi.air.dajsve.Helpers.Baza;
import hr.foi.air.dajsve.Helpers.Factory;
import hr.foi.air.dajsve.Helpers.SearchDataInterface;
import hr.foi.air.dajsve.Loaders.DatabaseDataLoader;
import hr.foi.air.dajsve.Loaders.WebServiceDataLoader;
import hr.foi.air.dajsve.R;

public class MainActivity extends AppCompatActivity implements DataLoadedListener {

    ListView listView;
    ArrayAdapter<String> listAdapter;
    String arrayFragment[] = {"Ponude", "Spremljene ponude", "Moje kategorije", "Mapa"};
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerListener;
    private String android_id;
    List<Grad> gradLista = null;
    Boolean adminPrijavljen = true;
    List<Ponuda> ponudaLista = null;
    private String uneseniUpit;
    private Boolean internetDostupan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

            SharedPreferences.Editor spref = getSharedPreferences("LOGGED", Context.MODE_PRIVATE).edit();
        spref.putBoolean("logged", false);
        spref.commit();

        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();

        SharedPreferences.Editor zadnjiUlazPref = getSharedPreferences("ULAZ", Context.MODE_PRIVATE).edit();
        zadnjiUlazPref.putLong("vrijeme", millis);
        zadnjiUlazPref.commit();

        android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);

        SharedPreferences.Editor editor = getSharedPreferences("ANDROID", MODE_PRIVATE).edit();
        editor.putString("android_id", android_id);
        editor.commit();

        SharedPreferences prefLogged = getSharedPreferences("LOGGED", Context.MODE_PRIVATE);
        adminPrijavljen = prefLogged.getBoolean("logged", true);



        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FlowManager.init(new FlowConfig.Builder(this).build());

        ActionBar ab = getSupportActionBar();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SharedPreferences prefs = getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        try{
            if(prefs != null){
                String android_id = prefs.getString("android_id", null);
                Baza baza = new Baza(android_id);
                baza.ZapisiUDnevnik(9, android_id, "Korisnik je pokrenuo aplikaciju", "", 1);
            }
        }catch (Exception ex){
            System.out.println("Exception");
        }


        listView = (ListView) findViewById(R.id.listview);
        listAdapter = new ArrayAdapter<String>(this, R.layout.textview_item, arrayFragment);
        listView.setAdapter(listAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


        //klasa za otvaranje i zatvaranje drawera
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,null, R.string.open_drawer,R.string.close_drawer )
        {
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadData();

        //postavljanje početnog fragmenta glavne aktivnosti
        Fragment home = new PocetnaFragment();
        FragmentManager fragmento = getSupportFragmentManager();
        fragmento.beginTransaction()
                .replace(R.id.linearlayout, home)
                .commit();
        //!!!kraj postavljanje početnog fragmenta glavne aktivnosti

        //postavljanje listenera za klik na item u meniju
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                switch (position){
                    case 0:
                        fragment = new PocetnaFragment();
                        break;
                    case 1:
                        fragment = new FavoritiFragment();
                        break;
                    case 2:
                        fragment = new MojeKategorijeFragment();
                        break;
                    case 3:
                        fragment = new MapFragment();
                        break;
                    default:
                        fragment = new PocetnaFragment();
                        break;

                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.linearlayout, fragment).commit();
                drawerLayout.closeDrawers();
            }



        });
        //!!!kraj postavljanje listenera za klik na item u meniju

//        if(adminPrijavljen){
//            button.setVisibility(View.VISIBLE);
//        }else {
//            button.setVisibility(View.GONE);
//        }

    }

    //Nekon submit-a u tražilici, uneseni upit se sprema u string uneseniUpit i otvara se fragment PretraživanjeFragment
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                uneseniUpit = query;

                SharedPreferences prefs = getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
                String android_id = prefs.getString("android_id", null);
                Baza baza = new Baza(android_id);
                baza.ZapisiUDnevnik(4, android_id, "Pretraga", query, 1);

                List<String> naziviPonude = new ArrayList<>();
                List<String> hashPonude = new ArrayList<>();
                List<String> hashPonudeLokacija = new ArrayList<>();
                List<Double> latPonude = new ArrayList<>();
                List<Double> longPonude = new ArrayList<>();
                for(Ponuda ponuda : Ponuda.getAll())
                {
                    naziviPonude.add(ponuda.getNaziv());
                    hashPonude.add(ponuda.getHash());
                    if(!ponuda.getLatitude().equals("nema")){
                        if(!ponuda.getLongitude().equals("nema")) {
                            latPonude.add(Double.parseDouble(ponuda.getLatitude()));
                            longPonude.add(Double.parseDouble(ponuda.getLongitude()));
                            hashPonudeLokacija.add(ponuda.getHash());
                        }
                    }
                }


                SearchDataInterface pk = Factory.Create(0);
                List<String> aaha = pk.DohvatiSve(uneseniUpit, naziviPonude,hashPonude, null, null, null, 0);


                Fragment fragment = null;

                Bundle bundle = new Bundle();

                bundle.putStringArrayList("AAA", (ArrayList<String>) aaha);

                searchView.clearFocus();
                fragment = new PretraživanjeFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.linearlayout, fragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }


    //kada kliknem na hamburger
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void loadData() {
        System.out.println("KOja je dretva: "+ Looper.myLooper());
        System.out.println("Poziva se funkcija za dohvat podataka");
        DataLoader dataLoader;

        SharedPreferences zadnjiUlazPref = getSharedPreferences("ULAZ", Context.MODE_PRIVATE);
        long startTime = zadnjiUlazPref.getLong("vrijeme", 0);
        long endTime = System.currentTimeMillis();

        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress("www.google.com",80);
        try {
            sock.connect(addr,3000);
            internetDostupan = true;
        } catch (IOException e) {
            internetDostupan = false;
        } finally {
            try {sock.close();}
            catch (IOException e) {}
        }


        long millis = endTime - startTime;

        if(((Ponuda.getAll().isEmpty() || Grad.getAll().isEmpty()) || Looper.myLooper()!=Looper.getMainLooper() || millis > 86400000) && internetDostupan==true){
            System.out.println("Dohvaćamo web podatke");
            Toast.makeText(this, "Dohvaćamo podatke s weba", Toast.LENGTH_LONG).show();
            dataLoader = new WebServiceDataLoader();
        } else {
            System.out.println("Dohvaćamo lokalne podatke");
            Toast.makeText(this, "Dohvaćamo podatke lokalno", Toast.LENGTH_LONG).show();
            dataLoader = new DatabaseDataLoader();
        }

        dataLoader.loadData(this);
    }


    @Override
    public void onDataLoaded(List<Grad> gradovi, List<Ponuda> ponude, List<Kategorija> kategorije) {

        if(  Looper.myLooper() == Looper.getMainLooper()) {
            ArrayAdapter<String> adapterGradovi;
            List<String> listaGradova = new ArrayList<>();
            ArrayList<Ponuda> ponudaArrayList = new ArrayList<Ponuda>();

            ponudaLista = ponude;
            gradLista = gradovi;

            for (Grad grad : gradovi) {
                listaGradova.add(grad.getNaziv());
            }
            for (Ponuda ponuda : ponude) {
                ponudaArrayList.add(ponuda);
            }
            adapterGradovi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaGradova);
        }

    }



}
