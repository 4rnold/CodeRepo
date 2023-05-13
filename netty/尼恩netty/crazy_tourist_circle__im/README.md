## 学习说明

本软件作为分布式Java开发的练习和实战入手项目，实际上，和 生产项目在架构上差不多。
只是在细节上处理的内容，生产项目更加丰富和复杂。

本项目的配套材料，请参考：
 
- 老版本[《Netty Zookeeper Redis 高并发实战》一书 ](https://www.cnblogs.com/crazymakercircle/p/9904544.html)
- 新版本[《Java高并发核心编程（卷1）》一书 ](https://www.cnblogs.com/crazymakercircle/p/9904544.html)
- 总目录：[**疯狂创客圈 高并发 总目录**](https://www.cnblogs.com/crazymakercircle/p/9904544.html) 

## NIO、Netty、Java高并发的发烧友圈子： [疯狂创客圈](https://www.cnblogs.com/crazymakercircle/p/9904544.html) 
 
 > [被誉为全网唯一不吹牛逼，只交流技术的Java工程师圈子。](https://www.cnblogs.com/crazymakercircle/p/9904544.html) 
---
## 有两点要特别说明：

- 第一：此项目的架构，和大厂的分布式Java项目的架构基本类同，故，可以作为入职 BAT 大厂的理想的练习项目。

- 第二：此项目的架构，和很多的大数据开源项目，在架构上也基本类同，也可以作为大数据工程师的基础练习项目。

后续，会在架构上做一些和大厂项目的架构比对，方便大家更好的了解行业知识。
---
## 使用说明

请参考 [《Netty Zookeeper Redis 高并发实战》一书 ](https://www.cnblogs.com/crazymakercircle/p/11397271.html)


---

## 特别说明

- 此项目，只是一个学习系统，主要是让大家了解分布式思维来的，很多分布式系统都这样的模式!

- 此项目，不关注工业细节：如，server1断了后，client1重启，连server2，那时候client2发给client1的消息就收不到了，要自己搞重连什么的

- 从学习的维度来说，此项目，现在的版本，已经很复杂了， 很多的小伙伴看起来有点难度，要求制作配置视频呢！！！ 目前配套的学习视频，正在制作中

- **只要深入的学习，凭借这个项目，进个大厂基本没有问题，不少小伙伴，已经做到拉！**

![如图](https://img-blog.csdnimg.cn/20210328114839764.png)

---


## 参照CrazyIM开发的生产项目：橙交（带语音聊天、视频聊天功能）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210415185144565.png)

---
## 下一轮迭代计划：

##  1 对客户端的消息，进行确认
   具体实现的步骤，请参考博文 《[IM的消息，如何保障可靠性](https://www.cnblogs.com/crazymakercircle/p/14663165.html)》
##  2 登录和 IM的路由解耦
    > 客户端断开之后，重新进行IM路由
    
    
## 参与者链接
> 通过如下的链接，可以加入到开发团队， 通过master创建自己的分支开发，完成之后，发起 merge 请求 到master 
> https://gitee.com/crazymaker/crazy_tourist_circle__im/invite_link?invite=975433315f0804511572656bbb8767a1da33793c9be7abc6e86b39cd2aa13a5cbfbb3fa1d447fa1363ce7e16acfba504   