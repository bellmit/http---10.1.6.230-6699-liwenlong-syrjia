package cn.syrjia.dao;


import cn.syrjia.common.BaseDaoInterface;

public interface SystemSettingDao extends BaseDaoInterface {

    /**
     * 通过键查询值
     *
     * @param key 键
     * @return 值。键不存在或键存在值不存在返回null
     */
    String getValueByKey(String key,String yztId);

    /**
     * 通过key更新value
     *
     * @param key   键
     * @param value 值
     * @return 更新的条数
     */
    int updateValueByKey(String key, String value);

}