package org.example.datalayer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.datalayer.GameRecords;

import java.io.File;
import java.io.IOException;

public class GameRecordsIO {
    private static final String FILE_PATH = "save/records.json";
    private static final ObjectMapper mapper = GameStateSerializer.getMapper();

    public static void saveRecords(GameRecords records) throws IOException {
        new File("save").mkdirs();
        mapper.writeValue(new File(FILE_PATH), records);
    }

    public static GameRecords loadRecords() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            return mapper.readValue(file, GameRecords.class);
        } else {
            return new GameRecords();
        }
    }
}