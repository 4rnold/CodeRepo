package com.heima.order.handler;

import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.initial.factory.InitialParserFactory;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.LocalCollectionUtils;
import com.heima.commons.utils.RequestUtils;
import com.heima.modules.po.AccountPO;
import com.heima.modules.po.OrderPO;
import com.heima.modules.po.StrokePO;
import com.heima.modules.po.VehiclePO;
import com.heima.modules.vo.OrderVO;
import com.heima.order.service.AccountAPIService;
import com.heima.order.service.OrderAPIService;
import com.heima.order.service.StrokeAPIService;
import com.heima.order.service.VehicleAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderHandler {
    @Autowired
    private OrderAPIService orderAPIService;

    @Autowired
    private AccountAPIService accountAPIService;

    @Autowired
    private StrokeAPIService strokeAPIService;

    @Autowired
    private VehicleAPIService vehicleAPIService;

    /**
     * 生成订单影子
     *
     * @return
     */
    public ResponseVO<OrderVO> generateShadow() {
        OrderPO orderPO = (OrderPO) InitialParserFactory.initialDefValueForPO(new OrderPO());
        orderPO.setStatus(CommonsUtils.randomInt(1, 2));
        orderPO.setEstimatedTime(CommonsUtils.randomInt(600, 4800));
        orderPO.setDistance(CommonsUtils.randomInt(5000, 75000));
        orderPO.setCost(CommonsUtils.valuationPrice(orderPO.getDistance()));
        orderPO.setDriverId("111");
        orderPO.setPassengerId("222");
        orderPO.setDriverStrokeId("333");
        orderPO.setPassengerStrokeId("444");
        orderAPIService.addShadow(orderPO);
        return ResponseVO.success(orderPO);
    }

    /**
     * 我的订单 首页
     *
     * @param orderVO
     * @return
     */
    public ResponseVO<OrderVO> list(OrderVO orderVO) {
        OrderPO orderPO = CommonsUtils.toPO(orderVO);
        //乘客
        if (orderVO.getRole() == 0) {
            orderPO.setPassengerId(orderVO.getCurrentUserId());
            orderPO.setStatus(1);
            //司机
        } else if (orderVO.getRole() == 1) {
            orderPO.setDriverId(orderVO.getCurrentUserId());
            orderPO.setStatus(1);
        }
        List<OrderPO> orderPOList = orderAPIService.selectAvailableList(orderPO);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderPO order : orderPOList) {
            orderVOList.add(renderOrder(order));
        }
        return ResponseVO.success(orderVOList);
    }


    /**
     * 已支付订单列表
     *
     * @return
     */
    public ResponseVO<OrderVO> paidList() {
        String userid = RequestUtils.getCurrentUserId();
        OrderPO orderPO = new OrderPO();
        orderPO.setPassengerId(userid);
        orderPO.setDriverId(userid);
        orderPO.setStatus(2);
        List<OrderPO> orderPOList = orderAPIService.selectPaidList(orderPO);
        return ResponseVO.success(orderPOList);
    }

    /**
     * 同行乘客列表
     *
     * @param orderVO
     * @return
     */
    public ResponseVO<OrderVO> fellows(OrderVO orderVO) {
        OrderPO orderPO = CommonsUtils.toPO(orderVO);
        orderPO.setDriverId(orderVO.getCurrentUserId());
        orderPO.setStatus(0);
        List<OrderPO> orderPOList = orderAPIService.selectAvailableList(orderPO);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderPO order : orderPOList) {
            orderVOList.add(renderOrder(order));
        }
        return ResponseVO.success(orderVOList);
    }

    /**
     * 订单渲染
     *
     * @param orderPO
     * @return
     */
    private OrderVO renderOrder(OrderPO orderPO) {
        OrderVO orderVO = (OrderVO) CommonsUtils.toVO(orderPO);
        //用户列表查询渲染
        //用户角色渲染
        AccountPO passengerAccount = accountAPIService.getAccountByID(orderPO.getPassengerId());
        if (null == passengerAccount) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "乘客不存在");
        }
        orderVO.setPassengerAvatar(passengerAccount.getAvatar());
        orderVO.setPassengerUseralias(passengerAccount.getUseralias());
        orderVO.setPassengerPhone(passengerAccount.getPhone());
        //用户行程封装
        StrokePO passengerStroke = strokeAPIService.selectByID(orderPO.getPassengerStrokeId());
        if (null == passengerStroke) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "乘客行程不存在");
        }
        orderVO.setPassengerStartDate(passengerStroke.getDepartureTime());
        orderVO.setPassengerStartAddr(passengerStroke.getStartAddr());
        orderVO.setPassengerEndAddr(passengerStroke.getEndAddr());
        orderVO.setPassengerStrokeStatus(passengerStroke.getStatus());
        //司机角色渲染
        AccountPO driverAccount = accountAPIService.getAccountByID(orderPO.getDriverId());
        if (null == driverAccount) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "司机不存在");
        }
        orderVO.setDriverAvatar(driverAccount.getAvatar());
        orderVO.setDriverUseralias(driverAccount.getUseralias());
        orderVO.setDriverPhone(driverAccount.getPhone());
        //司机行程封装
        StrokePO driverStroke = strokeAPIService.selectByID(orderPO.getDriverStrokeId());
        if (null == driverAccount) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "司机行程不存在");
        }
        orderVO.setDriverStartDate(driverStroke.getDepartureTime());
        orderVO.setDriverStartAddr(driverStroke.getStartAddr());
        orderVO.setDriverEndAddr(driverStroke.getEndAddr());
        orderVO.setDriverStrokeStatus(driverStroke.getStatus());
        return orderVO;
    }

    /**
     * 根据乘客信息查看司机以及行程ID
     *
     * @param tripid
     * @return
     */
    public ResponseVO<OrderVO> viewOrder(String tripid) {
        OrderPO orderReq = new OrderPO();
        orderReq.setPassengerStrokeId(tripid);
        List<OrderPO> orderList = orderAPIService.selectlist(orderReq);
        OrderPO orderPO = LocalCollectionUtils.getOne(orderList);
        if (orderPO == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "订单不存在");
        }
        //司机基础数据渲染
        OrderVO orderVO = renderOrder(orderPO);
        //车辆数据封装
        VehiclePO vehicleReq = new VehiclePO();
        AccountPO accountPO = accountAPIService.getAccountByID(orderPO.getDriverId());
        if (accountPO == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        VehiclePO vehiclePO = vehicleAPIService.selectByPhone(accountPO.getPhone());
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "车辆不存在");
        }
        orderVO.setCarNumber(vehiclePO.getCarNumber());
        orderVO.setCarFrontPhoto(vehiclePO.getCarFrontPhoto());
        orderVO.setCarBackPhoto(vehiclePO.getCarBackPhoto());
        orderVO.setCarSidePhoto(vehiclePO.getCarSidePhoto());
        return ResponseVO.success(orderVO);
    }


}
