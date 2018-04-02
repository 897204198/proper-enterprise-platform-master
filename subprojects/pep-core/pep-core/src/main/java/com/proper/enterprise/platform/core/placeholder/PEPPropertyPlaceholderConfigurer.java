package com.proper.enterprise.platform.core.placeholder;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;


public class PEPPropertyPlaceholderConfigurer extends Properties {

    private String location;
    private String names;

    public void init() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        if (StringUtils.isEmpty(location) || StringUtils.isEmpty(names)) {
            return;
        }
        Resource[] resources = resolver.getResources(location);
        for (Resource resource : resources) {
            this.load(resource.getInputStream());
        }
        for (String name : names.split(",")) {
            this.setProperty(name, getEnumerableProperty(this.getProperty(name)));
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    private static String getEnumerableProperty(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        for (PlaceholderEnum placeholder : PlaceholderEnum.values()) {
            if (value.contains(placeholder.getName())) {
                value = value.replaceAll(placeholder.getName(), placeholder.getPlaceholderValue());
            }
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, names);
    }

    private enum PlaceholderEnum {
        UUID_SUFFIX("random.uuid"),
        UUID_32_SUFFIX("random.uuid32");

        PlaceholderEnum(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String getPlaceholderValue() {
            switch (this) {
                case UUID_SUFFIX:
                    return UUID.randomUUID().toString();
                case UUID_32_SUFFIX:
                    return UUID.randomUUID().toString().replaceAll("-", "");
                default:
                    return "";
            }
        }
    }
}
