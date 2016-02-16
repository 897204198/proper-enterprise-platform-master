/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

// Change close editor target location
var KISBPM = KISBPM || {};
KISBPM.URL = KISBPM.URL || {};
KISBPM.URL.afterClose = '../../#/workflow/designer';

/**
 * 为了简化流程设计步骤，合并 activiti 提供的流程设计器中的 “模型”、“部署” 和 “流程定义” 概念
 * 对外统一表述为 “流程”
 * 故需要同步原来的三个概念中的 名称/描述 信息项
 * 此指令用来同步设计器中的 名称/描述 至保存对话框中
 */
var wdUnify = function() {
  return {
    restrict: 'A',
    link: function(scope) {
      // 弹出保存对话框时将设计器中的 名称/描述 同步到对话框中显示
      var properties = scope.editor.getJSON().properties;
      scope.saveDialog.name = properties.name;
      scope.saveDialog.description = properties.documentation;

      // 为避免在保存对话框中修改 名称/描述 后还需要同步回设计器，禁止在保存对话框中修改
      scope.readonly = true;
    }
  };
};

angular.module('activitiModeler').directive('wdUnify', wdUnify);