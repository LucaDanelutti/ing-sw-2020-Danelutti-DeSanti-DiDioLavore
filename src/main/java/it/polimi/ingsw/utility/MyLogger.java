package it.polimi.ingsw.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.logging.*;

public class MyLogger {
    static Logger logger;
    public Handler fileHandler;
    Formatter plainText;

    private MyLogger() throws IOException {
        //System.setProperty("java.util.logging.SimpleFormatter.format","[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$s]: %5$s [%2$s]%n");
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$s]: %5$s%n");
        //instance the logger
        logger = Logger.getLogger(MyLogger.class.getName());
        try {
            //instance the file handler
            fileHandler = new FileHandler("logs/serverLog.txt", true);
        } catch(NoSuchFileException ex) {
            //if parent directory does not exist then attempt to create it
            String parentName = new File("logs/serverLog.txt").getParent();
            if (parentName != null) {
                File parentDir = new File(parentName);
                if(!parentDir.exists() && parentDir.mkdirs()) {
                    fileHandler = new FileHandler("logs/serverLog.txt", true);
                } else {
                    throw ex;
                }
        } else {
            throw ex;
        }
    }
        //instance formatter, set formatting, and handler
        plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        logger.addHandler(fileHandler);
    }

    private static Logger getLogger(){
        if(logger == null){
            try {
                new MyLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    public static void log(Level level, String sourceClass, String sourceMethod, String msg){
        getLogger().logp(level, sourceClass, sourceMethod, msg);
    }
}
