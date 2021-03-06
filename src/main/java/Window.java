import javax.swing.*;

public class Window extends JFrame {

    public static void main(String[] args) {
        new Window();
    }

    public Window() {
        this.setTitle("Image Process");
        this.setLayout(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.add(new MainPanel(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        this.setVisible(true);
        this.setResizable(false);
    }
}