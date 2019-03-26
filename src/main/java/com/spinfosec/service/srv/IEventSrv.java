package com.spinfosec.service.srv;

import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpDscvrFiles;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IEventSrv
 * @Description: 〈检查事件接口〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
public interface IEventSrv
{
	/**
	 * 分页查询检查事件信息
	 * @param queryMap
	 * @return
	 */
	PageInfo<DscvrFilesRsp> queryByPage(Map<String, Object> queryMap);

	/**
	 * 按时间周期获取事件加密算法类型比例
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @param userId 当前用户ID
	 * @return
	 */
	List<Object[]> getAlgorithmScale(String period, String userId);

	/**
	 * 按时间周期获取事件 加密或未加密文件数量
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @param userId 当前用户ID
	 * @return
	 */
	List<Object[]> getIsEncrypScale(String period, String userId);


    /**
     * 保存事件
     * @param spDscvrFiles
     */
    void saveDscvrFiles(SpDscvrFiles spDscvrFiles);
}
