package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true, StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true, StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true, StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true, StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true, StatusCode.OK,"修改成功");
	}

	/**
	 * 删除操作
	 *   需要管理员权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id, HttpServletRequest request){
		//1.从请求域中获取管理员权限的claims
		Claims claims = (Claims)request.getAttribute("admin_claims");
		//2.判断是否有管理员权限
		if(claims == null){
			return new Result(false, StatusCode.ACCESSERROR,"没有权限");
		}
		//3.删除用户
		userService.deleteById(id);
		return new Result(true, StatusCode.OK,"删除成功");
	}

	/**
	 * 删除
	 *   要求管理员权限才能删除
	 * @param id
     * 此方法不能使用，因为会产生大量的重复代码，应该采用aop思想。就是使用springmvc提供的拦截器
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id,@RequestHeader(value = "Authorization",required = false) String header){
		//1.判断header是否存在
		if(StringUtils.isEmpty(header)){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问，没有消息头");
		}
		//2.判断消息头的格式是否正确
		if(!header.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问，消息头格式不对");
		}
		//3.取出token
		String token = "";
		try{
			token = header.split(" ")[1];
		}catch (Exception e){
			throw new RuntimeException("无权访问，没有得到token");
		}
		//4.验证token
		Claims claims = jwtUtil.parseJWT(token);
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问，token内容不对");
		}
		//5.取出claims中的roles信息
		String roles = (String)claims.get("roles");
		if(!"admin_role".equals(roles)){
			return new Result(false,StatusCode.ACCESSERROR,"无权访问，不是管理员权限");
		}
		//6.删除用户
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}*/

	/**
	 * 发送验证码（不是真正的发送，而是写入到消息队列）
	 * @param mobile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="/sendsms/{mobile}")
	public Result sendsms(@PathVariable("mobile") String mobile){
		//1.调用业务层实现功能
		userService.generateCode(mobile);
		//2.创建返回值并返回
		return new Result(true, StatusCode.OK,"发送成功");
	}

	/**
	 * 用户注册
	 * @param user 注册信息
	 * @param code 验证码
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/register/{code}")
	public Result register(@RequestBody User user , @PathVariable("code") String code){
		//1.调用业务层注册
		userService.register(user,code);
		//2.创建返回值并返回
		return new Result(true, StatusCode.OK,"注册成功");
	}

	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/login")
	public Result userLogin(@RequestBody User user){
		//1.调用业务层登录
		User sysUser = userService.userLogin(user);
		//2.给当前用户签发token
		String token = jwtUtil.createJWT(sysUser.getId(),sysUser.getNickname(),"user_role");
		//3.创建返回值信息
		Map<String,Object> map = new HashMap<>();
		map.put("name", sysUser.getNickname());
		map.put("token",token);
		map.put("avatar",sysUser.getAvatar());
		//4.创建返回值并返回
		return new Result(true, StatusCode.OK,"登录成功",map);
	}

	/**
	 * 更新用户的粉丝数
	 * @param userid
	 * @param num
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/incfans/{userid}/{num}")
	public void incfans(@PathVariable("userid") String userid,@PathVariable("num") int num){
		userService.incFans(userid,num);
	}


	/**
	 * 更新用户的关注数
	 * @param userid
	 * @param num
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/incfollow/{userid}/{num}")
	public void incfollow(@PathVariable("userid") String userid,@PathVariable("num") int num){
		userService.incFollow(userid,num);
	}
}
