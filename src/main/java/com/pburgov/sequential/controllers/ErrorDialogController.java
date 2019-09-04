package com.pburgov.sequential.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ErrorDialogController implements Initializable {

    private Throwable exceptionTask;

    @FXML
    private TextArea errorMessage;

    @FXML
    private TextArea errorStackTrace;

    @FXML
    private JFXButton bttClose;

    @FXML
    private VBox vbox;

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        errorMessage.setText(exceptionTask.getMessage().trim());
        errorMessage.setEditable(false);
        errorStackTrace.setText(getErrorStackTrace());
        errorStackTrace.setEditable(false);

        VBox.setVgrow(errorStackTrace, Priority.ALWAYS);
        bttClose.setOnAction(event -> {
            Stage stage = (Stage) bttClose.getScene().getWindow();
            stage.close();
        });
    }

    public ErrorDialogController( Throwable exceptionTask ) {
        this.exceptionTask = exceptionTask;
    }

    private String getErrorStackTrace(){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exceptionTask.printStackTrace(pw);
        return sw.toString().trim();
    }
}
