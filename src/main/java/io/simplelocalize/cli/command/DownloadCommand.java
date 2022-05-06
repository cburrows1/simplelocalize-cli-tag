package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TemplateKeys;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.DownloadableFile;
import io.simplelocalize.cli.configuration.Configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static io.simplelocalize.cli.client.dto.DownloadRequest.DownloadRequestBuilder.aDownloadRequest;

public class DownloadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(DownloadCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public DownloadCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public DownloadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
  }

  public void invoke()
  {
    String downloadPath = configuration.getDownloadPath();
    String secondaryDownloadPath = configuration.getSecondaryDownloadPath();
    List<String> secondaryDownloadLanguages = configuration.getSecondaryDownloadLanguages();
    String downloadFormat = configuration.getDownloadFormat();
    String languageKey = configuration.getLanguageKey();
    List<String> downloadOptions = configuration.getDownloadOptions();

    if (downloadPath.contains(TemplateKeys.NAMESPACE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_NAMESPACES");
    }


    if (downloadPath.contains(TemplateKeys.LANGUAGE_TEMPLATE_KEY))
    {
      downloadOptions.add("SPLIT_BY_LANGUAGES");
    }

    DownloadRequest downloadRequest = aDownloadRequest()
            .withFormat(downloadFormat)
            .withOptions(downloadOptions)
            .withLanguageKey(languageKey)
            .build();

    try
    {
    	//downloads some files twice - waste of resources fine for now
      List<DownloadableFile> downloadableFiles = client.fetchDownloadableFiles(downloadRequest);
      downloadableFiles
              .parallelStream()
              .forEach(downloadableFile -> {
            	  client.downloadFile(downloadableFile, downloadPath);
            	  if(StringUtils.isNotEmpty(secondaryDownloadPath) && (secondaryDownloadLanguages.isEmpty() || secondaryDownloadLanguages.contains(downloadableFile.getLanguage())))
            		  client.downloadFile(downloadableFile, secondaryDownloadPath);
              });
      log.info(" 🎉 Download success!");
    } catch (InterruptedException e)
    {
      log.error(" 😝 Translations could not be downloaded", e);
      Thread.currentThread().interrupt();
    } catch (IOException e)
    {
      log.error(" 😝 Translations could not be downloaded", e);
      System.exit(1);
    }
  }

}
