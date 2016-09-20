package com.cs.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Table
{
	private List<Column> listColumns = new ArrayList<Column>();
	
	public void Add(Column obj)
	{
		this.listColumns.add(obj);
	}
	
	public String getRadomLong(){
		Random r = new Random();//每次都不一致
		Long a= r.nextLong();
		return a.toString().replaceAll(",","");
	}
	
	// 表内容说明
	
	public List<Column> getListColumns()
	{
		return listColumns;
	}
	
	private String Comment;
	
	public String getObjectid()
	{
		return objectid;
	}
	
	public void setObjectid(String objectID)
	{
		objectid = objectID;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		Name = name;
	}
	
	 
	public String getCode()
	{
		return Code;
	}
	
	public void setCode(String code)
	{
		Code = code;
	}
	
	public String getCreator()
	{
		return Creator;
	}
	
	public void setCreator(String creator)
	{
		Creator = creator;
	}
	
	public String getComment()
	{
		return Comment==null?"":Comment;
	}
	
	public void setComment(String comment)
	{
		Comment = comment;
	}
	
	public List<Column> getKeys()
	{
		List<Column> keys = new ArrayList<Column>();
		if (this.listColumns != null)
		{
			
			for (Column c : listColumns)
			{
				if (c.getMandatory() != null && c.getMandatory().equals("1")) keys.add(c);
			}
		}
		return keys;
	}
	
	public Column getIdentityKey()
	{
		 
		if (this.listColumns != null)
		{
			
			for (Column c : listColumns)
			{
				if (c.getIdentity() != null && c.getIdentity().equals("1")) return c;
			}
			 
		}
		return new Column();
	}
	
	private String Creator;
	private String Name;
	private String Code;
	private String objectid;
}
