package ru.sychevv.cloudstorage.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    VBox local, remote;

    FileController localFC, remoteFC;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        localFC = (FileController) local.getProperties().get("controller");
        remoteFC = (FileController) remote.getProperties().get("controller");
    }

    public void btnCopyToRemoteAction(ActionEvent actionEvent) {
        copy(localFC, remoteFC);
    }

    public void btnCopyToLocalAction(ActionEvent actionEvent) {
        copy(remoteFC, localFC);
    }

    private void copy(FileController source, FileController target) {
        String selectedFileName = source.getSelectedFileName();
        if (selectedFileName == null) {
            Alert no_file_selected = new Alert(Alert.AlertType.INFORMATION, "No file selected");
            no_file_selected.showAndWait();
            return;
        }
        Path sourcePath = Paths.get(source.getCurrentPath(), selectedFileName);
        Path targetPath = Paths.get(target.getCurrentPath()).resolve(selectedFileName);
        try {
            Files.copy(sourcePath, targetPath);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
        target.updateTable();
    }
}
