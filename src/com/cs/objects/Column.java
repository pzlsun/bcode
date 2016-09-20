package com.cs.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column
{
	private String Code;
	private String Name;
	private String DataType;
	private String Mandatory;
	private String Comment;
	
	private String Identity;// 是否自增
	
	public String getIdentity()
	{
		return (Identity == null) ? "0" : "1";
	}
	
	public void setIdentity(String identity)
	{
		Identity = identity;
	}
	
	public String getCode()
	{
		return Code;
	}
	
	public void setCode(String code)
	{
		Code = code;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		Name = name;
	}
	
	public String getDataType()
	{
		return DataType;
	}
	
	public void setDataType(String dataType)
	{
		DataType = GetMySql_CodeType(dataType);
	}
	
	public String getCodeType()
	{
		return "String";
	}
	
	public String getMandatory()
	{
		return Mandatory == null ? "0" : "1";
	}
	
	public void setMandatory(String mandatory)
	{
		Mandatory = mandatory;
	}
	
	public String getComment()
	{
		return Comment==null?"":Comment;
	}
	
	public void setComment(String comment)
	{
		Comment = comment;
	}
	
	static String regEx = "([a-zA-Z])[\\(][\\d]+[,][\\d]+[\\)]"; // 表示一个或多个a
	static String regEx1 = "([a-zA-Z])[\\(][\\d]+[\\)]";
	static String regEx2 = "([a-zA-Z])";
	
	static Pattern p = Pattern.compile(regEx);
	
	static Pattern p1 = Pattern.compile(regEx1);
	
	static Pattern p2 = Pattern.compile(regEx2);
	
	private String GetMySql_CodeType(String datatypestr)
	{
		datatypestr = datatypestr.toLowerCase();
		String codeType = "Object";
		Matcher m, m1, m2;
		m = p.matcher(datatypestr);
		m1 = p1.matcher(datatypestr);
		m2 = p2.matcher(datatypestr);
		if (m.find()) codeType = m.replaceAll("$1");
		else if (m1.find()) codeType = m1.replaceAll("$1");
		else if (m2.find()) codeType = datatypestr;
		
		if (codeType.indexOf("bigint") >= 0) codeType = "Long";
		else if (codeType.indexOf("bit") >= 0) codeType = "Boolean";
		else if (codeType.indexOf("int") >= 0) codeType = "Integer";
		else if (codeType.indexOf("char") >= 0) codeType = "String";
		else if (codeType.indexOf("date") >= 0) codeType = "Date";
		else if (codeType.indexOf("decimal") >= 0) codeType = "Double";
		else if (codeType.indexOf("text") >= 0) codeType = "String";
		else if (codeType.indexOf("tinyint") >= 0) codeType = "Integer";
		else if (codeType.indexOf("tinytext") >= 0) codeType = "String";
		else if (codeType.indexOf("varchar") >= 0) codeType = "String";
		else if (codeType.indexOf("mediumint") >= 0) codeType = "Integer";
		else if (codeType.indexOf("smallint") >= 0) codeType = "Integer";
		else if (codeType.indexOf("float") >= 0) codeType = "Double";
		else if (codeType.indexOf("double") >= 0) codeType = "Double";
		else if (codeType.indexOf("double precision") >= 0) codeType = "Double";
		else if (codeType.indexOf("real") >= 0) codeType = "Double";
		else if (codeType.indexOf("datetime") >= 0) codeType = "Date";
		else if (codeType.indexOf("timestamp") >= 0) codeType = "Date";
		else if (codeType.indexOf("time") >= 0) codeType = "Date";
		else if (codeType.indexOf("year") >= 0) codeType = "Date";
		else if (codeType.indexOf("tinyblob") >= 0) codeType = "Byte[]";
		else if (codeType.indexOf("blob") >= 0) codeType = "Byte[]";
		else if (codeType.indexOf("mediumblob") >= 0) codeType = "Byte[]";
		else if (codeType.indexOf("mediumtext") >= 0) codeType = "String";
		else if (codeType.indexOf("longblob") >= 0) codeType = "Byte[]";
		else if (codeType.indexOf("longtext") >= 0) codeType = "String";
		
		else
			codeType = "Object";
		
		return codeType;
	}
	
	@SuppressWarnings("unused")
	private String GetSybase_CodeType(int datatype)
	{
		switch (datatype)
		{
			case -1:
				return "Object";
			case 1:
			case 2:
			case 24:
			case 25:
			case 19:
			case 36:
				return "String";
			case 3:
			case 10:
				return "Long";
			case 11:
			case 17:
				return "Double";
			case 5:
			case 6:
			case 7:
			case 13:
				return "Integer";
			case 12:
			case 37:
			case 38:
			case 39:
			case 40:
				return "Date";
			case 16:
				return "Boolean";
			default:
				return "Object";
				
		}
	}
	
	@SuppressWarnings("unused")
	private String GetMSSQL_CodeType(int datatype)
	{
		switch (datatype)
		{
			case 127:
				return "Long";
			case 60:
			case 62:
			case 106:
			case 108:
			case 122:
				return "Double";
			case 48:
			case 52:
			case 56:
				return "Integer";
				
			case 40:
			case 41:
			case 42:
			case 43:
			case 58:
			case 61:
				return "Date";
			case 59:
			case 98:
			case 240:
			case 189:
			case 241:
				return "Object";
			case 104:
				return "Boolean";
				
			case 34:
			case 165:
			case 173:
				return "byte[]";
				
			case 36:
				return "String";// ibatis 对GUID支持不好，无法识别
				// return "UUID";
				
			case 35:
			case 99:
			case 167:
			case 175:
			case 231:
			case 239:
				
				return "String";
				
			default:
				return "Object";
				
		}
		
	}
	
}
