package com.heima.notice.web;


import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.RequestInitial;
import com.heima.modules.vo.NoticeVO;
import com.heima.notice.handler.NoticeHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Api(value = "通知Controller", tags = {"通知管理"})
@ApiResponses(@ApiResponse(code = 200, message = "处理成功"))
public class APIController {
    @Autowired
    private NoticeHandler noticeHandler;


    @ApiOperation(value = "订单列表", tags = {"通知管理"})
    @PostMapping("/list")
    @RequestInitial(groups = {Group.Select.class})
    public ResponseVO<NoticeVO> list(@RequestBody NoticeVO noticeVO) {
        return noticeHandler.list(noticeVO);
    }

}
