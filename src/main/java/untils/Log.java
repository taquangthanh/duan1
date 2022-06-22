package dao;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    public Logger logger;
    FileHandler fh;

    public Log(String file) throws IOException {
        File file1 = new File(file);
        if (!file1.exists()) {
            file1.createNewFile();
        }
        fh = new FileHandler(file, true);
        logger = Logger.getLogger("hi");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
}
