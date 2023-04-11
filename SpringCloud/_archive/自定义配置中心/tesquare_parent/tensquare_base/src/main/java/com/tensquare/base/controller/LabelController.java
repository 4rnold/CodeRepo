//package com.tensquare.base.controller;
//
//import com.tensquare.base.pojo.Label;
//import com.tensquare.base.service.LabelService;
//import entity.PageResult;
//import entity.Result;
//import entity.StatusCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 标签的控制器
// * @author 黑马程序员
// * @Company http://www.ithiema.com
// */
//@RestController
//@RequestMapping("/label")
//@CrossOrigin //开启跨域访问
//public class LabelController {
//
//    @Autowired
//    private LabelService labelService;
//
//    /**
//     * 查询所有操作
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET)
//    public Result findAll(){
//        //1.调用业务层查询
//        List<Label> labels = labelService.findAll();
//        //2.创建返回值对象并返回
//        return new Result(true, StatusCode.OK,"查询成功",labels);
//    }
//
//    /**
//     * 根据id查询
//     * @param id
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET,value="/{id}")
//    public Result findById(@PathVariable("id") String id){
//        //1.调用业务层查询
//        Label label = labelService.findById(id);
//        //2.创建返回值对象并返回
//        return new Result(true, StatusCode.OK,"查询成功",label);
//    }
//
//    /**
//     * 保存
//     * @param label
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST)
//    public Result save(@RequestBody Label label){
//        //1.调用业务层保存
//        labelService.save(label);
//        //2.创建返回值并返回
//        return new Result(true, StatusCode.OK,"保存成功");
//    }
//
//    /**
//     * 更新操作
//     * @param label
//     * @param id
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
//    public Result update(@RequestBody Label label, @PathVariable("id") String id){
//        //给label的id属性赋值
//        label.setId(id);
//        //1.调用业务层更新
//        labelService.update(label);
//        //2.创建返回值并返回
//        return new Result(true, StatusCode.OK,"更新成功");
//    }
//
//    /**
//     * 根据id删除
//     * @param id
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
//    public Result delete(@PathVariable("id") String id){
//        //1.调用业务层删除
//        labelService.delete(id);
//        //2.创建返回值并返回
//        return new Result(true, StatusCode.OK,"删除成功");
//    }
//
//    /**
//     * 条件查询
//     * @param condition
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST,value="/search")
//    public Result findByCondition(@RequestBody Label condition){
//        //1.调用业务层查询
//        List<Label> labels = labelService.findByCondition(condition);
//        //2.创建返回值并返回
//        return new Result(true, StatusCode.OK,"查询成功",labels);
//    }
//
//    /**
//     * 条件查询带分页
//     * @param condition 查询条件
//     * @param page      当前页
//     * @param size      页大小
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST,value = "/search/{page}/{size}")
//    public Result findPage(@RequestBody Label condition, @PathVariable("page") int page, @PathVariable("size") int size){
//        //1.调用业务层查询
//        Page<Label> labelPage = labelService.findPage(condition,page,size);
//        //2.创建带有分页信息的返回值对象
//        PageResult<Label> pageResult = new PageResult<>(labelPage.getTotalElements(),labelPage.getContent());
//        //3.创建返回值并返回
//        return new Result(true, StatusCode.OK,"查询成功",pageResult);
//    }
//}
