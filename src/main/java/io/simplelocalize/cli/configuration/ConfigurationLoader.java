package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import io.simplelocalize.cli.exception.ConfigurationNotFoundException;
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

public class ConfigurationLoader {

  private static final String CURRENT_DIRECTORY = ".";
  private static final Path DEFAULT_CONFIG_FILE_NAME = Path.of("./simplelocalize.yml");

  private final Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

  public Configuration loadOrDefault(Path configurationFilePath){
    ConfigurationLoader configurationLoader = new ConfigurationLoader();

    if (configurationFilePath == null)
    {
      configurationFilePath = DEFAULT_CONFIG_FILE_NAME;
    }

    boolean defaultConfigurationExists = DEFAULT_CONFIG_FILE_NAME.toFile().exists();
    if (!defaultConfigurationExists)
    {
      return new Configuration();
    }

    return configurationLoader.load(configurationFilePath);
  }

  public Configuration load(Path configurationFilePath) {

    log.info("Loading file from path: {}", configurationFilePath);

    File file = new File(URLDecoder.decode(String.valueOf(configurationFilePath.toFile()), StandardCharsets.UTF_8));

    if (!file.exists()) {
      throw new ConfigurationNotFoundException("Could not find configuration file in: " + configurationFilePath);
    }

    Constructor yamlTargetClass = new Constructor(Configuration.class);
    Yaml yaml = new Yaml(yamlTargetClass);

    Configuration configuration;
    try {
      InputStream inputStream = new FileInputStream(file);
      configuration = yaml.load(inputStream);
    } catch (Exception e) {
      throw new ConfigurationNotFoundException("Could not read configuration file in: " + configurationFilePath, e);
    }

    String searchDir = configuration.getSearchDir();
    if (Strings.isNullOrEmpty(searchDir)) {
      configuration.setSearchDir(CURRENT_DIRECTORY);
    }

    String uploadToken = configuration.getUploadToken();
    String apiKey = configuration.getApiKey();
    if (Strings.isNullOrEmpty(apiKey)) {
      configuration.setApiKey(uploadToken);
    }
    return configuration;

  }

}
