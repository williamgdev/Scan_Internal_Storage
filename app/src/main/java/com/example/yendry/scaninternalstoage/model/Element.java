package com.example.yendry.scaninternalstoage.model;


import io.realm.RealmObject;

public abstract class Element{
    public String parent;
    public String parentPath;
    public String name;
    public ElementType.type type;

}
