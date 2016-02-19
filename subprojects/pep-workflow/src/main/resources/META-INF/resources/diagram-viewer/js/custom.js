'use strict';

var PREFIX = '/workflow/service';

var replacePrefix = function(str) {
  return str.replace('/service', PREFIX);
};

ActivitiRest.options.processInstanceHighLightsUrl = replacePrefix(ActivitiRest.options.processInstanceHighLightsUrl);
ActivitiRest.options.processDefinitionUrl = replacePrefix(ActivitiRest.options.processDefinitionUrl);
ActivitiRest.options.processDefinitionByKeyUrl = replacePrefix(ActivitiRest.options.processDefinitionByKeyUrl);