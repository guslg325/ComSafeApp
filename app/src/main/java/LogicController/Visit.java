package LogicController;

import java.util.Date;

public class Visit {
    int id;
    Date fecha;
    String apPat,apMat,nombre,mLlegada,placas;

    public Visit(int id,Date arrival,String name,String patern,String matern,String medium,String plates){
        this.id = id;
        this.fecha = arrival;
        this.nombre = name;
        this.apPat = patern;
        this.apMat = matern;
        this.mLlegada = medium;
        this.placas = plates;
    }

    public int getId(){
        return id;
    }

    public Date getDate(){
        return fecha;
    }

    public String getName(){
        return nombre;
    }

    public String getPatern(){
        return apPat;
    }

    public String getMatern(){
        return apMat;
    }

    public String getArriveMedium(){
        return mLlegada;
    }

    public String getPlates(){
        return placas;
    }
}
