import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class App {

    public static void main(String[] args) throws IOException, URISyntaxException {

        MainFrame main = new MainFrame();
        main.setTitle("Interactive Fish Feed Designer");
        main.setSize(800, 950);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
        main.setLocationRelativeTo(null);

        ImageIcon image = new ImageIcon("logo.png");
        main.setIconImage(image.getImage());
    }

}
