package de.codeyourapp.einkaufsliste_app;

public class DataModel {
    private String user_token;
    private String product_name;
    private String quantity;
    private String unit;
    private String key;
    private String notice;
    //private int settings;


    public DataModel(){}

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public DataModel(String user_token, String product_name, String quantity, String unit) {
        this.user_token = user_token;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unit = unit;
        this.key = key;
        this.notice = notice;
        //this.settings = settings;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
