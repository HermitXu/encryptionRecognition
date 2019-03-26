package com.spinfosec.service.impl;

import com.spinfosec.dao.entity.SpSystemOperateLogInfo;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.LotusDatabaseDto;
import com.spinfosec.service.srv.ILotusConfigSrv;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.GenUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName LotusConfigSrvImpl
 * @Description: 〈Lotusp配置业务处理接口实现类〉
 * @date 2018/11/16
 * All rights Reserved, Designed By SPINFO
 */
@Service("lotusConfigSrv")
public class LotusConfigSrvImpl implements ILotusConfigSrv
{
	private static final Logger log = LoggerFactory.getLogger(LotusConfigSrvImpl.class);

	private final String LOTUS_XML = System.getenv("SP_HOME") + File.separator + "DDMS" + File.separator + "config"
			+ File.separator + "lotus.xml";

	@Autowired
	private ISystemSrv systemSrv;

	/**
	 * 获取Lotus邮箱类型配置
	 * @return
	 */
	@Override
	public LotusDatabaseDto getLotusMailConfig()
	{
		LotusDatabaseDto lotusDatabaseDto = new LotusDatabaseDto();
		try
		{
			File file = new File(LOTUS_XML);

			if (!file.exists() || !file.isFile())
			{
				FileUtils.touch(file);
				log.debug("create lotus config xml via mail get interface...");

				Document document = DocumentHelper.createDocument();
				Element lotus = document.addElement("lotus");
				document.setRootElement(lotus);

				lotus.addElement("mail");
				lotus.addElement("database");

				FileWriter fileWriter = new FileWriter(LOTUS_XML);
				XMLWriter writer = new XMLWriter(fileWriter);
				writer.write(document);
				writer.close();
			}

			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element rootElement = document.getRootElement();
			Element mailElement = rootElement.element("mail");

			lotusDatabaseDto.setPath(mailElement.elementTextTrim("path"));
			lotusDatabaseDto.setView(mailElement.elementTextTrim("view"));
			lotusDatabaseDto.setField(mailElement.elementTextTrim("field"));
		}
		catch (Exception e)
		{
			log.error("get lotus mail config error:", e);
		}
		return lotusDatabaseDto;
	}

	/**
	 * 更新Lotus邮箱类型配置
	 * @param lotusDatabaseDto
	 * @return
	 */
	@Override
	public CodeRsp updateLotusMailConfig(LotusDatabaseDto lotusDatabaseDto)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		try
		{
			File file = new File(LOTUS_XML);

			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element rootElement = document.getRootElement();

			Element mailElement = rootElement.element("mail");
			if (null == mailElement)
			{
				mailElement = rootElement.addElement("mail");
			}
			mailElement.clearContent();
			mailElement.addElement("path").setText(lotusDatabaseDto.getPath());
			mailElement.addElement("view").setText(lotusDatabaseDto.getView());
			mailElement.addElement("field").setText(lotusDatabaseDto.getField());

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setIndentSize(4);
			XMLWriter writer = new XMLWriter(new FileWriter(LOTUS_XML), format);
			writer.write(document);
			writer.close();
		}
		catch (Exception e)
		{
			log.error("update lotus mail config error:", e);
			codeRsp = new CodeRsp(RspCode.INNER_ERROR);
		}
		return codeRsp;
	}

	/**
	 * 获取Lotus文档类型配置
	 * @return
	 */
	@Override
	public List<LotusDatabaseDto> getLotusDocumentConfig()
	{
		List<LotusDatabaseDto> lotusDatabases = new ArrayList<>();

		try
		{
			File file = new File(LOTUS_XML);

			if (!file.exists() || !file.isFile())
			{
				FileUtils.touch(file);
				log.debug("create lotus config xml via document get interface...");

				Document document = DocumentHelper.createDocument();
				Element lotus = document.addElement("lotus");
				document.setRootElement(lotus);

				lotus.addElement("mail");
				lotus.addElement("database");

				FileWriter fileWriter = new FileWriter(LOTUS_XML);
				XMLWriter writer = new XMLWriter(fileWriter);
				writer.write(document);
				writer.close();
			}

			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element rootElement = document.getRootElement();

			@SuppressWarnings("unchecked")
			List<Element> databaseElements = rootElement.elements("database");
			if (null == databaseElements)
			{
				databaseElements = new ArrayList<>();
			}

			LotusDatabaseDto lotusDatabaseDto;
			for (Element database : databaseElements)
			{
				lotusDatabaseDto = new LotusDatabaseDto();
				lotusDatabaseDto.setPath(database.attributeValue("path"));
				lotusDatabaseDto.setHost(database.attributeValue("host"));
				lotusDatabaseDto.setView(database.elementTextTrim("view"));
				lotusDatabaseDto.setName(database.elementTextTrim("name"));
				lotusDatabaseDto.setUuid(GenUtil.getUUID());
				lotusDatabases.add(lotusDatabaseDto);
			}
		}
		catch (Exception e)
		{
			log.error("get lotus document config error:", e);
		}

		if (lotusDatabases.size() == 0)
		{
			lotusDatabases.add(new LotusDatabaseDto());
		}

		return lotusDatabases;
	}

	/**
	 * 更新Lotus文档类型配置
	 * @param lotusDatabaseDtos
	 * @return
	 */
	@Override
	public CodeRsp updateLotusDocumentConfig(List<LotusDatabaseDto> lotusDatabaseDtos,
			SpSystemOperateLogInfo operateLogInfo)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		try
		{
			// 操作日志
			StringBuilder operateDesc = new StringBuilder();

			File file = new File(LOTUS_XML);

			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element rootElement = document.getRootElement();

			// 1. clear all database node
			@SuppressWarnings("unchecked")
			List<Element> databases = rootElement.elements("database");
			if (null == databases)
			{
				databases = new ArrayList<>();
			}
			for (Element element : databases)
			{
				element.clearContent();
				rootElement.remove(element);
			}

			// 2. persist
			for (LotusDatabaseDto lotusDatabaseDto : lotusDatabaseDtos)
			{
				Element database = rootElement.addElement("database");
				database.addAttribute("path", lotusDatabaseDto.getPath());
				database.addAttribute("host", lotusDatabaseDto.getHost());
				Element view = database.addElement("view");
				view.setText(lotusDatabaseDto.getView());
				Element name = database.addElement("name");
				name.setText(lotusDatabaseDto.getName());

				operateDesc.append("IP：" + lotusDatabaseDto.getHost() + "，路径：" + lotusDatabaseDto.getPath() + "，视图："
						+ lotusDatabaseDto.getView() + "，名称：" + lotusDatabaseDto.getName()).append("；");
			}

			// pretty format output
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setIndentSize(4);
			XMLWriter writer = new XMLWriter(new FileWriter(LOTUS_XML), format);
			writer.write(document);
			writer.close();

			// 操作日志
			if (operateDesc.length() > 0)
			{
				operateDesc.insert(0, "Lotus配置：文档类型，");
				operateLogInfo.setOperation(operateDesc.toString());
				systemSrv.saveOperateLog(operateLogInfo);
			}

		}
		catch (Exception e)
		{
			log.error("update lotus document config error:", e);
			codeRsp = new CodeRsp(RspCode.INNER_ERROR);
		}

		return codeRsp;
	}
}
