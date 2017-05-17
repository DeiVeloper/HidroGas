package mx.com.desoft.hidrogas;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

class Componentes  {

    private Spinner spinner;
    private Button imprimir;
    private Button guardar;
    private Button limpíar;
    private EditText economico;
    private EditText chofer;
    private EditText ayudante;
    private EditText salida1;
    private EditText llegada1;
    private EditText totalizadorInicial1;
    private EditText totalizadorFinal1;
    private EditText salida2;
    private EditText llegada2;
    private EditText totalizadorInicial2;
    private EditText totalizadorFinal2;
    private EditText salida3;
    private EditText llegada3;
    private EditText totalizadorInicial3;
    private EditText totalizadorFinal3;
    private EditText autoconsumo;
    private EditText medidores;
    private EditText traspasosRecibidos;
    private EditText traspasosRealizados;
    private TextView nombreChofer;
    private TextView nombreAyudante;
    private TextView alerta;
    private TextView variacion;
    private TextView porcentajeVariacion;
    private TextView clave;
    private TextView folio;

    public Integer getIdViaje1() {
        return idViaje1;
    }

    public void setIdViaje1(Integer idViaje1) {
        this.idViaje1 = idViaje1;
    }

    public Integer getIdViaje2() {
        return idViaje2;
    }

    public void setIdViaje2(Integer idViaje2) {
        this.idViaje2 = idViaje2;
    }

    public Integer getIdViaje3() {
        return idViaje3;
    }

    public void setIdViaje3(Integer idViaje3) {
        this.idViaje3 = idViaje3;
    }

    private Integer idViaje1;
    private Integer idViaje2;
    private Integer idViaje3;


    Componentes(ViewGroup viewGroup){
        this.setEconomico((EditText) viewGroup.findViewById(R.id.input_economico));
        this.imprimir = (Button) viewGroup.findViewById(R.id.btnImprimir);
        this.guardar = (Button) viewGroup.findViewById(R.id.btnGuardarLiquidacion);
        this.limpíar = (Button) viewGroup.findViewById(R.id.btnLimpiar);
        this.spinner = (Spinner) viewGroup.findViewById(R.id.spinner_ruta);
        this.salida1 = (EditText) viewGroup.findViewById(R.id.input_Salida_1);
        this.llegada1 = (EditText) viewGroup.findViewById(R.id.input_Llegada_1);
        this.totalizadorInicial1 = (EditText) viewGroup.findViewById(R.id.input_totInicial_1);
        this.totalizadorFinal1 = (EditText) viewGroup.findViewById(R.id.input_totFinal_1);
        this.salida2 = (EditText) viewGroup.findViewById(R.id.input_Salida_2);
        this.llegada2 = (EditText) viewGroup.findViewById(R.id.input_Llegada_2);
        this.totalizadorInicial2 = (EditText) viewGroup.findViewById(R.id.input_totInicial_2);
        this.totalizadorFinal2  = (EditText) viewGroup.findViewById(R.id.input_totFinal_2);
        this.salida3 = (EditText) viewGroup.findViewById(R.id.input_Salida_3);
        this.llegada3 = (EditText) viewGroup.findViewById(R.id.input_Llegada_3);
        this.totalizadorInicial3 = (EditText) viewGroup.findViewById(R.id.input_totInicial_3);
        this.totalizadorFinal3  = (EditText) viewGroup.findViewById(R.id.input_totFinal_3);
        this.chofer = (EditText) viewGroup.findViewById(R.id.input_chofer);
        this.nombreChofer = (TextView) viewGroup.findViewById(R.id.input_nombreChofer);
        this.ayudante = (EditText) viewGroup.findViewById(R.id.input_ayudante);
        this.nombreAyudante = (TextView) viewGroup.findViewById(R.id.input_nombreAyudante);
        this.alerta = (TextView) viewGroup.findViewById(R.id.labelAlerta);
        this.variacion = (TextView) viewGroup.findViewById(R.id.input_variacion);
        this.autoconsumo = (EditText) viewGroup.findViewById(R.id.autoconsumo);
        this.medidores= (EditText) viewGroup.findViewById(R.id.medidores);
        this.traspasosRecibidos = (EditText) viewGroup.findViewById(R.id.traspasosRecibidos);
        this.traspasosRealizados = (EditText) viewGroup.findViewById(R.id.traspasosRealizados);
        this.porcentajeVariacion = (TextView) viewGroup.findViewById(R.id.labelPorcentajeVariacion);
        this.clave = (TextView) viewGroup.findViewById(R.id.labelClave);
        this.folio = (TextView) viewGroup.findViewById(R.id.lblFolio);
    }

