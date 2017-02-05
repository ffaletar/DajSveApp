package entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Filip on 13.12.2016..
 */
@Table(database = MainDatabase.class)
public class OmiljenaKategorija extends BaseModel {

    @Column @PrimaryKey
    String naziv;

    public OmiljenaKategorija() {
    }

    public OmiljenaKategorija(String naziv) {
//        this.id = id;
        this.naziv = naziv;
    }


    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


    public static List<OmiljenaKategorija> getAll(){
        List<OmiljenaKategorija> omiljenaKategorijaList;
        omiljenaKategorijaList = new Select().from(OmiljenaKategorija.class).queryList();

        return omiljenaKategorijaList;
    }
    public static void deleteAll(){
        new Delete().from(OmiljenaKategorija.class).execute();
    }

}
