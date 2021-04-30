package io.simplelocalize.cli.command;

import com.google.common.collect.Iterables;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.ProjectProcessorFactory;
import io.simplelocalize.cli.extraction.processor.ExtractionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class ExtractCommand implements CliCommand
{
  private static final int BATCH_SIZE = 1000;

  private static final Logger log = LoggerFactory.getLogger(ExtractCommand.class);

  public void invoke(Configuration configuration)
  {
    String searchDir = configuration.getSearchDir();
    String apiKey = configuration.getApiKey();
    String projectType = configuration.getProjectType();
    String profile = configuration.getProfile();

    SimpleLocalizeClient client = SimpleLocalizeClient.withProductionServer(apiKey, profile);

    log.info(" 🕵️‍♂️ Running keys extraction");
    ProjectProcessorFactory processorFactory = new ProjectProcessorFactory();
    ExtractionProcessor extractionProcessor = processorFactory.createForType(projectType);
    ExtractionResult result = extractionProcessor.process(Paths.get(searchDir));

    Set<String> keys = result.getKeys();
    List<Path> processedFiles = result.getProcessedFiles();
    log.info(" 📦 Found {} unique keys in {} components", keys.size(), processedFiles.size());

    Set<String> ignoredKeys = configuration.getIgnoreKeys();
    keys.removeAll(ignoredKeys);

    for (List<String> partition : Iterables.partition(keys, BATCH_SIZE))
    {
      try
      {
        client.sendKeys(partition);
      } catch (Exception e)
      {
        log.error(" 😝 Could not send keys chunk. Contact support: contact@simplelocalize.io", e);
      }
    }
  }
}
