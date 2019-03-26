package com.spinfosec.service.impl;

import com.spinfosec.dao.common.ValidationDao;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ValidationBean;
import com.spinfosec.service.srv.IValidationSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ValidationSrvImpl
 * @Description: 〈后台校验实现类〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
@Service("validationSrv")
public class ValidationSrvImpl implements IValidationSrv
{
	@Autowired
	private ValidationDao validationDao;

	/**
	 * 重名校验
	 * @param checkData
	 * @return
	 */
	@Override
	public boolean checkDuplicateData(ValidationBean checkData)
	{
		boolean reslut = true;
		List<Object> list = validationDao.duplicate(checkData);
		if (!list.isEmpty())
		{
			reslut = false;
		}
		return reslut;
	}

	/**
	 * 校验是否被引用
	 * @param ids 校验ID
	 * @param type 校验类型
	 * @return
	 */
	@Override
	public String getNotInUseIds(List<String> ids, String type)
	{

		StringBuffer result = new StringBuffer();

		if ("host".equalsIgnoreCase(type))
		{
			for (String id : ids)
			{
				List<String> userIds = validationDao.findHostIsUsed(id);
				// 未被引用的返回
				if (userIds.size() == 0)
				{
					if (result.toString().length() == 0)
					{
						result.append(id);
					}
					else
					{
						result.append(",").append(id);
					}

				}
			}
		}
		else if ("roles".equalsIgnoreCase(type))
		{
			for (String id : ids)
			{
				List<String> userIds = validationDao.findRolesIsUsed(id);
				// 未被引用的返回
				if (userIds.size() == 0)
				{
					if (result.toString().length() == 0)
					{
						result.append(id);
					}
					else
					{
						result.append(",").append(id);
					}

				}
			}

		}
		else if ("org".equalsIgnoreCase(type))
		{
			for (String id : ids)
			{
				List<String> userIds = validationDao.findOrgIsUsed(id);
				// 未被引用的返回
				if (userIds.size() == 0)
				{
					if (result.toString().length() == 0)
					{
						result.append(id);
					}
					else
					{
						result.append(",").append(id);
					}

				}
			}
		}
		return result.toString();
	}
}
