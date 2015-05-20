package com.rpframework.website.edongwang.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rpframework.core.BaseService;
import com.rpframework.core.utils.DictionarySettingUtils;
import com.rpframework.core.utils.GsonUtils;
import com.rpframework.module.user.service.UserScoreService;
import com.rpframework.utils.NumberUtils;
import com.rpframework.utils.Pager;
import com.rpframework.website.edongwang.dao.IHouseRecommendDao;
import com.rpframework.website.edongwang.domain.HouseRecommend;
import com.rpframework.website.edongwang.domain.HouseRecommendProgress;
import com.rpframework.website.edongwang.domain.User;
import com.rpframework.website.edongwang.exception.APICodeException;
import com.rpframework.website.edongwang.utils.EConstants;

@Service
public class HouseRecommendService extends BaseService {
	@Resource UserService userService;
	@Resource public IHouseRecommendDao recommendDao;
	@Resource HouseRecommendProgressService houseRecommendProgressService;
	@Resource UserScoreService userScoreService;
	
	public Pager<HouseRecommend> getPager(Pager<HouseRecommend> pager) {
		long startTime = System.currentTimeMillis();
		List<HouseRecommend> itemList = recommendDao.doPager(this.packageMyBatisParam(pager));
		pager.setItemList(itemList);
		
		pager.setCostTime(System.currentTimeMillis() - startTime);
		return pager;
	}

	public Pager<HouseRecommend> getOverPager(Pager<HouseRecommend> pager) {
		long startTime = System.currentTimeMillis();
		List<HouseRecommend> itemList = recommendDao.doOverPager(this.packageMyBatisParam(pager));
		pager.setItemList(itemList);
		pager.setCostTime(System.currentTimeMillis() - startTime);
		return pager;
	}
	
