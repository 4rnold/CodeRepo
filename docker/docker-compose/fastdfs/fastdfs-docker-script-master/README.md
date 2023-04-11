# 注意：

- **network_mode 必须是host**
- **TRACKER_SERVER不能是localhost、127.0.0.1。必须使用192.xxxx。**



# docker fastdfs网络问题

需要使用network：host

- [使用Docker搭建测试环境问题 · tobato/FastDFS_Client Wiki](https://github.com/tobato/FastDFS_Client/wiki/%E4%BD%BF%E7%94%A8Docker%E6%90%AD%E5%BB%BA%E6%B5%8B%E8%AF%95%E7%8E%AF%E5%A2%83%E9%97%AE%E9%A2%98)

- [使用docker-compose安装FastDfs文件服务器 - 她的开呀 - 博客园](https://www.cnblogs.com/yloved/p/12649569.html)

# fastdfs-docker-script

- [gudqs7/fastdfs-docker-script: 使用docker + docker-compose 一键弄个 fastdfs 吧](https://github.com/gudqs7/fastdfs-docker-script)

使用docker + docker-compose 一键弄个 fastdfs 吧

```
vi nginx.conf #没啥可编辑的
vi storage.conf #也没啥可改的
vi tracker.conf #不存在这个文件, 想改自己修改一下 yml 文件

#起飞
docker-compose up -d

docker exec -it storage /bin/bash
# 进入 storage 容器中使用命令上传一个图片, 可使用容器自带的测试图片, 也可 docker cp 将本机图片复制进来
cd /fdfs_conf
fdfs_upload_file storage.conf anti-steal.jpg #上传一个图片吧, 返回路径如 /group1/M00/00/00/xxxx  
# 访问 localhost:8088/group1/M00/00/00/xxxx 吧

```
