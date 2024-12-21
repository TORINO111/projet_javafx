package dette.boutique.core.services.impl;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import dette.boutique.core.services.YamlService;

public class YamlServiceImpl implements YamlService {

    protected String path = "META-INF/application.yaml";

    @Override
    public Map<String, Object> loadYaml() {
        return this.loadYaml(path);
    }

    @Override
    public Map<String, Object> loadYaml(String path) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + path);
        }
        return yaml.load(inputStream);
    }

}
