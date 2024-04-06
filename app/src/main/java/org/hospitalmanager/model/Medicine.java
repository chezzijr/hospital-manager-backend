package org.hospitalmanager.model;

import java.util.ArrayList;
import java.util.Date;

public class Medicine {

    private String medicineName;
    private String barCode;
    private String description;
    private String manufacturer;
    private Integer price;
    private Date expiryDate;
    private String activeIngredients;
    private String dosage;
    private String medicineType;
    private Integer inventoryStatus;


    public Medicine(String medicineName, String barCode, String description, String manufacturer, Integer price, Date expiryDate, String activeIngredients, String dosage, String medicineType, Integer inventoryStatus) {
        this.medicineName = medicineName;
        this.barCode = barCode;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.expiryDate = expiryDate;
        this.activeIngredients = activeIngredients;
        this.dosage = dosage;
        this.medicineType = medicineType;
        this.inventoryStatus = inventoryStatus;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public Integer getInventoryStatus() {
        return inventoryStatus;
    }

    public Integer getPrice() {
        return price;
    }

    public String getActiveIngredients() {
        return activeIngredients;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getDescription() {
        return description;
    }

    public String getDosage() {
        return dosage;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
