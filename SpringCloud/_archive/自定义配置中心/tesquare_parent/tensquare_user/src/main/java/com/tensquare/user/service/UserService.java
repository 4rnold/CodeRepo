package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${user.queues.name}")
	private String queuesName;
	/**
	 * 生成验证码，把生成的验证码写入到消息队列中
	 * 同时存入redis中，用于注册时的比较。
	 * 由于要发短信，所以还需要当前注册用户的手机号。
	 * @param mobile  接收短信的手机号（也就是当前注册用户的手机号）
	 */
	public void generateCode(String mobile){
		//1.定义随机数对象
		Random random = new Random();
		//2.生成一个随机数，最大不能超过6位
		Integer code = random.nextInt(999999);
		//3.补全6位
		if(code < 100000){
			code = code + 100000;
		}
		//4.创建要写入队列的对象
		Map<String,String> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("code",code.toString());
		//5.把map写入到消息队列
//		rabbitTemplate.convertAndSend(queuesName,map);
		//6.把手机号和验证码写到redis中
//		redisTemplate.opsForValue().set(mobile,code.toString(),10, TimeUnit.MINUTES);//10分钟过期

		System.out.println("验证码是："+code);
	}

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 */
	public void register(User user, String code){
//		//1.判断用户是否输入了验证码
//		if(StringUtils.isEmpty(code)){
//			throw new NullPointerException("请输入验证码");
//		}
//		//2.从redis中获取用户的验证码信息
//		String sysCode = (String)redisTemplate.opsForValue().get(user.getMobile());
//		//3.比较验证码
//		if(!code.equals(sysCode)){
//			throw new IllegalArgumentException("验证码不匹配，请确认后再输入");
//		}
		//4.保存用户信息
		//给用户的id赋值
		user.setId(String.valueOf(idWorker.nextId()));
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期

		//给密码加密
		user.setPassword(encoder.encode(user.getPassword()));

		userDao.save(user);
		//5.清理redis中的验证码
//		redisTemplate.delete(user.getMobile());
	}

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public User userLogin(User user){
		//1.根据用户登录名称查询用户
		User sysUser = userDao.findByMobile(user.getMobile());
		//2.判断是否有此用户
		if(sysUser == null){
			//没有此用户
			throw new IllegalStateException("用户名或密码错误");
		}
		//3.校验密码是否匹配
		boolean checkPassword = encoder.matches(user.getPassword(),sysUser.getPassword());
		if(!checkPassword){
			//密码不匹配
			throw new IllegalStateException("用户名或密码错误");
		}
		//4.返回查询出来的用户
		return sysUser;
	}

	/**
	 * 更新关注数
	 * @param userid
	 * @param num
	 */
	@Transactional
	public void incFollow(String userid,int num){
		userDao.updateFollowcount(userid,num);
	}

	/**
	 * 更新粉丝数
	 * @param userid
	 * @param num
	 */
	@Transactional
	public void incFans(String userid,int num){
		userDao.updateFanscount(userid,num);
	}
}
