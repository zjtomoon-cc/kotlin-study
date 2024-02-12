package com.xxx.ssm.service;


import com.xxx.ssm.beans.UserBean;

public interface ILoginService {

    public UserBean Login(String username,String password);
    
    
}