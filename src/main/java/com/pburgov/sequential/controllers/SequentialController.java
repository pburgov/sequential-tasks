package com.pburgov.sequential.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.pburgov.sequential.tasks.AbstractCustomTask;
import com.pburgov.sequential.tasks.DeletePreciosInSAPTask;
import com.pburgov.sequential.tasks.UpdateFactorsInPSTask;
import com.pburgov.sequential.tasks.UpdatePreciosInSAPTask;
import com.pburgov.sequential.tasks.UpdateWebNamesInFNSAPTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SequentialController implements Initializable {

    private static String FINISHED_TASK = "FINISHED (%1$d/%2$d) TASKS";

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane mainPane;
    @FXML
    private VBox vbox;
    @FXML
    private JFXButton bttSequential;
    @FXML
    private JFXButton bttParallel;
    @FXML
    private Label lblFinishedTasks;

    private List <Task> tasks;
    private SimpleIntegerProperty numOfUnfinishedTasks;

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        numOfUnfinishedTasks = new SimpleIntegerProperty();
        tasks = new ArrayList <>();
        mainPane.heightProperty().addListener(( obs, oldValue, newValue ) -> vbox.setPrefHeight(newValue.doubleValue()));
        mainPane.widthProperty().addListener(( obs, oldValue, newValue ) -> vbox.setPrefWidth(newValue.doubleValue()));
        //vbox.setFillWidth(true);
        vbox.setSpacing(20.0);

        Label placeholder = new Label("Click on one of the buttons below");
        placeholder.getStyleClass().add("placeholder");

        vbox.getChildren().add(placeholder);
        vbox.alignmentProperty().setValue(Pos.CENTER);

        bttSequential.setOnAction(event -> {
            setUpTasks();
            resetScene();
            runSequentially();
        });

        bttParallel.setOnAction(event -> {
            setUpTasks();
            resetScene();
            runParallely();
        });

        bttSequential.disableProperty().bind(numOfUnfinishedTasks.greaterThan(0));
        bttSequential.setFocusTraversable(false);
        bttParallel.disableProperty().bind(numOfUnfinishedTasks.greaterThan(0));
        bttParallel.setFocusTraversable(false);
        lblFinishedTasks.setAlignment(Pos.CENTER);
    }

    private void setUpTasks() {
        int listSize = 20;

        DeletePreciosInSAPTask deletePreciosInSAPTask = new DeletePreciosInSAPTask(listSize);
        UpdatePreciosInSAPTask updatePreciosInSAPTask = new UpdatePreciosInSAPTask(listSize);
        UpdateFactorsInPSTask updateFactorsInPSTask = new UpdateFactorsInPSTask(listSize);
        UpdateWebNamesInFNSAPTask updateWebNamesInFNSAPTask = new UpdateWebNamesInFNSAPTask(listSize);

        tasks = new ArrayList <>();
        tasks.add(deletePreciosInSAPTask);
        tasks.add(updatePreciosInSAPTask);
        tasks.add(updateFactorsInPSTask);
        tasks.add(updateWebNamesInFNSAPTask);

        numOfUnfinishedTasks.setValue(tasks.size());
    }

    private void setUpScene() {
        tasks.forEach(task -> {
            Label lblTitle = new Label(task.getTitle());
            lblTitle.getStyleClass().add("label-title");

            Label lblText = new Label();
            lblText.getStyleClass().add("label-message");
            lblText.textProperty().bind(task.messageProperty());

            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(0.0);
            progressBar.setMinWidth(300.0);

            JFXSpinner spinner = new JFXSpinner();
            spinner.setProgress(0.0);
            spinner.progressProperty().bind(task.progressProperty());

            Label lblResult = new Label();
            lblResult.textProperty().bind(task.valueProperty());
            lblResult.getStyleClass().add("label-title");
            lblResult.setMinWidth(90.0);

            Hyperlink linkError = new Hyperlink();
            linkError.setText("Error ...");
            linkError.setMinWidth(50.0);
            linkError.setVisible(false);
            linkError.setOnAction(event -> {
                showErrorDialog(((AbstractCustomTask) task).getThrowable());
            });

            task.onCancelledProperty().setValue(new EventHandler <WorkerStateEvent>() {
                @Override
                public void handle( WorkerStateEvent event ) {
                    linkError.setVisible(true);
                }
            });

            HBox hBoxBottom = new HBox(15.0);
            hBoxBottom.setPadding(new Insets(2));
            hBoxBottom.getChildren().addAll(progressBar, spinner, lblResult, linkError);
            hBoxBottom.alignmentProperty().setValue(Pos.CENTER_LEFT);

            VBox taskVBox = new VBox(5.0);
            Separator separator = new Separator();
            taskVBox.getChildren().addAll(lblTitle, lblText, hBoxBottom, separator);
            taskVBox.setDisable(true);
            vbox.getChildren().addAll(taskVBox);
            lblFinishedTasks.setText(String.format(FINISHED_TASK, 0, tasks.size()));
            task.stateProperty().addListener(( observable, oldValue, newValue ) -> {
                if ( newValue == Worker.State.RUNNING ) {
                    progressBar.progressProperty().unbind();
                    progressBar.progressProperty().bind(task.progressProperty());
                    taskVBox.setDisable(false);
                }
                if ( newValue == Worker.State.SUCCEEDED ||
                         newValue == Worker.State.CANCELLED ||
                         newValue == Worker.State.FAILED ) {
                    numOfUnfinishedTasks.setValue(numOfUnfinishedTasks.get() - 1);
                    lblFinishedTasks.setText(String.format(FINISHED_TASK, tasks.size() - numOfUnfinishedTasks.get(), tasks.size()));

                }
            });
        });
    }

    private void resetScene() {
        if ( vbox.getChildren().size() > 0 ) {
            vbox.getChildren().removeAll(vbox.getChildren());
        }
        setUpScene();
    }

    private void runSequentially() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        tasks.forEach(task -> {
            executor.submit(task);
        });
        executor.shutdown();
    }

    private void runParallely() {
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        tasks.forEach(task -> {
            executor.submit(task);
        });
        executor.shutdown();
    }
    private void  showErrorDialog(Throwable exceptionTask) {
        try {
            AnchorPane root = null;
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/dialogError.fxml"));
            ErrorDialogController errorDialogController = new ErrorDialogController(exceptionTask);
            loader.setController(errorDialogController);
            try {
                root = loader.load();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Error Description");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.sizeToScene();
            stage.show();
//            ScenicView.show(scene);
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}

