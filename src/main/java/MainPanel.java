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
import java.net.MalformedURLException;
import java.net.URL;

public class MainPanel extends JPanel {

    private ChromeDriver driver;


    private JButton searchButton;
    private JTextField searchTextField;

    private Font buttonFont;
//    private ImageIcon bob;
//    private ImageIcon bobFiltered;
    private ImageIcon originalImage;

    private JButton colorShiftRight;
    private JButton colorShiftLeft;
    private JButton grayscale;
    private JButton negative;
    private JButton sepia;
    private JButton lighter;

    private String urlImageLinkPath;

    private boolean isSearchClicked;
    private boolean isFilterClicked;

    private BufferedImage imageForFiltering;


    public MainPanel(int x, int y, int width, int height) {
        this.setLayout(null);
        this.setBounds(x, y, width, height);
        buildPanel();


        this.buttonFont = new Font("David", Font.BOLD, Constants.BUTTON_FONT_SIZE);

        this.driver = null;
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\dasha\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\dasha\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1");

        this.searchButton.addActionListener((e -> {
            if (!this.searchTextField.getText().equals("")){
//                if (this.driver != null){
//                    this.driver.close();
//                }
                String lowerCaseName =  this.searchTextField.getText().toLowerCase();
                String[] splitName = lowerCaseName.split(" ");
                String name = "";
                for (int i = 0; i < splitName.length; i++) {
                    name += (splitName[i] + ((i != splitName.length-1) ? "." : ""));
                }

                this.driver = new ChromeDriver(options);
                this.driver.get("https://www.facebook.com/" + name);

                // dasha23kir@gmail.com
                // NEWpass230700

                WebElement profileCircle = null;
                boolean isProfileCircleExist = false;
                do {
                    try {
                        profileCircle = this.driver.findElement(By.cssSelector("a[class=\"oajrlxb2 gs1a9yip g5ia77u1 mtkw9kbi tlpljxtp qensuy8j ppp5ayq2 goun2846 ccm00jje s44p3ltw mk2mc5f4 rt8b4zig n8ej3o3l agehan2d sk4xxmp2 rq0escxv nhd2j8a9 mg4g778l pfnyh3mw p7hjln8o kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x tgvbjcpo hpfvmrgz jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso l9j0dhe7 i1ao9s8h esuyzwwr f1sip0of du4w35lb n00je7tq arfg74bv qs9ysxi8 k77z8yql btwxx1t3 abiwlrkh p8dawk7l lzcic4wl oo9gr5id q9uorilb\"]"));
                        isProfileCircleExist = true;
                    } catch (NoSuchElementException exception) {
                    }
                }while (!isProfileCircleExist);

                this.driver.get (profileCircle.getAttribute("href"));

                WebElement imageLink = null;
                boolean isImageLinkExist = false;
                do {
                    try {
                        imageLink = this.driver.findElement(By.cssSelector("img[class=\"gitj76qy d2edcug0 r9f5tntg r0294ipz\"]"));
                        isImageLinkExist = true;
                    } catch (NoSuchElementException exception) {
                    }
                }while (!isImageLinkExist);

                this.urlImageLinkPath = imageLink.getAttribute("src");

                this.isSearchClicked = true;
                repaint();
//                this.driver.close();
            }
        }));

        this.colorShiftRight.addActionListener((e -> {
            colorShiftRightOrLeft(true);
            this.isFilterClicked = true;
            repaint();
        }));

        this.colorShiftLeft.addActionListener((e -> {
            colorShiftRightOrLeft(false);
            this.isFilterClicked = true;
            repaint();
        }));

        this.grayscale.addActionListener((e -> {
            grayscale();
            this.isFilterClicked = true;
            repaint();
        }));

        this.negative.addActionListener((e -> {
            negative();
            this.isFilterClicked = true;
            repaint();
        }));

        this.sepia.addActionListener((e -> {
            sepia();
            this.isFilterClicked = true;
            repaint();
        }));

        this.lighter.addActionListener((e -> {
            lighter();
            this.isFilterClicked = true;
            repaint();
        }));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon leftImageIcon;
        URL url = null;
        this.imageForFiltering = null;
        if (this.isSearchClicked){
            try {
                url = new URL(this.urlImageLinkPath);
                imageForFiltering = ImageIO.read(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            leftImageIcon = new ImageIcon(imageForFiltering);
            resizeImage(leftImageIcon);
            leftImageIcon.paintIcon(this, g, 30, 30);

            if(this.isFilterClicked){
                resizeImage(this.originalImage);
                this.originalImage.paintIcon(this, g, this.searchButton.getX() + this.searchButton.getWidth() + 30, 30);
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


        this.colorShiftRight = new JButton();
        this.colorShiftRight.setBounds(this.searchTextField.getX(), this.searchTextField.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.colorShiftRight.setText("Color Shift Right");
        this.colorShiftRight.setFont(this.buttonFont);
        this.add(this.colorShiftRight);

        this.colorShiftLeft = new JButton();
        this.colorShiftLeft.setBounds(this.searchTextField.getX(), this.colorShiftRight.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.colorShiftLeft.setText("Color Shift Left");
        this.colorShiftLeft.setFont(this.buttonFont);
        this.add(this.colorShiftLeft);

        this.grayscale = new JButton();
        this.grayscale.setBounds(this.searchTextField.getX(), this.colorShiftLeft.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.grayscale.setText("Grayscale");
        this.grayscale.setFont(this.buttonFont);
        this.add(this.grayscale);

        this.negative = new JButton();
        this.negative.setBounds(this.searchTextField.getX(), this.grayscale.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.negative.setText("Negative");
        this.negative.setFont(this.buttonFont);
        this.add(this.negative);

        this.sepia = new JButton();
        this.sepia.setBounds(this.searchTextField.getX(), this.negative.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.sepia.setText("Sepia");
        this.sepia.setFont(this.buttonFont);
        this.add(this.sepia);

        this.lighter = new JButton();
        this.lighter.setBounds(this.searchTextField.getX(), this.sepia.getY() + Constants.BUTTON_HEIGHT + Constants.SPACE, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.lighter.setText("Lighter");
        this.lighter.setFont(this.buttonFont);
        this.add(this.lighter);

    }

    private void colorShiftRightOrLeft(boolean isRight) {
        for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
            for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                int current = this.imageForFiltering.getRGB(x, y);
                Color pixel = new Color(current);

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
        this.originalImage = new ImageIcon(imageForFiltering);
    }

    private void grayscale() {
        for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
            for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                int current = this.imageForFiltering.getRGB(x, y);
                Color pixel = new Color(current);

                int red = pixel.getRed();
                int green = pixel.getGreen();
                int blue = pixel.getBlue();
                int pixel2 = (red + green + blue) / 3;

                Color newColor = new Color(pixel2, pixel2, pixel2);

                this.imageForFiltering.setRGB(x, y, newColor.getRGB());
            }
        }

        this.originalImage = new ImageIcon(imageForFiltering);
    }

    private void negative() {
        for (int j = 0; j < this.imageForFiltering.getHeight(); j++) {
            for (int i = 0; i < this.imageForFiltering.getWidth(); i++) {
                Color currentColor = new Color(this.imageForFiltering.getRGB(i, j));
                Color negativeColor = new Color(255 - currentColor.getRed(), 255 - currentColor.getGreen(), 255 - currentColor.getBlue());
                this.imageForFiltering.setRGB(i, j, negativeColor.getRGB());
            }
        }
        this.originalImage = new ImageIcon(imageForFiltering);
    }

    private void sepia() {
        for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
            for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                int current = this.imageForFiltering.getRGB(x, y);
                Color pixel = new Color(current);

                int red = (int) ((0.393 * pixel.getRed()) + (0.769 * pixel.getRed()) + (0.189 * pixel.getBlue()));
                int green = (int) ((0.349 * pixel.getRed()) + (0.686 * pixel.getRed()) + (0.168 * pixel.getBlue()));
                int blue = (int) ((0.272 * pixel.getRed()) + (0.534 * pixel.getRed()) + (0.131 * pixel.getBlue()));

                Color newColor = new Color(intenseColor(red), intenseColor(green), intenseColor(blue));

                this.imageForFiltering.setRGB(x, y, newColor.getRGB());
            }
        }
        this.originalImage = new ImageIcon(imageForFiltering);
    }

    private void lighter() {
        for (int x = 0; x < this.imageForFiltering.getWidth(); x++) {
            for (int y = 0; y < this.imageForFiltering.getHeight(); y++) {
                int current = this.imageForFiltering.getRGB(x, y);
                Color pixel = new Color(current);

                int red = intenseColor(pixel.getRed() * 2);
                int green = intenseColor(pixel.getGreen() * 2);
                int blue = intenseColor(pixel.getBlue() * 2);

                Color newColor = new Color(red, green, blue);

                this.imageForFiltering.setRGB(x, y, newColor.getRGB());
            }
        }
        this.originalImage = new ImageIcon(imageForFiltering);
    }

    private int intenseColor(int originalColor) {
        if (originalColor > 255) {
            originalColor = 255;
        }
        return originalColor;
    }

    private void resizeImage (ImageIcon imageIcon){
        double proportion = ((double)imageIcon.getIconWidth()/(double)imageIcon.getIconHeight());
        int newWidth;
        int newHeight;
        if (proportion<1){
            newHeight = Constants.MAX_IMAGE_SIZE;
            newWidth = (int)((double)newHeight*proportion);
        }else{
            newWidth = Constants.MAX_IMAGE_SIZE;
            newHeight = (int)((double)newWidth*proportion);
        }
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(newWidth,newHeight,Image.SCALE_FAST));
    }
}

