/*
 * Copyright 2017-2022 shiyoufeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piperplatform.pes.commons.web.service;

import com.piperplatform.pes.commons.entity.BaseResponse;
import com.piperplatform.pes.commons.web.entity.dto.UserInfoDTO;

/**
 * User microservice interface
 *
 * @author youfeng.shi
 * @date 2022/4/22
 */
public interface UserMicroService {

    /**
     * login
     *
     * @param username username
     * @param password password
     * @return user ID
     */
    BaseResponse<Long> login(String username, String password);

    /**
     * The development user packages the ordinary user login, and the designated user logs in with one click in the test environment
     * @param devUsername Development username
     * @param devPassword Develop user password
     * @param username Specify login username
     * @return Specify login user ID
     */
    BaseResponse<Long> devWrapLogin(String devUsername, String devPassword, String username);

    /**
     * Get login user information
     * @return User information
     */
    BaseResponse<UserInfoDTO> userInfo();

    /**
     * Registered user
     *
     * @param username username
     * @param password password
     * @return Operation status
     */
    BaseResponse<?> register(String username, String password);

    /**
     * Check whether the username exists
     *
     * @param username username
     * @return Status 1: exists
     */
    BaseResponse<Integer> hasUsername(String username);

    /**
     * Modify user password
     *
     * @param username    username
     * @param oldPassword Old password
     * @param newPassword New password
     * @return Modification results
     */
    BaseResponse<?> changePassword(String username, String oldPassword, String newPassword);

}
