package io.simplelocalize.cli.util;

import com.google.common.collect.Lists;
import io.simplelocalize.cli.client.dto.FileToUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Feel free to use or extend this utility
 */
public class FileReaderUtil
{

  private static final Logger log = LoggerFactory.getLogger(FileReaderUtil.class);

  private FileReaderUtil()
  {

  }

  public static List<String> tryReadLines(Path filePath)
  {
    Path decodedFilePath = null;
    List<String> fileLines = Collections.emptyList();
    try
    {
      decodedFilePath = Paths.get(URLDecoder.decode(String.valueOf(Paths.get(String.valueOf(filePath))), StandardCharsets.UTF_8));
      fileLines = Files.readAllLines(decodedFilePath, StandardCharsets.UTF_8);
    } catch (IOException e)
    {
      log.warn("Cannot read file from path " + decodedFilePath.toString(), e);
    }
    return fileLines;
  }


  public static String tryReadContent(Path filePath)
  {
    Path decodedFilePath = null;
    try
    {
      decodedFilePath = Paths.get(URLDecoder.decode(String.valueOf(Paths.get(String.valueOf(filePath))), StandardCharsets.UTF_8));
      return Files.readString(decodedFilePath, StandardCharsets.UTF_8);
    } catch (IOException e)
    {
      log.warn("Cannot read file from path " + decodedFilePath.toString(), e);
    }
    return "";
  }


  public static List<FileToUpload> getMatchingFilesToUpload(Path uploadPathWithTemplateKey, String templateKey) throws IOException
  {
    List<FileToUpload> output = Lists.newArrayList();
    File file = uploadPathWithTemplateKey.toFile();
    File parentDirectoryFile = file.getParentFile();

    Path parentDirectory = Path.of("./");
    if (parentDirectoryFile != null)
    {
      parentDirectory = parentDirectoryFile.toPath();
    }

    try (Stream<Path> foundFilesStream = Files.walk(parentDirectory, 1))
    {
      var foundFiles = foundFilesStream.collect(Collectors.toList());
      Path fileNameWithTemplateKey = uploadPathWithTemplateKey.getFileName();
      String escapedTemplateKey = String.format("\\%s", templateKey);
      String[] split = fileNameWithTemplateKey.toString().split(escapedTemplateKey);
      String beforeLanguageTemplateKey = split[0];
      String afterLanguageTemplateKey = split[1];

      for (Path foundFile : foundFiles)
      {
        String fileName = foundFile.getFileName().toString();
        if (fileName.contains(beforeLanguageTemplateKey) && fileName.contains(afterLanguageTemplateKey))
        {
          String removedFirstPart = StringUtils.remove(fileName, beforeLanguageTemplateKey);
          String plainLanguageKey = StringUtils.remove(removedFirstPart, afterLanguageTemplateKey);
          output.add(FileToUpload.of(foundFile, plainLanguageKey));
        }
      }
      return output;
    }
  }

}
