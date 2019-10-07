package com.vidoto.visitech.visiTech.Model;

public class TpVisit {
    private String tp_visit, department;

    public TpVisit() {
    }

    public TpVisit(String tp_visit, String department) {
        this.tp_visit = tp_visit;
        this.department = department;
    }

    public String getTp_visit() {
        return tp_visit;
    }

    public void setTp_visit(String tp_visit) {
        this.tp_visit = tp_visit;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
