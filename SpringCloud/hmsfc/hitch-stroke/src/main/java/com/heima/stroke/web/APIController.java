package com.heima.stroke.web;

import com.heima.commons.domin.bo.GeoBO;
import com.heima.commons.domin.bo.RoutePlanResultBO;
import com.heima.commons.domin.bo.WorldMapBO;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.RequestInitial;
import com.heima.commons.utils.BaiduMapClient;
import com.heima.modules.vo.LocationVO;
import com.heima.modules.vo.OrderVO;
import com.heima.modules.vo.StrokeVO;
import com.heima.stroke.handler.StrokeHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/")
@Api(value = "行程操作Controller", tags = {"行程管理"})
@ApiResponses(@ApiResponse(code = 200, message = "处理成功"))
public class APIController {

    @Autowired
    private StrokeHandler strokeHandler;


    @ApiOperation(value = "发布行程", tags = {"行程管理"})
    @PostMapping("/publish")
    @RequestInitial(groups = {Group.Create.class})
    public ResponseVO<StrokeVO> publish(@Validated(Group.Create.class) @RequestBody StrokeVO strokeVO) {
        return strokeHandler.publish(strokeVO);
    }

    @ApiOperation(value = "发送起始坐标", tags = {"行程管理"})
    @PostMapping("/sendStartGeo")
    public ResponseVO<StrokeVO> sendStartGeo(@RequestBody WorldMapBO worldMapBO) {
        strokeHandler.sendStartGeo(worldMapBO);
        return ResponseVO.success(null);
    }

    @ApiOperation(value = "修改行程信息", tags = {"行程管理"})
    @PostMapping("/update")
    @RequestInitial(groups = {Group.Update.class})
    public ResponseVO<StrokeVO> update(@Validated(Group.Update.class) @RequestBody StrokeVO strokeVO) {
        return strokeHandler.update(strokeVO);
    }


    @ApiOperation(value = "查看个人行程列表", tags = {"行程管理"})
    @PostMapping("/list")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<StrokeVO> list(@RequestBody StrokeVO strokeVO) {
        return strokeHandler.list(strokeVO);
    }

    @ApiOperation(value = "查看行程细节", tags = {"行程管理"})
    @PostMapping("/detail/{id}")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<StrokeVO> detail(@PathVariable("id") String id) {
        return strokeHandler.detail(id);
    }

    @ApiOperation(value = "查看顺路行程列表", tags = {"行程管理"})
    @PostMapping("/itinerary/list")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<StrokeVO> itineraryList(@RequestBody StrokeVO strokeVO) {
        return strokeHandler.itineraryList(strokeVO);
    }

    @ApiOperation(value = "顺风车邀请接口", tags = {"行程管理"})
    @PostMapping("/invite")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<StrokeVO> invite(@RequestBody StrokeVO strokeVO) {
        return strokeHandler.invite(strokeVO);
    }

    @ApiOperation(value = "顺风车邀请列表", tags = {"行程管理"})
    @PostMapping("/invite/list/{tripid}")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<StrokeVO> inviteList(@PathVariable("tripid") String tripid) {
        return strokeHandler.inviteList(tripid);
    }

    @ApiOperation(value = "顺风车接受邀请", tags = {"行程管理"})
    @PostMapping("/invite/accept")
    public ResponseVO<StrokeVO> inviteAccept(@RequestBody StrokeVO strokeVO) {
        return strokeHandler.inviteAccept(strokeVO);
    }


    @ApiOperation(value = "乘客上车", tags = {"行程管理"})
    @PostMapping("/hitchhiker/{tripid}")
    public ResponseVO<StrokeVO> hitchhiker(@PathVariable("tripid") String tripid) {
        return strokeHandler.hitchhiker(tripid);
    }

    @ApiOperation(value = "乘客下车", tags = {"行程管理"})
    @PostMapping("/freeride/{tripid}")
    public ResponseVO<StrokeVO> freeride(@PathVariable("tripid") String tripid) {
        return strokeHandler.freeride(tripid);
    }

    @ApiOperation(value = "司机发车", tags = {"行程管理"})
    @PostMapping("/departure/{tripid}")
    public ResponseVO<StrokeVO> departure(@PathVariable("tripid") String tripid) {
        return strokeHandler.departure(tripid);
    }

    @ApiOperation(value = "确认送达", tags = {"行程管理"})
    @PostMapping("/delivery/{tripid}")
    public ResponseVO<StrokeVO> delivery(@PathVariable("tripid") String tripid) {
        return strokeHandler.delivery(tripid);
    }

    @ApiOperation(value = "实时位置", tags = {"行程管理"})
    @PostMapping("/realtimeLocation")
    public ResponseVO<LocationVO> realtimeLocation(@RequestBody LocationVO locationVO) {
        locationVO.setTime(new Date());
        return strokeHandler.realtimeLocation(locationVO);
    }

    @ApiOperation(value = "获取当前位置", tags = {"行程管理"})
    @PostMapping("/currentLocation/{tripid}")
    public ResponseVO<LocationVO> currentLocation(@PathVariable("tripid") String tripid) {
        return strokeHandler.currentLocation(tripid);
    }

}
