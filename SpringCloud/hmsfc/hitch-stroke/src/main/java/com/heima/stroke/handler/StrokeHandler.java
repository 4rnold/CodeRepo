package com.heima.stroke.handler;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.domin.bo.*;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.enums.InviteState;
import com.heima.commons.enums.QuickConfirmState;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.*;
import com.heima.modules.po.AccountPO;
import com.heima.modules.po.LocationPO;
import com.heima.modules.po.OrderPO;
import com.heima.modules.po.StrokePO;
import com.heima.modules.vo.LocationVO;
import com.heima.modules.vo.StrokeVO;
import com.heima.stroke.rabbitmq.MQProducer;
import com.heima.stroke.service.AccountAPIService;
import com.heima.stroke.service.LocationService;
import com.heima.stroke.service.OrderAPIService;
import com.heima.stroke.service.StrokeAPIService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Component
public class StrokeHandler {

    @Autowired
    private StrokeAPIService strokeAPIService;

    @Autowired
    private OrderAPIService orderAPIService;

    @Autowired
    private AccountAPIService accountAPIService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private MQProducer mqProducer;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 发布行程
     *
     * @return
     */
    public ResponseVO<StrokeVO> publish(StrokeVO strokeVO) {
        AccountPO accountPO = accountAPIService.getAccountByID(RequestUtils.getCurrentUserId());
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "发布人不存在");
        }
        if (strokeVO.getRole() == 1 && accountPO.getRole() == 0) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "请先认证为车主");
        }
        StrokePO strokePO = CommonsUtils.toPO(strokeVO);
        //入mysql库
        StrokePO tmp = strokeAPIService.publish(strokePO);
        Collection<HitchGeoBO> collection = initGeoData(tmp);
        for (HitchGeoBO hitchGeoBO : collection) {
            WorldMapBO worldMapBO = new WorldMapBO(hitchGeoBO.getStartGeo(), hitchGeoBO.getTargetId());
            sendStartGeo(worldMapBO);
        }
        return ResponseVO.success(tmp);
    }

    /**
     * 起始行程GEO发送
     *
     * @param worldMapBO
     */
    public void sendStartGeo(WorldMapBO worldMapBO) {
        kafkaTemplate.send(HtichConstants.STROKE_START_GEO, JSON.toJSONString(worldMapBO));
    }


    /**
     * 修改行程列表
     *
     * @return
     */
    public ResponseVO<StrokeVO> update(StrokeVO strokeVO) {
        StrokePO strokePO = CommonsUtils.toPO(strokeVO);
        strokeAPIService.update(strokePO);
        return ResponseVO.success(strokePO);
    }


    /**
     * 查看行程列表
     *
     * @return
     */
    public ResponseVO<StrokeVO> list(StrokeVO strokeVO) {
        StrokePO strokePO = CommonsUtils.toPO(strokeVO);
        List<StrokePO> result = strokeAPIService.selectlist(strokePO);
        return ResponseVO.success(result);
    }

    /**
     * 查看行程细节
     *
     * @param id
     * @return
     */
    public ResponseVO<StrokeVO> detail(String id) {
        StrokePO result = strokeAPIService.selectByID(id);
        return ResponseVO.success(result);
    }

    /**
     * 根据行程ID查看行程列表
     *
     * @param strokeVO
     * @return
     */
    public ResponseVO<StrokeVO> itineraryList(StrokeVO strokeVO) {
        StrokePO strokePO = CommonsUtils.toPO(strokeVO);
        List<StrokePO> resultList = strokeAPIService.selectlist(strokePO);
        StrokePO result = LocalCollectionUtils.getOne(resultList);
        if (null == result) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        List<StrokeVO> strokeVOS = getZsetResulets(result);
        return ResponseVO.success(strokeVOS);
    }


    /**
     * 顺风车邀请
     *
     * @param strokeVO
     * @return
     */
    public ResponseVO<StrokeVO> invite(StrokeVO strokeVO) {
        isFullStarffed(strokeVO);
        //获取司机行程ID
        String inviterTripId = strokeVO.getInviterTripId();
        //获取乘客行程ID
        String inviteeTripId = strokeVO.getInviteeTripId();

        //创建邀请状态
        // 0 = 未确认， 1 = 已确认 ， 2= 已拒绝
        redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviteeTripId, inviterTripId, String.valueOf(InviteState.UNCONFIRMED.getCode()));
        redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId, inviteeTripId, String.valueOf(InviteState.UNCONFIRMED.getCode()));
        //发送延时消息
        mqProducer.sendOver(JSON.toJSONString(strokeVO));
        quickConfirm(strokeVO);
        return ResponseVO.success(null);
    }

    /**
     * 处理邀请超时
     *
     * @param strokeVO
     */
    public void timeoutHandel(StrokeVO strokeVO) {
        //获取司机行程ID
        String inviterTripId = strokeVO.getInviterTripId();
        //获取乘客行程ID
        String inviteeTripId = strokeVO.getInviteeTripId();
        String tripeeStatus = redisHelper.getHash(HtichConstants.STROKE_INVITE_PREFIX, inviteeTripId, inviterTripId);
        String triperStatus = redisHelper.getHash(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId, inviteeTripId);
        //如果是邀请状态，未确认
        if (tripeeStatus.equals(String.valueOf(InviteState.UNCONFIRMED.getCode()))
                && triperStatus.equals(String.valueOf(InviteState.UNCONFIRMED.getCode()))) {
            //设置为超时状态
            redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviteeTripId, inviterTripId, String.valueOf(InviteState.TIMEOUT.getCode()));
            redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId, inviteeTripId, String.valueOf(InviteState.TIMEOUT.getCode()));
        }
    }

    /**
     * 乘客上车
     *
     * @param tripid
     * @return
     */
    public ResponseVO<StrokeVO> hitchhiker(String tripid) {
        //上车状态判断行程状态由 1->2
        StrokePO strokePO = travelStatusChange(tripid, 1, 2);
        return ResponseVO.success(strokePO);
    }

    /**
     * 乘客下车
     *
     * @param tripid
     * @return
     */
    @Transactional
    public ResponseVO<StrokeVO> freeride(String tripid) {
        StrokePO strokePO = travelStatusChange(tripid, 2, 3);

        OrderPO orderPO = orderAPIService.selectByTripid(tripid);
        if (null == orderPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "订单数据不存在");
        }
        //下车车状态判断订单状态由 0->1
        orderStatusChange(orderPO.getId(), 0, 1);
        return ResponseVO.success(strokePO);
    }


    /**
     * 司机发车
     *
     * @param tripid : 司机行程id
     * @return
     */
    public ResponseVO<StrokeVO> departure(String tripid) {
        //删除司机GEO数据，防止被别的乘客再看到
        redisHelper.delGEO(HtichConstants.STROKE_DRIVER_GEO_START, "", tripid);
        redisHelper.delGEO(HtichConstants.STROKE_DRIVER_GEO_END, "", tripid);

        //获取司机zset里放的乘客
        List<ZsetResultBO> zsetResultBOList = redisHelper.getZsetSortVaues(HtichConstants.STROKE_GEO_ZSET_PREFIX, tripid);
        //遍历这些乘客的zset，删除隐藏的司机信息
        for (ZsetResultBO zsetResultBO : zsetResultBOList) {
            //删除乘客zset里隐藏的司机行程信息
            redisHelper.delZsetByKey(HtichConstants.STROKE_GEO_ZSET_PREFIX, zsetResultBO.getValue(), tripid);
        }
        //删除司机的zset
        redisHelper.delKey(HtichConstants.STROKE_GEO_ZSET_PREFIX, tripid);

        //获取司机的邀请列表
        Map<String, String> inviteMap = redisHelper.getHashByMap(HtichConstants.STROKE_INVITE_PREFIX, tripid);
        //删除乘客列表中的司机信息
        for (Map.Entry<String, String> entry : inviteMap.entrySet()) {
            redisHelper.delHash(HtichConstants.STROKE_INVITE_PREFIX, entry.getKey(), tripid);
        }
        //删除司机的邀请 hset
        redisHelper.delKey(HtichConstants.STROKE_INVITE_PREFIX, tripid);

        //删除司机距离数据
        redisHelper.delKey(HtichConstants.STROKE_GEO_DISTANCE_PREFIX, tripid);

        //更新司机行程信息
        //行程ID更新，由 0 -> 1
        StrokePO strokePO = travelStatusChange(tripid, 0, 1);
        return ResponseVO.success(strokePO);
    }

    /**
     * 获取被邀请列表
     *
     * @param tripid
     * @return
     */
    public ResponseVO<StrokeVO> inviteList(String tripid) {
        Map<String, String> tripMap = redisHelper.getHashByMap(HtichConstants.STROKE_INVITE_PREFIX, tripid);
        List<StrokeVO> strokeVOS = new ArrayList<>();
        for (Map.Entry<String, String> entry : tripMap.entrySet()) {
            String key = entry.getKey();
            int status = Integer.parseInt(entry.getValue());
            StrokePO strokePO = strokeAPIService.selectByID(key);
            if (null == strokePO) {
                throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
            }
            StrokeVO strokeVO = (StrokeVO) CommonsUtils.toVO(strokePO);
            strokeVO.setInviterTripId(tripid);
            strokeVO.setInviteeTripId(key);
            strokeVO.setStatus(status);
            if (strokePO.getRole() == 1) {
                strokeVO.setQuantity(getSurplusSeats(strokePO.getId()));
            }
            strokeVOS.add(renderStrokeVO(strokeVO));
        }
        return ResponseVO.success(strokeVOS);
    }


    /**
     * 接受以及拒绝邀请
     *
     * @param strokeVO
     * @return
     */
    public ResponseVO<StrokeVO> inviteAccept(StrokeVO strokeVO) {
        isFullStarffed(strokeVO);
        //获取司机行程ID
        String inviterTripId = strokeVO.getInviterTripId();
        //判断司机是否已经发车
        boolean isDepart = redisHelper.exists(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId);
        if (!isDepart) {
            throw new BusinessRuntimeException(BusinessErrors.STOCK_ALREADY_DEPART);
        }
        //获取乘客行程ID
        String inviteeTripId = strokeVO.getInviteeTripId();
        //司机行程对象
        StrokePO inviter = strokeAPIService.selectByID(inviterTripId);
        //乘客行程对象
        StrokePO invitee = strokeAPIService.selectByID(inviteeTripId);
        //创建邀请状态
        int status = strokeVO.getStatus();
        InviteState state = InviteState.getState(status);
        if (null == state) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_STATUS_ERROR);
        }

        // 修改邀请状态
        // 0 = 未确认， 1 = 已确认 ， 2= 已拒绝
        redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviteeTripId, inviterTripId, String.valueOf(state.getCode()));
        redisHelper.addHash(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId, inviteeTripId, String.valueOf(state.getCode()));

        //确认同行
        if (state == InviteState.CONFIRMED) {
            //行程ID更新，由 0 -> 1
            travelStatusChange(inviteeTripId, 0, 1);
            addOrder(inviter, invitee);

            //清理相关缓存，重要！
            strokeVO.setRole(0);
            unbindStroke(strokeVO);
        }
        return ResponseVO.success(null);
    }

    /**
     * 确认送达
     *
     * @param tripid
     * @return
     */
    public ResponseVO<StrokeVO> delivery(String tripid) {
        //行程状态变更，由 1->3
        StrokePO strokePO = travelStatusChange(tripid, 1, 3);
        return ResponseVO.success(strokePO);
    }


    /**
     * 获取排序后的结果
     *
     * @param strokePO
     * @return
     */
    public List<StrokeVO> getZsetResulets(StrokePO strokePO) {
        List<ZsetResultBO> zsetResultBOList = redisHelper.getZsetSortVaues(HtichConstants.STROKE_GEO_ZSET_PREFIX, strokePO.getId());
        List<StrokeVO> strokeVOS = new ArrayList<>();
        for (ZsetResultBO zsetResultBO : zsetResultBOList) {
            StrokePO tmp = strokeAPIService.selectByID(zsetResultBO.getValue());
            if (null == tmp) {
                continue;
            }
            //跳过同一个用户是司机以及乘客的情况
            if (tmp.getPublisherId().equals(strokePO.getPublisherId())) {
                continue;
            }
            StrokeVO strokeVO = (StrokeVO) CommonsUtils.toVO(tmp);
            strokeVO.setInviterTripId(strokePO.getId());
            strokeVO.setInviteeTripId(tmp.getId());
            //乘客查看司机的空余座位数
            if (tmp.getRole() == 1) {
                strokeVO.setQuantity(getSurplusSeats(tmp.getId()));
            }
            renderStrokeVO(strokeVO);
            strokeVO.setSuitability(zsetResultBO.getScore().toString());
            strokeVOS.add(strokeVO);
        }
        return strokeVOS;
    }

    /**
     * 检查是否已满员
     *
     * @param strokeVO
     */
    private void isFullStarffed(StrokeVO strokeVO) {
        //获取司机行程ID
        int surplusSeats = getSurplusSeats(strokeVO.getInviterTripId());
        //座位数大于等于已确认人数 抛出异常
        if (surplusSeats <= 0) {
            throw new BusinessRuntimeException(BusinessErrors.STOCK_FULL_STARFFED);
        }
    }

    /**
     * 添加订单
     *
     * @param inviter 司机行程对象
     * @param invitee 乘客行程对象
     */
    private void addOrder(StrokePO inviter, StrokePO invitee) {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(CommonsUtils.getWorkerID());
        orderPO.setDriverStrokeId(inviter.getId());
        orderPO.setDriverId(inviter.getPublisherId());
        orderPO.setPassengerStrokeId(invitee.getId());
        orderPO.setPassengerId(invitee.getPublisherId());
        orderPO.setCreatedBy(invitee.getCreatedBy());
        orderPO.setCreatedTime(new Date());
        orderPO.setUpdatedBy(invitee.getCreatedBy());
        orderPO.setUpdatedTime(new Date());
        orderPO.setStatus(0);
        //批量算路服务
        RoutePlanResultBO resultBO = getRoutePlanResult(invitee);
        if (null != resultBO) {
            orderPO.setDistance(resultBO.getDistance().getValue());
            orderPO.setEstimatedTime(resultBO.getDuration().getValue());
            orderPO.setCost(CommonsUtils.valuationPrice(orderPO.getDistance()));
        }
        orderAPIService.add(orderPO);
    }

    private RoutePlanResultBO getRoutePlanResult(StrokePO invitee) {
        String start = invitee.getStartGeoLat() + "," + invitee.getStartGeoLng();
        String end = invitee.getEndGeoLat() + "," + invitee.getEndGeoLng();
        List<RoutePlanResultBO> routePlanResultBOS = BaiduMapClient.pathPlanning(start, end);
        if (null != routePlanResultBOS && !routePlanResultBOS.isEmpty()) {
            return routePlanResultBOS.stream().findFirst().get();
        }
        return null;
    }

    /**
     * 获取剩余座位数
     *
     * @return
     */
    private int getSurplusSeats(String inviterTripId) {
        StrokePO strokePO = strokeAPIService.selectByID(inviterTripId);
        if (null == strokePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        Map<String, String> driverMap = redisHelper.getHashByMap(HtichConstants.STROKE_INVITE_PREFIX, inviterTripId);
        //获取已确认的成员数量
        int member = 0;
        for (Map.Entry<String, String> entry : driverMap.entrySet()) {
            //已确认行程的数据进行统计
            if (entry.getValue().equals(String.valueOf(InviteState.CONFIRMED.getCode()))) {
                member++;
            }
        }
        return strokePO.getQuantity() - member;
    }


    /**
     * 闪电确认
     *
     * @param strokeVO
     */
    private void quickConfirm(StrokeVO strokeVO) {
        StrokePO strokePO = strokeAPIService.selectByID(strokeVO.getInviteeTripId());
        if (null == strokePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        QuickConfirmState state = QuickConfirmState.getState(strokePO.getQuickConfirm());
        if (state == QuickConfirmState.ENABLED) {
            strokeVO.setStatus(InviteState.CONFIRMED.getCode());
            inviteAccept(strokeVO);
        }
    }


    /**
     * 解除行程绑定
     *
     * @param strokeVO
     */
    private void unbindStroke(StrokeVO strokeVO) {
        String unbindId = null;

        if (strokeVO.getRole().equals(0)) {
            unbindId = strokeVO.getInviteeTripId();
            //删除乘客端起点GEO信息
            redisHelper.delGEO(HtichConstants.STROKE_PASSENGER_GEO_START, "", unbindId);
            //删除乘客端终点点GEO信息
            redisHelper.delGEO(HtichConstants.STROKE_PASSENGER_GEO_END, "", unbindId);
        } else if (strokeVO.getRole().equals(1)) {
            unbindId = strokeVO.getInviterTripId();
            //删除司机端起点GEO信息
            redisHelper.delGEO(HtichConstants.STROKE_DRIVER_GEO_START, "", unbindId);
            //删除司机端终点点GEO信息
            redisHelper.delGEO(HtichConstants.STROKE_DRIVER_GEO_END, "", unbindId);
        }

        //取乘客的zset
        //通过乘客获取司机的
        List<ZsetResultBO> zsetResultBOList = redisHelper.getZsetSortVaues(HtichConstants.STROKE_GEO_ZSET_PREFIX, unbindId);
        for (ZsetResultBO zsetResultBO : zsetResultBOList) {
            //删除司机的zset里所有当前乘客的id，包含确认同行的那个司机
            redisHelper.delZsetByKey(HtichConstants.STROKE_GEO_ZSET_PREFIX, zsetResultBO.getValue(), unbindId);
        }
        //删除乘客端zset
        redisHelper.delKey(HtichConstants.STROKE_GEO_ZSET_PREFIX, unbindId);

        //取乘客的邀请hset
        Map<String, String> driverMap = redisHelper.getHashByMap(HtichConstants.STROKE_INVITE_PREFIX, unbindId);
        //遍历，找对方（邀请过我的司机）的列表，从这些列表里移除自己的id
        for (Map.Entry<String, String> entry : driverMap.entrySet()) {
            //已确认同行的不进行删除，用于座位数计算，不包含确认同行的那个司机
            if (entry.getValue().equals(String.valueOf(InviteState.CONFIRMED.getCode()))) {
                continue;
            }
            //删除司机的hash关联关系
            redisHelper.delHash(HtichConstants.STROKE_INVITE_PREFIX, entry.getKey(), unbindId);
        }
        //删除乘客邀请hset
        redisHelper.delKey(HtichConstants.STROKE_INVITE_PREFIX, unbindId);

        //删除乘客距离hset
        redisHelper.delKey(HtichConstants.STROKE_GEO_DISTANCE_PREFIX, unbindId);
    }

    /**
     * 行程数据渲染
     *
     * @param strokeVO
     */
    private StrokeVO renderStrokeVO(StrokeVO strokeVO) {
        //渲染距离数据
        String distanceStr = redisHelper.getHash(HtichConstants.STROKE_GEO_DISTANCE_PREFIX, strokeVO.getInviterTripId(), strokeVO.getInviteeTripId());
        //设置起点距离
        if (StringUtils.isNotEmpty(distanceStr) && distanceStr.contains(":")) {
            String[] distances = distanceStr.split(":");
            strokeVO.setStartDistance(Float.parseFloat(distances[0]));
            strokeVO.setEndDistance(Float.parseFloat(distances[1]));
        }

        //获取用户对象
        AccountPO accountPO = accountAPIService.getAccountByID(strokeVO.getPublisherId());
        if (null != accountPO) {
            strokeVO.setUseralias(accountPO.getUseralias());
            strokeVO.setAvatar(accountPO.getAvatar());
        }
        return strokeVO;
    }


    /**
     * 初始化GEO数据
     *
     * @param strokePO
     */
    private Collection<HitchGeoBO> initGeoData(StrokePO strokePO) {
        //添加GEO数据
        publishGeoData(strokePO);
        //筛选出来对应的数据
        Collection<HitchGeoBO> hitchGeoBOList = geoFilterMatch(strokePO);
        //记录所有行程
        for (HitchGeoBO hitchGeoBO : hitchGeoBOList) {
            //所有行程记录我
            redisHelper.addZset(HtichConstants.STROKE_GEO_ZSET_PREFIX, hitchGeoBO.getTargetId(), strokePO.getId(), getScore(hitchGeoBO));
            redisHelper.addHash(HtichConstants.STROKE_GEO_DISTANCE_PREFIX, hitchGeoBO.getTargetId(), strokePO.getId(), getDistanceStr(hitchGeoBO));
            //我记录所有行程
            redisHelper.addZset(HtichConstants.STROKE_GEO_ZSET_PREFIX, strokePO.getId(), hitchGeoBO.getTargetId(), getScore(hitchGeoBO));
            redisHelper.addHash(HtichConstants.STROKE_GEO_DISTANCE_PREFIX, strokePO.getId(), hitchGeoBO.getTargetId(), getDistanceStr(hitchGeoBO));
        }
        return hitchGeoBOList;
    }


    /**
     * 添加GEO数据
     *
     * @param strokePO
     */
    private void publishGeoData(StrokePO strokePO) {
        //乘客/司机开始key 乘客筛选司机 。司机筛选乘客
        String start_key = strokePO.getRole() == 0 ? HtichConstants.STROKE_PASSENGER_GEO_START : HtichConstants.STROKE_DRIVER_GEO_START;
        //乘客/司机结束key
        String end_key = strokePO.getRole() == 0 ? HtichConstants.STROKE_PASSENGER_GEO_END : HtichConstants.STROKE_DRIVER_GEO_END;
        //添加开始结束的GEO信息
        redisHelper.addGEO(start_key, "", strokePO.getStartGeoLng(), strokePO.getStartGeoLat(), strokePO.getId());
        redisHelper.addGEO(end_key, "", strokePO.getEndGeoLng(), strokePO.getEndGeoLat(), strokePO.getId());
    }

    /**
     * Geo筛选匹配
     * 用户 筛选出来对应司机的GEO
     * 司机 筛选出来对应用户的GEO
     *
     * @param strokePO
     * @return
     */
    private Collection<HitchGeoBO> geoFilterMatch(StrokePO strokePO) {
        //乘客/司机开始key 乘客筛选司机 。司机筛选乘客
        String start_key = strokePO.getRole() == 0 ? HtichConstants.STROKE_DRIVER_GEO_START : HtichConstants.STROKE_PASSENGER_GEO_START;
        //乘客/司机结束key
        String end_key = strokePO.getRole() == 0 ? HtichConstants.STROKE_DRIVER_GEO_END : HtichConstants.STROKE_PASSENGER_GEO_END;
        //开始geo数据
        Map<String, GeoBO> startGeoBOMap = redisHelper.geoNearByXY(start_key, "", Float.parseFloat(strokePO.getStartGeoLng()), Float.parseFloat(strokePO.getStartGeoLat()));

        //结束的geo数据
        Map<String, GeoBO> endGeoBOMap = redisHelper.geoNearByXY(end_key, "", Float.parseFloat(strokePO.getEndGeoLng()), Float.parseFloat(strokePO.getEndGeoLat()));
        //定义map key的set
        Set<String> startSet = startGeoBOMap.keySet(), endSet = endGeoBOMap.keySet();
        //计算交集, 开始集合以及结束集合都包含的数据
        Collection<String> selectKeys = CollectionUtils.intersection(startSet, endSet);
        List<HitchGeoBO> hitchGeoBOList = new ArrayList<>();
        for (String key : selectKeys) {
            hitchGeoBOList.add(new HitchGeoBO(key, startGeoBOMap.get(key), endGeoBOMap.get(key)));
        }
        return hitchGeoBOList;
    }

    /**
     * 处理行程状态变更
     *
     * @param tripid
     * @param orginStatus
     * @param targetStatus
     */

    private StrokePO travelStatusChange(String tripid, int orginStatus, int targetStatus) {
        StrokePO strokePO = new StrokePO();
        strokePO.setId(tripid);
        strokePO.setStatus(orginStatus);
        //如果查询行程状态错误则抛出异常
        List<StrokePO> strokePOList = strokeAPIService.selectlist(strokePO);
        if (strokePOList.isEmpty()) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_STATUS_ERROR, "行程状态错误");
        }
        strokePO.setStatus(targetStatus);
        strokeAPIService.update(strokePO);
        return strokePO;
    }


    /**
     * 订单状态变更
     *
     * @param tripid
     * @param orginStatus
     * @param targetStatus
     */

    private OrderPO orderStatusChange(String tripid, int orginStatus, int targetStatus) {
        OrderPO orderPO = new OrderPO();
        orderPO.setId(tripid);
        orderPO.setStatus(orginStatus);
        //如果查询行程状态错误则抛出异常
        List<OrderPO> orderPOList = orderAPIService.selectlist(orderPO);
        if (orderPOList.isEmpty()) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_STATUS_ERROR, "订单状态错误");
        }
        orderPO.setStatus(targetStatus);
        orderAPIService.update(orderPO);
        return orderPO;
    }


    /**
     * 计算分值
     *
     * @param hitchGeoBO
     * @return
     */
    private float getScore(HitchGeoBO hitchGeoBO) {
        return ((float) (1 - (hitchGeoBO.getStartGeo().getDistance() * 0.5 + hitchGeoBO.getEndGeo().getDistance() * 0.5) / HtichConstants.STROKE_DIAMETER_RANGE)) * 100;
    }


    /**
     * 获取距离数据
     *
     * @param hitchGeoBO
     * @return
     */
    private String getDistanceStr(HitchGeoBO hitchGeoBO) {
        return hitchGeoBO.getStartGeo().toKilometre() + ":" + hitchGeoBO.getEndGeo().toKilometre().toString();
    }

    /**
     * 发送实时位置服务
     *
     * @param locationVO
     * @return
     */
    public ResponseVO<LocationVO> realtimeLocation(LocationVO locationVO) {
        mqProducer.sendLocation(JSON.toJSONString(locationVO));
        return ResponseVO.success(locationVO);
    }

    /**
     * 获取当前位置信息
     *
     * @param tripid
     * @return
     */
    public ResponseVO<LocationVO> currentLocation(String tripid) {
        LocationPO locationPO = locationService.currentLocation(tripid);
        return ResponseVO.success(locationPO);
    }
}
