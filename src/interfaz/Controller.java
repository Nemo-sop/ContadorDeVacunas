package interfaz;
import Logica.Dosis;
import Logica.TSBHashTableDA;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.*;
import java.text.SimpleDateFormat;

import java.util.*;

public class Controller {

    private TSBHashTableDA<String, TSBHashTableDA<String, Conteo>> tabla = new TSBHashTableDA<>();


    @FXML
    private Pane chartPane;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button btnAbrir;

    @FXML
    private CheckBox cbxTodos;

    @FXML
    private ComboBox<String> cmbDepartamento;

    @FXML
    private ComboBox<String> cmbDimension;

    @FXML
    private TableView<Conteo> dgvTabla;

    @FXML
    private TextField txtbArchivo;

    @FXML
    void btnAbrir_Click(ActionEvent event) throws FileNotFoundException {
        FileChooser selectorArchivos = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Archivos csv", "*.csv");
        selectorArchivos.setTitle("Abrir archivo :D");
        selectorArchivos.getExtensionFilters().add(filter);

        List listaArchivos = selectorArchivos.showOpenMultipleDialog(null);
        ArrayList lista = new ArrayList<Integer>();
        File f = (File) listaArchivos.get(0);
        txtbArchivo.setText(f.getName());



        BufferedReader bufferLectura = new BufferedReader(new FileReader(f.getAbsolutePath()));

        long inicio = System.currentTimeMillis();

        final String SEPARADOR=",";



        TSBHashTableDA<String, Conteo> tablaTodos = new TSBHashTableDA<>();
        tabla.put("TODOS", tablaTodos);


        try
        {
            // Leer una linea del archivo
            String linea = bufferLectura.readLine();
            //saltamos la primer linea
            linea = bufferLectura.readLine();



            while (linea != null)
            {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split(SEPARADOR);


                for (int i = 0; i < campos.length; i++) {
                    //System.out.print(campos[i]+"  ");
                }
                String cordoba = "\"14\"";
                System.out.println(campos[7]+"        "+ cordoba);

                if (campos[7].equals(cordoba))/*(true)*/
                {
                    //Conteos particulares
                    if (tabla.containsKey(campos[8]))
                    {
                        TSBHashTableDA<String, Conteo> x = tabla.get(campos[8]);

                        //Conteos de sexo
                        if (x.containsKey(campos[0]))
                        {
                            Conteo y = x.get(campos[0]);
                            y.setResultado(y.getResultado()+1);
                        }
                        else
                        {
                            Conteo y = new Conteo(campos[0].toString(), 1);
                            x.put(campos[0], y);
                        }

                        //Conteo de vacunas
                        if (x.containsKey(campos[11]))
                        {
                            Conteo y = x.get(campos[11]);
                            y.setResultado(y.getResultado()+1);
                        }
                        else
                        {
                            Conteo y = new Conteo(campos[11].toString(), 1);
                            x.put(campos[11], y);
                        }
                        //Conteo de Orden
                        if (x.containsKey(campos[13]))
                        {
                            Conteo y = x.get(campos[13]);
                            y.setResultado(y.getResultado()+1);
                        }
                        else
                        {
                            Conteo y = new Conteo(campos[13].toString(), 1);
                            x.put(campos[13], y);
                        }
                    }
                    else
                    {
                        Conteo x = new Conteo(campos[0], 1);
                        TSBHashTableDA<String, Conteo> y = new TSBHashTableDA<>();
                        y.put(campos[0], x);
                        tabla.put(campos[8], y);

                        Conteo x2 = new Conteo(campos[11], 1);
                        TSBHashTableDA<String, Conteo> y2 = new TSBHashTableDA<>();
                        y2.put(campos[11], x2);
                        tabla.put(campos[8], y2);

                        Conteo x3 = new Conteo(campos[13], 1);
                        TSBHashTableDA<String, Conteo> y3 = new TSBHashTableDA<>();
                        y3.put(campos[13], x3);
                        tabla.put(campos[8], y3);
                    }

                    //Contador de todos

                    //Conteos de sexo
                    if (tablaTodos.containsKey(campos[0]))
                    {
                        Conteo y = tablaTodos.get(campos[0]);
                        y.setResultado(y.getResultado()+1);
                    }
                    else
                    {
                        Conteo y = new Conteo(campos[0].toString(), 1);
                        tablaTodos.put(campos[0], y);
                    }

                    //Conteo de vacunas
                    if (tablaTodos.containsKey(campos[11]))
                    {
                        Conteo y = tablaTodos.get(campos[11]);
                        y.setResultado(y.getResultado()+1);
                    }
                    else
                    {
                        Conteo y = new Conteo(campos[11].toString(), 1);
                        tablaTodos.put(campos[11], y);
                    }
                    //Conteo de Orden
                    if (tablaTodos.containsKey(campos[13]))
                    {
                        Conteo y = tablaTodos.get(campos[13]);
                        y.setResultado(y.getResultado()+1);
                    }
                    else
                    {
                        Conteo y = new Conteo(campos[13].toString(), 1);
                        tablaTodos.put(campos[13], y);
                    }





                }


                // Volver a leer otra línea del fichero
                linea = bufferLectura.readLine();

            }


        }
        catch (Exception e)
        {
            System.err.println("Error enn la lectura: "+ e.getMessage() +"\n" +e.toString()+"\n"+e.getCause());
        }

        cmbDepartamento.setItems(this.cargarComboDepto());
        cmbDimension.setItems(this.cargarComboDimension());

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de carga: "+ (fin-inicio)/1000);



    }

