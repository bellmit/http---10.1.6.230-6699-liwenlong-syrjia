package cn.syrjia.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.BannerDao;
import cn.syrjia.entity.Banner;
import cn.syrjia.service.BannerService;

/** banner service实现
 * 
 * @pdOid a9df8d52-fec2-4571-a3d2-2f1f0c127b82 */
@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl implements BannerService {
   /** @pdOid fbed267f-7188-4dc9-b8be-0f89a60ac1c2 */
   @Resource(name="bannerDao")
   public BannerDao bannerDao;
   
   /** 查询活动列表
    * 
    * @param banner banner实体
    * @pdOid 252ba391-399d-448f-9420-0f57bc6d7a5d */
   public List<Map<String,Object>> queryBannerList(Banner banner) {
	   if(null==banner.getPort()||0==banner.getPort()){
		   banner.setPort(1);
	   }
      return bannerDao.queryBannerList(banner);
   }
   
   /** 根据活动id查询活动信息
    * 
    * @param bannerId bannerId
    * @pdOid e73512fc-c22e-4d80-9971-24817f7c5f49 */
   public Banner queryBannerById(String bannerId) {
      // TODO: implement
      return null;
   }

}