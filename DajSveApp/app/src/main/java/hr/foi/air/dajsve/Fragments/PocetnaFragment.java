package hr.foi.air.dajsve.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import entities.Kategorija;
import entities.OmiljenaKategorija;
import entities.Ponuda;
import hr.foi.air.dajsve.Helpers.Baza;
import hr.foi.air.dajsve.R;

/**
 * Created by Filip on 19.12.2016..
 */

public class PocetnaFragment extends Fragment{
    public String[] kategorije ;
    public  boolean[] oznaceneKategorijeDialog;
    Button urediOmiljeneKategorije;

    private FragmentTabHost mTabHost;

    //Mandatory Constructor
    public PocetnaFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pocetna_fragment, container, false);
        final AlertDialog.Builder ad =  new AlertDialog.Builder(getActivity());
        kategorije = new String[Kategorija.getAll().size()];
        oznaceneKategorijeDialog = new boolean[Kategorija.getAll().size()];
        int i=0;


        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("noveponude").setIndicator("Nove ponude"),
                NovePonudeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("sveponude").setIndicator("Sve ponude"),
                SvePonudeFragment.class, null);




        Boolean prvoPokretanje = this.getActivity().getSharedPreferences("PRVO_POKRETANJE", Context.MODE_PRIVATE).getBoolean("prvoPokretanje", true);

        if(prvoPokretanje){
            SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
            String android_id = prefs.getString("android_id", null);
            Baza baza = new Baza(android_id);

            baza.ZapisiUDnevnik(8, android_id, "Korisnik je pokrenuo aplikaciju prvi put", "",1);

            Toast.makeText(this.getActivity(), "Ovo je prvo pokretanje, prikazujem dialog", Toast.LENGTH_LONG).show();
            for(Kategorija kategorija : Kategorija.getAll())
            {
                kategorije[i]= kategorija.getNaziv();
                i++;
            }
            for(int j= 0;j<kategorije.length;j++)
            {
                for(OmiljenaKategorija a : OmiljenaKategorija.getAll())
                {
                    System.out.print("PRINT : "+a.getNaziv().equals(kategorije[j]));
                    if(a.getNaziv().equals(kategorije[j]))
                    {
                        oznaceneKategorijeDialog[j]=true;
                    }
                }
            }
            ad.setTitle("ODABERITE KATEGORIJU");
            ad.setMultiChoiceItems(kategorije, oznaceneKategorijeDialog, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    Toast.makeText(getContext(), kategorije[which], Toast.LENGTH_SHORT).show();
                    for(boolean aa : oznaceneKategorijeDialog)
                    {
                        System.out.println(aa);
                    }
                }
            });
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    OmiljenaKategorija.deleteAll();
                    for(int i = 0 ; i<kategorije.length;i++)
                    {
                        if(oznaceneKategorijeDialog[i]==true) {
                            OmiljenaKategorija novi = new OmiljenaKategorija(kategorije[i]);
                            novi.save();
                            System.out.println(kategorije[i]);
                        }

                    }
                    List<Ponuda> ponudePoKategoriji;
                    ponudePoKategoriji = new ArrayList<Ponuda>();
                    for (OmiljenaKategorija a : OmiljenaKategorija.getAll()){
                        ponudePoKategoriji.addAll(Ponuda.getByFavoriteCategory(a.getNaziv()));
                    }

                }
            });

            ad.setItems(kategorije,null);

            ad.show();

            this.getActivity().getSharedPreferences("PRVO_POKRETANJE", Context.MODE_PRIVATE).edit().putBoolean("prvoPokretanje", false).commit();
        }else{
            Toast.makeText(this.getActivity(), "Ovo nije prvo pokretanje, prikazujem fragment", Toast.LENGTH_LONG).show();
        }


        return rootView;
    }

}
