package entities;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helena on 12.11.2016..
 */
@Table(database = MainDatabase.class)
public class Favorit extends BaseModel {

    @Column
    @PrimaryKey int id;
    @Column boolean aktivan;


    @Column int idPonuda;
    @Column String hash;
    @Column  String tekstPonude;
    @Column  int cijena;
    @Column  int popust;
    @Column  int cijenaOriginal;
    @Column  String urlSlike;
    @Column  String urlLogo;
    @Column  String urlWeba;
    @Column  int usteda;
    @Column  String kategorija;
    @Column  String grad;
    @Column  String datumPonude;

    public Favorit() {
    }

    public Favorit(int id,String hash, boolean aktivan, int idPonuda, String tekstPonude, int cijena, int popust, int cijenaOriginal, String urlSlike, String urlLogo, String urlWeba, int usteda, String kategorija, String grad, String datumPonude) {
        this.hash = hash;
        this.id = id;
        this.aktivan = aktivan;
        this.idPonuda = idPonuda;
        this.tekstPonude = tekstPonude;
        this.cijena = cijena;
        this.popust = popust;
        this.cijenaOriginal = cijenaOriginal;
        this.urlSlike = urlSlike;
        this.urlLogo = urlLogo;
        this.urlWeba = urlWeba;
        this.usteda = usteda;
        this.kategorija = kategorija;
        this.grad = grad;
        this.datumPonude = datumPonude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getisIdPonude() {
        return idPonuda;
    }

    public void setIdPonuda(int idPonuda) {
        this.idPonuda = idPonuda;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public String getHash() {        return hash;    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public String getTekstPonude() {
        return tekstPonude;
    }

    public void setTekstPonude(String tekstPonude) {
        this.tekstPonude = tekstPonude;
    }

    public int getCijena() {
        return cijena;
    }

    public void setCijena(int cijena) {
        this.cijena = cijena;
    }

    public int getPopust() {
        return popust;
    }

    public void setPopust(int popust) {
        this.popust = popust;
    }

    public int getCijenaOriginal() {
        return cijenaOriginal;
    }

    public void setCijenaOriginal(int cijenaOriginal) {
        this.cijenaOriginal = cijenaOriginal;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlWeba() {
        return urlWeba;
    }

    public void setUrlWeba(String urlWeba) {
        this.urlWeba = urlWeba;
    }

    public int getUsteda() {
        return usteda;
    }

    public void setUsteda(int usteda) {
        this.usteda = usteda;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDatumPonude() {
        return datumPonude;
    }

    public void setDatumPonude(String datumPonude) {
        this.datumPonude = datumPonude;
    }
    public static List<Favorit> getAll(){
        List<Favorit> ponudaList;
        ponudaList= new Select().from(Favorit.class).queryList();
        return ponudaList;
    }
    public static void deleteAll(){
        new Delete().from(Favorit.class).execute();
    }

    public static void deleteFromHash(String i)
    {
        new Delete().from(Favorit.class).where(Favorit_Table.hash.is(i)).execute();
    }



}
