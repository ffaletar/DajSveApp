package entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Filip on 11.12.2016..
 */
@Table(database = MainDatabase.class)
public class Kategorija extends BaseModel {

    @Column
    @PrimaryKey int id;
    @Column String naziv;
    @Column String naslov;
    @Column String tag;

    public Kategorija() {
    }

    public Kategorija(int id, String tag, String naziv, String naslov) {
        this.id = id;
        this.tag = tag;
        this.naziv = naziv;
        this.naslov = naslov;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public static List<Kategorija> getAll(){
        List<Kategorija> kategorijaList;
        kategorijaList = new Select().from(Kategorija.class).queryList();
        return kategorijaList;
    }
    public static void deleteAll(){
        new Delete().from(Kategorija.class).execute();
    }

}
