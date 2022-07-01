import view.SelectIngredients;
import view.Welcome;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainFrame extends JFrame {

    Container cards;

    MainFrame() throws IOException, URISyntaxException {
        cards = getContentPane();
        cards.setLayout(new CardLayout());

        JPanel crd1 = new Welcome("welcome",cards);
        JPanel crd2 = new SelectIngredients("SelectIngredients",cards);

        cards.add(crd1,"welcome");
        cards.add(crd2,"SelectIngredients");

    }
}
