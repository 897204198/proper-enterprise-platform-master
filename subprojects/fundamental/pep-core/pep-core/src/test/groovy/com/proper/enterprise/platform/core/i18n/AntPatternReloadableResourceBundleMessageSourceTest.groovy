package com.proper.enterprise.platform.core.i18n

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test

class AntPatternReloadableResourceBundleMessageSourceTest extends AbstractSpringTest {
    @Test
    void testCalculateAllFilenames() {
        AntPatternReloadableResourceBundleMessageSource antPatternReloadableResourceBundleMessageSource = new AntPatternReloadableResourceBundleMessageSource();
        assert 0 == antPatternReloadableResourceBundleMessageSource.calculateAllFilenames("is a test", Locale.getDefault()).size()
    }
}
