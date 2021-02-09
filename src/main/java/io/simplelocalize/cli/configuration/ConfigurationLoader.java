package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigurationLoader
{

  private static final String CURRENT_DIRECTORY = ".";
  private static final Path DEFAULT_CONFIG_FILE_NAME = Path.of("simplelocalize.yml");

  private final Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

  public Configuration loadOrGetDefault(Path configurationFilePath)
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();

    if (configurationFilePath == null)
    {
      configurationFilePath = DEFAULT_CONFIG_FILE_NAME;
    }

    return configurationLoader.load(configurationFilePath);
  }

  private Configuration load(Path configurationFilePath)
  {
    File file = new File(URLDecoder.decode(String.valueOf(configurationFilePath.toFile()), StandardCharsets.UTF_8));
    Constructor yamlTargetClass = new Constructor(Configuration.class);
    Yaml yaml = new Yaml(yamlTargetClass);

    Configuration configuration;
    try
    {
      InputStream inputStream = new FileInputStream(file);
      configuration = yaml.load(inputStream);
      log.info(" 🗄 Loaded configuration file from: {}", configurationFilePath);
    } catch (Exception e)
    {
      log.info(" 🗄 Using default configuration. Configuration file not found at: {}", configurationFilePath);
      configuration = new Configuration();
      configuration.setSearchDir(CURRENT_DIRECTORY);
      return configuration;
    }

    String uploadToken = configuration.getUploadToken();
    String apiKey = configuration.getApiKey();
    if (Strings.isNullOrEmpty(apiKey))
    {
      configuration.setApiKey(uploadToken);
    }

    String searchDir = configuration.getSearchDir();
    if (Strings.isNullOrEmpty(searchDir))
    {
      configuration.setSearchDir(CURRENT_DIRECTORY);
    }

    return configuration;

  }

}
