package cn.syrjia.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.config.Config;
import cn.syrjia.config.configCode;
import cn.syrjia.dao.EvaluateDao;
import cn.syrjia.entity.Evaluate;
import cn.syrjia.entity.EvaluateEvalabel;
import cn.syrjia.entity.Piclib;
import cn.syrjia.service.EvaluateService;
import cn.syrjia.util.GetOpenId;
import cn.syrjia.util.JsonUtil;
import cn.syrjia.util.SessionUtil;
import cn.syrjia.util.StringUtil;
import cn.syrjia.util.Util;

@Service("evaluateService")
public class EvaluateServiceImpl extends BaseServiceImpl implements EvaluateService{

	
	@Resource(name = "evaluateDao")
	EvaluateDao evaluateDao;
	
	@Resource(name = "config")
	Config config;
	
	/**
	 * 查询评价标签
	 */
	@Override
	public Map<String, Object> queryEvalabels(Integer type) {
		//查询
		List<Map<String,Object>> list=evaluateDao.queryEvalabels(type);
		if(null==list){
			return Util.resultMap(configCode.code_1015,list);
		}
		return Util.resultMap(configCode.code_1001,list);
	}
	
	/**
	 * 查询评价列表
	 * @param request
	 * @param evaluate
	 * @param memberId
	 * @param page
	 * @param row
	 * @param level
	 * @return
	 */
	@Override
	public Map<String, Object> queryEvaluateList(Evaluate evaluate,Integer level,
			Integer page, Integer row) {
		//查询
		List<Map<String,Object>> list=evaluateDao.queryEvaluateList(evaluate,level,page, row);
		if(null==list){
			return Util.resultMap(configCode.code_1015,null);
		}
		return Util.resultMap(configCode.code_1001,list);
	}
	
	/**
	 * 查询好评率
	 * @param evaluate
	 * @param evaluateLevel
	 * @return
	 */
	@Override
	public Map<String, Object> queryEvaluateRate(Evaluate evaluate,
			Integer evaluateLevel) {
		//查询
		String rate=evaluateDao.queryEvaluateRate(evaluate,4);
		return Util.resultMap(configCode.code_1001,rate);
	}
	
	/**
	 * 查询差评 中评 好评数
	 * @param evaluate
	 * @return
	 */
	@Override
	public Map<String, Object> queryEvaluateNum(Evaluate evaluate) {
		//查询
		Map<String,Object> evaluateNum=evaluateDao.queryEvaluateNum(evaluate);
		return Util.resultMap(configCode.code_1001,evaluateNum);
	}
	
	/**
	 * 新增评价
	 * 
	 * @param eva
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> addEvaluate(List<Map<String,Object>> list, String orderNo,String memberId) {
		Object obj=null;
		
		Map<String,Object> goods=evaluateDao.queryOrderState(orderNo);//查询订单状态
		if(null==goods||goods.size()==0){
			return Util.resultMap(configCode.code_1034,null);
		}
		Integer i=evaluateDao.queryEvaluateByOrderNo(orderNo);//查询是否评价
		if(i>0){
			return Util.resultMap(configCode.code_1081,null);
		}
		//遍历
		for(Map<String,Object> map:list){
			//创建对象
			Evaluate evaluate=new Evaluate();
			//赋值
			evaluate.setGoodsId(map.get("goodsId").toString());
			evaluate.setOrderNo(orderNo);
			evaluate.setMemberId(memberId);
			evaluate.setCreateTime(Util.queryNowTime());
			evaluate.setOrderDetailId(map.get("orderDetailId").toString());
			evaluate.setEvaluateLevel(Integer.parseInt(map.get("evaluateLevel").toString()));
			evaluate.setEvaluate_note(map.get("evaluate_note").toString());
			//执行添加
			obj=evaluateDao.addEntityUUID(evaluate);
			if(!StringUtil.isEmpty(map.get("choosePicture"))){
				String[] pictures=map.get("choosePicture").toString().split(",");
				//执行更新
				evaluateDao.updateEvaluatePic(obj.toString(), pictures);
			}
			if(!StringUtil.isEmpty(map.get("checkLabel"))){
				String[] checkLabels=map.get("checkLabel").toString().split(",");
				for(String checkLabel:checkLabels){
					//创建对象
					EvaluateEvalabel evaluateEvalabel=new EvaluateEvalabel();
					//赋值
					evaluateEvalabel.setEvalableName(checkLabel);
					evaluateEvalabel.setEvaluateId(obj.toString());
					//执行添加
					evaluateDao.addEntityUUID(evaluateEvalabel);
				}
			}
		}
		return Util.resultMap(configCode.code_1001,obj);
	}
	
	/**
	 * 添加频率图片
	 * @param evaluateId
	 * @param picIds
	 * @return
	 */
	@Override
	public Integer updateEvaluatePic(String evaluateId, String[] picIds) {
		return evaluateDao.updateEvaluatePic(evaluateId,picIds);
	}

