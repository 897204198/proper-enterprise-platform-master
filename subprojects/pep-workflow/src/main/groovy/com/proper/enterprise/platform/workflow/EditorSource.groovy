package com.proper.enterprise.platform.workflow


class EditorSource {

    private EditorSource() { }

    /**
     * 返回创建流程模型时的初始化数据（json 字符串）
     * 在初始化数据中带入流程 id 及流程名称
     *
     * 利用 groovy 的多行字符串特性，
     * 避免使用 java 代码编写时转义换行等对初始数据内容造成的可读性干扰
     *
     * @param procId        流程 id
     * @param name          流程名称
     * @param description   流程描述
     * @return json 字符串
     */
    static String initialSource(String procId, String name, String description) {
        """
{
    "properties": {
      "process_id": "$procId",
      "name": "$name",
      "documentation": "${description ? description.replace('\r', '\\r').replace('\n', '\\n') : ''}"
    },
    "stencilset": {
      "url": "stencilsets/bpmn2.0/bpmn2.0.json",
      "namespace": "http://b3mn.org/stencilset/bpmn2.0#"
    }
}
"""
    }

}
