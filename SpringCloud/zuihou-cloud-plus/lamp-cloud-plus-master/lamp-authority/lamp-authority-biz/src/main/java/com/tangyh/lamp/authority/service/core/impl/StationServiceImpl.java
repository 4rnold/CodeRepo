package com.tangyh.lamp.authority.service.core.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangyh.basic.annotation.injection.InjectionResult;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.base.service.SuperCacheServiceImpl;
import com.tangyh.basic.cache.model.CacheKeyBuilder;
import com.tangyh.basic.database.mybatis.auth.DataScope;
import com.tangyh.basic.database.mybatis.conditions.Wraps;
import com.tangyh.basic.database.mybatis.conditions.query.LbqWrapper;
import com.tangyh.basic.utils.CollHelper;
import com.tangyh.lamp.authority.dao.core.StationMapper;
import com.tangyh.lamp.authority.dto.core.StationPageQuery;
import com.tangyh.lamp.authority.entity.core.Station;
import com.tangyh.lamp.authority.service.core.StationService;
import com.tangyh.lamp.common.cache.core.StationCacheKeyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 岗位
 * </p>
 *
 * @author zuihou
 * @date 2019-07-22
 */
@Slf4j
@Service
@DS("#thread.tenant")
public class StationServiceImpl extends SuperCacheServiceImpl<StationMapper, Station> implements StationService {
    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new StationCacheKeyBuilder();
    }

    @Override
    @InjectionResult
    public IPage<Station> findStationPage(IPage<Station> page, PageParams<StationPageQuery> params) {
        StationPageQuery data = params.getModel();
        Station station = BeanUtil.toBean(data, Station.class);

        //Wraps.lbQ(station); 这种写法值 不能和  ${ew.customSqlSegment} 一起使用
        LbqWrapper<Station> wrapper = Wraps.lbq(null, params.getExtra(), Station.class);

        // ${ew.customSqlSegment} 语法一定要手动eq like 等
        wrapper.like(Station::getName, station.getName())
                .like(Station::getDescribe, station.getDescribe())
                .eq(Station::getOrg, station.getOrg())
                .eq(Station::getState, station.getState());
        return baseMapper.findStationPage(page, wrapper, new DataScope());
    }

    @Override
    public Map<Serializable, Object> findStationByIds(Set<Serializable> ids) {
        return CollHelper.uniqueIndex(findStation(ids), Station::getId, station -> station);
    }

    @Override
    public Map<Serializable, Object> findStationNameByIds(Set<Serializable> ids) {
        return CollHelper.uniqueIndex(findStation(ids), Station::getId, Station::getName);
    }

    private List<Station> findStation(Set<Serializable> ids) {
        // 强转， 防止数据库隐式转换，  若你的id 是string类型，请勿强转
        return findByIds(ids,
                missIds -> super.listByIds(missIds.stream().filter(Objects::nonNull).map(Convert::toLong).collect(Collectors.toList()))
        );
    }

}
