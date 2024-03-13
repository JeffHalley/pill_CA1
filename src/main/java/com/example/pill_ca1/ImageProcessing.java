package com.example.pill_ca1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageProcessing {
    private BufferedImage originalImage;
    private DisjointSet disjointSet;

    public ImageProcessing(BufferedImage image) {
        this.originalImage = image;
        this.disjointSet = new DisjointSet(image.getWidth() * image.getHeight());
    }

    public int[][] processImage() {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int[][] outputImage = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = originalImage.getRGB(x, y);
                if (pixel == Color.BLACK.getRGB()) {
                    outputImage[x][y] = -1; // Set black pixels to -1
                } else {
                    outputImage[x][y] = disjointSet.find(y * width + x); // Assign disjoint set value
                }
            }
        }

        return outputImage;
    }

}
