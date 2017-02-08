package hr.foi.air.dajsve.Adapters;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hr.foi.air.dajsve.Fragments.DetaljiPonudeFragment;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import android.content.Context;

import entities.Ponuda;

/**
 * Created by Helena on 12.11.2016..
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PonudeViewHolder> {
    public static class PonudeViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView ponudaNaziv;
        TextView ponudaCijena;
        TextView ponudaStaraCijena;
        TextView ponudaPopust;
        ImageView ponudaSlika;
        ImageView ponudivacLogo;
        ImageView sss;

        PonudeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(hr.foi.air.dajsve.R.id.card_view);
            ponudaNaziv = (TextView)itemView.findViewById(hr.foi.air.dajsve.R.id.ponuda_name);
            ponudaCijena = (TextView)itemView.findViewById(hr.foi.air.dajsve.R.id.ponuda_cijena);
            ponudaSlika = (ImageView)itemView.findViewById(hr.foi.air.dajsve.R.id.ponuda_image);
            ponudaStaraCijena = (TextView)itemView.findViewById(hr.foi.air.dajsve.R.id.stara_cijena);
            ponudaPopust = (TextView) itemView.findViewById(hr.foi.air.dajsve.R.id.popust);
            ponudivacLogo = (ImageView) itemView.findViewById(hr.foi.air.dajsve.R.id.ponudivacLogo);
            sss = (ImageView) itemView.findViewById(hr.foi.air.dajsve.R.id.sss);
        }
    }


    List<Ponuda> ponuda;
    Context context;
    public RVAdapter(List<Ponuda> ponuda,Context context){
        this.ponuda = ponuda;
        this.context=context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PonudeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(hr.foi.air.dajsve.R.layout.sve_ponude_item, viewGroup, false);
        PonudeViewHolder pvh = new PonudeViewHolder(v);
        return pvh;
    }



    @Override
    public void onBindViewHolder(PonudeViewHolder PonudeViewHolder, int i) {
        PonudeViewHolder.ponudaNaziv.setText(ponuda.get(i).getNaziv());
        PonudeViewHolder.ponudaCijena.setText(ponuda.get(i).getCijena()+ " kuna");
        PonudeViewHolder.ponudaStaraCijena.setText(ponuda.get(i).getCijenaOriginal()+ " kuna");
        PonudeViewHolder.ponudaPopust.setText(ponuda.get(i).getPopust() + "%");
        if(ponuda.get(i).getKategorija().toLowerCase().contains("novo") == true){
            Picasso.with(context).load(hr.foi.air.dajsve.R.drawable.novo).into(PonudeViewHolder.sss);
            PonudeViewHolder.sss.bringToFront();
        }else{
            Picasso.with(context).load(hr.foi.air.dajsve.R.drawable.transparent).into(PonudeViewHolder.sss);
        }

        Picasso.with(context).load(ponuda.get(i).getURL()).into(PonudeViewHolder.ponudaSlika);
        Picasso.with(context).load(ponuda.get(i).getUrlLogo()).into(PonudeViewHolder.ponudivacLogo);

        final ArrayList<Ponuda> ponudaArrayList=new ArrayList<Ponuda>();
        ponudaArrayList.add(ponuda.get(i));

        //funkcija za strikethrough preko teksta
        PonudeViewHolder.ponudaStaraCijena.setPaintFlags(PonudeViewHolder.ponudaStaraCijena.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        PonudeViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment detaljiponude = new DetaljiPonudeFragment();
                Bundle bundle = new Bundle();
                activity.getSupportFragmentManager().beginTransaction();
                bundle.putParcelableArrayList("ponuda", ponudaArrayList);
                detaljiponude.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(hr.foi.air.dajsve.R.id.linearlayout, detaljiponude).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return ponuda.size();
    }
}
