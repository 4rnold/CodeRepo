package com.heima.storage.handler;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.CommonsUtils;
import com.heima.modules.po.AttachmentPO;
import com.heima.storage.mapper.AttachmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class AttachmentHandler {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentHandler.class);

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private FastFileStorageClient storageClient;

    public ResponseVO<AttachmentPO> uploadFile(MultipartFile file) throws Exception {
        //校验文件是否为空
        if (file.isEmpty()) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "文件不存在");
        }
        //构建附件对象
        AttachmentPO attachmentPO = getAttachmentPO(file);
        //根据文件签名查询文件是否存在
        AttachmentPO tmp = attachmentMapper.selectByMd5(attachmentPO.getMd5());
        //如果存在直接将附件对象返回
        if (null != tmp) {
            return ResponseVO.success(tmp);
        }
        //如果不存在则上传文件以及添加数据到附件表
        String url = dfsUploadFile(file);
        attachmentPO.setUrl(url);
        attachmentMapper.insert(attachmentPO);
        return ResponseVO.success(attachmentPO);
    }


    private AttachmentPO getAttachmentPO(MultipartFile file) throws IOException {
        AttachmentPO attachmentPO = new AttachmentPO();
        attachmentPO.setName(file.getOriginalFilename());
        attachmentPO.setLenght(file.getSize());
        attachmentPO.setExt(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        attachmentPO.setMd5(CommonsUtils.fileSignature(file.getBytes()));
        return attachmentPO;
    }

    /**
     * 上传文件
     */
    private String dfsUploadFile(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                multipartFile.getInputStream(),
                multipartFile.getSize(), originalFilename, null);
        return storePath.getFullPath();
    }

    /**
     * 删除文件
     */
    private void dfsDeleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            logger.info("fileUrl == >>文件路径为空...");
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

}
