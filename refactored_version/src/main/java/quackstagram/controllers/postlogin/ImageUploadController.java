package quackstagram.controllers.postlogin;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import quackstagram.utilities.FileHandler;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.postlogin.ImageUploadUI;

public class ImageUploadController {
    private ImageUploadUI view;
    private User currentUser;

    public ImageUploadController(ImageUploadUI view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
    }

    public void uploadAction(String caption) {
        File selectedFile = selectFile();
        if (selectedFile == null) {
            return;
        }

        try {
            Picture picture = Picture.createNewForUser(currentUser.getUsername(), caption);
            FileHandler.uploadImage(selectedFile, picture);
            FileHandler.savePicture(picture);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error saving image: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
