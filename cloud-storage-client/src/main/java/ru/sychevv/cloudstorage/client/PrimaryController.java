package ru.sychevv.cloudstorage.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.sychevv.cloudstorage.common.FileData;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
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

        TableColumn<FileData, Long> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSize()));
        sizeColumn.setCellFactory(fileDataLongTableColumn -> {
            return new TableCell<FileData, Long>() {
                @Override
                protected void updateItem(Long value, boolean empty) {
                    super.updateItem(value, empty);
                    String displayText = null;
                    if (value == null || empty) {
                    } else if (value == -1L) {
                        displayText = "DIR";
                    } else {
                        displayText = String.format("%,d b", value);
                    }
                    setText(displayText);
                }
            };
        });
        sizeColumn.setPrefWidth(120.);

        TableColumn<FileData, String> modifiedColumn = new TableColumn<>("Modified");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        modifiedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModified().format(formatter)));

        localFiles.getColumns().addAll(typeColumn, nameColumn, sizeColumn, modifiedColumn);
        localFiles.getSortOrder().add(typeColumn);
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
