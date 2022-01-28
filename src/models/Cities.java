    package models;

    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;
    import java.util.ArrayList;

    @XmlRootElement(name = "cities")
    @XmlAccessorType(XmlAccessType.FIELD)
    public class Cities
    {
        @XmlElement(name = "city")
        private ArrayList<City> cities = new ArrayList<City>();

        public ArrayList<City> getCities() {
            return cities;
        }

        public void setCities(ArrayList<City> cities) {
            this.cities = cities;
        }
    }