package com.vidoto.visitech.visiTech.Model;

public class Visit {
    private String tp_visit, product, customer, call_erp;

    public Visit() {
    }

    public Visit(String tp_visit, String product, String customer, String call_erp) {
        this.tp_visit = tp_visit;
        this.product = product;
        this.customer = customer;
        this.call_erp = call_erp;
    }

    public String getTp_visit() {
        return tp_visit;
    }

    public void setTp_visit(String tp_visit) {
        this.tp_visit = tp_visit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCall_erp() {
        return call_erp;
    }

    public void setCall_erp(String call_erp) {
        this.call_erp = call_erp;
    }
}
