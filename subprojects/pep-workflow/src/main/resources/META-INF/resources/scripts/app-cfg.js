/* Copyright 2005-2015 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

var FLOWABLE = FLOWABLE || {};

FLOWABLE.CONFIG = {
    'onPremise' : true,
    'contextRoot' : '/pep/workflow/service',
    'webContextRoot' : '/pep/workflow',
    'datesLocalization' : false
};

FLOWABLE.TOOLBAR = FLOWABLE.TOOLBAR || {};
FLOWABLE.TOOLBAR.ACTIONS = FLOWABLE.TOOLBAR.ACTIONS || {};

FLOWABLE.TOOLBAR.ACTIONS.customClose = function(services) {
	if (services) {
		var savePlugin;
		var plugins = services.editorManager.editor.loadedPlugins;
    		for (var i=0; i<plugins.length; i++) {
    			if (plugins[i].type == 'ORYX.Plugins.Save') {
    				savePlugin = plugins[i];
    				break;
    			}
    		}
		if (savePlugin) {
			if (savePlugin.hasChanged()) {
				// 点击X的时候自行判断是否发生变化 解决electron下onbeforeunload不好用的情况 页面跳转之前清除onbeforeunload事件防止弹2次
				if (confirm('系统可能不会保存您所做的更改，确定离开吗？')) {
					window.onbeforeunload = null;
					window.location.href = '/#/workflow/designer';
				}
			} else {
				window.location.href = '/#/workflow/designer';
			}
		}
	}

};
