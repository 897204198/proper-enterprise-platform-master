package com.proper.enterprise.platform.core.json

import spock.lang.Specification

class JSONObjectSpec extends Specification {

    def "Update json string from #input to #output"() {
        JSONObject jsonObject = JSONUtil.parseObject(input);
        if (jsonObject.containsKey(key)){
            jsonObject.remove(key);
        }

        jsonObject.put(key, value);

        expect:
        output == jsonObject.toString();

        where:
        input           | key   | value | output
        '{"a": "a1"}'   | 'b'   | 'b1'  | '{"a":"a1","b":"b1"}'
    }

}
