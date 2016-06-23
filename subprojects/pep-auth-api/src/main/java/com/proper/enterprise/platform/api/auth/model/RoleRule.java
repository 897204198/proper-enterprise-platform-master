package com.proper.enterprise.platform.api.auth.model;

/**
 * 角色规则
 *
 * 角色规则决定角色与`用户`的关系。
 *
 * 权限模块提供三种规则，并可由各模块自行扩展。
 *  - 所有用户拥有此角色
 *  - 限定用户拥有此角色
 *  - 限定用户不拥有此角色
 *
 *  // TODO
 > 需提供用户选择器

 角色规则中需配置定义规则的界面模板，用来嵌入至角色定义功能的界面中。
 */
public interface RoleRule {
}
