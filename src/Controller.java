import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {

    /*  ==== DEKLARÁCIÓK ====  */
    @FXML private CheckBox       bufferedEncrypt;
    @FXML private CheckBox       enableAdvanced;
    @FXML private CheckBox       encryptMaxSize;
    @FXML private TextField      bufferSizeArea;
    @FXML private VBox           advancedVBox;
    @FXML private CheckBox       keyFromFile;
    @FXML private TextField      filePath;
    @FXML private PasswordField  pwdField;
    @FXML private TextField      keyPath;
    @FXML private TextArea       logArea;
    @FXML private ProgressBar    pBar;
          private Locale         locale              = new Locale("en");
          private List<File>     fileList            = new ArrayList<>();
          private ResourceBundle resourceBundle      = ResourceBundle.getBundle("lang", locale);



    /*  ==== FÁJLOK KIVÁLASZTÁSA ====  */
    @FXML private void fileChooser() {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*"));
        fileList = fc.showOpenMultipleDialog(null);

        // Ha több mint 1 fájl lett kiválasztva
        if (fileList != null && fileList.size() > 1) {

            // Fájlok számának kiírása
            filePath.setDisable(true);
            filePath.setText(fileList.size() + " file opened!");

            // Log tab...
            logArea.setText(logArea.getText() + "Selected files (" + fileList.size() + "):\n");
            for (File a : fileList)
                logArea.setText(logArea.getText() + a + "\n");
            logArea.setText(logArea.getText() + "\n");

        } else if (fileList != null && fileList.size() == 1) { // Ha csak 1 fájl

            filePath.disableProperty().setValue(true);
            filePath.setText(fileList.get(0).getPath());
            logArea.setText(logArea.getText() + "Selected file: \n" + fileList.get(0) + "\n\n");

        }
    }

    /*  ==== ENCRIPTION ====  */
    @FXML private void encrypt() throws Exception {
        if (fileList != null && fileList.size() == 0) {
            fileList.add(new File(filePath.getText()));
        }

        if (handleErrors() != 0)
            return;

        boolean adv = enableAdvanced.isSelected();
        boolean key = keyFromFile.isSelected();
        boolean max = encryptMaxSize.isSelected();
        boolean buf = bufferedEncrypt.isSelected();
        normalEncryption.buffer = Main.defaultBuffer;


        int newBuffSize;
        if (adv && max) {
            if (bufferSizeArea.getText().length() > 0 && (newBuffSize = isNumber(bufferSizeArea.getText())) != -1)
                normalEncryption.buffer = newBuffSize;
        } else if (adv && buf && key) {
            if (keyPath.getText().length() > 0) {
                Thread th = new Thread(new bufferedEncryption(fileList, "", new File(keyPath.getText()), pBar, logArea));
                th.setPriority(7);
                th.start();
            }
            return;
        } else if (adv && buf) {
            Thread th = new Thread(new bufferedEncryption(fileList, pwdField.getText(), null, pBar, logArea));
            th.setPriority(7);
            th.start();
            return;
        }
        if (adv && key) {
            normalEncryption.start(fileList, new File(keyPath.getText()),logArea);
        } else {
            normalEncryption.start(fileList, pwdField.getText(), logArea);
        }
    }


    /*  ==== ELLENŐRZI, HOGY SZÁMOT ÍRT-E BE A FELHASZNÁLÓ ====  */
    private int isNumber(String text) {
        int i;

        try {
            i = Integer.parseInt(text);
        } catch (Exception e) {
            i = -1;
        }
        return i;
    }


    /*  ==== HIBA ÉS FIGYELMEZTETÉS KEZELÉS ====  */
    private int handleErrors() {

        // Ha a fájl- vagy a jelszó mező üres kilép
        if ((pwdField.getText().length() <= 3 && !(enableAdvanced.isSelected() && keyFromFile.isSelected()
                && keyPath.getText().length() > 0)) || filePath.getText().length() == 0) {
            AlertBox.show("Error", resourceBundle.getString("missing"), "Okay");
            return 1;
        }

        // Biztos titkosítani akarja?
        if (AlertBox.show("Warning", resourceBundle.getString("warning"), "Yes", "No") != 1)
            return 2;


        return 0;
    }


    /*  ==== FÁJL LISTA TÖRLÉSE ====  */
    @FXML private void clearList() {
        fileList = new ArrayList<>();
        filePath.setText("");
        pwdField.setText("");
        filePath.disableProperty().setValue(false);
        logArea.setText(logArea.getText() + "File list cleared!\n\n");
    }


    /*  ==== LOG TÖRLÉSE ====  */
    @FXML private void clearLog() {
        logArea.setText("");
    }

    /*  ==== VBOX FELOLDÁSA (HALADÓ BEÁLLÍTÁSOK) ====  */
    @FXML private void enableAdvencedClick() {
        advancedVBox.setDisable(!enableAdvanced.isSelected());
        keyFromFile.setSelected(false);
        encryptMaxSize.setSelected(false);
        bufferedEncrypt.setSelected(false);
    }

    /*  ==== KULCS FORRÁS FELOLDÁSA (HALADÓ BEÁLLÍTÁSOK) ====  */
    @FXML private void keyFromFileClick() {
        keyPath.setDisable(!keyFromFile.isSelected());
        pwdField.setDisable(keyFromFile.isSelected());
    }

    /*  ==== MAX TITKOSÍTÁSI MÉRET BEVITEL FELOLDÁSA (HALADÓ BEÁLLÍTÁSOK) ====  */
    @FXML private void encryptMaxSizeAction() {
        bufferSizeArea.setDisable(!encryptMaxSize.isSelected());
    }


    @FXML private void bufferedEncryptAction() {
        encryptMaxSize.setSelected(false);
        bufferSizeArea.setDisable(!encryptMaxSize.isSelected());
        encryptMaxSize.setDisable(bufferedEncrypt.isSelected());
    }

    @FXML private void closeEvent() {
        Main.window.close();
    }

    private double x,y;

    @FXML private void dragbarMousePressed(MouseEvent e) {
        x = e.getSceneX();
        y = e.getSceneY();
    }

    @FXML private void dragbarDrag(MouseEvent e) {
        Main.window.setX(e.getScreenX() - x);
        Main.window.setY(e.getScreenY() - y);
    }
}
