package com.cs.interfaces.pdm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.cs.objects.Column;
import com.cs.objects.Db;
import com.cs.objects.Table;

//Powerdesigner的PDM解析器
//
//由于项目需要做了一个从pdm中读取表/字段的代码,为以后使用方便记在此处
public class PDMHandler extends DefaultHandler2
{
	private static final String DBMS = "/" + Target.dbms.id;
	
	private static final String DBMS_NAME = DBMS + "/o:Shortcut/" + Target.dbmsName.id;
	
	private static final String DBMS_CODE = DBMS + "/o:Shortcut/" + Target.dbmsCode.id;

	private static final String TABLES = "/" + Target.tables.id;
	
	private static final String TABLES_TABLE = TABLES + "/" + Target.table.id;
	
	private static final String TABLES_TABLE_NAME = TABLES_TABLE + "/" + Target.tableName.id;
	
	private static final String TABLES_TABLE_CODE = TABLES_TABLE + "/" + Target.tableCode.id;
	
	// chenyh 2012 补充
	private static final String TABLES_TABLE_COMMENT = TABLES_TABLE + "/" + Target.tableComment.id;
	private static final String TABLES_TABLE_OBJECTID = TABLES_TABLE + "/" + Target.tableObjectID.id;
	
	private static final String TABLES_TABLE_COLUMNS = TABLES_TABLE + "/" + Target.columns.id;
	
	private static final String TABLES_TABLE_COLUMNS_COLUMN = TABLES_TABLE_COLUMNS + "/" + Target.column.id;
	
	private static Log log = LogFactory.getLog(PDMHandler.class);
	
	// 是否正在处理
	private boolean doing;
	
	// 结果对象
	private List<Table> tables = new ArrayList<Table>();
	
	// 上一次的表对象
	private Table lastTable;
	
	// 上一次的列对象
	private Column lastColumn;
	
	private Db db;
	
	public Db getDb()
	{
		return db;
	}
	
	// 上一次处理的对象
	private Target lastObject;
	
	// 上一次路径,元素之间用/进行分隔,属性则采用＠来标识，这是借用了XPath的规范
	private String lastPath = "";
	
	/** */
	/**
	 * 开始文档解析
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument()
	          throws SAXException
	{
		log.debug("powerdesigner pdm document parse start.");
	}
	
	/** */
	/**
	 * 结束文档解析
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument()
	          throws SAXException
	{
		log.debug("powerdesigner pdm document parse finish.");
	}
	
	/** */
	/**
	 * 启动元素解析
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes)
	          throws SAXException
	{
		// 首先处理dbms的解释
		if (Target.dbms.id.equals(name) || Target.tables.id.equals(name)) doing = true;
		
		// 如果没有开始，则直接退出
		if (!doing) return;
		
		// 设置路径
		String tempPath = lastPath + "/" + name;
		
		log.debug("开始解析:" + name);
		// DBMS
		
		if (tempPath.indexOf(DBMS) >= 0)
		{
			if (tempPath.equals(DBMS_CODE))
			{
				this.db = new Db();
				lastObject = Target.dbmsCode;
			}
			// if (tempPath.equals(DBMS_NAME)) {
			// this.db = new Db();
			// lastObject = Target.dbmsName;
			// } else
			
			// db = new Db();
			// lastObject= Target.dbmsCode;
			// TABLE
		}
		else if (lastPath.equals(TABLES))
		{
			if (tempPath.equals(TABLES_TABLE))
			{
				// 新建一个表
				lastTable = new Table();
				tables.add(lastTable);
			}
			// TABLE的属性
		}
		else if (lastPath.equals(TABLES_TABLE))
		{
			if (tempPath.equals(TABLES_TABLE_NAME))
			{
				lastObject = Target.tableName;
			}
			else if (tempPath.equals(TABLES_TABLE_CODE))
			{
				lastObject = Target.tableCode;
			}
			else if (tempPath.equals(TABLES_TABLE_COMMENT))
			{
				lastObject = Target.tableComment;
			}
			else if (tempPath.equals(TABLES_TABLE_OBJECTID))
			{
				lastObject = Target.tableObjectID;
			}
			
			// 列
		}
		else if (lastPath.equals(TABLES_TABLE_COLUMNS))
		{
			if (tempPath.equals(TABLES_TABLE_COLUMNS_COLUMN))
			{
				// 新建列
				lastColumn = new Column();
				lastTable.Add(lastColumn);
			}
		}
		else if (lastPath.equals(TABLES_TABLE_COLUMNS_COLUMN))
		{
			// 只处理列的属性
			lastObject = findTarget(Group.column, name);
		}
		lastPath = tempPath;
	}
	
	/** */
	/**
	 * 结束元素解析
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name)
	          throws SAXException
	{
		// 管理是否结束schema的解析
		if (!doing) return;
		
		log.debug("完成解析:" + name);
		if (Target.dbms.id.equals(name) || Target.tables.id.equals(name)) doing = false;
		
		// 恢复路径状态
		lastPath = StringUtils.substringBeforeLast(lastPath, "/" + name);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
	          throws SAXException
	{
		if (lastObject != null && lastObject.ob != null)
		{
			Object dest = null;
			
			dest = Group.dbms.equals(lastObject.og) ? db : Group.table.equals(lastObject.og) ? lastTable
			          : Group.column.equals(lastObject.og) ? lastColumn : tables;
			try
			{
				
				if (Group.table.equals(lastObject.og))
				{
					System.out.print(lastObject.ob + ":");
					System.out.println(new String(ch, start, length));
				}
				BeanUtils.setProperty(dest, lastObject.ob, new String(ch, start, length));
				lastObject = null;
			}
			catch (Exception e)
			{
				throw new SAXException(e);
			}
		}
	}
	
	/** */
	/**
	 * 取得解析成功的Table定义信息
	 * 
	 * @return
	 */
	public List<Table> getTables()
	{
		return tables;
	}
	
