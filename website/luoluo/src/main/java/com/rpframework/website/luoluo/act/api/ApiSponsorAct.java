package com.rpframework.website.luoluo.act.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.parser.ParserException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rpframework.core.BaseAct;
import com.rpframework.core.api.FileService;
import com.rpframework.website.luoluo.domain.Sponsorlis;
import com.rpframework.website.luoluo.domain.User;
import com.rpframework.website.luoluo.exception.APICodeException;
import com.rpframework.website.luoluo.service.SponsorService;


@Controller
@RequestMapping("api/spons")
public class ApiSponsorAct extends BaseAct{
	@Resource SponsorService sponsorService;
	@Resource FileService fileService;
	/**
	 * 通过id查询主办方的信息
	 * @param sponsorid
	 * @param session
	 * @return
	 * @throws ParserException
	 * @throws InterruptedException
	 */
	@RequestMapping("listone")
	public @ResponseBody JsonElement listone(
		
			HttpSession session) throws ParserException, InterruptedException{
		
		User currUser = getSessionUser(session);	
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}
		Sponsorlis sponsor=sponsorService.seletOnesponsor(currUser.getId());
		if(sponsor==null){
			JsonObject json = new JsonObject();
			json.addProperty("msg", "主办方不存在");
			json.addProperty("succ", true);
			return json;
		}
		JsonObject json=new JsonObject();
		json.addProperty("id", sponsor.getId());
		json.addProperty("name", sponsor.getName());
		json.addProperty("Userphone", sponsor.getUserphone());
		json.addProperty("usernowlive", sponsor.getUsernowlive());
		json.addProperty("telephone", sponsor.getTelephone());
		json.addProperty("userpicture", sponsor.getUserpicture());
		
		json.addProperty("companyname", sponsor.getCompanyname());
		json.addProperty("Username", sponsor.getUsername());
		json.addProperty("responname", sponsor.getResponname());
		json.addProperty("usertelephone", sponsor.getUsertelephone());
		json.addProperty("responsibility", sponsor.getResponsibility());
		json.addProperty("Userinformation", sponsor.getUserinformation());
		json.addProperty("entintroduction", sponsor.getEntintroduction());
		json.addProperty("type", sponsor.getType());
		json.addProperty("typeopp", sponsor.getTypeopp());
		
