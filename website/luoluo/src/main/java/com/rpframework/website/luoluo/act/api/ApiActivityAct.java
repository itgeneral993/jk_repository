package com.rpframework.website.luoluo.act.api;


import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.alibaba.druid.sql.parser.ParserException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rpframework.core.BaseAct;
import com.rpframework.core.api.FileService;
import com.rpframework.core.utils.TagUtils;
import com.rpframework.utils.CollectionUtils;
import com.rpframework.utils.DateUtils;
import com.rpframework.utils.NumberUtils;
import com.rpframework.utils.Pager;
import com.rpframework.website.luoluo.domain.Activity;
import com.rpframework.website.luoluo.domain.Activitypicture;
import com.rpframework.website.luoluo.domain.Sponsorlis;
import com.rpframework.website.luoluo.domain.User;
import com.rpframework.website.luoluo.exception.APICodeException;
import com.rpframework.website.luoluo.service.ActivityService;
import com.rpframework.website.luoluo.service.ActivitypictureSercice;
import com.rpframework.website.luoluo.service.SponsorService;



@Controller
@RequestMapping("api/activi")
public class ApiActivityAct extends BaseAct{
	Gson gson = new Gson();
	@Resource ActivityService   activityService ;
	@Resource ActivitypictureSercice activitypictureSercice;
	@Resource SponsorService sponsorSercice;
	@Resource FileService fileService;
	/**
	 * api查询所有的活动
	 * @param pager
	 * @return
	 * @throws ParserException
	 * @throws InterruptedException
	 */
	@RequestMapping("/activi_list")
	public @ResponseBody JsonElement activilist(@RequestParam(value="pager",required=false) Pager<Activity> pager 
			) throws ParserException, InterruptedException{
 		if(pager==null){
 			pager=new Pager<Activity>();
 		}
		pager.getSearchMap().put("type", String.valueOf(1));
		activityService.getpager(pager);
		JsonObject json = new JsonObject();
		json.addProperty("totalPages", pager.getTotalPages());
		json.addProperty("currentPage", pager.getCurrentPage());
		json.addProperty("totalCount", pager.getTotalCount());
		List<Activity> list = pager.getItemList();
		JsonArray array = new JsonArray();
		json.add("arrays", array);
		for (Activity act : list) {
			JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
			array.add(jsonObj);
		}
		System.out.println("user_list: "+json.toString());
		return json;
	}
	/**
	 * 通过名字，编号查询
	 * @param pager
	 * @return
	 * @throws ParserException
	 * @throws InterruptedException
	 */
	@RequestMapping("/earch_list")
	public @ResponseBody JsonElement earchlist(@RequestParam String search 
			) throws ParserException, InterruptedException{
		 List<Activity> atcuname=activityService.selectname(search);
		 JsonArray array = new JsonArray();
		 JsonObject json=new JsonObject();
		 json.add("arrays", array);
		 if(atcuname.size()==0){
			 List<Activity> atcunumber=activityService.selectnumbers(search);
			 if(atcunumber.size()==0){
				 json.addProperty("error", false);
				 return json;
			 }else{
				 for (Activity act : atcunumber) {
						JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
						array.add(jsonObj);
					} 
				 System.out.println("user_list: "+json.toString());
				 return json;
			 }
		 }else{
			 for (Activity act : atcuname) {
					JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
					array.add(jsonObj);
				}
			 System.out.println("user_list: "+json.toString());
			 return json;
		 }
	}
	/**
	 * 查询详细
	 * @param activiid
	 * @return
	 */
	@RequestMapping("/activi_seleone")
	public @ResponseBody JsonElement activiseleone(@RequestParam(value="pager",required=false) Pager<Activity> pager,
			@RequestParam(required=false) Integer activiid){
		JsonObject json=new JsonObject();
		
		Activity activity = activityService.selectcal(activiid);
		json.addProperty("id", activity.getId());
		json.addProperty("sponsorid", activity.getSponsorid());
		json.addProperty("cover", TagUtils.getFileFullPath(activity.getCover()));
		json.addProperty("activitynumber", activity.getActivitynumber());
		json.addProperty("activitycategory", activity.getActivitycategory());
		json.addProperty("activityname", activity.getActivityname());
		json.addProperty("activitylocation", activity.getActivitylocation());
		
		json.addProperty("number", activity.getNumber());
		json.addProperty("children_expense", activity.getChildren_expense());
		json.addProperty("old_expense", activity.getOld_expense());
		json.addProperty("gril_expense", activity.getGril_expense());
		json.addProperty("activitycontent", activity.getActivitycontent());
		json.addProperty("starttime", activity.getStarttime());
		json.addProperty("outtime", activity.getOuttime());
		json.addProperty("nowforetime", activity.getNowforetime());
		
		json.addProperty("lat", activity.getLat());
		json.addProperty("lng", activity.getLng());
		
		json.addProperty("starttime", activity.getStarttime());
		
		List<Activitypicture> activitypict=activitypictureSercice.selectlist(activiid);
		JsonArray array = new JsonArray();
		json.add("arrays", array);
			List<String> imgList = activity.getPhotoPathList();
			JsonArray imgArray = new JsonArray();
			if(CollectionUtils.isNotEmpty(imgList)) {
				for (String s : imgList) {
					imgArray.add(new JsonPrimitive(TagUtils.getFileFullPath(s)));
				}
			}
			json.add("imgs", imgArray);
			if(pager==null){
	 			pager=new Pager<Activity>();
	 		}
			pager.getSearchMap().put("typeok", String.valueOf(2));
			pager.getSearchMap().put("activiid", String.valueOf(activiid));
			activityService.getpager(pager);
			List<Activity> list = pager.getItemList();
			json.addProperty("size", list.size());
		for (Activitypicture act : activitypict) {
			JsonObject jsonObj=new JsonObject();
			jsonObj.addProperty("oldboy", act.getOldboy());
			jsonObj.addProperty("grilexpense", act.getGrilexpense());
			jsonObj.addProperty("chindenboy", act.getChindenboy());
			jsonObj.addProperty("id", act.getId());
			jsonObj.addProperty("myid", act.getMyld());
			array.add(jsonObj);
		}
		return json;
	}
	/**
	 * 查询分类的
	 */
	@RequestMapping("/activi_activitycategory")
	public @ResponseBody JsonElement activiactivitycategory(
			@RequestParam(required=false) Integer activitycategoryid,
			@RequestParam(value="pager",required=false) Pager<Activity> pager 
			){
		if(pager==null){
 			pager=new Pager<Activity>();
 		}
		pager.getSearchMap().put("calid", String.valueOf(activitycategoryid));
		pager.getSearchMap().put("type", String.valueOf(1));
		activityService.getpager(pager);
		JsonObject json = new JsonObject();
		List<Activity> list = pager.getItemList();
		JsonArray array = new JsonArray();
		json.add("arrays", array);
		for (Activity act : list) {
			JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
			array.add(jsonObj);
		}
		System.out.println("user_list: "+json.toString());
		return json;
	}
	
