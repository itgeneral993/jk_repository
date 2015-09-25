package com.rpframework.website.luoluo.act.api;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.parser.ParserException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rpframework.core.BaseAct;
import com.rpframework.utils.Pager;
import com.rpframework.website.luoluo.domain.Activitypicture;
import com.rpframework.website.luoluo.domain.User;
import com.rpframework.website.luoluo.exception.APICodeException;
import com.rpframework.website.luoluo.service.ActivitypictureSercice;


@Controller
@RequestMapping("api/picture")
public class ApiActivitypictureAct extends BaseAct{
		Gson gson = new Gson();
	@Resource ActivitypictureSercice activitypictureSercice;
	
	/**
	 * 添加报名信息
	 * @param name
	 * @param phone
	 * @param emergencyphone
	 * @param emergencyname
	 * @param oldboy
	 * @param chindenboy
	 * @param monely
	 * @param mood
	 * @param insure
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("add")
	public  @ResponseBody JsonElement add(
			@RequestParam(required=false) Integer sponsorld,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String phone,
			@RequestParam(required=false) String emergencyphone,
			@RequestParam(required=false) String emergencyname,
			@RequestParam(required=false) String oldboy,
			@RequestParam(required=false) String chindenboy,
			@RequestParam(required=false) String monely,
			@RequestParam(required=false) String mood,
			@RequestParam(required=false) String[] insure,
			@RequestParam(required=false) Integer type,
			HttpSession session
			)throws Exception{
			User currUser = getSessionUser(session);
			if(currUser == null){
				throw new APICodeException(-4, "你还没登陆!");
			}	
				Activitypicture Activitypi=new Activitypicture();
				Activitypi.setSponsorld(sponsorld);
				Activitypi.setMyld(currUser.getId());
				Activitypi.setName(name);
				Activitypi.setPhone(phone);
				Activitypi.setEmergencyname(emergencyname);
				Activitypi.setEmergencyphone(emergencyphone);
				Activitypi.setOldboy(oldboy);
				Activitypi.setChindenboy(chindenboy);
				Activitypi.setMonely(monely);
				Activitypi.setMood(mood);
				Activitypi.setInsure(insure);
				Activitypi.setType(type);
		boolean activi=activitypictureSercice.insertdo(Activitypi);
		JsonObject json=new JsonObject();
		if(activi==true){
			json.addProperty("succ", true);
		} else { // 添加失败
			json.addProperty("error", false);
		}
		return json;
	}
	@RequestMapping("list")
	public @ResponseBody JsonElement list(@RequestParam Integer id,@RequestParam(value="pager",required=false) Pager<Activitypicture> pager 
			) throws ParserException, InterruptedException{
 		if(pager==null){
 			pager=new Pager<Activitypicture>();
 		}
 		pager.getSearchMap().put("sponserid", String.valueOf(id));
 		activitypictureSercice.getpager(pager);
		JsonObject json = new JsonObject();
		json.addProperty("totalPages", pager.getTotalPages());
		json.addProperty("currentPage", pager.getCurrentPage());
		json.addProperty("totalCount", pager.getTotalCount());
		List<Activitypicture> list = pager.getItemList();
		JsonArray array = new JsonArray();
		json.add("arrays", array);
		for (Activitypicture act : list) {
			JsonObject jsonObj = gson.toJsonTree(act).getAsJsonObject();
			array.add(jsonObj);
		}
		System.out.println("user_list: "+json.toString());
		return json;
	}
	/**
	 * 查询详细信息 报名的 
	 * @param id
	 * @param pager
	 * @return
	 * @throws ParserException
	 * @throws InterruptedException
	 */
	@RequestMapping("listone")
	public @ResponseBody JsonElement cassifcationlist(@RequestParam Integer id
			,HttpSession session) throws ParserException, InterruptedException{
	
		JsonObject json = new JsonObject();
		User currUser = getSessionUser(session);
		if(currUser == null){
			throw new APICodeException(-4, "你还没登陆!");
		}
		Activitypicture cc=activitypictureSercice.selectone(id);
		json.addProperty("id", cc.getId());
		json.addProperty("username", cc.getName());
		json.addProperty("emergencyname", cc.getEmergencyname());
		json.addProperty("phone", cc.getPhone());
		json.addProperty("emergencyname", cc.getEmergencyname());
		json.addProperty("oldboy", cc.getOldboy());
		json.addProperty("chindenboy", cc.getChindenboy());
		json.addProperty("monely", cc.getMonely());
		return json;
	}
}
