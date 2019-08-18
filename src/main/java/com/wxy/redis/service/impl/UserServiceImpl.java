package com.wxy.redis.service.impl;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.common.UserInfoEnum;
import com.wxy.redis.entity.User;
import com.wxy.redis.exception.ParamExceprion;
import com.wxy.redis.mapper.UserMapper;
import com.wxy.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultResponse<User> userLogin(String username, String password) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ParamExceprion(UserInfoEnum.USERNAME_PASSWORD_EMPTY.getMessage());
        }
        User user = userMapper.findByNameAndPassword(username, password);
        List<User> all = userMapper.findAll();
        if (user == null) {
            throw new ParamExceprion(UserInfoEnum.USERNAME_PASSWORD_ERROR.getMessage());
        }

        return ResultResponse.success(user);
    }
}