	/** */
	/**
	 * 根据标识找符合标识的东西
	 * 
	 * @param group
	 * @param id
	 * @return
	 */
	private static Target findTarget(Group group, String id)
	{
		for (Target item : Target.class.getEnumConstants())
		{
			if (group == item.og && item.id.equals(id)) return item;
		}
		return null;
	}
	
	/** */
	/**
	 * 操作的组分类
	 * 
	 * @author wilesun
	 * @create 2007-11-3
	 */
	private enum Group
	{
		dbms, table, column
	}
	
	/** */
	/**
	 * 解析操作码
	 * 
	 * @author wilesun
	 * @create 2007-11-3
	 */
	private enum Target
	{
		/** */
		/**
		 * dbms
		 */
		dbms("c:DBMS"),
		/** */
		/**
		 * dbms名称
		 */
		dbmsName("a:Name", "dbmsName", Group.dbms),
		/** */
		/**
		 * dbms编码
		 */
		dbmsCode("a:Code", "dbmsCode", Group.dbms),
		/** */
		/**
		 * 表集合
		 */
		tables("c:Tables"),
		/** */
		/**
		 * 表
		 */
		table("o:Table"),
		/** */
		/**
		 * 表名称
		 */
		tableName("a:Name", "name", Group.table),
		/** */
		/**
		 * 表编码
		 */
		tableCode("a:Code", "code", Group.table),
		/** */
		tableObjectID("a:ObjectID", "objectid", Group.table),
		/** */
		tableComment("a:Comment", "comment", Group.table),
		
		/** */
		/**
		 * 列集合
		 */
		columns("c:Columns"),
		/** */
		/**
		 * 列
		 */
		column("o:Column"),
		/** */
		/**
		 * 列名称
		 */
		columnName("a:Name", "name", Group.column),
		/** */
		/**
		 * 列编号
		 */
		columnCode("a:Code", "code", Group.column),
		/** */
		/**
		 * 列格式
		 */
		columnFormat("a:Format", "format", Group.column),
		/** */
		/**
		 * 列下拉
		 */
		columnList("a:ListOfValues", "list", Group.column),
		/** */
		/**
		 * 列类型
		 */
		columnDataType("a:DataType", "dataType", Group.column),
		/** */
		/**
		 * 列长度
		 */
		columnLength("a:Length", "length", Group.column),
		/** */
		
		columnIdentity("a:Identity", "identity", Group.column),
		
		/**
		 * 列强制
		 */
		
		columnComment("a:Comment", "comment", Group.column),
		
		/**
		 * 列强制
		 */
		columnMandatory("a:Column.Mandatory", "mandatory", Group.column);
		
		// 操作码标识
		private String id;
		
		private String ob;
		
		private Group og;
		
		/** */
		/**
		 * 只根据标识,无组和操作构造
		 * 
		 * @param id
		 */
		Target(String id)
		{
			this.id = id;
			this.og = Group.dbms;
		}
		
		/** */
		/**
		 * 根据标识和操作做内部构造
		 * 
		 * @param id
		 */
		Target(String id, String ob, Group og)
		{
			this.id = id;
			this.ob = ob;
			this.og = og;
		}
	}
}