 		return json;
	}
	


	/**
	 * 通过id查询主办方的信息
	 * @param sponsorid
	 * @param session
	 * @return
	 * @throws ParserException
	 * @throws InterruptedException
	 *//*
	@RequestMapping("listspan")
	public @ResponseBody JsonElement listspan(
			@RequestParam(required= false)Integer id,
			HttpSession session) throws ParserException, InterruptedException{
		Sponsorlis sponsor=sponsorService.seletOne(id);
		User currUser = getSessionUser(session);	
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}
		JsonObject json=new JsonObject();
		
		json.addProperty("id", sponsor.getId());
		json.addProperty("Userphone", sponsor.getUserphone());
		json.addProperty("Username", sponsor.getUsername());
		json.addProperty("responname", sponsor.getResponname());
		json.addProperty("usertelephone", sponsor.getUsertelephone());
		json.addProperty("responsibility", TagUtils.getFileFullPath(sponsor.getResponsibility()));
		json.addProperty("Userinformation", TagUtils.getFileFullPath(sponsor.getUserinformation()));
		json.addProperty("entintroduction", sponsor.getEntintroduction());
		return json;
	}
	*/
	
	
	
	//通过主办方id 查询
	@RequestMapping("/listonetoone")
	public @ResponseBody JsonElement listonetoone(
			@RequestParam(value="sponsorid",required= false) Integer sponsorid
			//,HttpSession session
			) throws ParserException, InterruptedException{
		Sponsorlis sponsor=sponsorService.seletOne(sponsorid);

		JsonObject json=new JsonObject();
		json.addProperty("id", sponsor.getId());
		json.addProperty("name", StringUtils.isBlank(sponsor.getName()) ? "" : sponsor.getName());
		json.addProperty("Userphone", sponsor.getUserphone());
		json.addProperty("usernowlive", sponsor.getUsernowlive());
		json.addProperty("telephone", sponsor.getTelephone());
		json.addProperty("userpicture", sponsor.getUserpicture());
		
		json.addProperty("companyname", sponsor.getCompanyname());
		json.addProperty("Username", sponsor.getUsername());
		json.addProperty("responname", sponsor.getResponname());
		json.addProperty("usertelephone", sponsor.getUsertelephone());
		json.addProperty("responsibility", sponsor.getResponsibility());
		json.addProperty("Userinformation", sponsor.getUserinformation());
		json.addProperty("entintroduction", sponsor.getEntintroduction());
		json.addProperty("type", sponsor.getType());
		
		return json;
	}
	//添加公司
	
	@RequestMapping("/add")
	public @ResponseBody JsonElement add(
			@RequestParam(required= false)String name,
			@RequestParam(required= false)String username,
			@RequestParam(required= false)String usernowlive,
			@RequestParam(required= false)String userphone,
			@RequestParam(required= false)String usertelephone,
			@RequestParam(required= false)String telephone,
			@RequestParam(required= false)String companyname,
			@RequestParam(required= false)String responname,
			@RequestParam(required= false)String entintroduction,
			
			
			@RequestParam(required= false)String userpicture,
			@RequestParam(required= false)String userinformation,
			@RequestParam(required= false)String responsibility,
			
			/**@RequestParam(value="myFile", required=false) CommonsMultipartFile myFile,
			@RequestParam(value="iconFile[]", required=false) MultipartFile iconFile[],
			@RequestParam(value="apicture[]", required=false) MultipartFile apicture[],**/HttpSession session){
	
		JsonObject json = new JsonObject();
		User currUser = getSessionUser(session);
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}
		Sponsorlis ss=sponsorService.seletOnesponsor(currUser.getId());
		if(ss==null){
		Sponsorlis sponsor=new Sponsorlis();
		sponsor.setName(name);
		sponsor.setUserid(currUser.getId());
		sponsor.setUsername(username);
		sponsor.setUsernowlive(usernowlive);
		sponsor.setUserphone(userphone);
		sponsor.setUsertelephone(usertelephone);
		sponsor.setTelephone(telephone);
		sponsor.setCompanyname(companyname);
		sponsor.setResponname(responname);
		sponsor.setEntintroduction(entintroduction);
		sponsor.setType(2);
		sponsor.setTypeopp(1);
		sponsor.setUserpicture(userpicture);
		sponsor.setActivityTime(System.currentTimeMillis()/1000);
		sponsor.setResponsibility(responsibility);
		sponsor.setUserinformation(userinformation);
		
		
		//头像图片		
		/*if(myFile.getSize() > 0) {
			try {
				String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(myFile.getOriginalFilename());
				fileService.saveFile(myFile.getInputStream(), relativelyCardFrontPhoto); 
				sponsor.setUserpicture(relativelyCardFrontPhoto);
				sponsor.setActivityTime(System.currentTimeMillis()/1000);
			} catch (Exception e) {
				throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
			}
		} 
			//公司图片
			String corle=sponsorService.addPhotos(apicture);
			sponsor.setResponsibility("["+corle+"]");
			sponsor.setActivityTime(System.currentTimeMillis()/1000);
			//领队图片
			String iconFiletrl=sponsorService.addPhotos(iconFile);
			sponsor.setUserinformation("["+iconFiletrl+"]");*/
			sponsorService.insertsponsor(sponsor);
			json.addProperty("succ", "添加成功");
			return json;
		}else{
			ss.setName(name);
			ss.setUserid(currUser.getId());
			ss.setUsername(username);
			ss.setUsernowlive(usernowlive);
			ss.setUserphone(userphone);
			ss.setUsertelephone(usertelephone);
			ss.setTelephone(telephone);
			ss.setCompanyname(companyname);
			ss.setResponname(responname);
			ss.setEntintroduction(entintroduction);
			ss.setType(2);
			ss.setUserpicture(userpicture);
			ss.setActivityTime(System.currentTimeMillis()/1000);
			ss.setResponsibility(responsibility);
			ss.setUserinformation(userinformation);
			//头像图片		
		/*	if(myFile.getSize() > 0) {
				try {
					String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(myFile.getOriginalFilename());
					fileService.saveFile(myFile.getInputStream(), relativelyCardFrontPhoto); 
					ss.setUserpicture(relativelyCardFrontPhoto);
					ss.setActivityTime(System.currentTimeMillis()/1000);
				} catch (Exception e) {
					throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
				}
			} 
			//公司图片
			String corle=sponsorService.addPhotos(apicture);
			ss.setResponsibility("["+corle+"]");
			ss.setActivityTime(System.currentTimeMillis()/1000);
			
			//领队图片
			String iconFiletrl=sponsorService.addPhotos(iconFile);
			ss.setUserinformation("["+iconFiletrl+"]");*/
			ss.setTypeopp(1);
				sponsorService.updatedo(ss);
				json.addProperty("succ", "添加成功");
				return json;
		}
	}
	//个人
	@RequestMapping("/addone")
	public @ResponseBody JsonElement addonel(
			@RequestParam(required= false)String name,
			@RequestParam(required= false)String username,
			@RequestParam(required= false)String usernowlive,
			@RequestParam(required= false)String userphone,
			
			
			@RequestParam(required= false)String userpicture,
			@RequestParam(required= false)String userinformation,
			
			
			/*@RequestParam(value="myFile", required=false) CommonsMultipartFile myFile,
			@RequestParam(value="iconFile[]", required=false) MultipartFile iconFile[],*/
			@RequestParam(required= false)String entintroduction,
		HttpSession session){
		
		JsonObject json = new JsonObject();
		User currUser = getSessionUser(session);
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}
		Sponsorlis ss=sponsorService.seletOnesponsor(currUser.getId());
		if(ss==null){
			Sponsorlis sponsor=new Sponsorlis();
			sponsor.setName(name);
			sponsor.setUserid(currUser.getId());
			sponsor.setUsername(username);
			sponsor.setUsernowlive(usernowlive);
			sponsor.setUserphone(userphone);
			sponsor.setEntintroduction(entintroduction);
			sponsor.setType(1);
			sponsor.setTypeopp(1);
			sponsor.setUserpicture(userpicture);
			sponsor.setUserinformation(userinformation);
	//头像图片		
			/*if(myFile.getSize() > 0) {
				try {
					String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(myFile.getOriginalFilename());
					fileService.saveFile(myFile.getInputStream(), relativelyCardFrontPhoto); 
					sponsor.setUserpicture(relativelyCardFrontPhoto);
					sponsor.setActivityTime(System.currentTimeMillis()/1000);
				} catch (Exception e) {
					throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
				}
			} 
	
	//个人信息图片
			//公司图片
			String corle=sponsorService.addPhotos(iconFile);
			sponsor.setUserinformation("["+corle+"]");*/
			sponsor.setActivityTime(System.currentTimeMillis()/1000);
			
			sponsorService.insertsponsor(sponsor);
			json.addProperty("succ", "添加成功");
			return json;
		}else{
			//修改用户
			ss.setName(name);
			ss.setUserid(currUser.getId());
			ss.setUsername(username);
			ss.setUsernowlive(usernowlive);
			ss.setUserphone(userphone);
			ss.setEntintroduction(entintroduction);
			ss.setType(1);
			ss.setUserpicture(userpicture);
			ss.setUserinformation(userinformation);
	//头像图片		
			/*if(myFile.getSize() > 0) {
				try {
					String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(myFile.getOriginalFilename());
					fileService.saveFile(myFile.getInputStream(), relativelyCardFrontPhoto); 
					ss.setUserpicture(relativelyCardFrontPhoto);
					ss.setActivityTime(System.currentTimeMillis()/1000);
				} catch (Exception e) {
					throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
				}
			} 
	
	//个人信息图片
			String corle=sponsorService.addPhotos(iconFile);
			ss.setUserinformation("["+corle+"]");*/
			ss.setActivityTime(System.currentTimeMillis()/1000);	
			ss.setTypeopp(1);
			sponsorService.updatedo(ss);
			json.addProperty("succ", "修改成功");
			return json;
			
		}
	}
	
	
	
	
}
