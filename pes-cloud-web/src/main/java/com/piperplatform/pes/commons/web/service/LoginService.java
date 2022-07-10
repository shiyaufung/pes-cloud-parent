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

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.piperplatform.pes.commons.entity.BaseResponse;
import com.piperplatform.pes.commons.entity.MsSession;
import com.piperplatform.pes.commons.enums.HttpStatus;
import com.piperplatform.pes.commons.enums.PesConstants;
import com.piperplatform.pes.commons.util.StrUtils;
import com.piperplatform.pes.commons.web.entity.dto.DevLoginDTO;
import com.piperplatform.pes.commons.web.entity.dto.LoginDTO;
import com.piperplatform.pes.commons.web.entity.dto.UserInfoDTO;
import com.piperplatform.pes.commons.web.entity.vo.TokenVO;
import com.piperplatform.pes.commons.web.manager.PesSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * User business layer implementation
 *
 * @author youfeng.shi
 * @date 2022/4/24
 */
@Service
public class LoginService {

    @Value("${spring.profiles.active}")
    private String env;

    public void captcha(Producer captchaProducer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // Generate verification code
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // Write to client
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    public BaseResponse<?> login(UserMicroService userService, LoginDTO loginDto, HttpServletRequest request) {
        if (StrUtil.equals(env, PesConstants.ENV_PROD)){
            String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (StrUtils.isEmpty(loginDto.getCaptcha()) || StrUtils.isEmpty(captcha) || !captcha.equals(loginDto.getCaptcha())) {
                return BaseResponse.error(HttpStatus.A0130);
            }
        }
        BaseResponse<Long> response = userService.login(loginDto.getUsername(), loginDto.getPassword());

        if (!response.isSuccess()) {
            return response;
        }
        Long uid = response.getData();

        TokenVO tokenVO = PesSessionManager.login(uid, loginDto.getRememberMe());

        MsSession msSession = new MsSession();
        msSession.setId(uid);
        msSession.setToken(tokenVO.getTv());
        StpUtil.getTokenSessionByToken(tokenVO.getTv()).set(PesConstants.SESSION, msSession);
        return BaseResponse.success(tokenVO);
    }

    public BaseResponse<?> devWrapLogin(UserMicroService userService, DevLoginDTO devLoginDTO, HttpServletRequest request) {
        if (StrUtil.equals(env, PesConstants.ENV_PROD)){
            String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (StrUtils.isEmpty(devLoginDTO.getCaptcha()) || StrUtils.isEmpty(captcha) || !captcha.equals(devLoginDTO.getCaptcha())) {
                return BaseResponse.error(HttpStatus.A0130);
            }
        }
        BaseResponse<Long> response = userService.devWrapLogin(devLoginDTO.getDevUsername(), devLoginDTO.getDevPassword(), devLoginDTO.getTestUsername());

        if (!response.isSuccess()) {
            return response;
        }

        TokenVO tokenVO = PesSessionManager.login(response.getData(), devLoginDTO.getRememberMe());
        return BaseResponse.success(tokenVO);
    }

    /**
     * Get basic user information
     *
     * @return User basic information
     */
    public BaseResponse<?> info(UserMicroService userService) throws Exception {
        BaseResponse<UserInfoDTO> result = userService.userInfo();
        if (!result.isSuccess()) {
            return result;
        }
        String token = StpUtil.getTokenValue();
        UserInfoDTO userInfoDTO = result.getData();
        MsSession msSession = (MsSession) StpUtil.getTokenSessionByToken(token).get(PesConstants.SESSION);
        msSession.setName(userInfoDTO.getEmpName());
        msSession.setEmpId(userInfoDTO.getEmpId());
        StpUtil.getTokenSessionByToken(token).set(PesConstants.SESSION, msSession);

        PesSessionManager.set(PesConstants.USER_INFO, userInfoDTO);
        return result;
    }

    public BaseResponse<?> logout() {
        StpUtil.logout();
        return BaseResponse.success();
    }

}
