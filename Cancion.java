public class Cancion {
    protected String nombre;
    protected String cantante;
    protected String ruta;
    protected String portada;
    
    Cancion(String nombre, String cantante, String ruta, String portada){
        this.nombre = nombre;
        this.cantante = cantante;
        this.ruta = ruta;
        this.portada = portada;
    }
    public String getCantante() {
        return cantante;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPortada() {
        return portada;
    }
    public String getRuta() {
        return ruta;
    }
}
