package com.rpframework.website.luoluo.act.admin;



import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpframework.core.exception.AdminIllegalArgumentException;
import com.rpframework.utils.Pager;

import com.rpframework.website.luoluo.domain.Reportactivity;
import com.rpframework.website.luoluo.service.ReportactivityService;

@Controller
@RequestMapping("admin/repot")
public class AdminReportactivityAct extends AdminAct{
	@Resource ReportactivityService reporService;
	@RequestMapping("list")

	public String list(@RequestParam(value="pager", required=false)Pager<Reportactivity> pager, Map<String, Pager<Reportactivity>> model){
		if(pager==null){
			pager=new Pager<Reportactivity>();
		}
		pager=reporService.sepager(pager);
		model.put("pager", pager);
		return this.doPackageURI("reportac/list");
	}
	
	@RequestMapping("/{id}/edit")
	public String edit(@PathVariable Integer id, Map<Object, Object> model,RedirectAttributes attr){
		Reportactivity reportactivity = reporService.selectcal(id);
		if(reportactivity == null) {
			throw new AdminIllegalArgumentException("不存在的ID:" + id);
		}
		model.put("user", reportactivity);
		
		return this.doPackageURI("reportac/edit");
	}
	/**
	 * 删除用户
	 * @param id
	 * @param attr
	 * @return
	 */
	@RequestMapping("/{id}/deleteli")
	public String deleteli(@PathVariable Integer id,RedirectAttributes attr){
		reporService.deletesell(id);
		setInfoMsg("删除成功！", attr);
		return this.doPackageURI("list");
	}
}