    @FXML
    void cmbDepto_Seleccion(ActionEvent event) {
        if (cmbDimension.getValue() != null)
        {
            llenarTabla();
        }
    }

    @FXML
    void cmbDimension_seleccion(ActionEvent event) {
        if (cmbDepartamento.getValue() != null || cbxTodos.isSelected())
        {
            llenarTabla();
        }
    }



    // Metodos para Cargar combobox

    public ObservableList<String> cargarComboDepto()
    {
        ObservableList<String> departamentos = FXCollections.observableArrayList();
        Set listaDeptos = tabla.keySet();
        for (Object x : listaDeptos) {
            if (!(x.equals("TODOS"))) {
                departamentos.add((String) x);
            }
        }
        return departamentos;

    }

    public ObservableList<String> cargarComboDimension()
    {
        ObservableList<String> dimensiones = FXCollections.observableArrayList();
        dimensiones.addAll("Por sexo", "Por Vacuna", "Por Orden de Dosis");

        return dimensiones;
    }


    // Metodo para llenar la tabla

    public void llenarTabla()
    {
        //Limpiamos la tabla
        dgvTabla.getColumns().clear();
        dgvTabla.getItems().clear();

        String departamento = cmbDepartamento.getValue();
        String dimension = cmbDimension.getValue();

        ArrayList<Object> keys = new ArrayList<>();

        if (dimension.equals("Por sexo")){keys.add("\"F\""); keys.add("\"M\"");}
        if (dimension.equals("Por Orden de Dosis")){keys.add("1"); keys.add("2");}
        if (dimension.equals("Por Vacuna")){keys.add("\"Sputnik\""); keys.add("\"AstraZeneca\"");keys.add("\"Sinopharm\""); keys.add("\"Moderna\"");keys.add("\"COVISHIELD\""); keys.add("\"Pfizer\"");keys.add("\"Cansino\"");}

        Set<String> keySet = tabla.keySet();

        System.out.println("Key set: "+ keySet);

        ObservableList<Conteo> listadoCalculos = FXCollections.observableArrayList();


        if (cbxTodos.isSelected())
        {
            TSBHashTableDA tablaDepto = tabla.get("TODOS");
            for (int i = 0; i < keys.size(); i++)
            {
                System.out.println("TablaDepto: "+tablaDepto);
                System.out.println( tablaDepto.get(keys.get(i)));
                listadoCalculos.add((Conteo) tablaDepto.get(keys.get(i)));
            }

        }
        else
        {
            TSBHashTableDA tablaDepto = tabla.get(departamento);
            for (int i = 0; i < keys.size(); i++)
            {
                System.out.println("TablaDepto: "+tablaDepto);
                System.out.println( tablaDepto.get(keys.get(i)));
                listadoCalculos.add((Conteo) tablaDepto.get(keys.get(i)));
            }
        }





        TableColumn<Conteo, String> colDimension = new TableColumn<>("Categoria: ");
        TableColumn<Conteo, Long> colResultados = new TableColumn<>("Cantidad: ");

        colDimension.setCellValueFactory(new PropertyValueFactory<Conteo, String>("categoria"));
        colResultados.setCellValueFactory(new PropertyValueFactory<Conteo, Long>("resultado"));

        dgvTabla.getColumns().setAll(colDimension, colResultados);

        System.out.println("Tamaño de la tabla principal: "+tabla.size());
        for (int i = 0; i < tabla.size(); i++) {
            //System.out.println(tabla.values());
        }

        System.out.println("listadoCalculos: "+listadoCalculos);

        Boolean t = dgvTabla.getItems().addAll(listadoCalculos);
        System.out.println(t);


        chartPane.getChildren().removeAll();

        mostrarPieChart(listadoCalculos);


    }


