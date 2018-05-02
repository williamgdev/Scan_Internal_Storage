package com.example.yendry.scaninternalstoage.model;


import java.util.ArrayList;
import java.util.List;

public class ElementDirectory extends Element {
   private List<Element> childList = new ArrayList<>();


    public List<Element> getChildList() {
        return childList;
    }

    public void setChildList(List<Element> childList) {
        this.childList = childList;
    }
}
