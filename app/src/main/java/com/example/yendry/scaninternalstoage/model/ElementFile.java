package com.example.yendry.scaninternalstoage.model;



public class ElementFile extends Element {
   private String ext;
   private long size;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