	/**
	 * 查询我发布的活动
	 * @param pager
	 * @param activiid
	 * @return
	 */
	@RequestMapping("/activi_myseleone")
	public @ResponseBody JsonElement activimyseleone(@RequestParam(value="pager",required=false) Pager<Activity> pager,
			@RequestParam(required=false) Integer activiid){
		JsonObject json=new JsonObject();
		if(pager==null){
 			pager=new Pager<Activity>();
 		}
		pager.getSearchMap().put("activiid", String.valueOf(activiid));
		activityService.getpager(pager);
		
		List<Activity> list = pager.getItemList();
		JsonArray array = new JsonArray();
		json.add("arrays", array);
		for (Activity act : list) {
			JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
			array.add(jsonObj);
		}
		System.out.println("user_list: "+json.toString());
		return json;
	}
	/**
	 * 发布活动
	 * @param pager
	 * @param activiid
	 * @return
	 */
	@RequestMapping("/activi_upou")
	public @ResponseBody JsonElement activiupou(
			@RequestParam(required=false)Integer id,
			@RequestParam(value="iconFile[]", required=false) MultipartFile iconFile[],
			@RequestParam(required=false)Integer sponsorid,
			@RequestParam(required=false)String activityname,
			@RequestParam(required=false)Integer activitycategory,
			@RequestParam(required=false)String activitylocation,
			@RequestParam(required=false)Integer number,
			@RequestParam(required=false)Integer children_expense,
			@RequestParam(required=false)Integer old_expense,
			@RequestParam(required=false)Integer gril_expense,
			@RequestParam(value="apicture", required=false) CommonsMultipartFile apicture,
			@RequestParam(required=false)String activitycontent,
			@RequestParam(required=false)String starttime,
			@RequestParam(required=false)String outtime,
			@RequestParam(required=false)Integer type,
			@RequestParam(required=false)Integer typeok,
			@RequestParam(required=false)String lng,
			@RequestParam(required=false)String lat,HttpSession session){
		JsonObject json = new JsonObject();
		Sponsorlis bfgs=sponsorSercice.seletOne(sponsorid);
		if(bfgs==null){
			json.addProperty("error", "没有主办方");
			return json;
		}
		
		User currUser = getSessionUser(session);
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}	
		if(typeok==0){
			Activity activity=activityService.selectcal(id);
			activity.setActivityname(activityname);
			activity.setSponsorid(currUser.getId());
			activity.setActivitycategory(activitycategory);
			activity.setActivitylocation(activitylocation);
			activity.setNumber(number);
			activity.setGril_expense(gril_expense);
			activity.setChildren_expense(children_expense);
			activity.setOld_expense(old_expense);
			
			String corle=activityService.addPhotos(iconFile);
			activity.setActivitypicture("["+corle+"]");
			if(apicture.getSize() > 0) {
				try {
					String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(apicture.getOriginalFilename());
					fileService.saveFile(apicture.getInputStream(), relativelyCardFrontPhoto); 
					activity.setCover(relativelyCardFrontPhoto);
				} catch (Exception e) {
					throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
				}
			} 
			//缺活动图片
			activity.setActivitycontent(activitycontent);
			activity.setType(0);
			activity.setTypeok(1);
			activity.setLng(lng);
			activity.setLat(lat);
			boolean bFlag = activityService.updatedo(activity);
			if(bFlag == true){ // 修改成功
				json.addProperty("succ", true);
			} else { // 修改失败
				json.addProperty("error", false);
			} 
		}else{
				json.addProperty("addlseorr", "你已经修改过一次了，不能重复修改");
			}
				return json;
		
	}
	/**
	 * 发布活动
	 * @param pager
	 * @param activiid
	 * @return
	 */
	@RequestMapping("/activi_addsave")
	public @ResponseBody JsonElement activiaddsave(
			@RequestParam(value="iconFile[]", required=false) MultipartFile iconFile[],
			@RequestParam(required=false)Integer sponsorid,
			@RequestParam(required=false)String activityname,
			@RequestParam(required=false)Integer activitycategory,
			@RequestParam(required=false)String activitylocation,
			@RequestParam(required=false)Integer number,
			@RequestParam(required=false)Integer children_expense,
			@RequestParam(required=false)Integer old_expense,
			@RequestParam(required=false)Integer gril_expense,
			@RequestParam(value="apicture", required=false) CommonsMultipartFile apicture,
			@RequestParam(required=false)String activitycontent,
			@RequestParam(required=false)String starttime,
			@RequestParam(required=false)String outtime,
			@RequestParam(required=false)Integer type,
			
			@RequestParam(required=false)String lng,
			@RequestParam(required=false)String lat,HttpSession session){
		JsonObject json = new JsonObject();
		
				User currUser = getSessionUser(session);
				if(currUser == null){
					throw new APICodeException(-4, "你还没登陆!");
				}	
					Activity activity=new Activity();
					activity.setActivityname(activityname);
					activity.setSponsorid(currUser.getId());
					activity.setActivitycategory(activitycategory);
					activity.setActivitylocation(activitylocation);
					activity.setNumber(number);
					activity.setGril_expense(gril_expense);
					activity.setChildren_expense(children_expense);
					activity.setOld_expense(old_expense);
					
					String corle=activityService.addPhotos(iconFile);
					activity.setActivitypicture("["+corle+"]");
					if(apicture.getSize() > 0) {
						try {
							String relativelyCardFrontPhoto = "/luoluo/user/userPic/" + DateUtils.nowDate(DateUtils.YYYYMMDDHHMMSS) + NumberUtils.random() + "." + FilenameUtils.getExtension(apicture.getOriginalFilename());
							fileService.saveFile(apicture.getInputStream(), relativelyCardFrontPhoto); 
							activity.setCover(relativelyCardFrontPhoto);
						} catch (Exception e) {
							throw new IllegalArgumentException("文件上传失败，原因:" + e.getLocalizedMessage());
						}
					} 
					//缺活动图片
					activity.setActivitycontent(activitycontent);
					activity.setType(0);
					
					activity.setLng(lng);
					activity.setLat(lat);
					boolean bFlag = activityService.insertone(activity);
					if(bFlag == true){ // 修改成功
						json.addProperty("succ", true);
					} else { // 修改失败
						json.addProperty("error", false);
					} 
					return json;
	}
	/*
	 * 删除
	 */
	@RequestMapping("/activi_delete")
	public @ResponseBody JsonElement actividelete(
			@RequestParam(required=false) Integer id){
		boolean activity = activityService.deletesell(id);
		JsonObject json = new JsonObject();
		if(activity){ // 添加成功
			json.addProperty("succ", true);
		} else { // 添加失败
			json.addProperty("error", false);
		} 
		return json;
	}
	
	@RequestMapping(value="/upload",produces = "application/json; charset=utf-8")
	public @ResponseBody String dosave(@RequestParam(value="file", required=false) MultipartFile file ,//图片对象
			@RequestParam(value="folder",required=false) String folder,//文件夹名
			@RequestParam(value="callback",required=false) String callback,//跨域
			HttpServletRequest request){
		JsonObject json = new JsonObject();
		
		if(file.getSize()>0){
			folder = (folder==null || folder == "") ? "folder" : folder;//如果上传的文件夹名为空，则默认为folder
			String url =activityService.uploadImg(file,folder);//保存图片
			json.addProperty("succ", true);
			json.addProperty("url", url);//返回物理存储地址
		}else{
			json.addProperty("succ", false);
		}
		if(StringUtils.isBlank(callback)){
			return json.toString();
		}
		return (callback + "(" + json.toString() + ")");
	}
	/*
	 * 筛选
	 */
	@RequestMapping("/lete")
	public @ResponseBody JsonElement lete(@RequestParam(value="pager",required=false) Pager<Activity> pager,
			@RequestParam(required=false) Integer day, //天数
			@RequestParam(required=false) long idtime//今天 明天 后天
			){
		if(pager==null){
 			pager=new Pager<Activity>();
 		}
		pager.getSearchMap().put("type", String.valueOf(1));
		activityService.getpager(pager);
		JsonObject json = new JsonObject();
		json.addProperty("totalPages", pager.getTotalPages());
		json.addProperty("currentPage", pager.getCurrentPage());
		json.addProperty("totalCount", pager.getTotalCount());
		List<Activity> list = pager.getItemList();
		JsonArray array = new JsonArray();
		json.add("arrays", array);
		for (Activity act : list) {
			
		int ss=(int) Math.abs((act.getOuttime()-act.getStarttime())/(60*60*24));
		int ff=(int) Math.abs((act.getStarttime()-idtime)/(60*60*24));
		//筛选信息
			if(ff==0){
				if(ss==day){
					JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
					array.add(jsonObj);
					}
			}else if(ff==1){
				if(ss==day){
					JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
					array.add(jsonObj);
					}
			}else if(ff==2){
				if(ss==day){
					JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
					array.add(jsonObj);
					}
			}else{
				if(ss==day){
					JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
					array.add(jsonObj);
				}
			}
		}
		return json;
	}
}