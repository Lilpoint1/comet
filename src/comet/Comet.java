/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comet;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 *
 * @author Tim
 */
public class Comet extends Circle {

    private double moveToX;
    private double moveToY;

    private boolean alreadyMoving;
    private boolean done;
    private boolean scaling;

    public Comet(double x, double y) {

        this.setRadius(generateRadius());
        this.setFill(Color.GRAY);
        this.setCenterX(x);
        this.setCenterY(y);
        this.setOpacity(0);

        alreadyMoving = false;
        scaling = false;
        done = false;

        moveTo();
        scaling();

    }

    private double generateRadius() {
        double multiplier = (Math.random() * 1.5) + 0.75;
        double r = (Math.random() * 2) + 2;

        return r * multiplier;
    }

    public boolean hasCollision(Circle circle) {
        double diffX = this.getCenterX() - circle.getCenterX();
        double diffY = this.getCenterY() - circle.getCenterY();
        double radiusSum = this.getRadius() + circle.getRadius();
        return diffX * diffX + diffY * diffY <= radiusSum * radiusSum; // true if collision

    }

    private void moveTo() {
        double xCoordinate = getX() - 300;
        double yCoordinate = 200 - getY();

        for (int i = 1; true; i++) {
            if (xCoordinate > 0 && yCoordinate > 0) {
                double screenX = getX() + (xCoordinate * i);
                double screenY = getY() - (yCoordinate * i);
                if (screenX >= 625 || screenY <= -25) {
                    this.moveToX = screenX;
                    this.moveToY = screenY;
                    break;
                }

            } else if (xCoordinate > 0 && yCoordinate < 0) {
                double screenX = getX() + (xCoordinate * i);
                double screenY = getY() - (yCoordinate * i);
                if (screenX >= 625 || screenY >= 425) {
                    this.moveToX = screenX;
                    this.moveToY = screenY;
                    break;
                }

                //
            } else if (xCoordinate < 0 && yCoordinate < 0) {
                double screenX = getX() + (xCoordinate * i);
                double screenY = getY() - (yCoordinate * i);
                if (screenX <= -25 || screenY >= 425) {
                    this.moveToX = screenX;
                    this.moveToY = screenY;
                    break;
                }
//
            } else if (xCoordinate < 0 && yCoordinate > 0) {
                double screenX = getX() + (xCoordinate * i);
                double screenY = getY() - (yCoordinate * i);
                if (screenX <= -25 || screenY <= - 25) {
                    this.moveToX = screenX;
                    this.moveToY = screenY;
                    break;
                }

            } else if (xCoordinate == 0 && yCoordinate == 0) {
//                this.moveToX = Math.random() * 600;
                break;

            }
        }
    }

    public void move() {

        if (alreadyMoving) {
            return;
        }

        Line line = new Line(getX(), getY(), moveToX, moveToY);

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.seconds(5));
//        pt.setNode(this);
        pt.setPath(line);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(1);
        pt.setAutoReverse(false);
        pt.setOnFinished((e) -> {
            done = true;
        });

        FadeTransition ft = new FadeTransition();
        ft.setDuration(Duration.seconds(4));
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);

        ScaleTransition st = new ScaleTransition();
        if (scaling) {
            st.setDuration(Duration.seconds(5));
            st.setByX(1.35f);
            st.setByY(1.35f);
            st.setCycleCount(1);
            st.setAutoReverse(false);
        }

        ParallelTransition parallel = new ParallelTransition(this, pt, ft, st);
        parallel.play();

//        pt.play();
        alreadyMoving = true;

    }

    private void scaling() {
        double rand = Math.random();
        if (rand <= 0.125) {
            scaling = true;
        }
    }

    public double getX() {
        return this.getCenterX();
    }

    public double getY() {
        return this.getCenterY();
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        String input
                = "X: " + getX() + "\n"
                + "Y: " + getY() + "\n"
                + "TranslateX: " + getTranslateX() + "\n"
                + "TranslateY: " + getTranslateY() + "\n"
                + "R: " + getRadius() + "\n"
                + "MoveX: " + moveToX + "\n"
                + "MoveY: " + moveToY;
        return input;
    }

}
