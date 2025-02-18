package com.example.capteurs;

public class SliderItem {
    private int image;
    private String title;

    public SliderItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
