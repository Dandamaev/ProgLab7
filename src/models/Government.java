package models;

import exceptions.InvalidValueException;

import java.io.Serializable;

public enum Government implements Serializable {
    MATRIARCHY(1),
    MERITOCRACY(2),
    NOOCRACY(3),
    PLUTOCRACY(4);

    private int id;

    Government(int id){
        this.id = id;
    }

    public static Government byOrdinal(int s) {
        for (Government value : Government.values()) {
            if (value.getId() == s) {
                return value;
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public static Government byName(String s) {
        for (Government value : Government.values()) {
            if (value.equals(s.toUpperCase())) {
                return value;
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public int getId() {
        return id;
    }
}