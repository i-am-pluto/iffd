package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class AIRecordRepository {
    ArrayList<AIRecord> records;
    IngredientsRepository ingredientsRepository;
    public AIRecordRepository() throws IOException, URISyntaxException {


        this.ingredientsRepository = new IngredientsRepository();

        this.records = new ArrayList<>();
        String file = "/ai_saved_data.csv";
        this.setupRecords(file);
    }

    private void setupRecords(String fileName) throws IOException {
        InputStream file = getClass().getResourceAsStream(fileName);



        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        br.readLine();

    }

}
