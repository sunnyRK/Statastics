package com.classify.statastics;

/**
 * Created by ADMIN on 02-02-2018.
 */

public class patient_class {

    public String patient_no;
    public String name;
    public String blood;
    public String breath;
    public String pulse;
    public String temp;

    public patient_class(){
    }

    public patient_class(String patient_no) {
        this.patient_no = patient_no;
    }



    public patient_class(String patient_no, String name, String blood, String breath, String pulse, String temp) {
        this.patient_no = patient_no;
        this.name = name;
        this.blood = blood;
        this.breath = breath;
        this.pulse = pulse;
        this.temp = temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getBreath() {
        return breath;
    }

    public void setBreath(String breath) {
        this.breath = breath;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }


    public String getPatient_no() {
        return patient_no;
    }

    public void setPatient_no(String patient_no) {
        this.patient_no = patient_no;
    }
}
