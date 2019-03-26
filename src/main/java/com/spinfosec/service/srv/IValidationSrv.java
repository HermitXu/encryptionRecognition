package com.spinfosec.service.srv;

import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.ValidationBean;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IValidationSrv
 * @Description: 〈后台校验接口〉
 * @date 2018/10/24
 * All rights Reserved, Designed By SPINFO
 */
public interface IValidationSrv
{
	/**
	 * 重名校验
	 * @param checkData
	 * @return
	 */
	boolean checkDuplicateData(ValidationBean checkData);

	/**
	 * 校验是否被引用
	 * @param ids 校验ID
	 * @param type 校验类型
	 * @return
	 */
	String getNotInUseIds(List<String> ids, String type);
}
