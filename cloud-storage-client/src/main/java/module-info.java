module cloudstorage.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires cloudstorage.common;

    opens ru.sychevv.cloudstorage.client to javafx.fxml;
    exports ru.sychevv.cloudstorage.client;
}
