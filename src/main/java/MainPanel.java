import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainPanel extends JPanel {

    private ChromeDriver driver;

    private JButton searchButton;
    private JTextField searchTextField;

    private Font buttonFont;

    private ImageIcon filteredImage;
    private BufferedImage imageForFiltering;

    private JButton colorShiftRightButton;
    private JButton colorShiftLeftButton;
    private JButton grayscaleButton;
    private JButton negativeButton;
    private JButton sepiaButton;
    private JButton lighterButton;

    private String urlImageLinkPath;

    private boolean isSearchClickedBefore;


    public MainPanel(int x, int y, int width, int height) {
        this.setLayout(null);
        this.setBounds(x, y, width, height);

        this.buttonFont = new Font("David", Font.BOLD, Constants.BUTTON_FONT_SIZE);
        buildPanel();

        this.driver = null;
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\dasha\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\dasha\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1");


        this.searchButton.addActionListener((e -> {
            if (!this.searchTextField.getText().equals("")) {
                String nameInFormat = changeNameToFormat();

                this.filteredImage = null;

                this.driver = new ChromeDriver(options);
                this.driver.get(Constants.FACEBOOK_ADDRESS + nameInFormat);

                if (isFindImageURL()){
                    this.isSearchClickedBefore = true;
                    repaint();
                }
                this.driver.close();

            }
        }));

        actionListenerFliersButtons();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon leftImageIcon;
        URL url;
        this.imageForFiltering = null;
        if (this.isSearchClickedBefore) {
            try {
                url = new URL(this.urlImageLinkPath);
                this.imageForFiltering = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            leftImageIcon = new ImageIcon(this.imageForFiltering);
            resizeImage(leftImageIcon);
            leftImageIcon.paintIcon(this, g, Constants.X_IMAGE, Constants.Y_IMAGE);

            if (this.filteredImage != null) {
                resizeImage(this.filteredImage);
                this.filteredImage.paintIcon(this, g, this.searchButton.getX() + this.searchButton.getWidth() + Constants.X_IMAGE, Constants.Y_IMAGE);
            }
        }
    }

    private void buildPanel() {
        this.searchTextField = new JTextField();
        this.searchTextField.setBounds((Constants.WINDOW_WIDTH / 2 - (Constants.BUTTON_WIDTH / 2)), Constants.WINDOW_HEIGHT / 10, Constants.BUTTON_WIDTH / 2, Constants.BUTTON_HEIGHT);
        this.add(this.searchTextField);


        this.searchButton = new JButton();
        this.searchButton.setBounds(this.searchTextField.getX() + this.searchTextField.getWidth(), this.searchTextField.getY(), Constants.BUTTON_WIDTH / 2, Constants.BUTTON_HEIGHT);
        this.searchButton.setText("search");
        this.searchButton.setFont(this.buttonFont);
        this.add(this.searchButton);


        this.colorShiftRightButton = new JButton();
        this.colorShiftRightButton.setBounds(this.searchTextField.getX(), this.searchTextField.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.colorShiftRightButton.setText("Color Shift Right");
        this.colorShiftRightButton.setFont(this.buttonFont);
        this.add(this.colorShiftRightButton);

        this.colorShiftLeftButton = new JButton();
        this.colorShiftLeftButton.setBounds(this.searchTextField.getX(), this.colorShiftRightButton.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.colorShiftLeftButton.setText("Color Shift Left");
        this.colorShiftLeftButton.setFont(this.buttonFont);
        this.add(this.colorShiftLeftButton);

        this.grayscaleButton = new JButton();
        this.grayscaleButton.setBounds(this.searchTextField.getX(), this.colorShiftLeftButton.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.grayscaleButton.setText("Grayscale");
        this.grayscaleButton.setFont(this.buttonFont);
        this.add(this.grayscaleButton);

        this.negativeButton = new JButton();
        this.negativeButton.setBounds(this.searchTextField.getX(), this.grayscaleButton.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.negativeButton.setText("Negative");
        this.negativeButton.setFont(this.buttonFont);
        this.add(this.negativeButton);

        this.sepiaButton = new JButton();
        this.sepiaButton.setBounds(this.searchTextField.getX(), this.negativeButton.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.sepiaButton.setText("Sepia");
        this.sepiaButton.setFont(this.buttonFont);
        this.add(this.sepiaButton);

        this.lighterButton = new JButton();
        this.lighterButton.setBounds(this.searchTextField.getX(), this.sepiaButton.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.lighterButton.setText("Lighter");
        this.lighterButton.setFont(this.buttonFont);
        this.add(this.lighterButton);

    }

    private void actionListenerFliersButtons() {
        this.colorShiftRightButton.addActionListener((e -> {
            colorShiftRightOrLeft(true);
            repaint();
        }));

        this.colorShiftLeftButton.addActionListener((e -> {
            colorShiftRightOrLeft(false);
            repaint();
        }));

        this.grayscaleButton.addActionListener((e -> {
            grayscale();
            repaint();
        }));

        this.negativeButton.addActionListener((e -> {
            negative();
            repaint();
        }));

        this.sepiaButton.addActionListener((e -> {
            sepia();
            repaint();
        }));

        this.lighterButton.addActionListener((e -> {
            lighter();
            repaint();
        }));
    }

    private String changeNameToFormat() {
        String lowerCaseName = this.searchTextField.getText().toLowerCase();
        String[] splitName = lowerCaseName.split(" ");
        String nameInFormat = "";
        for (int i = 0; i < splitName.length; i++) {
            nameInFormat += (splitName[i] + ((i != splitName.length - 1) ? "." : ""));
        }
        return nameInFormat;
    }

    private boolean isFindImageURL() {

        WebElement facebookElement = null;
        do {
            try {
                facebookElement = this.driver.findElement(By.className("p361ku9c"));
            } catch (NoSuchElementException exception) {
            }
        } while (facebookElement == null);


        WebElement profileCircle = null;
        try {
            profileCircle = this.driver.findElement(By.cssSelector("a[class=\"oajrlxb2 gs1a9yip g5ia77u1 mtkw9kbi tlpljxtp qensuy8j ppp5ayq2 goun2846 ccm00jje s44p3ltw mk2mc5f4 rt8b4zig n8ej3o3l agehan2d sk4xxmp2 rq0escxv nhd2j8a9 mg4g778l pfnyh3mw p7hjln8o kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x tgvbjcpo hpfvmrgz jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso l9j0dhe7 i1ao9s8h esuyzwwr f1sip0of du4w35lb n00je7tq arfg74bv qs9ysxi8 k77z8yql btwxx1t3 abiwlrkh p8dawk7l lzcic4wl oo9gr5id q9uorilb\"]"));
        } catch (NoSuchElementException exception) {

        }

        if (profileCircle != null){
//            String currentUrl = this.driver.getCurrentUrl();

//            this.driver.get(profileCircle.getAttribute("href"));

            WebElement imageLink = null;
//            if (currentUrl.equals(this.driver.getCurrentUrl())) {
                try {
                    List<WebElement> profileElements = this.driver.findElements(By.cssSelector("image[preserveAspectRatio=\"xMidYMid slice\"]"));
                    imageLink = profileElements.get(1);
                } catch (NoSuchElementException exception) {

                }
                this.urlImageLinkPath = imageLink.getAttribute("xlink:href");
//            } else {
//                try {
//                    imageLink = this.driver.findElement(By.cssSelector("img[class=\"gitj76qy d2edcug0 r9f5tntg r0294ipz\"]"));
//                } catch (NoSuchElementException exception) {
//                }
//                this.urlImageLinkPath = imageLink.getAttribute("src");
//            }
            return true;
        }
        return false;
    }



    private void colorShiftRightOrLeft(boolean isRight) {
        if (this.imageForFiltering != null) {
            for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
                for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                    int currentColor = this.imageForFiltering.getRGB(x, y);
                    Color pixel = new Color(currentColor);

                    int red;
                    int green;
                    int blue;
                    if (isRight) {
                        red = pixel.getGreen();
                        green = pixel.getBlue();
                        blue = pixel.getRed();
                    } else {
                        red = pixel.getBlue();
                        green = pixel.getRed();
                        blue = pixel.getGreen();
                    }
                    Color newColor = new Color(red, green, blue);
                    this.imageForFiltering.setRGB(x, y, newColor.getRGB());
                }
            }
            this.filteredImage = new ImageIcon(imageForFiltering);
        }
    }

    private void grayscale() {
        if (this.imageForFiltering != null) {
            for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
                for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                    int currentColor = this.imageForFiltering.getRGB(x, y);
                    Color pixel = new Color(currentColor);

                    int red = pixel.getRed();
                    int green = pixel.getGreen();
                    int blue = pixel.getBlue();
                    int pixel2 = (red + green + blue) / 3;

                    Color newColor = new Color(pixel2, pixel2, pixel2);

                    this.imageForFiltering.setRGB(x, y, newColor.getRGB());
                }
            }
            this.filteredImage = new ImageIcon(imageForFiltering);
        }
    }

    private void negative() {
        if (this.imageForFiltering != null) {
            for (int j = 0; j < this.imageForFiltering.getHeight(); j++) {
                for (int i = 0; i < this.imageForFiltering.getWidth(); i++) {
                    Color currentColor = new Color(this.imageForFiltering.getRGB(i, j));
                    Color negativeColor = new Color(Constants.MAX_RGB - currentColor.getRed(), Constants.MAX_RGB - currentColor.getGreen(), Constants.MAX_RGB - currentColor.getBlue());
                    this.imageForFiltering.setRGB(i, j, negativeColor.getRGB());
                }
            }
            this.filteredImage = new ImageIcon(imageForFiltering);
        }
    }

    private void sepia() {
        if (this.imageForFiltering != null) {
            for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
                for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                    int currentColor = this.imageForFiltering.getRGB(x, y);
                    Color pixel = new Color(currentColor);

                    int red = (int) ((0.393 * pixel.getRed()) + (0.769 * pixel.getRed()) + (0.189 * pixel.getBlue()));
                    int green = (int) ((0.349 * pixel.getRed()) + (0.686 * pixel.getRed()) + (0.168 * pixel.getBlue()));
                    int blue = (int) ((0.272 * pixel.getRed()) + (0.534 * pixel.getRed()) + (0.131 * pixel.getBlue()));

                    Color newColor = new Color(fixColor(red), fixColor(green), fixColor(blue));

                    this.imageForFiltering.setRGB(x, y, newColor.getRGB());
                }
            }
            this.filteredImage = new ImageIcon(imageForFiltering);
        }
    }

    private void lighter() {
        if (this.imageForFiltering != null) {
            for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
                for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                    int currentColor = this.imageForFiltering.getRGB(x, y);
                    Color pixel = new Color(currentColor);

                    int red = fixColor(pixel.getRed() + pixel.getRed() / 3);
                    int green = fixColor(pixel.getGreen() + pixel.getGreen() / 3);
                    int blue = fixColor(pixel.getBlue() + pixel.getBlue() / 3);

                    Color newColor = new Color(red, green, blue);

                    this.imageForFiltering.setRGB(x, y, newColor.getRGB());
                }
            }
            this.filteredImage = new ImageIcon(imageForFiltering);
        }
    }

    private int fixColor(int Color) {
        return Math.min(Color, Constants.MAX_RGB);
    }

    private void resizeImage(ImageIcon imageIcon) {
        double proportion = ((double) imageIcon.getIconWidth() / (double) imageIcon.getIconHeight());
        int newWidth;
        int newHeight;
        if (proportion < 1) {
            newHeight = Constants.MAX_IMAGE_SIZE;
            newWidth = (int) ((double) newHeight * proportion);
        } else {
            newWidth = Constants.MAX_IMAGE_SIZE;
            newHeight = (int) ((double) newWidth * proportion);
        }
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_FAST));
    }
}

