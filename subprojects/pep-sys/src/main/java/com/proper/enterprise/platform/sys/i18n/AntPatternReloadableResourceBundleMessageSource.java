package com.proper.enterprise.platform.sys.i18n;

import com.proper.enterprise.platform.core.utils.AntResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AntPatternReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AntPatternReloadableResourceBundleMessageSource.class);

    private static final String PROPERTIES_SUFFIX = ".properties";

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /** Cache to hold filename lists per Locale */
    private final ConcurrentMap<String, Map<Locale, List<String>>> cachedFilenames = new ConcurrentHashMap<>();

    @Override
    protected List<String> calculateAllFilenames(String basenamePattern, Locale locale) {
        Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basenamePattern);
        if (localeMap != null) {
            List<String> fileNames = localeMap.get(locale);
            if (fileNames != null) {
                return fileNames;
            }
        }

        List<String> fileNames = new ArrayList<>(7);
        try {
            Resource[] resources = AntResourceUtil.getResources(basenamePattern);
            for (Resource resource : resources) {
                String basename = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
                LOGGER.debug("Calculate basename {} from pattern {}", basename, basenamePattern);
                fileNames.addAll(calculateFilenamesForLocale(basename, locale));
                if (isFallbackToSystemLocale() && !locale.equals(Locale.getDefault())) {
                    List<String> fallbackFilenames = calculateFilenamesForLocale(basename, Locale.getDefault());
                    for (String fallbackFilename : fallbackFilenames) {
                        if (!fileNames.contains(fallbackFilename)) {
                            // Entry for fallback locale that isn't already in filenames list.
                            fileNames.add(fallbackFilename);
                        }
                    }
                }
                fileNames.add(basename);
            }
        } catch (IOException e) {
            LOGGER.warn("Error occurs when loading i18n resources!", e);
        }

        if (localeMap == null) {
            localeMap = new ConcurrentHashMap<>(1);
            Map<Locale, List<String>> existing = this.cachedFilenames.putIfAbsent(basenamePattern, localeMap);
            if (existing != null) {
                localeMap = existing;
            }
        }
        localeMap.put(locale, fileNames);
        return fileNames;
    }

    @Override
    protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
        if (filename.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
            return refreshClassPathProperties(filename, propHolder);
        } else {
            return super.refreshProperties(filename, propHolder);
        }
    }

    private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
        Properties properties = new Properties();
        long lastModified = -1;
        try {
            Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
            for (Resource resource : resources) {
                String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
                PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
                properties.putAll(holder.getProperties());
                if (lastModified < resource.lastModified()) {
                    lastModified = resource.lastModified();
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Error occurs when retrieving some i18n resource!", e);
        }
        return new PropertiesHolder(properties, lastModified);
    }

}