	/**
	 * 抢单
	 * @param userId
	 * @param houseRecommendId
	 * @return
	 */
	public boolean grab(Integer userId, Integer houseRecommendId) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		User user = userService.select(userId);
		if(houseRecommend == null || user == null) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_OPEN) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(user.getIsSalesman() != 1) {
			throw new APICodeException(-3, "只有业务员才能抢单.");
		}
		
		
		JsonObject scoreExtJson = new JsonObject();
		Integer grabRecommendScore = NumberUtils.parseInt(DictionarySettingUtils.getParameterValue(EConstants.ScoreChannel.USERSCORE_VALUECFG_GRABRECOMMEND), 0);
		scoreExtJson.addProperty("houseRecommendId", houseRecommend.getId());
		scoreExtJson.addProperty("houseId", houseRecommend.getHouseId());
		scoreExtJson.addProperty("houseName", houseRecommend.getHouse().getName());
		//抢单积分给 业务员的
		userScoreService.addScore(houseRecommend.getAcceptSalesmanId(), grabRecommendScore, EConstants.ScoreChannel.GRAB, scoreExtJson.toString());
		
		houseRecommend.setState(EConstants.Recommend.STATE_DEALING);
		houseRecommend.setAcceptSalesmanId(userId);
		houseRecommend.setAcceptTime(System.currentTimeMillis() / 1000);
		return update(houseRecommend);
	}

	/**
	 * 
	 * 回访记录
	 * @param userId
	 * @param houseRecommendId
	 * @param state 回访 状态 1-有效 0-无效
	 * @param infoStar 描述星级
	 * @param intentStar 意向星级
	 * @return
	 */
	public boolean valid(Integer userId, Integer houseRecommendId, Integer state, Integer intentStar, String remark) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		User user = userService.select(userId);
		if(houseRecommend == null || user == null) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_DEALING) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(user.getIsSalesman() != 1) {
			throw new APICodeException(-3, "只有业务员才能回访.");
		}
		
		if(userId.equals(houseRecommend.getAcceptSalesmanId()) 
				|| (user.getUserSalesman().getIsLeader() == 1 && houseRecommend.getHouseId().equals(user.getUserSalesman().getHouse().getId()))) {
		} else {
			throw new APICodeException(-5, "你无权操作!");
		}
		
		if(!houseRecommend.checkNextProgressIsVaild(EConstants.Progress.P1)) {
			throw new APICodeException(-4, "进度流不正确.");
		}
		JsonObject json = new JsonObject();
		json.addProperty("intentStar", intentStar);
		json.addProperty("remark", remark);
		
		HouseRecommendProgress hrp = new HouseRecommendProgress();
		hrp.setHouseRecommendId(houseRecommendId);
		hrp.setRecordCreateTime(System.currentTimeMillis() /1000);
		hrp.setState(state);
		hrp.setType(EConstants.Progress.P1);
		hrp.setExt(json.toString());
		
		if(state == 0) {//在进度中间有一环节无效，则该推荐状态为完结状态
			houseRecommend.setState(EConstants.Recommend.STATE_OVER);
			update(houseRecommend);
		} else if(state == 1 && intentStar >= 2) {
			JsonObject scoreExtJson = new JsonObject();
			scoreExtJson.addProperty("houseRecommendId", houseRecommend.getId());
			scoreExtJson.addProperty("houseId", houseRecommend.getHouseId());
			scoreExtJson.addProperty("houseName", houseRecommend.getHouse().getName());
			
			Integer recommendScore = NumberUtils.parseInt(DictionarySettingUtils.getParameterValue(EConstants.ScoreChannel.USERSCORE_VALUECFG_RECOMMEND), 0);
			userScoreService.addScore(houseRecommend.getRecommendUserId(), recommendScore, EConstants.ScoreChannel.RECOMMEND_STAR, scoreExtJson.toString());
		}
		
		return houseRecommendProgressService.insert(hrp);
	}

	public boolean visit(Integer userId, Integer houseRecommendId, String remark) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		User user = userService.select(userId);
		if(houseRecommend == null || user == null) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_DEALING) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(user.getIsSalesman() != 1) {
			throw new APICodeException(-3, "只有业务员才能回访.");
		}
		
		if(userId.equals(houseRecommend.getAcceptSalesmanId()) 
				|| (user.getUserSalesman().getIsLeader() == 1 && houseRecommend.getHouseId().equals(user.getUserSalesman().getHouse().getId()))) {
		} else {
			throw new APICodeException(-5, "你无权操作!");
		}
		
		if(!houseRecommend.checkNextProgressIsVaild(EConstants.Progress.P2)) {
			throw new APICodeException(-4, "进度流不正确.");
		}
		JsonObject json = new JsonObject();
		json.addProperty("remark", remark);
		
		HouseRecommendProgress hrp = new HouseRecommendProgress();
		hrp.setHouseRecommendId(houseRecommendId);
		hrp.setRecordCreateTime(System.currentTimeMillis() /1000);
		hrp.setState(1);
		hrp.setType(EConstants.Progress.P2);
		hrp.setExt(json.toString());
		
		/*if(state == 0) {//在进度中间有一环节无效，则该推荐状态为完结状态
			houseRecommend.setState(EConstants.Recommend.STATE_OVER);
			update(houseRecommend);
		}*/
		
		return houseRecommendProgressService.insert(hrp);
	}
	
	/**
	 * 
	 * 楼盘的负责人确认成交
	 * @param userId
	 * @param houseRecommendId
	 * @param state
	 * @param dealTime
	 * @param surface
	 * @return
	 */
	/*public boolean deal_stage2(Integer userId, Integer houseRecommendId, Double recommendPrice,
			Double commissionPrice) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		User user = userService.select(userId);
		if(houseRecommend == null || user == null) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_DEALING) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(user.getIsSalesman() != 1) {
			throw new APICodeException(-3, "只有业务员才能成交.");
		}
		
		if(user.getUserSalesman().getIsLeader() != 1 || 
				!houseRecommend.getHouseId().equals(user.getUserSalesman().getHouse().getId())) {
			throw new APICodeException(-4, "你不是该楼盘的负责人.");
		}
		
		HouseRecommendProgress progress = houseRecommendProgressService.getProgressByRecommendIdAndType(houseRecommendId,EConstants.Progress.P3);
		Assert.notNull(progress);
		
		if(progress.getState() != 2) {//处理中
			throw new APICodeException(-4, "确认成交时错误的状态.");
		}
		
		JsonObject json = new JsonParser().parse(progress.getExt()).getAsJsonObject();
		json.addProperty("recommendPrice", recommendPrice);
		json.addProperty("commissionPrice", commissionPrice);
		
		HouseRecommendProgress hrp = progress;
		hrp.setState(1);
		hrp.setExt(json.toString());
		
		return houseRecommendProgressService.update(hrp);
	}*/
	
	public boolean deal(Integer userId, Integer houseRecommendId, Long dealTime, Double surface,Double price) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		User user = userService.select(userId);
		if(houseRecommend == null || user == null || !userId.equals(houseRecommend.getAcceptSalesmanId())) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_DEALING) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(user.getIsSalesman() != 1) {
			throw new APICodeException(-3, "只有业务员才能成交.");
		}
		
		if(userId.equals(houseRecommend.getAcceptSalesmanId()) 
				|| (user.getUserSalesman().getIsLeader() == 1 && houseRecommend.getHouseId().equals(user.getUserSalesman().getHouse().getId()))) {
		} else {
			throw new APICodeException(-5, "你无权操作!");
		}
		
		if(!houseRecommend.checkNextProgressIsVaild(EConstants.Progress.P3)) {
			throw new APICodeException(-4, "进度流不正确.");
		}
		JsonObject json = new JsonObject();
		json.addProperty("dealTime", dealTime);
		json.addProperty("surface", surface);
		json.addProperty("price", price);
		
		HouseRecommendProgress hrp = new HouseRecommendProgress();
		hrp.setHouseRecommendId(houseRecommendId);
		hrp.setRecordCreateTime(System.currentTimeMillis() /1000);
		hrp.setState(1);
		hrp.setType(EConstants.Progress.P3);
		hrp.setExt(json.toString());
		
		/*if(state == 0) {//在进度中间有一环节无效，则该推荐状态为完结状态
			houseRecommend.setState(EConstants.Recommend.STATE_OVER);
			update(houseRecommend);
		}*/
		
		return houseRecommendProgressService.insert(hrp);
	}
	
	public boolean over(Integer houseRecommendId) {
		HouseRecommend houseRecommend = select(houseRecommendId);
		if(houseRecommend == null) {
			throw new APICodeException(-1, "无效参数.");
		}
		
		if(houseRecommend.getState() != EConstants.Recommend.STATE_DEALING) {
			throw new APICodeException(-2, "无效的状态.");
		}
		
		if(!houseRecommend.checkNextProgressIsVaild(EConstants.Progress.P_OVER)) {
			throw new APICodeException(-4, "进度流不正确.");
		}
		JsonObject json = new JsonObject();
		
		HouseRecommendProgress hrp = new HouseRecommendProgress();
		hrp.setHouseRecommendId(houseRecommendId);
		hrp.setRecordCreateTime(System.currentTimeMillis() /1000);
		hrp.setState(1);
		hrp.setType(EConstants.Progress.P_OVER);
		hrp.setExt(json.toString());
		
		houseRecommend.setState(EConstants.Recommend.STATE_OVER);
		boolean flag = update(houseRecommend);
		Assert.isTrue(flag, "结佣时更新失败.");
		if(flag) {
			flag = houseRecommendProgressService.insert(hrp);
			
			//成交积分 和推荐积分 是后台 结佣后 才增加 增加积分
			Integer dealScoreRecommend = NumberUtils.parseInt(DictionarySettingUtils.getParameterValue(EConstants.ScoreChannel.USERSCORE_VALUECFG_HOUSEDEAL_RECOMMEND), 0);
			Integer dealScoreJD = NumberUtils.parseInt(DictionarySettingUtils.getParameterValue(EConstants.ScoreChannel.USERSCORE_VALUECFG_HOUSEDEAL_JD), 0);
			
			JsonObject scoreExtJson = new JsonObject();
			scoreExtJson.addProperty("houseRecommendId", houseRecommend.getId());
			scoreExtJson.addProperty("houseId", houseRecommend.getHouseId());
			scoreExtJson.addProperty("houseName", houseRecommend.getHouse().getName());
			
			//成交积分给 推荐者的
			userScoreService.addScore(houseRecommend.getRecommendUserId(), dealScoreRecommend, EConstants.ScoreChannel.DEAL_RECOMMEND, scoreExtJson.toString());
			userScoreService.addScore(houseRecommend.getAcceptSalesmanId(), dealScoreJD, EConstants.ScoreChannel.DEAL_JD, scoreExtJson.toString());
			HouseRecommendProgress progress = houseRecommend.getProgress(EConstants.Progress.P1);
			Assert.notNull(progress, "完结状态下找不到进度type ：" + EConstants.Progress.P1 + ":houseRecommendId:" + houseRecommendId);
			JsonObject jsonObject = new JsonParser().parse(progress.getExt()).getAsJsonObject();
			int infoStar = GsonUtils.getInt(jsonObject, "infoStar");
			if(infoStar >= 2) {
				//推荐积分也是给 推荐者的
				//基础上 + 星级 * 10
				
				Integer pjjf = NumberUtils.parseInt(DictionarySettingUtils.getParameterValue(EConstants.ScoreChannel.USERSCORE_VALUECFG_PJJF), 0);
				userScoreService.addScore(houseRecommend.getRecommendUserId(), pjjf, EConstants.ScoreChannel.COMMENT, scoreExtJson.toString());
			}
				
		}
		return flag;
	}
}