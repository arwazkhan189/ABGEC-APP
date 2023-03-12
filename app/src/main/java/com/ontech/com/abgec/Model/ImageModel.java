package com.ontech.com.abgec.Model;

public class ImageModel {

    private String imagepath;
    private String text;

    public ImageModel() {
    }

    public ImageModel(String imagepath, String text) {
        this.imagepath = imagepath;
        this.text = text;
    }
    
    public String getImagepath() {
        return imagepath;
    }

    public String getText() {
        return text;
    }
}
