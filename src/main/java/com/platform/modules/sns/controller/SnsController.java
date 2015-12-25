package com.platform.modules.sns.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.platform.common.config.Global;
import com.platform.common.persistence.Page;
import com.platform.common.web.BaseController;
import com.platform.common.utils.StringUtils;
import com.platform.modules.sns.bean.Sns;
import com.platform.modules.sns.service.SnsService;

/**
 * 圈子Controller
 * @author sunshine
 * @version 2015-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sns/sns")
public class SnsController extends BaseController {

	@Autowired
	private SnsService snsService;
	
	@ModelAttribute
	public Sns get(@RequestParam(required=false) String id) {
		Sns entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = snsService.get(id);
		}
		if (entity == null){
			entity = new Sns();
		}
		return entity;
	}
	
	@RequiresPermissions("sns:sns:view")
	@RequestMapping(value = {"list", ""})
	public String list(Sns sns, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sns> page = snsService.findPage(new Page<Sns>(request, response), sns); 
		model.addAttribute("page", page);
		return "modules/sns/snsList";
	}

	@RequiresPermissions("sns:sns:view")
	@RequestMapping(value = "form")
	public String form(Sns sns, Model model) {
		model.addAttribute("sns", sns);
		return "modules/sns/snsForm";
	}

	@RequiresPermissions("sns:sns:edit")
	@RequestMapping(value = "save")
	public String save(Sns sns, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sns)){
			return form(sns, model);
		}
		snsService.save(sns);
		addMessage(redirectAttributes, "保存添加圈子成功");
		return "redirect:"+Global.getAdminPath()+"/sns/sns/?repage";
	}
	
	@RequiresPermissions("sns:sns:edit")
	@RequestMapping(value = "delete")
	public String delete(Sns sns, RedirectAttributes redirectAttributes) {
		snsService.delete(sns);
		addMessage(redirectAttributes, "删除添加圈子成功");
		return "redirect:"+Global.getAdminPath()+"/sns/sns/?repage";
	}

}