    void deshabilitarComponentes(){
        this.getSpinner().setEnabled(false);
        this.getChofer().setEnabled(false);
        this.getAyudante().setEnabled(false);
        this.getSalida1().setEnabled(false);
        this.getSalida2().setEnabled(false);
        this.getSalida3().setEnabled(false);
        this.getLlegada1().setEnabled(false);
        this.getLlegada2().setEnabled(false);
        this.getLlegada3().setEnabled(false);
        this.getTotalizadorInicial1().setEnabled(false);
        this.getTotalizadorInicial2().setEnabled(false);
        this.getTotalizadorInicial3().setEnabled(false);
        this.getTotalizadorFinal1().setEnabled(false);
        this.getTotalizadorFinal2().setEnabled(false);
        this.getTotalizadorFinal3().setEnabled(false);
    }

    void habilitarCampos(){
        this.getSpinner().setEnabled(true);
        this.getChofer().setEnabled(true);
        this.getAyudante().setEnabled(true);
        this.getSalida1().setEnabled(true);
        this.getSalida2().setEnabled(true);
        this.getSalida3().setEnabled(true);
        this.getLlegada1().setEnabled(true);
        this.getLlegada2().setEnabled(true);
        this.getLlegada3().setEnabled(true);
        this.getTotalizadorInicial1().setEnabled(true);
        this.getTotalizadorInicial2().setEnabled(true);
        this.getTotalizadorInicial3().setEnabled(true);
        this.getTotalizadorFinal1().setEnabled(true);
        this.getTotalizadorFinal2().setEnabled(true);
        this.getTotalizadorFinal3().setEnabled(true);
    }

    void limpiarCampos(){
        this.getSalida1().setText("");
        this.getLlegada1().setText("");
        this.getTotalizadorInicial1().setText("");
        this.getTotalizadorFinal1().setText("");
        this.getSalida2().setText("");
        this.getLlegada2().setText("");
        this.getTotalizadorInicial2().setText("");
        this.getTotalizadorFinal2().setText("");
        this.getSalida3().setText("");
        this.getLlegada3().setText("");
        this.getTotalizadorInicial3().setText("");
        this.getTotalizadorFinal3().setText("");
        this.getAlerta().setText("");
        this.getVariacion().setText("");
        this.getClave().setText("");
        this.getFolio().setText("");
        this.getPorcentajeVariacion().setText("");
        this.getEconomico().setText("");
        this.getAutoconsumo().setText("");
        this.getMedidores().setText("");
        this.getTraspasosRecibidos().setText("");
        this.getTraspasosRealizados().setText("");
        this.getChofer().setText("");
        this.getAyudante().setText("");
        this.getNombreChofer().setText("");
        this.getNombreAyudante().setText("");
        this.getSpinner().setSelection(0);
        this.getSpinner().setEnabled(true);
        this.getEconomico().setEnabled(true);
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    Button getImprimir() { return imprimir; }

    public Button getGuardar() {
        return guardar;
    }

    public void setGuardar(Button guardar) {
        this.guardar = guardar;
    }

    Button getLimpíar() {
        return limpíar;
    }

    public EditText getEconomico() {
        return economico;
    }

    public void setEconomico(EditText economico) {
        this.economico = economico;
    }

    public EditText getChofer() {
        return chofer;
    }

    public void setChofer(EditText chofer) {
        this.chofer = chofer;
    }

    public EditText getAyudante() {
        return ayudante;
    }

    public void setAyudante(EditText ayudante) {
        this.ayudante = ayudante;
    }

    EditText getSalida1() {
        return salida1;
    }

    EditText getLlegada1() { return llegada1; }

    EditText getTotalizadorInicial1() {
        return totalizadorInicial1;
    }

    EditText getTotalizadorFinal1() {
        return totalizadorFinal1;
    }

    EditText getSalida2() {
        return salida2;
    }

    EditText getLlegada2() {
        return llegada2;
    }

    EditText getTotalizadorInicial2() {
        return totalizadorInicial2;
    }

    EditText getTotalizadorFinal2() {
        return totalizadorFinal2;
    }

    EditText getSalida3() {
        return salida3;
    }

    EditText getLlegada3() {
        return llegada3;
    }

    EditText getTotalizadorInicial3() {
        return totalizadorInicial3;
    }

    EditText getTotalizadorFinal3() {
        return totalizadorFinal3;
    }

    EditText getAutoconsumo() {
        return autoconsumo;
    }

    EditText getMedidores() {
        return medidores;
    }

    EditText getTraspasosRecibidos() {
        return traspasosRecibidos;
    }

    EditText getTraspasosRealizados() {
        return traspasosRealizados;
    }

    TextView getNombreChofer() {
        return nombreChofer;
    }

    TextView getNombreAyudante() {
        return nombreAyudante;
    }

    TextView getAlerta() {
        return alerta;
    }

    public TextView getVariacion() {
        return variacion;
    }

    public void setVariacion(TextView variacion) {
        this.variacion = variacion;
    }

    TextView getPorcentajeVariacion() {
        return porcentajeVariacion;
    }

    public TextView getClave() {
        return clave;
    }

    public void setClave(TextView clave) {
        this.clave = clave;
    }

    TextView getFolio() {
        return folio;
    }


}
