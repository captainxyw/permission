# 企业级权限管理系统
[课程地址](https://coding.imooc.com/class/149.html)

## 项目介绍
基于最流行的RBAC拓展模型的，高灵活性，高拓展性的企业级权限管理系统。

## 功能模块
### 1.部门模块
部门信息树形结构展示，对部门的增加、修改和删除功能。
### 2.用户模块
根据部门分页获取用户列表并展示，对用户的增加、修改和删除功能。
### 3.权限模块
权限模块信息树形结构展示，对权限模块的增加、修改和删除功能。
### 4.权限点模块
根据权限模块分页获取权限点列表并展示，对权限点的增加、修改和删除功能。
### 5.角色模块
获取角色列表并展示，对角色的增加、修改和删除功能。
### 6.权限关系维护
角色-权限树形结构列表，更新角色-权限关系功能。
### 7.用户关系维护
获取指定角色已分配用户列表和为分配用户列表，更新角色-用户关系功能。
### 8.日志模块
带查询条件分页展示权限日志，根据权限日志撤销之前的操作。


## 技术架构
### 后端技术：
* Spring
* Spring MVC
* MyBatis
* Maven
* Tomcat
* MySQL
* JDK 1.8
### 前端技术：
* jQuery
* Bootstrap

## 系统效果图
* 部门用户界面，可以看到所有部门和部门下的所有用户，可以对其进行修改，和删除（修改状态）
![alt 部门用户界面](https://raw.githubusercontent.com/captainxyw/permission/master/img/user.png "部门用户界面")
* 角色与用户界面，可以看到所有角色，以及角色与用户的关系（用户是否拥有该角色）
![alt 角色与用户界面](https://raw.githubusercontent.com/captainxyw/permission/master/img/role-user.png "角色与用户界面")
* 角色与权限界面，可以看到所有角色，以及角色与权限的关系（角色是否拥有该权限）
![alt 角色与权限界面](https://raw.githubusercontent.com/captainxyw/permission/master/img/role-perm.png "角色与权限界面")
* 权限模块界面，可以看到所有权限模块，以及该权限模块下的所有权限
![alt 权限模块界面](https://raw.githubusercontent.com/captainxyw/permission/master/img/permmodule.png "权限模块界面")
