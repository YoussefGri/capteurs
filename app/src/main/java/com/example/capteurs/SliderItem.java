package com.example.capteurs;

public class SliderItem {
    private int image;
    private String title;
    private Class<?> targetActivity;

    public SliderItem(int image, String title, Class<?> targetActivity) {
        this.image = image;
        this.title = title;
        this.targetActivity = targetActivity;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }
}
