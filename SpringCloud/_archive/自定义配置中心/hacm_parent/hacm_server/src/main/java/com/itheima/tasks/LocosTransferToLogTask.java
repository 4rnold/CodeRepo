package com.itheima.tasks;

import com.itheima.dao.ConfigLocosDao;
import com.itheima.domain.ConfigLocos;
import com.itheima.utils.KryoSerializeUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 定时执行轨迹转移的任务类
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
@EnableScheduling
public class LocosTransferToLogTask {

    @Autowired
    private ConfigLocosDao configLocosDao;

    private static final String SYSTEM_PATH = LocosTransferToLogTask.class.getResource("/").getPath();

    /**
     * 每天凌晨3点执行处理
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void transferToLog(){
        //获取当前系统时间的前一年的毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-1);
        Long time = calendar.getTimeInMillis();
        //根据前一年的毫秒值查询轨迹列表
        List<ConfigLocos> configLocosList = configLocosDao.findByCreateTimeLessThan(time);
        //遍历
        for(ConfigLocos configLocos : configLocosList){
            try{
                //使用项目名称+当前日期的作为文件名
                String prefix = configLocos.getProjectName();
                String suffix = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                String fileName = prefix+"_"+suffix+".log";
                byte[] bytes = KryoSerializeUtil.serialize(configLocos);
                //写文件
                FileUtils.writeByteArrayToFile(new File(SYSTEM_PATH+File.separator+"logs",fileName),bytes);
            }catch (Exception e){
                e.printStackTrace();
            }
            //删除已经写成文件的轨迹
            configLocosDao.deleteById(configLocos.getId());
        }
    }
}
