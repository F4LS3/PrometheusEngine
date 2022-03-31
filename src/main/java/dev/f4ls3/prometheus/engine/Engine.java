package dev.f4ls3.prometheus.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Engine {

    private static Logger logger = null;

    public static void init() {
        InputStream stream = Engine.class.getClassLoader().getResourceAsStream("logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger("Engine");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        if(logger == null) init();
        return logger;
    }
}
