package cn.syrjia.controller;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.syrjia.config.configCode;
import cn.syrjia.entity.Banner;
import cn.syrjia.service.BannerService;
import cn.syrjia.util.Util;

/** banner控制器
 * 
 * @pdOid 63136139-b477-4074-a29b-efca813f1e9d */
@Controller 
@RequestMapping("/banner")
public class BannerController {
   /** @pdOid 8eab2509-9aff-48c4-9e7c-84614a2a0ce1 */
   @Resource(name = "bannerService")
   private BannerService bannerService;
   
   /** 查询banner列表
    * 
    * @param request 
    * @param banner banner实体
    * @pdOid de524366-f26b-480c-b193-58a38d3bf5d3 */
   @ResponseBody
   @RequestMapping(value="/queryBannerList")
   public Map<String,Object> queryBannerList(HttpServletRequest request, Banner banner) {
       List<Map<String,Object>> banners=bannerService.queryBannerList(banner);
       return Util.resultMap(configCode.code_1001, banners);
   }
   
   /** 根据bannerid查询Banner信息
    * 
    * @param request 
    * @param bannerId bannerId
    * @pdOid bc12219e-1013-402d-b0de-adc446ccdc63 */
   @ResponseBody
   @RequestMapping(value="/queryBannerById")
   public Map<String,Object> queryBannerById(HttpServletRequest request, String bannerId) {
	   Banner banner=bannerService.queryById(Banner.class,bannerId);
	   return Util.resultMap(configCode.code_1001, banner);
   }

}