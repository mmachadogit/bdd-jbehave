package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BddContext {

  private static final String PROPERTIES_FILE = "config/environment.properties";

  private static BddContext instance = null;
  private static String environment;

  public static BddContext createOrGetInstance() {
    if (instance == null) {
      instance = newInstance();
    }
    return instance;
  }

  private static BddContext newInstance() {
    environment = getPropertiesFromFile(PROPERTIES_FILE);
    return new BddContext();
  }

  public String getEnvironment() {
    return environment;
  }

  private static String getPropertiesFromFile(String propertiesPath) {
    Properties returnProperties = new Properties();
    try {
      InputStream inputStream = BddContext.class.getClassLoader().getResourceAsStream(propertiesPath);
      returnProperties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Could not load property file");
    }
    return returnProperties.getProperty("environment");
  }
}
