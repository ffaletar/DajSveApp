package hr.foi.air.dajsve.Helpers;
import android.app.Activity;
import android.os.AsyncTask;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.provider.Settings.Secure;

import com.raizlabs.android.dbflow.sql.language.Condition;


/**
 * Created by Filip on 24.1.2017..
 */

public class Baza extends Activity{
    Connection connection = null;
    String className = "net.sourceforge.jtds.jdbc.Driver";
    public String android_id = "";

    public Baza() {
    }

    public Baza(String android_id) {
        try {
            /* Povezivanje na bazu sa jtdsom 1.3.0
            85.94.77.105
            DB = dajsveandroid
            User = dajsveappuser, Pass = Pa55word
             10.0.3.2 - Genymotion
                 */
            this.android_id = android_id;

            Class.forName(className).newInstance();
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://85.94.77.105;databaseName=dajsveandroid;user=dajsveappuser;password=Pa55word!1234;");
            System.out.print("Uspjesno spojeni na bazu");
            Statement stmt = connection.createStatement();
            ResultSet reset = stmt.executeQuery("select * from Korisnik");

            if (!reset.isBeforeFirst() ) {
                System.out.println("nema podataka");
            }else{
                System.out.println("ima podataka");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Greska pri spajanju na bazu");
        } catch (ClassNotFoundException e){
            System.out.print("Greska - klasa nije pronađena");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public ResultSet IzvrsiUpit(String upit) throws SQLException {
        ResultSet reset = null;
        if(connection!=null) {
            Statement stmt = connection.createStatement();
            reset = stmt.executeQuery(upit);
        }
        return reset;
    }

    public void Upisi(String upit) throws SQLException {
        if(connection!=null){
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(upit);
        }
    }

    public boolean Prijava(String nadimak, String lozinka) throws SQLException {
        try{
            String upit = "select * from Korisnik where nadimak ='" + nadimak + "' and lozinka ='" + lozinka +"';";

            ResultSet reset = IzvrsiUpit(upit);

            if (!reset.isBeforeFirst() ) {
                ZapisiUDnevnik(5, android_id, "Neuspješna prijava", nadimak, 1);
                return false;
            }else{
                ZapisiUDnevnik(5, android_id, "Uspješna prijava", nadimak, 1);
                return true;
            }
        }catch (SQLException ex){
            ZapisiUDnevnik(5, android_id, "Neuspješna prijava", nadimak, 1);
            return false;
        }
    }

    public void ZapisiUDnevnik(int tipZapisa, String korisnikID, String opis, String dodatneInformacije, int status){
        String upit = "insert into Dnevnik(tipZapisa, korisnikID,opis,vrijeme,dodatneInformacije,status) values(" + tipZapisa +",'"+korisnikID+"','"+opis+"','"+trenutnoVrijeme()+"','"+dodatneInformacije+"',"+status+");";
        try{
            Upisi(upit);
        }catch (SQLException ex){
            System.out.println("Zapis nije zapisan u dnevnik");
        }
    }


    public static Timestamp trenutnoVrijeme() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }

    //funkcija za dohvaćanje statistike za određenu ponudu
    public Integer DohvatiBrojOtvaranjaPonude(int tipZapisa,String hash){
        try{
//            String upit = "select count(*) from Dnevnik where tipZapisa = " + tipZapisa + " and dodatneInformacije ='" + nazivPonude +"';";
            String upit2 = "select count(*) as broj from Dnevnik GROUP BY dodatneInformacije, tipZapisa having tipZapisa = "+tipZapisa+" and dodatneInformacije = '"+hash+"';";
            ResultSet reset = IzvrsiUpit(upit2);
            int brojOtvaranja = 0;
            if(reset!=null){
                while(reset.next()){
                    brojOtvaranja = reset.getInt("broj");
                }
            }

            return brojOtvaranja;
        }catch (SQLException ex){
            return 0;
        }
    }

    public Integer DohvatiLajkoveNaPonudu(String hash){
        try{
//            String upit = "select count(*) from Dnevnik where tipZapisa = " + tipZapisa + " and dodatneInformacije ='" + nazivPonude +"';";
            String upit1 = "select count(*) as broj from Dnevnik GROUP BY dodatneInformacije, tipZapisa, status having tipZapisa = 1 and dodatneInformacije = '"+hash+"' and status = 1;";
            String upit2 = "select count(*) as broj from Dnevnik GROUP BY dodatneInformacije, tipZapisa, status having tipZapisa = 1 and dodatneInformacije = '"+hash+"' and status = 0;";
            ResultSet resetOmiljenih = IzvrsiUpit(upit1);
            ResultSet resetBrisanja = IzvrsiUpit(upit2);
            int brojOmiljenih = 0;
            int brojBrisanja = 0;
            if(resetOmiljenih!=null){
                while(resetOmiljenih.next()){
                    brojOmiljenih = resetOmiljenih.getInt("broj");
                }
            }

            if(resetBrisanja!=null){
                while(resetBrisanja.next()){
                    brojBrisanja = resetBrisanja.getInt("broj");
                }
            }

            return brojOmiljenih - brojBrisanja;
        }catch (SQLException ex){
            return 0;
        }
    }

    public Integer DohvatiBrojPreuzimanjaAplikacije(){
        try{
            String upit2 = "select count(*) as broj from Dnevnik where tipZapisa = 8;";
            ResultSet reset = IzvrsiUpit(upit2);
            int brojPreuzimanja = 0;
            if(reset!=null){
                while(reset.next()){
                    brojPreuzimanja = reset.getInt("broj");
                }
            }

            return brojPreuzimanja;
        }catch (SQLException ex){
            return 0;
        }

    }

    public Integer DohvatiBrojOmiljenihPonuda(){
        try{
            String upit2 = "select count(*) as broj from Dnevnik where tipZapisa = 1;";
            ResultSet reset = IzvrsiUpit(upit2);
            int brojOmiljenih = 0;
            if(reset!=null){
                while(reset.next()){
                    brojOmiljenih = reset.getInt("broj");
                }
            }

            return brojOmiljenih;
        }catch (SQLException ex){
            return 0;
        }

    }

    public Integer DohvatiBrojOmiljenihKategorija(){
        try{
            String upit2 = "select count(*) as broj from Dnevnik where tipZapisa = 2;";
            ResultSet reset = IzvrsiUpit(upit2);
            int brojOmiljenihKategorija = 0;
            if(reset!=null){
                while(reset.next()){
                    brojOmiljenihKategorija = reset.getInt("broj");
                }
            }

            return brojOmiljenihKategorija;
        }catch (SQLException ex){
            return 0;
        }

    }

    public List<Integer> DohvatiBrojPokretanjaZaDan(int a){
        int brojPokretanja = 0;
        String upit1;
        String upit2;
        ResultSet reset;
        List<Integer> lista = new ArrayList<Integer>();
        try{
            if(a == 0){
                upit1 = "select count(*) as count from Dnevnik where tipZapisa = 9 and vrijeme > getdate() - 7 group by  CAST(vrijeme as date);";
                reset = IzvrsiUpit(upit1);
            }else{
                upit2 = "select count(*) as count from Dnevnik where tipZapisa = 9 and vrijeme >= getdate() - 14 and vrijeme < getdate() - 7 group by  CAST(vrijeme as date);";
                reset = IzvrsiUpit(upit2);
            }
            if(reset!=null){
                while(reset.next()){
                    lista.add(reset.getInt("count"));
                }
            }
            return lista;
        }catch (SQLException ex){
            return lista;
        }

    }

    public void IzbrisiSveOmiljeneKategorijeZaKorisnika(String korisnikID) {
        String upit = "delete from Dnevnik where korisnikID = '"+korisnikID+"' and tipZapisa = 2";
        try{
            Upisi(upit);
        }catch (SQLException ex){
            System.out.println("Dnevnik nije ažuriran");
        }
    }

    public List<String> DohvatiNajcescePretrage(){
        String upit = "select top 5 count(*) as zbroj, dodatneInformacije from Dnevnik where tipZapisa = 4  group by Dnevnik.dodatneInformacije order by zbroj DESC";

        ResultSet reset = null;
        List<String> listaQuerya = new ArrayList<>();
        try{
            reset = IzvrsiUpit(upit);
            if(reset!=null){
                while(reset.next()){
                    listaQuerya.add(reset.getString("dodatneInformacije"));
                }
            }
            return listaQuerya;
        }catch (SQLException ex){
            System.out.println("Nije uspio dohvat najučestalijih pretraživanja");
        }

        return null;
    }


    public List<Integer> DohvatiStatistikuZaDetaljnuPonudu(String ponudaHash, int tipZapisa){
        String upit = "declare @start datetime = CAST(getdate() as date)\n" +
                "declare @end  datetime = dateadd(day, -10, @start)\n" +
                "\n" +
                ";with amonth(day) as\n" +
                "(\n" +
                "   select @end as day\n" +
                "       union all\n" +
                "   select day + 1\n" +
                "       from amonth\n" +
                "       where day < CAST(@start as date)\n" +
                ")\n" +
                "select count(vrijeme) as c \n" +
                "from amonth \n" +
                "left join Dnevnik on CAST(vrijeme as date) = CAST(amonth.day as date) \n" +
                "                     and tipZapisa = " + tipZapisa + " and dodatneInformacije = '" + ponudaHash + "'\n" +
                "group by CAST(amonth.day as date)";

        ResultSet rs = null;


        List<Integer> listaQuerya = new ArrayList<>();
        try{
            rs = IzvrsiUpit(upit);
            if(rs!=null){
                while(rs.next()){
                    listaQuerya.add(rs.getInt("c"));
                }
            }
            return listaQuerya;
        }catch (SQLException ex){
            System.out.println("Nije uspio dohvat otvaranja ponude");
        }

        return null;
    }

}