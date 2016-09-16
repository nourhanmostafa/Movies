package com.example.nourhan.movies;

/**
 * Created by Nourhan on 9/4/2016.
 */
public class Trailer {
    private String id;
    private String key;
    private String name;

    public Trailer(String id,String key,String name){
        this.id=id;
        this.key=key;
        this.name=name;
    }

    public String getId(){
        return id;
    }

    public String getKey(){
        return key;
    }
    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
