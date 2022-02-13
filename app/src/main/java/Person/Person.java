package Person;

import com.google.firebase.database.Exclude;

// Klasse Person
public class Person {
    @Exclude
    private String key;
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
        if(name.length()>2) {
            this.short_name = this.name.substring(0, 2).toUpperCase();
        }
        else{
            this.short_name = this.name.toUpperCase();
        }
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
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}