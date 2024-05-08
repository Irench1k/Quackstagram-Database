package quackstagram.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class BaseFrameManager extends JFrame {
    protected static final int WIDTH = 300;
    protected static final int HEIGHT = 500;
    private String title;
    private Theme theme = Theme.getInstance();

    public BaseFrameManager(String title) {
        System.out.println("Instantiating BaseFrmameManager and theme is : " + this.theme);
        this.title = title;
        setTitle(getFormattedTitle());
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLayout(new BorderLayout(10, 10));
    }

    protected String getRawTitle() {
        return this.title;
    }

    protected String getFormattedTitle() {
        return this.title;
    }

    protected Color getColor(ColorID id) {
        return this.theme.getColor(id);
    }

    protected String getIconPath(IconID id) {
        return this.theme.getIconPath(id);
    }

    @SuppressWarnings("unused")
    protected void initializeUI() {
        JComponent headerPanel = createHeaderPanel();
        JComponent mainPanel = createMainContentPanel();
        JComponent controlPanel = createControlPanel();

        // Add panels to the frame
        if (getHeaderText() != null) {
            add(headerPanel, BorderLayout.NORTH);
        }
        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Defined here, but needs configuration
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(getColor(ColorID.BACKGROUND_HEADER)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(getHeaderText());
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(getColor(ColorID.OPPOSITE_TEXT)); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    protected String getHeaderText() {
        return getRawTitle() + "🐥";
    }

    protected abstract JComponent createMainContentPanel();

    protected abstract JComponent createControlPanel();
}
