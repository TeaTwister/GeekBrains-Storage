package ru.sychevv.cloudstorage.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.sychevv.cloudstorage.common.FileData;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {
    @FXML
    TableView<FileData> localFiles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileData, String> typeColumn = new TableColumn<>("T");
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        typeColumn.setPrefWidth(24.);

        TableColumn<FileData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        nameColumn.setPrefWidth(240.);

        TableColumn<FileData, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> new SimpleStringProperty(Double.toString(data.getValue().getSize())));
        sizeColumn.setPrefWidth(120.);

        localFiles.getColumns().addAll(typeColumn, nameColumn, sizeColumn);
        updateTable(Paths.get("."), localFiles);
    }

    public void updateTable(Path path, TableView<FileData> table) {
        try {
            table.getItems().clear();
            table.getItems().addAll(Files.list(path).map(FileData::new).toList());
            table.sort();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING, "Oops!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
