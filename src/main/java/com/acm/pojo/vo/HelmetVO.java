package com.acm.pojo.vo;

public class HelmetVO {

    private String resultImage;

    private BBOX[] res;

    public BBOX[] getRes() {
        return res;
    }

    public String getResultImage() {
        return resultImage;
    }

    public void setRes(BBOX[] res) {
        this.res = res;
    }

    public void setResultImage(String resultImage) {
        this.resultImage = resultImage;
    }
}
