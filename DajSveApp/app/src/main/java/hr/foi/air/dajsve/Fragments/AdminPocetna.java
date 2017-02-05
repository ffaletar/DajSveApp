package hr.foi.air.dajsve.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import entities.Kategorija;
import hr.foi.air.dajsve.R;

/**
 * Created by Filip on 27.1.2017..
 */

public class AdminPocetna extends Fragment {

    public String[] kategorije ;
    public List<String> oznaceneKategorije;
    public  boolean[] oznaceneKategorijeDialog;
    Button urediOmiljeneKategorije;

    private FragmentTabHost mTabHost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_pocetna_fragment, container, false);
        final AlertDialog.Builder ad =  new AlertDialog.Builder(getActivity());


        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("statistika").setIndicator("Statistika"),
                AdminStatistikaFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("facebook").setIndicator("Facebook"),
                AdminFacebookFragment.class, null);

        return rootView;
    }
}
