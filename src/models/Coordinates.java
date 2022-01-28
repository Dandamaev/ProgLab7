package models;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "coordinates")
@XmlRootElement
public class Coordinates implements Serializable {
    private float x;
    private Double y; //Значение поля должно быть больше -587, Поле не может быть null

    public Coordinates(){}

    public Coordinates(float x, Double y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        sb.append("\t\t").append("x: ").append(x).append("\n");
        sb.append("\t\t").append("y: ").append(y).append("\n");
        sb.append("\t}");

        return sb.toString();
    }
}
