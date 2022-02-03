package Person;

import java.util.Locale;

// Klasse Person
public class Person {
    private String name;
    private String color;
    private String short_name;

    public Person() {
    }

    // Im Konstruktor wird zusätzlich eine Variable erstellt,
    // die nochmla extra die ersten 2 Zeichen des Namens abspeichert
    public Person(String name, String color)
    {
        this.name = name;
        this.color = color;
        this.short_name = this.name.substring(0, 2).toUpperCase();
    }

    // Getter und Setter
    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() { return color; }

    public void setColor(String color) {
        this.color = color;
    }
}