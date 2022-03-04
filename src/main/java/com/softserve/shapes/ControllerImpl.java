package com.softserve.shapes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ControllerImpl implements Controller {

    @FXML
    Canvas canvas;

    @FXML
    Label label1;

    @FXML
    TextField textField;

    @FXML
    Label errorLabel;

    private List<Polygon> shapes = new ArrayList<>();

    @FXML
    private void applyKeyPressed(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            String[] parts = textField.getText().split("\\s+");
            switch (parts[0]) {
                case "C":
                    createCanvas(parts);
                    break;

                case "L":
                    drawLine(parts);
                    break;

                case "R":
                    drawRectangle(parts);
                    break;

                case "F":
                    fill(parts);
                    break;

                case "Q":
                    quit();
            }
        }
    }

    private Polygon getMatch(double x, double y) {
        Polygon match = null;
        double leastArea = Double.MAX_VALUE;

        for (Polygon p : shapes) {
            List<Double> points = p.getPoints();
            double area = (points.get(2) - points.get(0)) * (points.get(3) - points.get(1));
            if (x >= points.get(0) && x <= points.get(2) &&
                y >= points.get(1) && y <= points.get(3) &&
                area < leastArea) {
                match = p;
                leastArea = area;
            }
        }

        return match;
    }

    /**
     * @param parts a string array with 3 components
     * @author Timothy Joseph Biegeleisen
     *
     * Usage: C w h
     *        w is the width and h the height of the canvas
     */
    @Override
    public void createCanvas(String[] parts) {
        double width = Double.parseDouble(parts[1]);
        double height = Double.parseDouble(parts[2]);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(width);
        canvas.setHeight(height);
        shapes.clear();
    }

    /**
     * @param parts a string array with 5 components
     * @author Timothy Joseph Biegeleisen
     *
     * Usage: L x1 y1 x2 y2
     *        (x1, y1) and (x2, y2) are the start of end of the line
     *        lines may only be vertical or horizontal and must lie within the bounds of the canvas
     */
    @Override
    public void drawLine(String[] parts) {
        double x1 = Double.parseDouble(parts[1]);
        double y1 = Double.parseDouble(parts[2]);
        double x2 = Double.parseDouble(parts[3]);
        double y2 = Double.parseDouble(parts[4]);

        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 ||
            x1 > canvas.getWidth() || x2 > canvas.getWidth() ||
            y1 > canvas.getHeight() || y2 > canvas.getHeight()) {
            errorLabel.setText("All coordinates must lie on the canvas");
            return;
        }

        if (x1 != x2 && y1 != y2) {
            errorLabel.setText("Only vertical and horizontal lines are allowed");
            return;
        }

        errorLabel.setText("");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeLine(x1, y1, x2, y2);
    }

    /**
     * @param parts a string array with 5 components
     * @author Timothy Joseph Biegeleisen
     *
     * Usage: R x1 y1 x2 y2
     *        (x1, y1) and (x2, y2) are the two corners of the rectangle
     *        both points must lie within the bounds of the canvas
     */
    @Override
    public void drawRectangle(String[] parts) {
        double x1 = Double.parseDouble(parts[1]);
        double y1 = Double.parseDouble(parts[2]);
        double x2 = Double.parseDouble(parts[3]);
        double y2 = Double.parseDouble(parts[4]);

        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 ||
                x1 > canvas.getWidth() || x2 > canvas.getWidth() ||
                y1 > canvas.getHeight() || y2 > canvas.getHeight()) {
            errorLabel.setText("All coordinates must lie on the canvas");
            return;
        }

        errorLabel.setText("");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeRect(x1, y1, x2, y2);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[] { x1, y1, x2, y2 });
        shapes.add(polygon);
    }

    /**
     * @param parts a string array with 3 components
     * @author Timothy Joseph Biegeleisen
     *
     * Usage: F x y
     *        (x, y) should be a point lying within an existing rectangle
     *        (x, y) should also lie within the bounds of the canvas
     */
    @Override
    public void fill(String[] parts) {
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);

        if (x < 0 || y < 0 || x > canvas.getWidth() || y > canvas.getHeight()) {
            errorLabel.setText("Fill coordinates must lie on the canvas");
            return;
        }

        Polygon p = getMatch(x, y);

        if (p != null) {
            List<Double> points = p.getPoints();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLUE);
            gc.fillRect(points.get(0), points.get(1), points.get(2), points.get(3));
        }
    }

    /**
     * @author Timothy Joseph Biegeleisen
     *
     * Usage: Q
     *        Terminates the JavaFX canvas application
=     */
    @Override
    public void quit() {
        Platform.exit();
    }
}