import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class AlertBox {

    static void show(String title, String textL, String textB) {
        Scene scene;
        Stage stage              =   new Stage();
        Label  label     =   new Label();
        Button button    =   new Button();
        VBox   vbox      =   new VBox(10);

        //  Settings
        label.setText(textL);
        button.setText(textB);
        button.setOnAction(e -> {
            stage.close();
        });
        button.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                stage.close();
            }
        });

        vbox.getChildren().addAll(label, button);
        vbox.setAlignment(Pos.CENTER);

        scene = new Scene(vbox);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setWidth(240);
        stage.setHeight(120);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    static int show(String title, String textL, String textB, String textB2) {

        Scene scene;
        Stage stage      =   new Stage();
        Label  label     =   new Label();
        Button button    =   new Button();
        Button button2   =   new Button();
        VBox   vbox      =   new VBox(10);
        HBox   hbox      =   new HBox(8);
        int[] retVal     =   new int[1];

        //  Settings
        label.setText(textL);
        label.setAlignment(Pos.CENTER);
        button.setText(textB);
        button2.setText(textB2);

        button2.setOnAction(e -> {
            retVal[0] = 0;
            stage.close();
        });

        button.setOnAction(e -> {
            retVal[0] = 1;
            stage.close();
        });
        button.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                retVal[0] = 1;
                stage.close();
            }
        });

        hbox.getChildren().addAll(button, button2);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, hbox);
        vbox.setAlignment(Pos.CENTER);

        scene = new Scene(vbox);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setWidth(350);
        stage.setHeight(120);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.showAndWait();
        return retVal[0];
    }
}
