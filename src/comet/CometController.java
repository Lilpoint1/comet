/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comet;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tim
 */
public class CometController implements Initializable, Runnable {

    @FXML
    Pane spaceRoot;

    private Circle spawnArea;
    private Thread thread;

    private LinkedList<Comet> cometList;
    public static boolean isRunning;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cometList = new LinkedList<>();
        spawnArea = new Circle();

        spawnArea.setCenterX(300);
        spawnArea.setCenterY(200);
        spawnArea.setRadius(80);

        spaceRoot.getChildren().add(spawnArea);

        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }

    private void stop() {

        Platform.runLater(() -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(CometController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public void run() {

        while (isRunning) {
            spawnComet();

            try {
                Thread.sleep(8);
            } catch (InterruptedException ex) {
                Logger.getLogger(CometController.class.getName()).log(Level.SEVERE, null, ex);
            }
            moveComet();

            removeComet();
        }

        stop();
    }

    private double generateXCoordinate() {
        return Math.random() * 400 + 100;
    }

    private double generateYCoordinate() {
        return Math.random() * 300 + 50;
    }

    private void removeComet() {
        LinkedList<Comet> removedComet = new LinkedList<>();
        for (Comet comet : cometList) {
            if (comet.isDone()) {

                removedComet.add(comet);
            }
        }

        this.cometList.removeAll(removedComet);
        Platform.runLater(() -> {
            this.spaceRoot.getChildren().removeAll(removedComet);
        });
    }

    private void spawnComet() {
        double X = generateXCoordinate();
        double Y = generateYCoordinate();

        Comet comet = new Comet(X, Y);

        if (cometList.size() < 500) {
            if (comet.hasCollision(spawnArea)) {
                cometList.add(comet);
                showComet(comet);
            }

        }

    }

    private void showComet(Comet comet) {
        Platform.runLater(() -> {
            spaceRoot.getChildren().add(comet);
        });
    }

    private void moveComet() {
        for (Comet comet : cometList) {
            comet.move();
        }
    }

}//end controller
