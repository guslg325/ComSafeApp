package LogicController;

public class Car {
    private String placas,modelo,marca,carColor;

    public Car(String plates,String model,String brand,String color){
        placas=plates;
        modelo=model;
        marca=brand;
        carColor=color;
    }

    public String getPlates(){
        return placas;
    }

    public String getModel(){
        return modelo;
    }

    public String getBrand(){
        return marca;
    }

    public String getCarColor(){
        return carColor;
    }
}
