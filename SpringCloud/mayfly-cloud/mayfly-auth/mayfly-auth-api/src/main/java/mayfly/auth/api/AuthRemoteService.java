package mayfly.auth.api;

import mayfly.auth.api.constant.AuthConst;
import mayfly.auth.api.model.req.AuthDTO;
import mayfly.auth.api.model.res.AuthResDTO;
import mayfly.core.model.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author meilin.huang
 * @date 2022-03-19 19:33
 */
@FeignClient(AuthConst.PROJECT_NAME)
public interface AuthRemoteService {

    /**
     * 请求鉴权
     *
     * @param authReq 鉴权信息请求
     * @return 鉴权结果
     */
    @PostMapping("/remote/auth")
    Result<AuthResDTO> auth(@RequestBody AuthDTO authReq);
}
