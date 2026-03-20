package com.qjprojects.AA_History.DTO;

public class CategoryCreateRequest {

    private String name;

    public CategoryCreateRequest(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}