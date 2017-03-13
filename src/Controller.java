import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by fajek on 2/28/17.
 */
public class Controller {
    private String extension;
    private String textFieldExtension;
    private int timesComboBox;
    private static List<String> extensionsList = new ArrayList<>();

    @FXML
    private TextField toReplace;
    @FXML
    private TextField replacing;
    @FXML
    private TextField filenameExtension;
    @FXML
    private ListView listView;
    @FXML
    private Alert alert;
    @FXML
    private ComboBox comboBox;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Button addButton;
    @FXML
    private Button closeButton;

    @FXML
    private void addExtensionStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("AddStage.fxml"));
        BorderPane addExtension = loader.load();

        Stage extensionsStage = new Stage();
        extensionsStage.setTitle("Add filename extension");
        extensionsStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(addExtension);
        extensionsStage.setScene(scene);
        extensionsStage.showAndWait();
    }

    private boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    private boolean ifListContains(String s) {
        for (String string : extensionsList) {
            if (string.trim().contains(s)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleAddButtonAction() {
        textFieldExtension = filenameExtension.getText().toLowerCase();

        if (textFieldExtension.isEmpty()) {
            alert = new Alert(Alert.AlertType.NONE, "Please enter filename extension", ButtonType.OK);
            alert.showAndWait();
        } else {
            if (ifListContains(textFieldExtension)) {
                alert = new Alert(Alert.AlertType.NONE, "This extension is already on the list", ButtonType.OK);
                alert.showAndWait();
            } else {
                if (isAlphaNumeric(textFieldExtension)) {
                    extensionsList.add(0, textFieldExtension);
                    Stage stage = (Stage) addButton.getScene().getWindow();
                    stage.close();
                } else {
                    alert = new Alert(Alert.AlertType.NONE, "Filename extension has to be alphanumeric", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    public void handleCloseButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private String prepareExtension() {
        if (comboBox.getValue() == null) {
            alert = new Alert(Alert.AlertType.NONE, "Please choose filename extension", ButtonType.OK);
            alert.showAndWait();

            return null;
        } else {
            extension = comboBox.getSelectionModel().getSelectedItem().toString();
            if (extension.contains("(")) {
                extension = extension.substring(extension.lastIndexOf("(") + 1);
                extension = extension.substring(0, extension.length() - 1);
            } else {
                extension = "*." + extension;
            }

            return extension;
        }
    }

    private void defaultExtensions() {
        extensionsList.add("Text File (*.txt)");
        extensionsList.add("Extensible Markup Language (*.xml)");
        extensionsList.add("HyperText Markup Language (*.html)");
        extensionsList.add("HyperText Markup Language (*.htm)");
        extensionsList.add("Rich Text Format (*.rtf)");
        extensionsList.add("Cascading Style Sheets (*.css)");
        extensionsList.add("Comma-Separated Values (*.csv)");
        extensionsList.add("Encapsulated Comma-Separated Values (*.ecsv)");
        extensionsList.add("SubRip Video Subtitle Format (*.srt)");
        extensionsList.add("Subtitles File Format (*.sub)");
    }

    public void initializeComboBox() {
        if (timesComboBox == 0) {
            defaultExtensions();
        }
        timesComboBox++;
        ObservableList extensionsOb = FXCollections.observableList(extensionsList);
        comboBox.setItems(extensionsOb);
    }

    @FXML
    protected void loadSingleFile() {
        extension = prepareExtension();

        if (extension != null) {
            FileChooser singleChooser = new FileChooser();
            singleChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files (" + extension + ")", extension));
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
            multipleChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files (" + extension + ")", extension));
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

    public void doTheReplacement() {
        File file = new File();
        String fileText;
        String toReplaceString;
        String replacingString;
        String replacedString;
        int numberOfChanges;

        ObservableList filesToConvert = listView.getItems();
        boolean ifChanged = false;

        if (!filesToConvert.isEmpty()) {
            numberOfChanges = 0;

            for (int i = 0; i < filesToConvert.size(); i++) {
                fileText = file.loadFile(filesToConvert.get(i).toString());
                toReplaceString = toReplace.getText();
                replacingString = replacing.getText();

                if (toReplaceString.isEmpty() && replacingString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Please enter both pieces of text", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (toReplaceString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Please enter a piece of text to replace", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (replacingString.isEmpty()) {
                    alert = new Alert(Alert.AlertType.NONE, "Please enter a piece of text replacing previous one", ButtonType.OK);
                    alert.showAndWait();
                    break;
                } else if (toReplaceString.equals(replacingString)) {
                    alert = new Alert(Alert.AlertType.NONE, "Both pieces of text are identical", ButtonType.OK);
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
                    alert = new Alert(Alert.AlertType.NONE, "No changes have been made", ButtonType.OK);
                    alert.showAndWait();
                } else if (numberOfChanges == 1) {
                    alert = new Alert(Alert.AlertType.NONE, "1 change has been made", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.NONE, numberOfChanges + " changes have been made", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } else {
            alert = new Alert(Alert.AlertType.NONE, "Please choose files", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
