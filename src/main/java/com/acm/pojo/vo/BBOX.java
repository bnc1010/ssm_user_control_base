package com.acm.pojo.vo;

public class BBOX {
    private String className;

    private double rate;

    private float xmin;

    private float ymin;

    private float xmax;

    private float ymax;

    public BBOX(){}

    public BBOX(String ss){
        String[] data = ss.split(" ");
        this.className = data[0];
        this.rate = Double.parseDouble(data[1]);
        this.xmin = Float.parseFloat(data[2]);
        this.ymin = Float.parseFloat(data[3]);
        this.xmax = Float.parseFloat(data[4]);
        this.ymax = Float.parseFloat(data[5]);
    }

    public double getRate() {
        return rate;
    }

    public float getXmax() {
        return xmax;
    }

    public float getXmin() {
        return xmin;
    }

    public float getYmax() {
        return ymax;
    }

    public float getYmin() {
        return ymin;
    }

    public String getClassId() {
        return className;
    }

    public void setClassId(String className) {
        this.className = className;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

}
