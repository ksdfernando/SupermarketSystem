package com.ksd.supermarket.supermarketsystem.services;

public class Product {
    private String subCode;
    private String name;
    private String barcode;
    private Float unitPrice;
    private Float totalQty;
    private Float totalPrice;
    private String sinhalaName;
    private Float myQTY;
    private Float marketPrice;



    public Product(String subCode, String name, String barcode, Float unitPrice, Float totalQty, Float totalPrice, String sinhalaName, Float myQTY, Float marketPrice) {
        this.subCode = subCode;
        this.name = name;
        this.barcode = barcode;
        this.unitPrice = unitPrice;
        this.totalQty = totalQty;
        this.totalPrice = totalPrice;
        this.sinhalaName = sinhalaName;
        this.myQTY = myQTY;
        this.marketPrice = marketPrice;
    }

    public Float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSinhalaName() {
        return sinhalaName;
    }

    public void setSinhalaName(String sinhalaName) {
        this.sinhalaName = sinhalaName;
    }

    public Float getMyQTY() {
        return myQTY;
    }

    public void setMyQTY(Float myQTY) {
        this.myQTY = myQTY;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Float getTotalQty() {
        return totalQty;
    }

    public Product setTotalQty(Float totalQty) {
        this.totalQty = totalQty;
        return null;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "subCode='" + subCode + '\'' +
                ", name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", unitPrice=" + unitPrice +
                ", totalQty=" + totalQty +
                ", totalPrice=" + totalPrice +
                ", sinhalaName='" + sinhalaName + '\'' +
                ", myQTY=" + myQTY +
                ", marketPrice=" + marketPrice +
                '}';
    }
}