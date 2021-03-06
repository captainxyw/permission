package com.permission.dao;

import com.permission.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    int insert(SysRoleAcl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    int insertSelective(SysRoleAcl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    SysRoleAcl selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysRoleAcl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role_acl
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}