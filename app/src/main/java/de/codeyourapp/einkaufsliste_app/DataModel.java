package de.codeyourapp.einkaufsliste_app;

public class DataModel {
    private String user_token;
    private String product_name;
    private double quantity;
    private String unit;
    //private int settings;

    public DataModel(String user_token, String product_name, double quantity, String unit) {
        this.user_token = user_token;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unit = unit;
        //this.settings = settings;
    }


    public String getUserToken() {
        return user_token;
    }
    public void setUserToken(String user_token) {
        this.user_token = user_token;
    }


    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
