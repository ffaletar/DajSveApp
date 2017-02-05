package entities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

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
public class Ponuda extends BaseModel implements Parcelable{

    @Column
    @PrimaryKey int id;
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
    @Column  String latitude;
    @Column  String longitude;


    public Ponuda() {
    }

    public Ponuda(int id,String hash, String tekstPonude, int cijena, int popust, int cijenaOriginal, String urlSlike, String urlLogo, String urlWeba, int usteda, String kategorija, String grad, String datumPonude,String latitude, String longitude){
        this.id=id;
        this.hash =hash;
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
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNaziv() {
        return tekstPonude;
    }
    public String getCijena() {
        return Integer.toString(cijena);
    }
    public String getURL() {
        return urlSlike;
    }



    public void setCijena(int cijena) {
        this.cijena = cijena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTekstPonude() {
        return tekstPonude;
    }

    public void setTekstPonude(String tekstPonude) {
        this.tekstPonude = tekstPonude;
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

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {this.longitude = longitude;}

    public static List<Ponuda> getAll(){
        List<Ponuda> ponudaList;
        ponudaList= new Select().from(Ponuda.class).queryList();
        return ponudaList;
    }

    public static List<Ponuda> getAllOrderBy(String order, boolean asc){
        List<Ponuda> ponudaList = null;
        if(order.equals("cijena")){
            ponudaList = new Select().from(Ponuda.class).orderBy(Ponuda_Table.cijena, asc).queryList();
        } else if(order.equals("popust")){
            ponudaList = new Select().from(Ponuda.class).orderBy(Ponuda_Table.popust, asc).queryList();
        }
        return ponudaList;
    }

    public static List<Ponuda> getNew(){
        List<Ponuda> ponudaList;
        ponudaList = new Select().from(Ponuda.class).where(Ponuda_Table.kategorija.like("%Novo%")).queryList();
        return ponudaList;
    }

    public static List<Ponuda> getNewOrderBy(String order, boolean asc){
        List<Ponuda> ponudaList = null;
        if(order.equals("cijena")){
            ponudaList = new Select().from(Ponuda.class).where(Ponuda_Table.kategorija.like("%Novo%")).orderBy(Ponuda_Table.cijena, asc).queryList();
        } else if(order.equals("popust")){
            ponudaList = new Select().from(Ponuda.class).where(Ponuda_Table.kategorija.like("%Novo%")).orderBy(Ponuda_Table.popust, asc).queryList();
        }
        return ponudaList;
    }

    public static List<Ponuda> getByFavoriteCategory(String kategorija){
        List<Ponuda> ponudaList;
        ponudaList = new Select().from(Ponuda.class).where(Ponuda_Table.kategorija.like("%" + kategorija +"%")).queryList();
        return ponudaList;
    }

    public static void deleteAll(){
        new Delete().from(Ponuda.class).execute();
    }

    public Ponuda(Parcel in){
        String[] data = new String[9];

        in.readStringArray(data);
        this.id = in.readInt();
        this.tekstPonude = in.readString();
        this.cijena = in.readInt();
        this.popust = in.readInt();
        this.cijenaOriginal = in.readInt();
        this.urlSlike = in.readString();
        this.urlLogo = in.readString();
        this.urlWeba = in.readString();
        this.usteda = in.readInt();
        this.kategorija = in.readString();
        this.grad = in.readString();
        this.datumPonude = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tekstPonude);
        dest.writeInt(cijena);
        dest.writeInt(popust);
        dest.writeInt(cijenaOriginal);
        dest.writeString(urlSlike);
        dest.writeString(urlLogo);
        dest.writeString(urlWeba);
        dest.writeInt(usteda);
        dest.writeString(kategorija);
        dest.writeString(grad);
        dest.writeString(datumPonude);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ponuda createFromParcel(Parcel in) {
            return new Ponuda(in);
        }

        public Ponuda[] newArray(int size) {
            return new Ponuda[size];
        }
    };

}
