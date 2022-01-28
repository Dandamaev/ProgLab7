package managers;

import models.Cities;
import models.City;
import models.Human;
import exceptions.DuplicateIdException;

import javax.xml.bind.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {


    private LocalDate initDate;
    private Cities citiesArray;
    private File xmlCollection;
    private Long maxId = 0L;
    private List<City> collection;

    /**
     * Класс, который работает с коллекцией
     * @param fileName
     * @throws IOException
     */
    public CollectionManager(String fileName) throws IOException {
        if (fileName != null) {
            xmlCollection = new File(fileName);
            try {
                if (!xmlCollection.exists()) throw new FileNotFoundException();
            } catch (FileNotFoundException ex) {
                System.out.println("Файл не существует.");
                System.exit(1);
            }

            this.load();
            initDate = LocalDate.now();
            System.out.println("Коллекция загружена");
        }else {
            System.out.println("Файл не найден");
            System.exit(1);
        }
    }

    public CollectionManager(){
        this.collection = new ArrayList<>();
        this.initDate = LocalDate.now();
    }

    public CollectionManager(ArrayList<City> collection){
        this.initDate = LocalDate.now();
        this.collection = Collections.synchronizedList(collection);
        this.maxId = (long) (collection.size() + 1);
    }

    /**
     * добавляет новый элемент
     * @param city
     */
    public void addElement(City city){
        maxId+=1;
//        city.setId(maxId);
//        city.setCreationDate(LocalDate.now());
//        city.checkFields();

        this.getCityCollection().add(city);
    }

    /**
     * добавляет новый элемент с условием
     * @param city
     * @return
     */
    public boolean addIfMin(City city){
        List<City> cities = this.getCityCollection().stream().sorted().collect(Collectors.toList());
        if (cities.isEmpty()) {
            return false;
        }

        City firstCity = cities.get(0);
        if (firstCity.compareTo(city) > 0) {
            addElement(city);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Возвращает кол-во городов, у которых значение governor меньше данного.
     */
    public long countLessThanGovernor(Human h) {
        return this.getCityCollection().stream().filter(x -> x.getGovernor().compareTo(h) < 0).count();
    }

    /**
     * удаляет элементы, меньше заданного
     * @param city
     */
    public void removeLower(City city){
        List<City> cities = this.getCityCollection().stream().sorted().collect(Collectors.toList());
        cities.forEach(x -> {
            if(x.compareTo(city) < 0){
                this.getCityCollection().remove(x);
            }
        });
    }


    /**
     * удаляет элемент коллекции
     * @param id
     */
    public boolean remove(long id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, City> entry = findById(id).entrySet().iterator().next();
            this.getCityCollection().remove(entry.getValue());
            return true;
        }

        return false;
    }

    /**
     * обновляет коллекцию по его id
     * @param city
     * @param id
     */
    public boolean update(City city, Long id){
        if(this.checkIdExist(id)) {
            Map.Entry<Integer, City> entry = findById(id).entrySet().iterator().next();
            City updCity = entry.getValue();
            updCity.setName(city.getName());
            updCity.setArea(city.getArea());
            updCity.setCapital(city.getCapital());
            updCity.setGovernment(city.getGovernment());
            updCity.setGovernor(city.getGovernor());
            updCity.setCoordinates(city.getCoordinates());
            updCity.setTimezone(city.getTimezone());
            updCity.setPopulation(city.getPopulation());
            updCity.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());

            this.getCityCollection().set(entry.getKey(), updCity);
            return true;
        }

        return false;
    }

    public boolean checkIdExist(Long id){
        for(int i=0;i<this.getCityCollection().size();i++) {
            if(this.getCityCollection().get(i).getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ищет элемент по id
     * @param id
     * @return
     */
    private Map<Integer, City> findById(Long id){
        Map<Integer, City> map = new HashMap<>();
        for(int i=0;i<this.getCityCollection().size();i++) {
            if(this.getCityCollection().get(i).getId().equals(id)) {
                map.put(i, this.getCityCollection().get(i));
                return map;
            }
        }

        return null;
    }

    /**
     * находит элементы по его имени
     * @param name
     * @return
     */
    public ArrayList<City> findByName(String name){
        ArrayList<City> cities = new ArrayList<City>();
        this.getCityCollection().forEach(x ->{
            if(x.getName().contains(name))
                cities.add(x);
        });

        return cities;
    }

    /**
     * загрузка xml из файла
     * @throws IOException
     */
    private void load() throws IOException {
        try{
            if(!xmlCollection.canWrite() || !xmlCollection.canRead()) throw new SecurityException();
        }catch (SecurityException ex){
            System.out.println("Файл недоступен");
            System.exit(1);
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(xmlCollection))) {
            System.out.println("Загружаем коллекцию из файла...");

            JAXBContext jaxbContext = JAXBContext.newInstance(Cities.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            citiesArray = (Cities) jaxbUnmarshaller.unmarshal(inputStreamReader);
            collection = citiesArray.getCities();
            citiesArray.getCities().forEach(x-> {
                if(maxId < x.getId())
                    maxId = x.getId();
            });

            checkDuplicateId();

        } catch (JAXBException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }

    /**
     * очищает коллекцию
     */
    public void clear(){
        this.getCityCollection().clear();
    }


    /**
     * сохранить коллекцию в xml
     */
    public void save(){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(xmlCollection));
            JAXBContext jaxbContext = JAXBContext.newInstance(Cities.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(citiesArray, outputStreamWriter);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * перемешивает элементы в случайном порядке
     */
    public void shuffle(){
        Collections.shuffle(this.getCityCollection());
    }

    public ArrayList<City> sortById(){
        ArrayList<City> sortColl = new ArrayList<City>(this.getCityCollection());
        sortColl.sort(Comparator.comparing(City::getId));

        return sortColl;
    }

    /**
     * сортирует по часовому поясу
     * @return
     */
    public ArrayList<City> sortByTimezone(){
        ArrayList<City> sortColl = new ArrayList<City>(this.getCityCollection());
        sortColl.sort(Comparator.comparing(City::getTimezone));

        return sortColl;
    }

    /**
     * находит элементы с одинаковыми id
     */
    private void checkDuplicateId(){
        List<City> cities = sortById();

        for(int i=1;i<cities.size();i++) {
            if(cities.get(i-1).getId().equals(cities.get(i).getId())) {
                throw new DuplicateIdException("Поле id должно быть уникальным");
            }
        }
    }



    public List<City> getCityCollection() {
        return collection;
    }

    public void setCityCollection(ArrayList<City> collection){
        this.collection = collection;
    }

    public LocalDate getInitDate() {
        return initDate;
    }
}
