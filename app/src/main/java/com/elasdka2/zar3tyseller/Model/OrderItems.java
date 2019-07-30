package com.elasdka2.zar3tyseller.Model;

public class OrderItems {
    private String key;
    private String price;
    private String title;
    private String quantity;
    private String date;
    private String itemimg;
    private String sellerid;
    private String customerid;
    private String customerimg;
    private String customername;

    public OrderItems() {
    }

    public OrderItems(String key, String price, String title, String quantity, String date, String itemimg, String sellerid, String customerid, String customerimg, String customername) {
        this.key = key;
        this.price = price;
        this.title = title;
        this.quantity = quantity;
        this.date = date;
        this.itemimg = itemimg;
        this.sellerid = sellerid;
        this.customerid = customerid;
        this.customerimg = customerimg;
        this.customername = customername;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemimg() {
        return itemimg;
    }

    public void setItemimg(String itemimg) {
        this.itemimg = itemimg;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomerimg() {
        return customerimg;
    }

    public void setCustomerimg(String customerimg) {
        this.customerimg = customerimg;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }
}
