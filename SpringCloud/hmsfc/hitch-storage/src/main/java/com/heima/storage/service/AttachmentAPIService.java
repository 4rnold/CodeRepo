package com.heima.storage.service;

import com.heima.modules.po.AttachmentPO;
import com.heima.storage.mapper.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attachment")
public class AttachmentAPIService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    /**
     * 账户注册
     *
     * @param record
     * @return
     */
    @RequestMapping("/register")
    public AttachmentPO register(@RequestBody AttachmentPO record) {
        attachmentMapper.insert(record);
        return record;
    }

    /**
     * 列表查询
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    List<AttachmentPO> selectList(AttachmentPO record) {
        return attachmentMapper.selectList(record);
    }

    /**
     * 账户修改
     *
     * @param record
     */

    @RequestMapping("/update")
    public void update(@RequestBody AttachmentPO record) {
        attachmentMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 根据ID获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public AttachmentPO selectByID(@PathVariable("id") String id) {
        return attachmentMapper.selectByPrimaryKey(id);
    }
}
