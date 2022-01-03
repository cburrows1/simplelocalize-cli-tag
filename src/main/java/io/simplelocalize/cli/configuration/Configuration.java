package io.simplelocalize.cli.configuration;

import io.micronaut.core.annotation.Introspected;

import java.util.Set;

@Introspected
public class Configuration
{

  private String apiKey;

  private String projectType;
  private String searchDir;
  private Set<String> ignoreKeys = Set.of();

  private Set<String> ignorePaths = Set.of();

  private String uploadPath;
  private String uploadFormat;
  private String uploadOptions;

  private String downloadPath;
  private String downloadFormat;
  private String downloadOptions;

  private String languageKey;

  public String getSearchDir()
  {
    return searchDir;
  }

  public void setSearchDir(String searchDir)
  {
    this.searchDir = searchDir;
  }

  public String getApiKey()
  {
    return apiKey;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  public String getProjectType()
  {
    return projectType;
  }

  public void setProjectType(String projectType)
  {
    this.projectType = projectType;
  }

  public Set<String> getIgnoreKeys()
  {
    return ignoreKeys;
  }

  public void setIgnoreKeys(Set<String> ignoreKeys)
  {
    this.ignoreKeys = ignoreKeys;
  }

  public String getUploadPath()
  {
    return uploadPath;
  }

  public void setUploadPath(String uploadPath)
  {
    this.uploadPath = uploadPath;
  }

  public String getUploadFormat()
  {
    return uploadFormat;
  }

  public void setUploadFormat(String uploadFormat)
  {
    this.uploadFormat = uploadFormat;
  }

  public String getDownloadPath()
  {
    return downloadPath;
  }

  public void setDownloadPath(String downloadPath)
  {
    this.downloadPath = downloadPath;
  }

  public String getDownloadFormat()
  {
    return downloadFormat;
  }

  public String getDownloadOptions()
  {
    return downloadOptions;
  }

  public void setDownloadOptions(String downloadOptions)
  {
    this.downloadOptions = downloadOptions;
  }

  public void setDownloadFormat(String downloadFormat)
  {
    this.downloadFormat = downloadFormat;
  }

  public String getLanguageKey()
  {
    return languageKey;
  }

  public void setLanguageKey(String languageKey)
  {
    this.languageKey = languageKey;
  }

  public String getUploadOptions()
  {
    return uploadOptions;
  }

  public void setUploadOptions(String uploadOptions)
  {
    this.uploadOptions = uploadOptions;
  }

  public Set<String> getIgnorePaths()
  {
    return ignorePaths;
  }

  public void setIgnorePaths(Set<String> ignorePaths)
  {
    this.ignorePaths = ignorePaths;
  }
}
