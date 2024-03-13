package com.example.pill_ca1;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HelloController {

    public ColorPicker colorPicker;
    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Slider thresholdSlider;

    private Stage stage;

    private BufferedImage originalImage;

    private FileChooser fileChooser = new FileChooser();

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void openFile() {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null && selectedFile.exists()) {
            originalImage = loadImage(selectedFile);
            Image image = SwingFXUtils.toFXImage(originalImage, null);
            imageView1.setImage(image);
            updateImages();
        }
    }

    @FXML
    private void onSliderChanged() {
        updateImages();
    }

    public void updateImages() {
        if (originalImage != null) {
            double thresholdValue = thresholdSlider.getValue(); // Get the value from the slider and normalize it
            javafx.scene.paint.Color targetColor = colorPicker.getValue(); // Get the selected color from the color picker
            Image originalJavaFXImage = SwingFXUtils.toFXImage(originalImage, null); // Convert BufferedImage to JavaFX Image

            // Convert the image to black and white and get the processed image
            Image processedImage = toBlackAndWhite(originalJavaFXImage, targetColor, thresholdValue);

            // Set the newly processed image as the original image
            BufferedImage b = SwingFXUtils.fromFXImage(processedImage, null);

            // Update the imageView2 with the processed image
            imageView2.setImage(processedImage);

            // Process and print the new processed image
            processAndPrintImage(b);

        }
    }

    public static BufferedImage createBufferedImage(int[][] pixelValues) {
        int width = pixelValues.length;
        int height = pixelValues[0].length;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Convert pixel value to RGB color
                int rgbColor = pixelValues[x][y] << 16 | pixelValues[x][y] << 8 | pixelValues[x][y];
                bufferedImage.setRGB(x, y, rgbColor);
            }
        }

        return bufferedImage;
    }

    public Image processAndPrintImage(int[][] processedImage) {
        // Print out the processed image array
        for (int y = 0; y < processedImage[0].length; y++) {
            for (int x = 0; x < processedImage.length; x++) {
                System.out.print(processedImage[x][y] + " ");
            }
            System.out.println(); // Move to the next row
        }

        // Convert the processed image array to BufferedImage
        BufferedImage bufferedImage = createBufferedImage(processedImage);

        // Convert BufferedImage to JavaFX Image
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }


    private BufferedImage loadImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Assuming you have a method to process the image and print out the resulting arrays
    public void processAndPrintImage(BufferedImage originalImage) {
        // Create an instance of ImageProcessing class
        ImageProcessing imageProcessing = new ImageProcessing(originalImage);

        // Process the image
        int[][] processedImage = imageProcessing.processImage();

        // Print out the processed image array
        for (int y = 0; y < processedImage[0].length; y++) {
            for (int x = 0; x < processedImage.length; x++) {
                System.out.print(processedImage[x][y] + " ");
            }
            System.out.println(); // Move to the next row
        }
    }


    private Image toBlackAndWhite(Image fxImage, Color targetColor, double threshold) {
        System.out.println("Converting to Black and White with target color of:  " + targetColor + ":  and threshold of:   " + threshold);

        PixelReader pixelReader = fxImage.getPixelReader();
        WritableImage blackAndWhiteImage = new WritableImage((int) fxImage.getWidth(), (int) fxImage.getHeight());
        PixelWriter pixelWriter = blackAndWhiteImage.getPixelWriter();

        for (int x = 0; x < fxImage.getWidth(); x++) {
            for (int y = 0; y < fxImage.getHeight(); y++) {
                Color pixelColor = pixelReader.getColor(x, y);

                if (isColorCloseTo(pixelColor, targetColor, threshold)) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }

        return blackAndWhiteImage;
    }
    private boolean isColorCloseTo(Color color1, Color color2, double threshold) {
        return Math.abs(color1.getRed() - color2.getRed()) < threshold &&
                Math.abs(color1.getGreen() - color2.getGreen()) < threshold &&
                Math.abs(color1.getBlue() - color2.getBlue()) < threshold;
    }


    public void handleImageClick(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        // Convert BufferedImage to JavaFX Image
        Image fxImage = SwingFXUtils.toFXImage(originalImage, null);

        // Get PixelReader from JavaFX Image
        PixelReader pixelReader = fxImage.getPixelReader();

        // Get selected color
        Color selectedColor = pixelReader.getColor((int) x, (int) y);

        // Set the selected color in the color picker
        colorPicker.setValue(selectedColor);

        // Update the black and white image based on the newly selected color
        updateImages();
    }



}
