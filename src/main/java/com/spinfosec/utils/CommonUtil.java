package com.spinfosec.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.utils.CommonUtil
 * @description [一句话描述]
 * @create 2018/3/7 10:11
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class CommonUtil
{

	public static String getDatabaseTypeTextForEvent(String databaseType)
	{
		String databaseTypeText = "";
		if (StringUtils.isNotEmpty(databaseType))
		{
			if ("MYSQL".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "MySql数据库";
			}
			else if ("MARIADB".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "MariaDB数据库";
			}
			else if ("SQLSERVER".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "SqlServer数据库";
			}
			else if ("ORACLE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Oracle数据库";
			}
			else if ("DM6".equalsIgnoreCase(databaseType) || "DM7".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "达梦数据库";
			}
			else if ("HIGHGO".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "瀚高数据库";
			}
			else if ("SHENTONG".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "神通数据库";
			}
			else if ("XUGU".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "虚谷数据库";
			}
			else if ("KINGBASE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "人大金仓数据库";
			}
			else if ("DB2".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "DB2数据库";
			}
			else if ("INFORMIX".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Informix数据库";
			}
			else if ("POSTGRESQL".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "PostgreSql数据库";
			}
			else if ("MONGODB".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "MongoDB数据库";
			}
			else if ("HBASE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Hbase数据库";
			}
			else if ("REDIS".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Redis数据库";
			}
			else if ("TERADATA".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Teradata数据库";
			}
			else if ("GREENPLUM".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Greenplum数据库";
			}
			else if ("SYBASE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Sybase数据库";
			}
			else if ("GBASE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "南大通用数据库";
			}
			else if ("CASSANDRA".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Cassandra数据库";
			}
			else if ("SQLITE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Sqlite数据库";
			}
			else if ("ACCESS".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Access数据库";
			}
			else if ("SHENZHOU".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "神舟数据库";
			}
			else if ("NEO4JV3".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "图数据库";
			}
			else if ("ORIENT".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Orient数据库";
			}
			else if ("HIVE".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "Hive数据库";
			}
			else if ("ElasticSearch".equalsIgnoreCase(databaseType))
			{
				databaseTypeText = "ElasticSearch数据库";
			}
			else
			{
				databaseTypeText = databaseType;
			}
		}
		return databaseTypeText;
	}

	public static String getDscvrFilesType(String resourceType)
	{
		String resourceTypeText = "";
		if (StringUtils.isNotEmpty(resourceType))
		{
			switch (resourceType)
			{
				case "FILE_SYSTEM":
					resourceTypeText = "文件共享";
					break;
				case "SHAREPOINT":
					resourceTypeText = "SharePoint";
					break;
				case "EXCHANGE":
					resourceTypeText = "Exchange";
					break;
				case "LOTUS":
					resourceTypeText = "Lotus";
					break;
				case "FTP":
					resourceTypeText = "Ftp";
					break;
				case "SFTP":
					resourceTypeText = "Linux主机";
					break;
				case "DATA_BASE":
					resourceTypeText = "数据库";
					break;
				default:
					resourceTypeText = getDatabaseTypeTextForEvent(resourceType);
					break;
			}
		}
		return resourceTypeText;
	}

}
