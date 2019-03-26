package com.spinfosec.dto.pojo.system;

/**
 * lotus database cfg dto
 *
 * @author liuqianfei
 */
public class LotusDatabaseDto
{
	private String path;
	private String host;
	private String view;
	private String name;
	private String field;

	private String uuid;

	public LotusDatabaseDto()
	{
	}

	public LotusDatabaseDto(String path, String view, String field)
	{
		this.path = path;
		this.view = view;
		this.field = field;
	}

	public LotusDatabaseDto(String path, String host, String view, String name)
	{
		this.path = path;
		this.host = host;
		this.view = view;
		this.name = name;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getView()
	{
		return view;
	}

	public void setView(String view)
	{
		this.view = view;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public String toString()
	{
		return "LotusDatabaseDto{" + "path='" + path + '\'' + ", host='" + host + '\'' + ", view='" + view + '\''
				+ ", name='" + name + '\'' + ", field='" + field + '\'' + ", uuid='" + uuid + '\'' + '}';
	}
}
