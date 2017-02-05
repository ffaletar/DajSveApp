package hr.foi.air.dajsve.Helpers;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Filip on 29.1.2017..
 */

public class PretrazivanjeKeyword implements SearchDataInterface{

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='x') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }


    @Override
    public List<String> DohvatiSve(String search, List<String> nazivPonude, List<String> hash, Context context, List<Double> allElementsLat, List<Double> allElementsLong, int km) {
        int Slength = search.length();
        int brojac=-1;
        String word ="";
        List<String> ListaKljučnihRiječi = new ArrayList<>();

        //Petlja za prolaz kroz upisani string u trazilici, te prema razmaku spremanje kljucnih rijeci u listu

        for (int i = 0 ; i<Slength;i++)
        {
            if(Character.isWhitespace(search.charAt(i)) || i==Slength-1){
                System.out.println(word);
                if(word!=""){
                    if(i!=Slength-1) {
                        word = word.substring(0, word.length() - 1);
                    }
                    ListaKljučnihRiječi.add(word);
                    word="";
                }
            }
            else
            {
                word+=search.charAt(i);
            }
        }

        //Formatiranje kljucnih rijeci u odgovorajuci string za matcher

        Set<String> targetSet = new HashSet();
        String sviKeywords = TextUtils.join("|", ListaKljučnihRiječi);
        System.out.println(sviKeywords);
        ArrayList<String> allMatchedElements = new ArrayList<>();

        //Petlja za prolaz kroz sve nazive te pretrazivanje tih naziva po listi kljucnih rijeci
        for (String element : nazivPonude)
        {
            brojac++;
            Matcher matcher = Pattern.compile(sviKeywords.toLowerCase()).matcher(element.toLowerCase());
            if (matcher.find()) {
                targetSet.add(element);
                System.out.println("OVO : "+element+ " HASH : "+ hash.get(brojac));
                allMatchedElements.add(hash.get(brojac));
            }

        }
        return allMatchedElements;
    }
}
