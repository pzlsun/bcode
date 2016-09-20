package com.cs.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuildObj implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1066814369323411449L;
	
	private String buildDate = "";
	private String authorName = "";
	private String projectName = "";
	private String email="";
	
	// projectName
	// 作者
	// 数据库类型
	// 模板路径
	// 输出路径
	private String outPutPath = "";
	private Table lastHandleTable;
	
	private List<Table> tableList = new ArrayList<Table>();
	
	public List<Table> getTableList()
	{
		return tableList;
	}
	
	public String getDbType()
	{
		if (this.dbObj == null) return "mysql";
		String dbtype = dbObj.getDbmsCode().toLowerCase();
		if (dbtype.indexOf("mssql") >= 0)
		{
			return "mssql";
		}
		else if (dbtype.indexOf("oracle") >= 0)
		{
			return "oracle";
		}
		else if (dbtype.indexOf("sybase") >= 0)
		{
			return "sybase";
		}
		else
		{
			return "mysql";
		}
	}
	
	public void setTableList(List<Table> tableList)
	{
		this.tableList = tableList;
	}
	
	public String getProjectName()
	{
		return projectName;
	}
	
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	
	public String getOutPutPath()
	{
		return outPutPath;
	}
	
	public void setOutPutPath(String outPutPath)
	{
		this.outPutPath = outPutPath;
	}
	
	public Table getLastHandleTable()
	{
		return lastHandleTable;
	}
	
	public void setLastHandleTable(Table lastHandleTable)
	{
		this.lastHandleTable = lastHandleTable;
	}
	
	public Db getDbObj()
	{
		return dbObj;
	}
	
	public void setDbObj(Db dbObj)
	{
		this.dbObj = dbObj;
	}
	
	public String getAuthorName()
	{
		return authorName;
	}
	
	public void setAuthorName(String authorName)
	{
		this.authorName = authorName;
	}
	
	public String getBuildDate()
	{
		return buildDate;
	}
	
	public void setBuildDate(String buildDate)
	{
		this.buildDate = buildDate;
	}
	 

	public String getEmail()
     {
	     return email;
     }

	public void setEmail(String email)
     {
	     this.email = email;
     }

	private Db dbObj;
	
}
