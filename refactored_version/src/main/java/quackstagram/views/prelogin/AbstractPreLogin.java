package quackstagram.views.prelogin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import quackstagram.views.BaseFrameManager;
import quackstagram.views.ColorID;

public abstract class AbstractPreLogin extends BaseFrameManager {
    private JButton primaryButton;
    private JButton secondaryButton;
    private JLabel lblPhoto;

    public AbstractPreLogin(String title) {
        super(title);
        initializeUI();
    }

    @Override
    protected String getFormattedTitle() {
        return "Quackstagram - " + this.getRawTitle();
    }

    @Override
    protected String getHeaderText() {
        return "Quackstagram 🐥";
    }

    @Override
    protected JComponent createControlPanel() {
        JComponent primaryButton = createPrimaryButton();
        JComponent secondaryButton = createSecondaryButton();

        // Panel to hold both buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        buttonPanel.add(primaryButton);
        buttonPanel.add(secondaryButton);

        return buttonPanel;
    }

    private JComponent createPrimaryButton() {
        primaryButton = new JButton(getRawTitle());
        primaryButton.addActionListener(this::onPrimaryButtonClick);
        primaryButton.setBackground(getColor(ColorID.PRIMARY_lOGIN_BUTTON)); // Use a red color that matches the mockup
        primaryButton.setForeground(getColor(ColorID.TEXT_PRIMARY)); // Set the text color to black
        primaryButton.setFocusPainted(false);
        primaryButton.setBorderPainted(false);
        primaryButton.setFont(new Font("Arial", Font.BOLD, 14));
        return primaryButton;
    }

    private JComponent createSecondaryButton() {
        // New button for navigating to SignUpUI
        secondaryButton = new JButton(getSecondButtonText());
        secondaryButton.addActionListener(this::onSecondaryButtonCLick);
        secondaryButton.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        secondaryButton.setForeground(getColor(ColorID.TEXT_PRIMARY));
        secondaryButton.setFocusPainted(false);
        secondaryButton.setBorderPainted(false);
        return secondaryButton;
    }

    protected JPanel getDuckIcon() {
        // Profile picture placeholder without border
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(
                new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);
        photoPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return photoPanel;
    }

    protected abstract String getSecondButtonText();

    protected abstract JComponent createMainContentPanel();

    protected abstract void onPrimaryButtonClick(ActionEvent event);

    protected abstract void onSecondaryButtonCLick(ActionEvent event);
}
