package com.proper.enterprise.platform.api.auth.model;

/**
 * 应用
 *
 * 应用是一组资源的集合，系统以应用为单位为用户提供服务。
 * 应用将资源组织成树形结构的`菜单`（应用 即为最顶层的 菜单）。
 * 系统提供内置的应用、菜单和资源对应关系，并允许用户自定义。
 * 以应用为单位为`角色`进行权限分配（可选择应用内的菜单）。
 */
public interface Application extends Menu {

}
