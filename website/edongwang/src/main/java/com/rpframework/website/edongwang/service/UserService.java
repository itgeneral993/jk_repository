package com.rpframework.website.edongwang.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.rpframework.core.BaseService;
import com.rpframework.utils.Pager;
import com.rpframework.website.edongwang.dao.IUserDao;
import com.rpframework.website.edongwang.domain.User;

@Service
public class UserService extends BaseService {
	
	@Resource public IUserDao userDao;
	
	public Pager<User> getPager(Pager<User> pager) {
		long startTime = System.currentTimeMillis();
		List<User> itemList = userDao.doPager(this.packageMyBatisParam(pager));
		pager.setItemList(itemList);
		
		pager.setCostTime(System.currentTimeMillis() - startTime);
		return pager;
	}

	public User findUserByContact(String contact) {
		return userDao.findUserByContact(contact);
	}

	public boolean doLoginRecord(User user, HttpServletRequest request) {
		user.setLastLoginIp(user.getLoginIp()) ;
		user.setLoginIp(request.getRemoteAddr());
		user.setLastLoginTime(user.getLoginTime());
		user.setLoginTime(System.currentTimeMillis()/1000);
		
		return update(user);
	}

}
