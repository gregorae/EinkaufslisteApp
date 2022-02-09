package Search;

import com.google.firebase.database.Exclude;

//L: Klasse für Konstrukt/Aufbau Datenbank-"Zweig" der ProduktSuche
public class Produkt {

    @Exclude    //L: Zweig Produktdata aus Produkttitel und Suchtitel(Unterschied Groß-/Klein für Suchfunktion)
    String title;
    String search;

    public Produkt(){}
    //Getter und Setter Produkttitel
    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}
    //Getter und Setter Suchtitel
    public String getSearch(){return this.search;}
    public void setSearch(String title){this.search = search;}
}
