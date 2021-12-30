package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.FileListReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static io.simplelocalize.cli.io.FileListReader.LANGUAGE_TEMPLATE_KEY;

public class UploadCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(UploadCommand.class);
  private final FileListReader fileListReader;
  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public UploadCommand(Configuration configuration)
  {
    this.configuration = configuration;
    this.client = SimpleLocalizeClient.withProductionServer(configuration);
    this.fileListReader = new FileListReader();
  }

  public void invoke()
  {
    Path configurationUploadPath = configuration.getUploadPath();
    String uploadPath = configurationUploadPath.toString();

    List<FileToUpload> filesToUpload = List.of();
    try
    {
      filesToUpload = getFilesToUpload(configuration);
    } catch (IOException e)
    {
      log.error(" 😝 Matching files could not be found", e);
      System.exit(1);
    }

    for (FileToUpload fileToUpload : filesToUpload)
    {
      try
      {
        String language = fileToUpload.getLanguage();
        String uploadFormat = configuration.getUploadFormat();
        String uploadOptions = configuration.getUploadOptions();
        String relativePath = null;

        if (isMultiFileUpload(uploadOptions))
        {
          String filePath = fileToUpload.getPath().toString();
          relativePath = filePath.replaceFirst(uploadPath, "");
        }

        client.uploadFile(fileToUpload.getPath(), language, uploadFormat, uploadOptions, relativePath);
      } catch (InterruptedException | IOException e)
      {
        log.warn(" 😝 File {} could not be uploaded", fileToUpload.getPath(), e);
      }
    }
  }

  private List<FileToUpload> getFilesToUpload(Configuration configuration) throws IOException
  {
    Path configurationUploadPath = configuration.getUploadPath();
    String uploadPath = configurationUploadPath.toString();
    boolean hasLanguageKeyInPath = uploadPath.contains(LANGUAGE_TEMPLATE_KEY);
    String uploadLanguageKey = configuration.getLanguageKey();
    boolean isMultiFileUpload = isMultiFileUpload(configuration.getUploadOptions());
    if (hasLanguageKeyInPath && StringUtils.isNotBlank(uploadLanguageKey))
    {
      log.error(" 😝 You cannot use '{lang}' param in upload path and '--languageKey' option together.");
      System.exit(1);
    }
    if (isMultiFileUpload)
    {
      File uploadPathFile = configurationUploadPath.toFile();
      if (!uploadPathFile.isDirectory())
      {
        log.error(" 😝 You must a directory with '--uploadPath' parameter in order to use 'MULTI_FILE' upload option.");
        System.exit(1);
      }
      return fileListReader.getFilesForMultiFileUpload(configuration);
    }
    if (hasLanguageKeyInPath)
    {
      return fileListReader.getMatchingFilesToUpload(configurationUploadPath, LANGUAGE_TEMPLATE_KEY);
    }
    FileToUpload fileToUpload = FileToUpload.of(configurationUploadPath, uploadLanguageKey);
    return Collections.singletonList(fileToUpload);
  }

  private boolean isMultiFileUpload(String uploadOptions)
  {
    if (StringUtils.isEmpty(uploadOptions))
    {
      return false;
    }
    return uploadOptions.toUpperCase(Locale.ROOT).contains("MULTI_FILE");
  }
}
