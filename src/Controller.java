import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.util.*;

/**
 * Created by fajek on 2/28/17.
 */
public class Controller {
    private String fileText;
    private String toReplaceString;
    private String replacingString;
    private String extension;
    private File file;
    private String replacedString = null;
    private int numberOfChanges = 0;

    @FXML
    private TextField toReplace;
    @FXML
    private TextField replacing;
    @FXML
    private ListView listView;
    @FXML
    private Alert alert;
    @FXML
    private ComboBox comboBox;
    @FXML
    private CheckBox checkBox;

    private String prepareExtension() {
        if (comboBox.getValue() == null) {
            alert = new Alert(Alert.AlertType.NONE, "Proszę wybrać rozszerzenie plików", ButtonType.OK);
            alert.showAndWait();

            return null;
        } else {
            extension = comboBox.getSelectionModel().getSelectedItem().toString();
            extension = extension.substring(extension.lastIndexOf("(") + 1);
            extension = extension.substring(0, extension.length() - 1);

            return extension;
        }
    }

    public void initializeComboBox() {
        List<String> extensions = new ArrayList<>();
        extensions.add("Text File (*.txt)");
        extensions.add("Extensible Markup Language (*.xml)");
        extensions.add("HyperText Markup Language (*.html)");
        extensions.add("HyperText Markup Language (*.htm)");
        extensions.add("Rich Text Format (*.rtf)");
        extensions.add("Cascading Style Sheets (*.css)");
        extensions.add("Comma-Separated Values (*.csv)");
        extensions.add("Encapsulated Comma-Separated Values (*.ecsv)");
        extensions.add("SubRip Video Subtitle Format (*.srt)");
        extensions.add("Subtitles File Format (*.sub)");
        extensions.add("JPEG (*.jpg)");

        ObservableList extensionsOb = FXCollections.observableList(extensions);
        comboBox.setItems(extensionsOb);
    }

    @FXML
    protected void loadSingleFile() {
        extension = prepareExtension();

        if (extension != null) {
            FileChooser singleChooser = new FileChooser();
            singleChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pliki (" + extension + ")", extension));
            java.io.File singleFile = singleChooser.showOpenDialog(null);

            if (singleFile != null) {
                ObservableList<String> files = FXCollections.observableArrayList(singleFile.getPath());
                listView.getItems().clear();
                listView.getItems().addAll(files);
            }
        }
    }

    @FXML
    protected void loadMultipleFiles() {
        extension = prepareExtension();

        if (extension != null) {
            FileChooser multipleChooser = new FileChooser();
            multipleChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pliki (" + extension + ")", extension));
            List<java.io.File> multipleFiles = multipleChooser.showOpenMultipleDialog(null);

            if (multipleFiles != null) {
                ObservableList<String> files;
                listView.getItems().clear();
                for (int i = 0; i < multipleFiles.size(); i++) {
                    files = FXCollections.observableArrayList(multipleFiles.get(i).getPath());
                    listView.getItems().addAll(files);
                }
            }
        }
    }

    @FXML
    protected void loadDirectory() {
        extension = prepareExtension();

        if (extension != null) {
            extension = extension.substring(extension.lastIndexOf(".") + 1);
            String[] extensions = new String[]{extension};
            extensions[0] = extension;
            DirectoryChooser directory = new DirectoryChooser();
            java.io.File selectedDirectory = directory.showDialog(null);

            if (selectedDirectory != null) {
                List<java.io.File> files = (List<java.io.File>) FileUtils.listFiles(selectedDirectory, extensions, true);
                listView.getItems().clear();
                listView.getItems().addAll(files);
            }
        }
    }

    public void pressButton() {
        ObservableList filesToConvert = listView.getItems();
        boolean ifChanged = false;

        if (!filesToConvert.isEmpty()) {
            numberOfChanges = 0;

            for (int i = 0; i < filesToConvert.size(); i++) {
                fileText = file.loadFile(filesToConvert.get(i).toString());
                toReplaceString = toReplace.getText();
                replacingString = replacing.getText();

                if (toReplaceString.isEmpty() && replacingString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Proszę wprowadzić oba fragmenty tekstu", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (toReplaceString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Proszę wprowadzić fragment tekstu do zastąpienia", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (replacingString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Proszę wprowadzić fragment tekstu zastępującego", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (toReplaceString.equals(replacingString)) {
                    alert = new Alert(Alert.AlertType.NONE, "Oba wprowadzone fragmenty są identyczne", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else {
                    ifChanged = true;
                }

                if (checkBox.isSelected()) {
                    numberOfChanges += file.ifContainsIgnoringCase(fileText, toReplaceString);
                    replacedString = fileText.replace(toReplaceString.toLowerCase(), replacingString.toLowerCase());
                } else {
                    numberOfChanges += file.ifContains(fileText, toReplaceString);
                    replacedString = fileText.replace(toReplaceString, replacingString);
                }


                if (numberOfChanges != 0 && replacedString != null) {
                    file.writeFile(filesToConvert.get(i).toString(), replacedString);
                }
            }

            if (ifChanged) {
                if (numberOfChanges == 0) {
                    alert = new Alert(Alert.AlertType.NONE, "Nie dokonano zmian", ButtonType.OK);
                    alert.showAndWait();
                } else if (numberOfChanges == 1) {
                    alert = new Alert(Alert.AlertType.NONE, "Dokonano 1 zmiany", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.NONE, "Dokonano " + numberOfChanges + " zmian", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } else {
            alert = new Alert(Alert.AlertType.NONE, "Proszę wybrać pliki", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