    // Metodos de conteo
/*
    public TSBHashTableDA<String,Conteo> contarPorSexo(String departamento, TSBHashTableDA<String,Conteo> conteo)
    {
        if((cbxTodos.isSelected()))
        {
            long masculino = 0;
            long femenino = 0;
            for (int i = 0; i < dosis.size(); i++) {
                if (dosis.get(i).getSexo().equals("F")) {femenino ++;}
                else {masculino ++;}
            }

            Conteo conteoFemenino = new Conteo("Femenino", femenino);
            Conteo conteoMasculino = new Conteo("Masculino", masculino);
            conteo.put("F", conteoFemenino);
            conteo.put("M", conteoMasculino);
        }
        else
        {
            long masculino = 0;
            long femenino = 0;
            for (int i = 0; i < dosis.size(); i++) {
                if (dosis.get(i).getSexo().equals("F") && (dosis.get(i).getDepto_aplicacion().equals(departamento)) ) {femenino ++;}
                if (dosis.get(i).getSexo().equals("M") && (dosis.get(i).getDepto_aplicacion().equals(departamento)) ) {masculino ++;}
            }
            Conteo conteoFemenino = new Conteo("Femenino", femenino);
            Conteo conteoMasculino = new Conteo("Masculino", masculino);
            conteo.put("F", conteoFemenino);
            conteo.put("M", conteoMasculino);
        }
        //System.out.println(""+conteo);
        return conteo;
    }

    public TSBHashTableDA<String,Conteo> contarPorVacuna(String departamento, TSBHashTableDA<String,Conteo> conteo)
    {
        if((cbxTodos.isSelected())){
            long Sputnik = 0;
            long AstraZeneca = 0;
            long Sinopharm = 0;
            long Moderna =0;
            long COVISHIELD =0 ;
            long Pfizer = 0;
            long Cansino =0;

            for (int i = 0; i < dosis.size(); i++) {
                //System.out.println( dosis.get(i).getVacuna());
                if (dosis.get(i).getVacuna().equals("Sputnik")) {Sputnik ++;}
                if (dosis.get(i).getVacuna().equals("AstraZeneca")) {AstraZeneca ++;}
                if (dosis.get(i).getVacuna().equals("Sinopharm")) {Sinopharm ++;}
                if (dosis.get(i).getVacuna().equals("Moderna")) {Moderna ++;}
                if (dosis.get(i).getVacuna().equals("COVISHIELD")) {COVISHIELD ++;}
                if (dosis.get(i).getVacuna().equals("Pfizer")) {Pfizer ++;}
                if (dosis.get(i).getVacuna().equals("Cansino")) {Cansino ++;}
            }
            Conteo conteoSputnik = new Conteo("Sputnik", Sputnik);
            Conteo conteoAstraZeneca = new Conteo("AstraZeneca", AstraZeneca);
            Conteo conteoSinopharm = new Conteo("Sinopharm", Sinopharm);
            Conteo conteoModerna = new Conteo("Moderna", Moderna);
            Conteo conteoCOVISHIELD = new Conteo("COVISHIELD", COVISHIELD);
            Conteo conteoPfizer = new Conteo("Pfizer", Pfizer);
            Conteo conteoCansino = new Conteo("Cansino", Cansino);

            conteo.put("Sputnik", conteoSputnik);
            conteo.put("AstraZeneca", conteoAstraZeneca);
            conteo.put("Sinpharm", conteoSinopharm);
            conteo.put("Moderna", conteoModerna);
            conteo.put("COVISHIELD", conteoCOVISHIELD);
            conteo.put("Pfizer", conteoPfizer);
            conteo.put("Cansino", conteoCansino);
        }
        else
        {
            long Sputnik = 0;
            long AstraZeneca = 0;
            long Sinopharm = 0;
            long Moderna =0;
            long COVISHIELD =0 ;
            long Pfizer = 0;
            long Cansino =0;

            for (int i = 0; i < dosis.size(); i++) {
                //System.out.println( dosis.get(i).getVacuna());
                if (dosis.get(i).getVacuna().equals("Sputnik") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {Sputnik ++;}
                if (dosis.get(i).getVacuna().equals("AstraZeneca") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {AstraZeneca ++;}
                if (dosis.get(i).getVacuna().equals("Sinopharm") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {Sinopharm ++;}
                if (dosis.get(i).getVacuna().equals("Moderna") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {Moderna ++;}
                if (dosis.get(i).getVacuna().equals("COVISHIELD") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {COVISHIELD ++;}
                if (dosis.get(i).getVacuna().equals("Pfizer") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {Pfizer ++;}
                if (dosis.get(i).getVacuna().equals("Cansino") && dosis.get(i).getDepto_aplicacion().equals(departamento)) {Cansino ++;}
            }
            Conteo conteoSputnik = new Conteo("Sputnik", Sputnik);
            Conteo conteoAstraZeneca = new Conteo("AstraZeneca", AstraZeneca);
            Conteo conteoSinopharm = new Conteo("Sinopharm", Sinopharm);
            Conteo conteoModerna = new Conteo("Moderna", Moderna);
            Conteo conteoCOVISHIELD = new Conteo("COVISHIELD", COVISHIELD);
            Conteo conteoPfizer = new Conteo("Pfizer", Pfizer);
            Conteo conteoCansino = new Conteo("Cansino", Cansino);

            conteo.put("Sputnik", conteoSputnik);
            conteo.put("AstraZeneca", conteoAstraZeneca);
            conteo.put("Sinpharm", conteoSinopharm);
            conteo.put("Moderna", conteoModerna);
            conteo.put("COVISHIELD", conteoCOVISHIELD);
            conteo.put("Pfizer", conteoPfizer);
            conteo.put("Cansino", conteoCansino);
        }

        return conteo;
    }

    public TSBHashTableDA<String,Conteo> contarPorOrden(String departamento, TSBHashTableDA<String,Conteo> conteo)
    {
        if((cbxTodos.isSelected()))
        {
            long primera = 0;
            long segunda = 0;
            for (int i = 0; i < dosis.size(); i++) {
                if (dosis.get(i).getOrden_dosis() == 1) {primera ++;}
                else {segunda ++;}
            }
            Conteo conteoPrimera = new Conteo("Primera Dosis", primera);
            Conteo conteoSegunda = new Conteo("Segunda Dosis", segunda);
            conteo.put("1", conteoPrimera);
            conteo.put("2", conteoSegunda);
        }
        else
        {
            long primera = 0;
            long segunda = 0;
            for (int i = 0; i < dosis.size(); i++) {
                if (dosis.get(i).getOrden_dosis() == 1 && dosis.get(i).getDepto_aplicacion().equals(departamento)) {primera ++;}
                if (dosis.get(i).getOrden_dosis() == 2 && dosis.get(i).getDepto_aplicacion().equals(departamento)) {segunda ++;}
            }
            Conteo conteoPrimera = new Conteo("Primera Dosis", primera);
            Conteo conteoSegunda = new Conteo("Segunda Dosis", segunda);
            conteo.put("1", conteoPrimera);
            conteo.put("2", conteoSegunda);
        }

        return conteo;
    }
*/
    // Metodo de Graficos

    public void mostrarPieChart(ObservableList<Conteo> listadoConteo)
    {

        chartPane.getChildren().clear();

        ObservableList<PieChart.Data> datos = FXCollections.observableArrayList();

        for (int i = 0; i < listadoConteo.size(); i++) {
            Conteo x = listadoConteo.get(i);
            PieChart.Data y = new PieChart.Data(x.getCategoria(), x.getResultado());
            datos.add(y);
        }

        PieChart pieChart = new PieChart(datos);
        pieChart.setTitle("Distribucion");

        chartPane.getChildren().addAll(pieChart);


    }

}