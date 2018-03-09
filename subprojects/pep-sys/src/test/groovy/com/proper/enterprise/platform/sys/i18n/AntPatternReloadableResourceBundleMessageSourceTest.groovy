package com.proper.enterprise.platform.sys.i18n

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class AntPatternReloadableResourceBundleMessageSourceTest extends AbstractTest {
    @Test
    void testCalculateAllFilenames() {
        AntPatternReloadableResourceBundleMessageSource antPatternReloadableResourceBundleMessageSource = new AntPatternReloadableResourceBundleMessageSource();
        assert 0 == antPatternReloadableResourceBundleMessageSource.calculateAllFilenames("is a test", Locale.getDefault()).size()
    }
}
