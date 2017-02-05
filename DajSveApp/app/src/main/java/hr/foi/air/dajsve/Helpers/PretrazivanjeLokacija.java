package hr.foi.air.dajsve.Helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.Ponuda;

/**
 * Created by Filip on 29.1.2017..
 */

public class PretrazivanjeLokacija implements SearchDataInterface{

    // Funkcija za izracun udaljenosti od jednog pointa na mapi do drugog(U kilometrima, preciznost algoritma 0,5 mm)
    public static double distVincenty(double lat1, double lon1, double lat2, double lon2) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
        double L = Math.toRadians(lon2 - lon1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                    + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0)
                return 0; // co-incident points
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM))
                cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0)
            return Double.NaN; // formula failed to converge

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B
                * sinSigma
                * (cos2SigmaM + B
                / 4
                * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double dist = b * A * (sigma - deltaSigma);

        return dist/1000;
    }


    @Override
    public List<String> DohvatiSve(String search, List<String> nazivPonude, List<String> hash, Context context, List<Double> allElementsLat, List<Double> allElementsLong, int km) {
        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        ArrayList<String> hashs= new ArrayList<>();


        //Dobivanje lokacije po upisanoj adresi
        int brojac = -1 ;

        try {

            address = coder.getFromLocationName(search, 5);
        } catch (IOException e) {
            System.out.println("GRESKA");
        }

        Address location = address.get(0);
        double unesenaLokacijaLat = location.getLatitude();
        double unesenaLokacijaLog = location.getLongitude();
        List<Ponuda> test = Ponuda.getAll();

        //petlja za prolaz kroz sve lokacije u bazi
        for (Double element : allElementsLat)

        {
            brojac++;
            //Usporedujemo da lije odredena lokacija unutar odredene kilometraze
            if(distVincenty(unesenaLokacijaLat, unesenaLokacijaLog, element, allElementsLong.get(brojac))<km)
            {
                hashs.add(hash.get(brojac));
            }
        }
        System.out.print(hashs.size());
        return hashs;
    }
}

    // Funkcija za implementaciju pretrazivanje po lokaciji
//    @Override
//    public ArrayList<String> onDataArrived(String strAddress, Context context, List<Double> allElementsLat,List<Double> allElementsLong, List<String> Id,int km) {
//        Geocoder coder = new Geocoder(context);
//        List<Address> address = null;
//        ArrayList<String> hashs= new ArrayList<>();
//
//
//        //Dobivanje lokacije po upisanoj adresi
//        int brojac = -1 ;
//
//        try {
//
//            address = coder.getFromLocationName(strAddress, 5);
//        } catch (IOException e) {
//            System.out.println("GRESKA");
//        }
//
//        Address location = address.get(0);
//        double unesenaLokacijaLat = location.getLatitude();
//        double unesenaLokacijaLog = location.getLongitude();
//        List<Ponuda> test = Ponuda.getAll();
//
//        //petlja za prolaz kroz sve lokacije u bazi
//        for (Double element : allElementsLat)
//
//        {
//            brojac++;
//            //Usporedujemo da lije odredena lokacija unutar odredene kilometraze
//            if(distVincenty(unesenaLokacijaLat, unesenaLokacijaLog, element, allElementsLong.get(brojac))<km)
//            {
//                hashs.add(Id.get(brojac));
//            }
//        }
//        System.out.print(hashs.size());
//        return hashs;
//    }
//}
