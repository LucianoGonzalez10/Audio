import java.util.ArrayList;

public class index{
    public static void main(String[] args) {
        Cancion cancion1 = new Cancion("BAILE CHORRA SE TE CAEN LAS BALAS", " Aniasko ", "audio\\pruebasAudio.wav", "media/portadaCancion1.jpg");
        Cancion cancion2 = new Cancion("BAILE CHORRA SE TE CAEN LAS BALAS", " Aniasko ", "audio\\pruebasAudio.wav", "media/portadaCancion2.jpg");
        ArrayList <Cancion> canciones = new ArrayList<>();
        canciones.add(cancion1);
        canciones.add(cancion2);

        new ReproductorMusica(canciones);

    }
}