package com.elasdka2.zar3tyseller.Model;

public class SellerItems {
    private String key;
    private String description;
    private String price;
    private String title;
    private String category;
    private String maincategory;
    private String quantity;
    private String id;
    private String date;
    private String date_to_show;
    private String img_uri;
    //-------------------------------------

    public SellerItems() {
    }

    public SellerItems(String description, String price, String title, String category, String maincategory, String quantity, String id, String date, String date_to_show, String img_uri) {
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
        this.maincategory = maincategory;
        this.quantity = quantity;
        this.id = id;
        this.date = date;
        this.date_to_show = date_to_show;
        this.img_uri = img_uri;
    }

    public String getMaincategory() {
        return maincategory;
    }

    public void setMaincategory(String maincategory) {
        this.maincategory = maincategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_to_show() {
        return date_to_show;
    }

    public void setDate_to_show(String date_to_show) {
        this.date_to_show = date_to_show;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }


}
