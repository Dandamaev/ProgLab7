package models;

import exceptions.InvalidValueException;
import utils.NumUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

class LocalDateAdapter extends XmlAdapter<String, LocalDate> implements Serializable {
    public LocalDate unmarshal(String v)  {
        return LocalDate.parse(v);
    }

    public String marshal(LocalDate v)  {
        return v.toString();
    }
}

/**
 * Модель города
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class City implements Comparable, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private Double area; //Значение поля должно быть больше 0, Поле не может быть null
    private long population; //Значение поля должно быть больше 0
    private long metersAboveSeaLevel;
    private double timezone; //Значение поля должно быть больше -13, Максимальное значение поля: 15
    private boolean capital;
    private Government government; //Поле может быть null
    private Human governor; //Поле не может быть null

    public City(){}
    public City(String name, Coordinates coordinates, Double area, long population, long metersAboveSeaLevel, double timezone, boolean capital, Government government, Human governor){
        //this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        //this.creationDate = LocalDate.now();
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.timezone = timezone;
        this.capital = capital;
        this.government = government;
        this.governor = governor;
    }


    public City(Long id, String name, Coordinates coordinates, LocalDate creationDate, Double area, long population, long metersAboveSeaLevel, double timezone, boolean capital, Government government, Human governor){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.timezone = timezone;
        this.capital = capital;
        this.government = government;
        this.governor = governor;
    }


    public void checkFields(){
        if(id != 0L){
            if(!NumUtil.isInRange(id, new BigDecimal(0), NumUtil.LONG_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }else {
            throw new InvalidValueException("Поле id пустое.");
        }


        if (name.isEmpty()) {
            throw new InvalidValueException("Поле name пустое.");
        }

        if(population != 0L){
            if(!NumUtil.isInRange(population, new BigDecimal(0), NumUtil.FLOAT_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }

        if(!NumUtil.isInRange(area, new BigDecimal(0), NumUtil.DOUBLE_MAX) ||
                !NumUtil.isInRange(timezone, new BigDecimal(-13), new BigDecimal(15))
        ){
            throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
        }

        if (coordinates != null){
            if(!NumUtil.isInRange(coordinates.getY(), new BigDecimal(-587), NumUtil.DOUBLE_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }else {
            throw new InvalidValueException("Поле coordinates пустое.");
        }

        if(governor != null){
            if(!NumUtil.isInRange(governor.getAge(), new BigDecimal(0), NumUtil.INTEGER_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }else {
            throw new InvalidValueException("Поле governor пустое.");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public void setMetersAboveSeaLevel(long metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public double getTimezone() {
        return timezone;
    }

    public void setTimezone(double timezone) {
        this.timezone = timezone;
    }

    public boolean getCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public Government getGovernment() {
        return government;
    }

    public void setGovernment(Government government) {
        this.government = government;
    }

    public Human getGovernor() {
        return governor;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        sb.append("\t").append("id: ").append(this.id).append("\n");
        sb.append("\t").append("name: ").append(this.name).append("\n");
        sb.append("\t").append("area: ").append(this.area).append("\n");
        sb.append("\t").append("capital: ").append(this.capital).append("\n");
        sb.append("\t").append("metersAboveSeaLevel: ").append(this.metersAboveSeaLevel).append("\n");
        sb.append("\t").append("population: ").append(this.population).append("\n");
        sb.append("\t").append("timezone: ").append(this.timezone).append("\n");
        sb.append("\t").append("coordinates: ").append(this.coordinates).append("\n");
        sb.append("\t").append("creationDate: ").append(this.creationDate).append("\n");
        sb.append("\t").append("government: ").append(this.government).append("\n");
        sb.append("\t").append("governor: ").append(this.governor).append("\n");
        sb.append("}").append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof City)) {
            throw new ClassCastException();
        }
        City fo = (City) o;
        if (this.getArea().equals(fo.getArea())) {
            return 0;
        } else {
            return this.getArea() < fo.getArea() ? -1 : 1;
        }
    }
}

