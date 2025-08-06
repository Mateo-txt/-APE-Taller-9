import java.util.ArrayList;
import java.util.List;

class RecursoTecnico {
    public double costoSonido;
    public double costoIluminacion;
    public double costoEscenografia;
    public double presupuesto;

    RecursoTecnico(double sonido, double iluminacion, double escenografia) {
        this.costoSonido      = sonido;
        this.costoIluminacion = iluminacion;
        this.costoEscenografia = escenografia;
    }

    void calcularTotalPresupuesto() {
        this.presupuesto = costoSonido + costoIluminacion + costoEscenografia;
    }

    @Override
    public String toString() {
        return "RecursoTecnico [sonido=" + costoSonido
             + ", iluminacion=" + costoIluminacion
             + ", escenografia=" + costoEscenografia
             + ", presupuesto=" + presupuesto + "]";
    }
}

abstract class Presentacion {
    public String titulo;
    public int duracion;
    public int aforo;
    public double costo;           
    RecursoTecnico recursos;

    Presentacion(String titulo, int duracion, int aforo, RecursoTecnico recursos) {
        this.titulo   = titulo;
        this.duracion = duracion;
        this.aforo    = aforo;
        this.recursos = recursos;
    }

    void calcularCostoBase() {
        recursos.calcularTotalPresupuesto();
        if (recursos.presupuesto != 0) {
            this.costo = (double) aforo / recursos.presupuesto;
        } else {
            this.costo = 0;
        }
    }
    abstract void calcularCostoFinal();

    @Override
    public String toString() {
        return "Presentacion [titulo=" + titulo
             + ", duracion=" + duracion
             + ", aforo=" + aforo
             + ", costo=" + String.format("%.4f", costo)
             + ", recursos=" + recursos + "]";
    }
}

class PresentacionMusical extends Presentacion {
    public double limiteAforo;

    PresentacionMusical(String titulo, int duracion, int aforo, RecursoTecnico recursos) {
        super(titulo, duracion, aforo, recursos);
    }

    void determinarLimiteForo() {
        this.limiteAforo = (aforo >= 500) ? 0.05 : 0.01;
    }

    @Override
    void calcularCostoFinal() {
        calcularCostoBase();
        determinarLimiteForo();
        this.costo = this.costo
                   + this.duracion * 0.08
                   + this.aforo    * this.limiteAforo;
    }

    @Override
    public String toString() {
        return "PresentacionMusical: " + super.toString()
             + ", limiteAforo=" + limiteAforo;
    }
}

class PresentacionTeatral extends Presentacion {
    PresentacionTeatral(String titulo, int duracion, int aforo, RecursoTecnico recursos) {
        super(titulo, duracion, aforo, recursos);
    }

    @Override
    void calcularCostoFinal() {
        calcularCostoBase();
        this.costo = this.costo * 1.1 + this.duracion * 0.05;
    }

    @Override
    public String toString() {
        return "PresentacionTeatral: " + super.toString();
    }
}

public class EjecutorPresentacion {
    public static void main(String[] args) {
        RecursoTecnico resMusical = new RecursoTecnico(1000, 500, 200);
        PresentacionMusical pm = new PresentacionMusical(
            "Concierto Rock", 120, 600, resMusical
        );
        pm.calcularCostoFinal();
        RecursoTecnico resTeatro = new RecursoTecnico(800, 300, 400);
        PresentacionTeatral pt = new PresentacionTeatral(
            "Obra Clásica", 90, 300, resTeatro);
        pt.calcularCostoFinal();

        List<Presentacion> lista = new ArrayList<>();
        lista.add(pm);
        lista.add(pt);

        System.out.println("---- Reporte de Presentaciones ----");
        for (Presentacion p : lista) {
            System.out.println(p);
        }
    }
}