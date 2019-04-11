package runner;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.context.Context;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.ContextOutput;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ContextStepMonitor;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.NullStepMonitor;
import org.jbehave.core.steps.ScanningStepsFactory;
import org.junit.runner.RunWith;
import utils.BddContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnitReportingRunner.class)
public class JBehaveRunner extends JUnitStories {

  private static final String STORY_TIMEOUT = "storyTimeout";
  private static final String STEPS_PATH = "steps";
  private static final String STORIES_PATH = "**/*.story";
  private static final String THREAD_NUMER = "threadNumber";
  private static final String SKIP = "-skip";

  private final Context jbehaveContext;
  private BddContext bddContext;
  private Configuration configuration;
  private String storyTimeout;
  private Integer multiThread;

  public JBehaveRunner() {
    bddContext = BddContext.createOrGetInstance();
    storyTimeout = System.getProperty(STORY_TIMEOUT, "80m");
    multiThread = Integer.valueOf(System.getProperty(THREAD_NUMER, "1"));
    jbehaveContext = new Context();
    configuration = this.buildConfiguration();
  }

  private Configuration buildConfiguration() {
    return new MostUsefulConfiguration()
        .useStoryLoader(new LoadFromClasspath(this.getClass()))
        .useStepMonitor(
            new ContextStepMonitor(this.jbehaveContext, new JBehaveContextView(),
                new NullStepMonitor()))
        .useFailureStrategy(new FailingUponPendingStep())
        .useStoryReporterBuilder(buildStoryReporterBuilder());
  }

  private StoryReporterBuilder buildStoryReporterBuilder() {
    return new StoryReporterBuilder()
        .withFormats(new ContextOutput(this.jbehaveContext), Format.HTML, Format.XML)
        .withReporters(new StoryReporter())
        .withFailureTrace(true)
        .withFailureTraceCompression(true);
  }

  @Override
  public Embedder configuredEmbedder() {
    Embedder embedder = super.configuredEmbedder();

    embedder.useEmbedderControls(this.embedderControls());
    embedder.useMetaFilters(this.metaFilters());
    embedder.useConfiguration(this.configuration);

    return embedder;
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }

  @Override
  public InjectableStepsFactory stepsFactory() {
    return new ScanningStepsFactory(this.configuration, STEPS_PATH);
  }

  @Override
  protected List<String> storyPaths() {
    final String codeLocation =
        CodeLocations.codeLocationFromClass(JBehaveRunner.class).getFile();
    return new StoryFinder().findPaths(codeLocation, STORIES_PATH, null);
  }

  private EmbedderControls embedderControls() {
    return new EmbedderControls()
        .useStoryTimeouts(this.storyTimeout)
        .useThreads(this.multiThread)
        .doFailOnStoryTimeout(true)
        .doVerboseFailures(true)
        .doVerboseFiltering(false);
  }

  private List<String> metaFilters() {

    String stringMetaFilters = System.getProperty("metaFilters", SKIP);
    List<String> metafiltersReceived = new ArrayList<>(Arrays.asList(stringMetaFilters.split(",")));

    if (!metafiltersReceived.contains(SKIP)) {
      metafiltersReceived.add(SKIP);
    }

    return metafiltersReceived.stream()
        .map(this::formatMetaFilter)
        .collect(Collectors.toList());
  }

  private String formatMetaFilter(String metaFilter) {
    if (metaFilter.startsWith("-") || metaFilter.startsWith("+")) {
      return metaFilter;
    }
    String includeMetaFilterPrefix = "+";
    return includeMetaFilterPrefix.concat(metaFilter);
  }

}
