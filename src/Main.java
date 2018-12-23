import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    static final int defaultBuffer = 2097152;
    static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("main.css");
        window.setTitle("Encription / Decription");
        window.setScene(scene);
        window.setWidth(640);
        window.setHeight(520);
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
