package hr.foi.air.dajsve.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.dajsve.Activities.MainActivity;
import hr.foi.air.dajsve.Helpers.Baza;
import hr.foi.air.dajsve.R;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * Created by Filip on 27.1.2017..
 */

public class AdminStatistikaFragment extends Fragment {

    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = false;
    private ArrayAdapter<String> najcescePretrageAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_statistika_fragment, container, false);

        TextView brojPreuzimanjaTV = (TextView)rootView.findViewById(R.id.brojPreuzimanja);
        TextView brojOmiljenihTV = (TextView)rootView.findViewById(R.id.brojOmiljenihPonuda);
        TextView brojOmiljenihKategorijaTV = (TextView)rootView.findViewById(R.id.brojOmiljenihKategorija);
        ListView najcescePretrage = (ListView) rootView.findViewById(R.id.najcesce_pretrage);
        Button buttonOdjava = (Button) rootView.findViewById(R.id.button_odjava);
        final Button adminButtonPrijava = (Button) rootView.findViewById(R.id.admin_login_button);
        chart = (ComboLineColumnChartView) rootView.findViewById(R.id.chart);

        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);
        String brojPreuzimanja = String.valueOf(baza.DohvatiBrojPreuzimanjaAplikacije());
        String brojOmiljenihPonuda = String.valueOf(baza.DohvatiBrojOmiljenihPonuda());
        String brojOmiljenihKategorija = String.valueOf(baza.DohvatiBrojOmiljenihKategorija());
        brojPreuzimanjaTV.setText(brojPreuzimanja);
        brojOmiljenihTV.setText(brojOmiljenihPonuda);
        brojOmiljenihKategorijaTV.setText(brojOmiljenihKategorija);

        //odjava administratora
        buttonOdjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("LOGGED", Context.MODE_PRIVATE).edit();
                editor.putBoolean("logged", false);
                editor.commit();

                Fragment adminLoginFragment = new AdminLoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(hr.foi.air.dajsve.R.id.linearlayout, adminLoginFragment).commit();;

            }
        });


        List<String> listaNajcescihPretraga = baza.DohvatiNajcescePretrage();

        najcescePretrageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaNajcescihPretraga);

        najcescePretrage.setAdapter(najcescePretrageAdapter);

        generateData();

        return rootView;
    }

    //funkcija koja prima generiranu liniju i stupce i radi graf
    private void generateData() {
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Dan");
                axisY.setName("Broj pokretanja aplikacije");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setComboLineColumnChartData(data);
    }

    //generiranje charta s podacima od ovog tjedna
    private ColumnChartData generateColumnData() {

        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);

        List<Integer> lista = baza.DohvatiBrojPokretanjaZaDan(0);

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < lista.size(); i++) {

            values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(lista.get(i), ChartUtils.COLOR_GREEN));

            columns.add(new Column(values));
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }

    //generiranje charta s podacima od proslog tjedna
    private LineChartData generateLineData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ANDROID", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", null);
        Baza baza = new Baza(android_id);

        List<Integer> lista = baza.DohvatiBrojPokretanjaZaDan(1);


        List<Line> lines = new ArrayList<Line>();

        for(int b=0; b<lista.size(); b++){
            List<PointValue> values = new ArrayList<PointValue>();

            values.add(new PointValue(b, lista.get(b)));

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_BLUE);
            line.setCubic(isCubic);
            line.setHasLabels(hasLabels);
            line.setHasLines(true);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }


        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;
    }

}
