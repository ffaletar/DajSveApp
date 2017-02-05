package hr.foi.air.dajsve.Loaders;

import hr.foi.air.core.DataLoadedListener;
import hr.foi.air.core.DataLoader;

import entities.Grad;
import entities.Kategorija;
import entities.Ponuda;

/**
 * Created by Filip on 20.11.2016..
 */

public class DatabaseDataLoader extends DataLoader {

    @Override
    public void loadData(DataLoadedListener dataLoadedListener){
        super.loadData(dataLoadedListener);
        try{
            gradovi = Grad.getAll();
            ponude = Ponuda.getAll();
            kategorije = Kategorija.getAll();

            mDataLoadedListener.onDataLoaded(gradovi, ponude, kategorije);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
