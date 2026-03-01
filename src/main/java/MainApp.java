import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = MainApp.class.getResource("/TallerView.fxml");
        if (fxml == null) {
            throw new IllegalStateException("No se encontro TallerView.fxml en resources");
        }
        FXMLLoader loader = new FXMLLoader(fxml);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Taller de Bicicletas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
