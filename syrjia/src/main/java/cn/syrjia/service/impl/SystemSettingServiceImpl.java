package cn.syrjia.service.impl;

import cn.syrjia.common.impl.BaseServiceImpl;
import cn.syrjia.dao.SystemSettingDao;
import cn.syrjia.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("systemSettingService")
public class SystemSettingServiceImpl extends BaseServiceImpl implements SystemSettingService {

    public static final String DRUG_UPDATED_AT = "drug_updated_at";

    @Autowired
    private SystemSettingDao systemSettingDao;

    /**
     * 通过键查询值
     *
     * @param key 键
     * @return 值。键不存在或键存在值不存在返回null
     */
    @Override
    public String getValueByKey(String key,String yztId) {
        return systemSettingDao.getValueByKey(key,yztId);
    }

    /**
     * 通过key更新value
     *
     * @param key   键
     * @param value 值
     * @return 更新的条数
     */
    @Override
    public int updateValueByKey(String key, String value) {
        return systemSettingDao.updateValueByKey(key, value);
    }

}
