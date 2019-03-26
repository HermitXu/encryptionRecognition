package com.spinfosec.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName BaseDao
 * @Description: 〈一句话功能简述〉
 * @date 2019/1/30
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface BaseDao
{
	List<Map<String, Object>> getBySql(@Param("sql") String sql);
}
