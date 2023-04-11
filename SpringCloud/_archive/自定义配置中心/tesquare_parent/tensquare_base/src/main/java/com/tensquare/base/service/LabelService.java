//package com.tensquare.base.service;
//
//import com.tensquare.base.dao.LabelDao;
//import com.tensquare.base.pojo.Label;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import util.IdWorker;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 标签的业务层代码
// * @author 黑马程序员
// * @Company http://www.ithiema.com
// */
//@Service
//public class LabelService {
//
//    @Autowired
//    private LabelDao labelDao;
//
//    @Autowired
//    private IdWorker idWorker;
//
//    /**
//     * 查询所有
//     * @return
//     */
//    @Cacheable(value = "labellist",key = "")
//    public List<Label> findAll(){
//        return labelDao.findAll();
//    }
//
//    /**
//     * 根据id查询
//     * @param id
//     * @return
//     */
//    public Label findById(String id){
//        return labelDao.findById(id).get();
//    }
//
//    /**
//     * 保存
//     * @param label
//     */
//    @CacheEvict(value="labellist",key = "")
//    public void save(Label label){
//        //1.给id赋值
//        label.setId(String.valueOf(idWorker.nextId()));
//        //保存
//        labelDao.save(label);
//    }
//
//    /**
//     * 更新
//     * @param label
//     */
//    @CacheEvict(value="labellist",key = "")
//    public void update(Label label){
//        labelDao.save(label);
//    }
//
//    /**
//     * 删除
//     * @param id
//     */
//    @CacheEvict(value="labellist",key = "")
//    public void delete(String id){
//        labelDao.deleteById(id);
//    }
//
//    /**
//     * 条件查询
//     * @param condition  查询的条件 最多就是labelname  state recommend三个属性有值
//     * @return
//     */
//    public List<Label> findByCondition(Label condition){
//        //1.创建条件对象
//        Specification<Label> spec = this.generateCondition(condition);
//        //2.调用持久层方法查询并返回
//        return labelDao.findAll(spec);
//    }
//
//    /**
//     * 查询带条件和分页
//     * @param condition 查询条件
//     * @param page      当前页 它是从0开始的
//     * @param size      每页显示的条数
//     * @return
//     */
//    public Page<Label> findPage(Label condition, int page, int size){
//        //1.创建查询条件
//        Specification<Label> spec = this.generateCondition(condition);
//        //2.创建查询条件对象
//        Pageable pageable = PageRequest.of(page-1,size);
//        //3.调用持久层方法查询并返回
//        return labelDao.findAll(spec,pageable);
//    }
//
//    /**
//     * 生成查询条件
//     * @param condition
//     * @return
//     */
//    private Specification<Label> generateCondition(Label condition){
//      return  new Specification<Label>() {
//            /**
//             * 拼装条件
//             * @param root    操作的实体封装
//             * @param query   查询对象
//             * @param cb      条件构建对象
//             * @return
//             */
//            @Override
//            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                //定义条件的集合
//                List<Predicate> predicates = new ArrayList<>();
//
//                //1.判断是否输入了标签名称
//                if(!StringUtils.isEmpty(condition.getLabelname())) {
//                    Predicate p1 = cb.like(root.get("labelname"), "%" + condition.getLabelname() + "%");
//                    predicates.add(p1);
//                }
//                //2.判断是否选择了标签状态
//                if(!StringUtils.isEmpty(condition.getState())){
//                    Predicate p2 = cb.equal(root.get("state"),condition.getState());
//                    predicates.add(p2);
//                }
//                //3.判断是否选择了标签是否推荐
//                if(!StringUtils.isEmpty(condition.getRecommend())){
//                    Predicate p3 = cb.equal(root.get("recommend"),condition.getRecommend());
//                    predicates.add(p3);
//                }
//
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//    }
//
//
//
//
//}