	/**
	 * 添加评价
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object addEvaluate(HttpServletRequest request,List<Evaluate> evaluate,String picServerIds) {
		String memberId = GetOpenId.getMemberId(request);
		Object isOk = 0;
		//遍历评价
		for(Evaluate eva:evaluate){
			//为空判断
			if(!StringUtil.isEmpty(picServerIds)){
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> serverids = JsonUtil.parseJSON2List(picServerIds);
				String filePath="";
				//创建图片对象
				Piclib pic = new Piclib();
				//赋值
				pic.setPicId(Util.getUUID());
				pic.setGoodId(memberId);
				pic.setRsrvStr1(eva.getOrderNo());
				pic.setStatus("10");
				pic.setStatusDate((int) (System.currentTimeMillis() / 1000));
				/**存储晒图图片
				 * mediaIds(服务IDs)
				*/
				/*for(String mediaId:mediaIds){
					System.out.println(mediaId+"你好mediaId");
					if(!StringUtil.isEmpty(mediaId)){
						String wxMediaId = "";
						map = DownloadImgByMediaId.downloadMedia(wxMediaId,"evaluate",request, evaluateDao);
						filePath=map.get("fwPath").toString();//存储访问路径
						String localPath = map.get("riskPath").toString();//本地路径
						System.out.println("\nfilePath:"+filePath+"\n");
						System.out.println("localPath:"+localPath+"\n");
						evaluateDao.addEntity(pic);
					}
				}*/
			}
			isOk = 0;
			eva.setCreateTime((int)(System.currentTimeMillis()/1000));
			eva.setState(1);
			eva.setMemberId(memberId);
			eva.setEvaluateLevel(5);
			//执行添加
			Object id = evaluateDao.addEntityUUID(eva);
			if(id!=null){
				/*Order order = new Order();
				order.setOrderNo(eva.getOrderNo());
				order = evaluateDao.queryById(Order.class, eva.getOrderNo());
				if(order!=null){
					order.setPaymentStatus("8");//已评价
					medicalOrderDao.updateOrder(order);
				}*/
				isOk = 1;
			}
		}
		return isOk;
	}

	/**
	 * 更新评价
	 */
	@Override
	public Object updateEvaluate(Evaluate evaluate, String state) {
		Object i = 0;
		if("high".equals(state)){//改為好評
			i = evaluateDao.updateHigh(evaluate);
		}else{
			i = evaluateDao.updateEntity(evaluate);
		}
		return i;
	}

	/**
	 * 添加评论图片
	 */
	@Override
	public List<Map<String, Object>> uploadEvaluatePic(
			MultipartFile[] multipartFile, HttpServletRequest request,
			String id,String orderNo) {
		List<Map<String, Object>> urlList = new ArrayList<Map<String, Object>>();
		//获取参数
		String nowpath = System.getProperty("user.dir");
		//创建路径
		String path = nowpath.replace("bin", "webapps");
		path = path + "/uploadFiles";
		//获取openid
		String openid = SessionUtil.getOpenId(request);
		Piclib haspic = new Piclib();
		//设置参数
		haspic.setGoodId(openid);
		haspic.setRsrvStr1(orderNo);
		//查询实体
		List<Piclib> piclist = evaluateDao.query(haspic);
		//计算大小
		int isHasCnt = piclist.size()+multipartFile.length;
		if (multipartFile.length == 0||multipartFile.length>3||isHasCnt>3) {
			return urlList;
		}else{
			for (MultipartFile file : multipartFile) {
				// 保存
				try {
					String url = request.getScheme() + "://"
							+ config.getImgIp() + ":"
							+ request.getServerPort() + File.separator
							+ "uploadFiles" + File.separator + "evaluate";
					Map<String, Object> map = new HashMap<String, Object>();
					String fileName = file.getOriginalFilename();

					Long name = System.currentTimeMillis();
					String addr = name.toString()
							+ fileName.substring(fileName.lastIndexOf("."));
					String furl = path + File.separator + "evaluate";

					BufferedImage bi = ImageIO.read(file.getInputStream());
					int fileSize = (int) (file.getSize()/1024);  //图片大小  KB
					if(bi.getWidth()<150||bi.getHeight()<150||fileSize>1024){
						break;
					}

					File targetFile = new File(furl, addr);
					if (!targetFile.exists()) {
						targetFile.mkdirs();
					}

					file.transferTo(targetFile);
					String pathUrl = furl + File.separator + addr;
					String fwUrl = url + File.separator + addr;
					map.put("localUrl", pathUrl);
					map.put("serverUrl", fwUrl);
					//执行添加
					urlList.add(map);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		return urlList;
	}

	/**
	 * 添加评论
	 */
	@Override
	public Object addEva(HttpServletRequest request, Evaluate eva) {
//		String memberId = request.getSession().getAttribute("memberId").toString();
		String memberId = "73d280418c214f3d941bbf917a7a6299";
		Object isOk = 0;
		//赋值
		eva.setCreateTime((int)(System.currentTimeMillis()/1000));
		eva.setState(1);
		eva.setMemberId(memberId);
		eva.setEvaluateLevel(5);
		//执行添加
		Object id = evaluateDao.addEntityUUID(eva);
		if(id!=null){
			/*Order order = new Order();
			order.setOrderNo(eva.getOrderNo());
			order = evaluateDao.queryById(Order.class, eva.getOrderNo());
			if(order!=null){
				order.setPaymentStatus("8");//已评价
				medicalOrderDao.updateOrder(order);
			}*/
			isOk = 1;
		}
		return Util.resultMap(configCode.code_1001,isOk);
	}

}
