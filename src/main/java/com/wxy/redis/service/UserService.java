package com.wxy.redis.service;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.entity.User;

public interface UserService {

    ResultResponse<User> userLogin(String username, String password);

}
