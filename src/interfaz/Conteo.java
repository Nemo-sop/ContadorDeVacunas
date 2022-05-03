package interfaz;

public class Conteo {

    private String categoria;
    private long resultado;

    public Conteo(){}

    public Conteo(String categoria, long resultado){
        this.categoria = categoria;
        this.resultado = resultado;

    }

    public String toString(){
        String r = "categoria: "+categoria+" Value: "+this.resultado;
        return r;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public long getResultado() {
        return resultado;
    }

    public void setResultado(long resultado) {
        this.resultado = resultado;
    }


}
