package runner;

import org.jbehave.core.reporters.NullStoryReporter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class StoryReporter extends NullStoryReporter {


  private static final Logger logger = LoggerFactory.getLogger(StoryReporter.class);

  @Override
  public void successful(String step) {
    logger.info(">>Successful Step: " + step);
  }

  @Override
  public void pending(String step) {
   logger.warn(">>>Step: " + step + " is pending");
  }

  @Override
  public void failed(String step, Throwable cause) {
    logger.error(">>Failed Step: " + step + "\nReason: " + cause.getCause() + "\nMessage: " + cause.getMessage());
  }
}
