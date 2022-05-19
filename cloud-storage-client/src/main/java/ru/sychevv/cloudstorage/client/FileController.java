package ru.sychevv.cloudstorage.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.sychevv.cloudstorage.common.FileData;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FileController implements Initializable {
    @FXML
    TextField pathField;

    @FXML
    TableView<FileData> fileDataTableView;

    @FXML
    ComboBox<String> panelCB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPanelCB();
        initFileDataTableView();
    }

    private void initPanelCB() {
        panelCB.getItems().clear();
        for (Path rootDirectory : FileSystems.getDefault().getRootDirectories()) {
            panelCB.getItems().add(rootDirectory.toString());
        }
        panelCB.getSelectionModel().select(0);
    }

    private void initFileDataTableView() {
        TableColumn<FileData, String> typeColumn = initTypeColumn();
        TableColumn<FileData, String> nameColumn = initNameColumn();
        TableColumn<FileData, Long> sizeColumn = initSizeColumn();
        TableColumn<FileData, String> modifiedColumn = initModifiedColumn();
        fileDataTableView.getColumns().addAll(typeColumn, nameColumn, sizeColumn, modifiedColumn);
        fileDataTableView.getSortOrder().add(typeColumn);
        fileDataTableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Path path = Paths.get(pathField.getText()).resolve(fileDataTableView.getSelectionModel().getSelectedItem().getName());
                if (Files.isDirectory(path)) {updateTable(path, fileDataTableView);}
            }
        });
        updateTable(Paths.get("."), fileDataTableView);
    }

    private TableColumn<FileData, String> initTypeColumn() {
        TableColumn<FileData, String> typeColumn = new TableColumn<>("T");
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        typeColumn.setPrefWidth(24.);
        return typeColumn;
    }

    private TableColumn<FileData, String> initNameColumn() {
        TableColumn<FileData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        nameColumn.setPrefWidth(240.);
        return nameColumn;
    }

    private TableColumn<FileData, Long> initSizeColumn() {
        TableColumn<FileData, Long> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSize()));
        sizeColumn.setCellFactory(column -> new TableCell<>() {
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
        });
        sizeColumn.setPrefWidth(120.);
        return sizeColumn;
    }

    private TableColumn<FileData, String> initModifiedColumn() {
        TableColumn<FileData, String> modifiedColumn = new TableColumn<>("Modified");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        modifiedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModified().format(formatter)));
        return modifiedColumn;
    }

    public void btnUpAction(ActionEvent actionEvent) {
        Path path = Paths.get(pathField.getText()).getParent();
        if (path != null) {
            updateTable(path, fileDataTableView);
        }
    }

    public void updateTable() {
        updateTable(Paths.get(getCurrentPath()), fileDataTableView);
    }

    public void updateTable(Path path, TableView<FileData> table) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            table.getItems().clear();
            table.getItems().addAll(Files.list(path).map(FileData::new).toList());
            table.sort();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void cBoxChangeDiskAction(ActionEvent actionEvent) {
        ComboBox<String> source = (ComboBox<String>) actionEvent.getSource();
        updateTable(Paths.get(source.getSelectionModel().getSelectedItem()), fileDataTableView);
    }

    public String getSelectedFileName() {
        return fileDataTableView.getSelectionModel().getSelectedItem().getName();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }
}
