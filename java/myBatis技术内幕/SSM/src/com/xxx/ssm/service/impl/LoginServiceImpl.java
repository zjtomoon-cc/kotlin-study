package com.xxx.ssm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xxx.ssm.beans.UserBean;
import com.xxx.ssm.mapper.UserMapper;
import com.xxx.ssm.service.ILoginService;
@Service
public class LoginServiceImpl implements ILoginService{
    
    @Resource
    private UserMapper um;


    @Override
    public UserBean Login(String username, String password) {
        return um.login(username, password);
    }
}