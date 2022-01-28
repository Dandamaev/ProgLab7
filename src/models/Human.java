package models;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "human")
@XmlRootElement
public class Human implements Comparable, Serializable {
    private Integer age; //Значение поля должно быть больше 0

    public Human(){}
    public Human(Integer age){
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        sb.append("\t\t").append("age: ").append(age).append("\n");
        sb.append("\t}");

        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || !(o instanceof Human)) {
            return 1;
        }
        Human other = (Human) o;
        if (this.getAge() != other.getAge()) {
            return this.getAge() - other.getAge();
        }
        return 0;
    }
}
