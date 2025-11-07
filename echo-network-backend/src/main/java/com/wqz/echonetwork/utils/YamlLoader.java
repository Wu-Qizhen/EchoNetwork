package com.wqz.echonetwork.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class YamlLoader {
    private Map<String, Object> config;

    public YamlLoader(String configPath) {
        loadConfig(configPath);
    }

    /**
     * 智能加载配置文件
     * 支持类路径、绝对路径、相对路径
     */
    private void loadConfig(String configPath) {
        try {
            InputStream inputStream;

            // 1. 首先尝试从类路径加载
            inputStream = getClass().getClassLoader().getResourceAsStream(configPath);

            // 2. 如果类路径找不到，尝试作为文件系统路径加载
            if (inputStream == null) {
                Path confPath = Paths.get(configPath);
                if (Files.exists(confPath)) {
                    // inputStream = new FileInputStream(configPath);
                    inputStream = Files.newInputStream(confPath);
                } else {
                    // 3. 尝试在当前工作目录下查找
                    String workingDirPath = System.getProperty("user.dir") + "/" + configPath;
                    Path workPath = Paths.get(workingDirPath);
                    if (Files.exists(workPath)) {
                        // inputStream = new FileInputStream(workingDirPath);
                        inputStream = Files.newInputStream(workPath);
                    } else {
                        throw new RuntimeException("配置文件未找到：" + configPath);
                    }
                }
            }

            Yaml yaml = new Yaml();
            config = yaml.load(inputStream);
            inputStream.close();

        } catch (Exception e) {
            throw new RuntimeException("配置文件加载失败：" + configPath, e);
        }
    }

    /**
     * 获取字符串配置值
     */
    public String getString(String key) {
        Object value = getNestedValue(key);
        if (value == null) {
            throw new RuntimeException("配置项 “" + key + "” 不存在");
        }

        // 无论原始类型是什么，都转换为字符串
        return String.valueOf(value);
    }

    public String getString(String key, String defaultValue) {
        Object value = getNestedValue(key);
        if (value == null) {
            return defaultValue;
        }
        return String.valueOf(value);
    }

    /**
     * 获取整数配置值
     */
    public Integer getInt(String key) {
        return getValue(key, Integer.class);
    }

    public Integer getInt(String key, Integer defaultValue) {
        Object value = getNestedValue(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.valueOf(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取布尔配置值
     */
    public Boolean getBoolean(String key) {
        return getValue(key, Boolean.class);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object value = getNestedValue(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String stringValue = String.valueOf(value).toLowerCase();
        if ("true".equals(stringValue)) {
            return true;
        } else if ("false".equals(stringValue)) {
            return false;
        }
        return defaultValue;
    }

    /**
     * 获取嵌套配置
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSection(String key) {
        Object value = getNestedValue(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        throw new RuntimeException("配置项 “" + key + "” 不是对象类型");
    }

    /**
     * 通用获取配置值方法
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        Object value = getNestedValue(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        throw new RuntimeException("配置项 “" + key + "” 不存在或类型不匹配");
    }

    /**
     * 支持点分隔符的嵌套配置访问
     */
    private Object getNestedValue(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = config;

        for (int i = 0; i < keys.length - 1; i++) {
            Object next = current.get(keys[i]);
            if (next instanceof Map) {
                current = (Map<String, Object>) next;
            } else {
                return null;
            }
        }

        return current.get(keys[keys.length - 1]);
    }

    /**
     * 检查配置项是否存在
     */
    public boolean contains(String key) {
        try {
            return getNestedValue(key) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取整个配置
     */
    public Map<String, Object> getAllConfig() {
        return config;
    }
}
