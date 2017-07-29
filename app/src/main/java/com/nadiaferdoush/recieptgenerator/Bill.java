package com.nadiaFerdoush.recieptgenerator;

public class Bill {
    private float grossAmount;
    private float paidAmount;
    private float netAmount;
    private float changeAmount;
    private float vatPt;
    private float discountPt;
    private String timeCreated;
    private int tableNumber;
    private int paymentType;

    public Bill(float grossAmount, float paidAmount, float netAmount, float changeAmount, float vatPt,
                float discountPt, String timeCreated, int tableNumber, int paymentType) {

        this.grossAmount = grossAmount;
        this.paidAmount = paidAmount;
        this.netAmount = netAmount;
        this.changeAmount = changeAmount;
        this.vatPt = vatPt;
        this.discountPt = discountPt;
        this.timeCreated = timeCreated;
        this.tableNumber = tableNumber;
        this.paymentType = paymentType;
    }

    public float getGrossAmount() {
        return grossAmount;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public float getNetAmount() {
        return netAmount;
    }

    public float getChangeAmount() {
        return changeAmount;
    }

    public float getVatPt() {
        return vatPt;
    }

    public float getDiscountPt() {
        return discountPt;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getPaymentType() {
        return paymentType;
    }
}
