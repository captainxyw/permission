package com.permission.dao;

import com.permission.beans.PageQuery;
import com.permission.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  int insert(SysUser record);

  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  int insertSelective(SysUser record);

  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  SysUser selectByPrimaryKey(Integer id);

  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  int updateByPrimaryKeySelective(SysUser record);

  /**
   * This method was generated by MyBatis Generator.
   * This method corresponds to the database table sys_user
   *
   * @mbg.generated
   */
  int updateByPrimaryKey(SysUser record);

  SysUser findByKeyword(@Param("keyword") String keyword);

  int countByMail(@Param("mail") String mail, @Param("id") Integer id);

  int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

  int countByDeptId(@Param("deptId") int deptId);

  List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);
}