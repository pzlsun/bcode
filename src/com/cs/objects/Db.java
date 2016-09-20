package com.cs.objects;

public class Db
{
	private String DbmsCode;
	
	public String getDbmsCode()
	{
		return DbmsCode;
	}
	
	public void setDbmsCode(String dbmsCode)
	{
		DbmsCode = dbmsCode;
	}
	
	public String getDbmsName()
	{
		return DbmsName;
	}
	
	public void setDbmsName(String dbmsName)
	{
		DbmsName = dbmsName;
	}
	
	public String getComment()
     {
	     return Comment;
     }

	public void setComment(String comment)
     {
	     Comment = comment;
     }

	private String DbmsName;

	private String Comment;
}
