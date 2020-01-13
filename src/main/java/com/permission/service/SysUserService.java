package com.permission.service;

import com.google.common.base.Preconditions;
import com.permission.beans.PageQuery;
import com.permission.beans.PageResult;
import com.permission.common.RequestHolder;
import com.permission.dao.SysUserMapper;
import com.permission.exception.ParamException;
import com.permission.model.SysUser;
import com.permission.param.UserParam;
import com.permission.util.BeanValidator;
import com.permission.util.IpUtil;
import com.permission.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Package:com.permission.service
 * Description:
 *
 * @Date:2020/1/8 20:24
 * @Author:xuyewei
 */
@Service
public class SysUserService {


  @Resource
  private SysUserMapper sysUserMapper;

  public void save(UserParam param) {
    BeanValidator.check(param);
    if(checkTelephoneExist(param.getTelephone(), param.getId())) {
      throw new ParamException("电话已被占用");
    }
    if(checkEmailExist(param.getMail(), param.getId())) {
      throw new ParamException("邮箱以被占用");
    }
//    String password = PasswordUtil.randomPassword();
    //TODO
    String password = "123456";
    String encryptedPassword = MD5Util.encrypt(password);

    SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
        .password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
    user.setOperator(RequestHolder.getCurrentUser().getUsername());
    user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    user.setOperateTime(new Date());
    //TODO send email


    sysUserMapper.insertSelective(user);

  }

  public void update(UserParam param) {
    BeanValidator.check(param);
    if(checkTelephoneExist(param.getTelephone(), param.getId())) {
      throw new ParamException("电话已被占用");
    }
    if(checkEmailExist(param.getMail(), param.getId())) {
      throw new ParamException("邮箱以被占用");
    }
    SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
    Preconditions.checkNotNull(before, "待更新的用户不存在");
    SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
        .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
    after.setOperator(RequestHolder.getCurrentUser().getUsername());
    after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    after.setOperateTime(new Date());
    sysUserMapper.updateByPrimaryKeySelective(after);


  }

  public boolean checkEmailExist(String mail, Integer userId) {
    return sysUserMapper.countByMail(mail, userId) > 0;
  }

  public boolean checkTelephoneExist(String telephone, Integer userId) {
    return sysUserMapper.countByTelephone(telephone, userId) > 0;
  }

  public SysUser findByKeyword(String keyword) {
    return sysUserMapper.findByKeyword(keyword);
  }


  public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
    BeanValidator.check(page);
    int count = sysUserMapper.countByDeptId(deptId);
    if(count > 0) {
      List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
      return PageResult.<SysUser>builder().total(count).data(list).build();
    }
    return PageResult.<SysUser>builder().build();
  }

  public List<SysUser> getAll() {
    return sysUserMapper.getAll();
  }

}
