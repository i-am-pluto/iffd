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
        JScrollPane scrollPane1 = new JScrollPane( crd1 );
        JScrollPane scrollPane2 = new JScrollPane( crd2 );

        cards.add(scrollPane1,"welcome");
        cards.add(scrollPane2,"SelectIngredients");

    }
}
