package com.spinfosec.utils;

import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.enums.SessionItem;
import com.spinfosec.dto.pojo.common.TreeData;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName GenUtil
 * @Description: 〈通用工具类〉
 * @date 2018/10/10
 * All rights Reserved, Designed By SPINFO
 */
public class GenUtil<T>
{

	private static Logger log = LoggerFactory.getLogger(GenUtil.class);

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 生成UUID
	 *
	 * @return
	 */
	public static String getUUID()
	{
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}

	/**
	 * 组装权限树结构
	 * @param spCodeDecodesList
	 * @param isShowConAndUrl 是否显示图标和url（角色获取权限时不显示）
	 * @return
	 */
	public static List<TreeData> getModuleTree(List<SpCodeDecodes> spCodeDecodesList, boolean isShowConAndUrl)
	{

		List<TreeData> parents = new ArrayList<TreeData>();
		List<SpCodeDecodes> others = new ArrayList<SpCodeDecodes>();
		for (SpCodeDecodes spCodeDecodes : spCodeDecodesList)
		{
			// 如果父节点为空 则存在父节点集合中
			if (StringUtils.isEmpty(spCodeDecodes.getParentId()))
			{
				TreeData treeDataParent = new TreeData();
				treeDataParent.setId(spCodeDecodes.getId());
				treeDataParent.setName(spCodeDecodes.getName());
				treeDataParent.setShowChild(true);
				treeDataParent.setOrder(spCodeDecodes.getOrders());
				treeDataParent.setIsPreset(spCodeDecodes.getIsPreset());
				if (isShowConAndUrl)
				{
					treeDataParent.setUrl(spCodeDecodes.getUrl());
					treeDataParent.setIcon(spCodeDecodes.getIcon());
				}

				treeDataParent.setIsShow(true);
				parents.add(treeDataParent);
			}
			else
			{
				others.add(spCodeDecodes);
			}
		}

		for (TreeData parTreeData : parents)
		{
			List<TreeData> childTree = new ArrayList<>();
			for (SpCodeDecodes spCodeDecodes : others)
			{
				// 角色获取菜单时不显示用户和角色权限（只有预置用户才能创建下级用户和角色）
				if (!isShowConAndUrl)
				{
					if (spCodeDecodes.getUrl().contains("userRole"))
					{
						continue;
					}
				}

				if (spCodeDecodes.getParentId().equals(parTreeData.getId()))
				{
					TreeData treeChild = new TreeData();
					treeChild.setId(spCodeDecodes.getId());
					treeChild.setName(spCodeDecodes.getName());
					treeChild.setShowChild(true);
					treeChild.setUrl(spCodeDecodes.getUrl());
					treeChild.setIsShow((spCodeDecodes.getIsShow().intValue()) == 0 ? true : false);
					treeChild.setParentId(spCodeDecodes.getParentId());
					treeChild.setOrder(spCodeDecodes.getOrders());
					treeChild.setIsPreset(spCodeDecodes.getIsPreset());
					childTree.add(treeChild);
				}
			}
			// 有子菜单则存放在children中，没有则是否显示子菜单showChild为false
			if (!childTree.isEmpty())
			{
				parTreeData.setChildren(childTree);
			}
			else
			{
				parTreeData.setShowChild(false);
			}
		}
		return parents;
	}

	/**
	 * 将bean转换为json
	 *
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String beanToJson(Object obj) throws IOException
	{
		StringWriter writer = new StringWriter();
		JsonGenerator gen = new JsonFactory().createJsonGenerator(writer);
		mapper.writeValue(gen, obj);
		gen.close();
		String json = writer.toString();
		writer.close();
		return json;
	}

	/**
	 * 格式转换
	 * @param mapList
	 * @return
	 */
	public static List<Object[]> mapToListObj(List<Map<String, Object>> mapList)
	{
		List<Object[]> obj = new ArrayList<Object[]>();
		for (Map<String, Object> map : mapList)
		{
			Collection values = map.values();
			List list = new ArrayList(values);
			obj.add(list.toArray());
		}
		return obj;
	}

	/**
	 * 转换javaBean对象
	 * @param datas
	 * @param beanClass
	 * @return
	 */
	public List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass)
	{
		// 返回数据集合
		List<T> list = null;
		// 对象字段名称
		String fieldname = "";
		// 对象方法名称
		String methodname = "";
		// 对象方法需要赋的值
		Object methodsetvalue = "";
		try
		{
			list = new ArrayList<T>();

			// 遍历数据
			for (Map<String, Object> mapdata : datas)
			{
				// 创建一个泛型类型实例
				T t = beanClass.newInstance();

				Set<String> keySet = mapdata.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext())
				{
					fieldname = it.next();
					Field field = beanClass.getDeclaredField(fieldname);
					methodsetvalue = mapdata.get(fieldname);

					// 组装set方法
					methodname = "set" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);

					Method method = beanClass.getDeclaredMethod(methodname, field.getType());
					method.invoke(t, methodsetvalue);
				}

				list.add(t);
			}
		}
		catch (InstantiationException e)
		{
			log.info("实例化发生错误：" + e.toString());
		}
		catch (InvocationTargetException e)
		{
			log.info("调用发生错误：" + e.toString());
		}
		catch (NoSuchMethodException e)
		{
			log.info("没有匹配的方法：" + e.toString());
		}
		catch (IllegalAccessException e)
		{
			log.info("非法访问：" + e.toString());
		}
		catch (NoSuchFieldException e)
		{
			log.info("没有匹配的属性：" + e.toString());
		}

		return list;
	}

}
