package mayfly.auth.biz.remote;

import mayfly.auth.api.AuthRemoteService;
import mayfly.auth.api.model.req.AuthDTO;
import mayfly.auth.api.model.res.AuthResDTO;
import mayfly.auth.biz.service.PermissionService;
import mayfly.core.model.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @date 2022-03-25 11:17
 */
@RestController
public class AuthRemoteServiceImpl implements AuthRemoteService {

    @Autowired
    private PermissionService permissionService;

    @Override
    public Result<AuthResDTO> auth(AuthDTO authReq) {
        return Result.success(permissionService.auth(authReq));
    }
}
