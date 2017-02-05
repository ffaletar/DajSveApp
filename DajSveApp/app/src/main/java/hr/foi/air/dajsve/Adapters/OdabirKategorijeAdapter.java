package hr.foi.air.dajsve.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entities.Kategorija;
import entities.OmiljenaKategorija;

/**
 * Created by Filip on 13.12.2016..
 */

public class OdabirKategorijeAdapter extends RecyclerView.Adapter<OdabirKategorijeAdapter.OdabirKategorijeViewHolder> {
    public static class OdabirKategorijeViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView kategorijaNaziv;
        Button gumbDalje;
        CheckBox cb;
        public RecyclerView rv;

        OdabirKategorijeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(hr.foi.air.dajsve.R.id.card_view);
            kategorijaNaziv = (TextView)itemView.findViewById(hr.foi.air.dajsve.R.id.kategorija_naziv);
            gumbDalje = (Button) itemView.findViewById(hr.foi.air.dajsve.R.id.button_dalje);
            cb = (CheckBox) itemView.findViewById(hr.foi.air.dajsve.R.id.odabir_kategorije_checkbox);

            rv = (RecyclerView) itemView.findViewById(hr.foi.air.dajsve.R.id.rv_kategorije);


        }
    }

    List<Kategorija> kategorije;
    Context context;
    public OdabirKategorijeAdapter(List<Kategorija> kategorije,Context context){
        this.kategorije = kategorije;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public OdabirKategorijeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(hr.foi.air.dajsve.R.layout.odabir_kategorije_item, parent, false);
        OdabirKategorijeViewHolder okvh = new OdabirKategorijeViewHolder(v);
        return okvh;
    }

    @Override
    public void onBindViewHolder(OdabirKategorijeViewHolder holder, int i) {

        holder.kategorijaNaziv.setText(kategorije.get(i).getNaziv());
        holder.cb.setText(kategorije.get(i).getNaziv());
        holder.cb.setLabelFor(hr.foi.air.dajsve.R.id.kategorija_naziv);
        holder.cb.setTextSize(0);

        final CheckBox checkBox = holder.cb;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    System.out.println("Tekst stisnutog checkboxa je " + checkBox.getText());

                    ViewGroup row = (ViewGroup) checkBox.getParent();
                    TextView textView = (TextView) row.findViewById(hr.foi.air.dajsve.R.id.kategorija_naziv);

                    OmiljenaKategorija novaOmiljenaKategorija = new OmiljenaKategorija((String)textView.getText());
                    novaOmiljenaKategorija.save();

                    System.out.println(novaOmiljenaKategorija.getNaziv() + " dodana");

                    List<OmiljenaKategorija> lista = OmiljenaKategorija.getAll();
                    for(OmiljenaKategorija omiljenaKategorija : lista){
                        System.out.println("Ispis svih kategorija: " + omiljenaKategorija.getNaziv());
                    }
                }else{
                    ViewGroup row = (ViewGroup) checkBox.getParent();
                    TextView textView = (TextView) row.findViewById(hr.foi.air.dajsve.R.id.kategorija_naziv);

                    OmiljenaKategorija novaOmiljenaKategorija = new OmiljenaKategorija((String)textView.getText());
                    novaOmiljenaKategorija.delete();



                    System.out.println(novaOmiljenaKategorija.getNaziv() + " izbrisana");

                    List<OmiljenaKategorija> lista = OmiljenaKategorija.getAll();
                    for(OmiljenaKategorija omiljenaKategorija : lista){
                        System.out.println("Ispis svih kategorija: " + omiljenaKategorija.getNaziv());
                    }
                }

            }
        });


        final ArrayList<Kategorija> kategorijaArrayList = new ArrayList<Kategorija>();
        kategorijaArrayList.add(kategorije.get(i));
    }


    @Override
    public int getItemCount() {
        return kategorije.size();
    }

}
