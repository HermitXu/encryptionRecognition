package com.spinfosec.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.DscvrFilesDao;
import com.spinfosec.dao.entity.SpDscvrFiles;
import com.spinfosec.dto.pojo.system.DscvrFilesRsp;
import com.spinfosec.service.srv.IEventSrv;
import com.spinfosec.utils.GenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName EventSrvImpl
 * @Description: 〈检查事件接口实现类〉
 * @date 2018/10/17
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("eventSrv")
public class EventSrvImpl implements IEventSrv
{

	@Autowired
	private DscvrFilesDao dscvrFilesDao;

	/**
	 * 分页查询检查事件信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<DscvrFilesRsp> queryByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<DscvrFilesRsp> dscvrFilesRspList = dscvrFilesDao.queryDscvrFiles(queryMap);
		PageInfo<DscvrFilesRsp> pageList = new PageInfo<>(dscvrFilesRspList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 按时间周期获取事件加密算法类型比例
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @param userId 当前用户ID
	 * @return
	 */
	@Override
	public List<Object[]> getAlgorithmScale(String period, String userId)
	{
		return GenUtil.mapToListObj(dscvrFilesDao.getAlgorithmScale(period, userId));
	}

	/**
	 * 按时间周期获取事件 加密或未加密文件数量
	 * @param period 周期 DAY日 WEEK周 MONTH月 YEAR年
	 * @param userId 当前用户ID
	 * @return
	 */
	@Override
	public List<Object[]> getIsEncrypScale(String period, String userId)
	{
		return GenUtil.mapToListObj(dscvrFilesDao.getIsEncrypScale(period, userId));
	}

    /**
     * 事件保存方法
     * @param spDscvrFiles
     */
    @Override
    public void saveDscvrFiles(SpDscvrFiles spDscvrFiles)
    {
        dscvrFilesDao.saveDscvrFiles(spDscvrFiles);
    }
}
