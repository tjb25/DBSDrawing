package com.softserve.shapes;

public interface Controller {
    public void createCanvas(String[] parts);

    public void drawLine(String[] parts);

    public void drawRectangle(String[] parts);

    public void fill(String[] parts);

    public void quit();
}