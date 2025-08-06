import java.util.ArrayList;
import java.util.List;

class Usuario {
    public String nombre;
    public String cedula;
    public int calificacionMovilidad;
    public boolean usuarioEspecial;

    public Usuario(String nombre, String cedula, int calificacionMovilidad, boolean usuarioEspecial) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.calificacionMovilidad = calificacionMovilidad;
        this.usuarioEspecial = usuarioEspecial;
    }

    @Override
    public String toString() {
        return "Usuario[nombre=" + nombre +
               ", cedula=" + cedula +
               ", calificacionMovilidad=" + calificacionMovilidad +
               ", usuarioEspecial=" + usuarioEspecial + "]";
    }
}

abstract class Movilidad {
    public ArrayList<Usuario> usuarios;
    public int calificacion;

    public Movilidad(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public abstract void definirCalificacion(int calificacion);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
               "[usuarios=" + usuarios +
               ", calificacion=" + calificacion + "]";
    }
}

abstract class MovilidadPago extends Movilidad {
    public double costoPasaje;

    public MovilidadPago(ArrayList<Usuario> usuarios) {
        super(usuarios);
    }

    public abstract void calcularCostoPasaje();

    @Override
    public String toString() {
        return super.toString() +
               ", costoPasaje=" + String.format("%.2f", costoPasaje);
    }
}

class MovilidadKtaxi extends MovilidadPago {
    public String origen;
    public String destino;
    public double distanciaKm;
    public int tiempoMin;
    public double tarifaKm;

    public MovilidadKtaxi(ArrayList<Usuario> usuarios,
                          String origen,
                          String destino,
                          double distanciaKm,
                          int tiempoMin,
                          double tarifaKm) {
        super(usuarios);
        this.origen = origen;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
        this.tiempoMin = tiempoMin;
        this.tarifaKm = tarifaKm;
    }

    @Override
    public void calcularCostoPasaje() {
        this.costoPasaje = distanciaKm * tarifaKm + tiempoMin * tarifaKm;
    }

    @Override
    public void definirCalificacion(int calificacion) {
        this.calificacion = calificacion;
        for (Usuario u : usuarios) {
            u.calificacionMovilidad = calificacion;
        }
    }

    @Override
    public String toString() {
        return super.toString() +
               ", origen=" + origen +
               ", destino=" + destino +
               ", distanciaKm=" + distanciaKm +
               ", tiempoMin=" + tiempoMin +
               ", tarifaKm=" + tarifaKm + "]";
    }
}

class MovilidadSITU extends MovilidadPago {
    public List<String> rutas;
    public double tarifaFija;
    public double descuento; 

    public MovilidadSITU(ArrayList<Usuario> usuarios,
                         List<String> rutas,
                         double tarifaFija,
                         double descuento) {
        super(usuarios);
        this.rutas = rutas;
        this.tarifaFija = tarifaFija;
        this.descuento = descuento;
    }

    @Override
    public void calcularCostoPasaje() {
        this.costoPasaje = tarifaFija;
        if (!usuarios.isEmpty() && usuarios.get(0).usuarioEspecial) {
            this.costoPasaje *= descuento;
        }
    }

    @Override
    public void definirCalificacion(int calificacion) {
        this.calificacion = calificacion;
        for (Usuario u : usuarios) {
            u.calificacionMovilidad = calificacion;
        }
    }

    @Override
    public String toString() {
        return super.toString() +
               ", rutas=" + rutas +
               ", tarifaFija=" + tarifaFija +
               ", descuento=" + descuento + "]";
    }
}

class MovilidadUTPL extends Movilidad {
    public List<String> rutasUtpl;

    public MovilidadUTPL(ArrayList<Usuario> usuarios, List<String> rutasUtpl) {
        super(usuarios);
        this.rutasUtpl = rutasUtpl;
    }

    @Override
    public void definirCalificacion(int calificacion) {
        this.calificacion = calificacion;
        for (Usuario u : usuarios) {
            u.calificacionMovilidad = calificacion;
        }
    }

    @Override
    public String toString() {
        return super.toString() +
               ", rutasUtpl=" + rutasUtpl + "]";
    }
}

public class EjecutorMovilidad {
    public static void main(String[] args) {
        ArrayList<Usuario> lista1 = new ArrayList<>();
        lista1.add(new Usuario("Juan Perez", "0102030405", 0, false));
        lista1.add(new Usuario("Ana Gomez", "0504030201", 0, true));

        MovilidadKtaxi ktaxi = new MovilidadKtaxi(
            lista1,
            "Centro",
            "Terminal",
            12.5,
            20,
            0.9
        );
        ktaxi.calcularCostoPasaje();
        ktaxi.definirCalificacion(4);
        System.out.println(ktaxi);

        ArrayList<Usuario> lista2 = new ArrayList<>();
        lista2.add(new Usuario("Luis Vargas", "1122334455", 0, true));
        List<String> rutas = new ArrayList<>();
        rutas.add(" Jipiro ");
        rutas.add(" Puerta de la Ciudad");
        MovilidadSITU situ = new MovilidadSITU(
            lista2,
            rutas,
            0.30,
            0.5
        );
        situ.calcularCostoPasaje();
        situ.definirCalificacion(5);
        System.out.println(situ);

        ArrayList<Usuario> lista3 = new ArrayList<>();
        lista3.add(new Usuario("Sebastian Jaramillo", "6677889900", 0, false));
        List<String> rutasUtpl = new ArrayList<>();
        rutasUtpl.add(" La Banda ");
        rutasUtpl.add(" Epoca ");
        MovilidadUTPL utpl = new MovilidadUTPL(lista3, rutasUtpl);
        utpl.definirCalificacion(3);
        System.out.println(utpl);
    }
}