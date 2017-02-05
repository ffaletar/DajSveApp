package entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import entities.MainDatabase;

/**
 * Created by Filip on 8.11.2016..
 */
@Table(database = MainDatabase.class)

public class Grad extends BaseModel{
    @Column
    @PrimaryKey int id;
    @Column String naziv;


    public Grad() {
    }

    public Grad(int id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public static List<Grad> getAll(){
        List<Grad> gradoviList;
        gradoviList= new  Select().from(Grad.class).queryList();

        return gradoviList;
    }
}
