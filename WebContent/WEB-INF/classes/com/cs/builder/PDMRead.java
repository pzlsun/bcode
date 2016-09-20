package com.cs.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.cs.interfaces.pdm.PDMHandler;
import com.cs.objects.BuildObj;
import com.cs.objects.Db;
import com.cs.objects.Table;

public class PDMRead implements IDataRead
{
	
	public BuildObj ReadData(String fileName)
	{
		BuildObj bo = new BuildObj();
		// TODO Auto-generated method stub
		XMLReader xmlReader = null;// XMLReaderFactory.createXMLReader();
		
		PDMHandler pdmHandler = new PDMHandler();
		
		// 创建工厂类
		SAXParserFactory s = SAXParserFactory.newInstance();
		
		// 获取XMLReader :
		
		try
		{
			xmlReader = s.newSAXParser().getXMLReader();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 设置handler
		xmlReader.setContentHandler(pdmHandler);
		
		// 开始解析:
		
		try
		{
			
			File file = new File(fileName);
			InputSource input = new InputSource(new FileInputStream(file));
			
			input.setSystemId("file://" + file.getAbsolutePath());
			
			xmlReader.parse(input);
			List<Table> tables = pdmHandler.getTables();
			for (Table t : tables)
			{
				bo.getTableList().add(t);
				// System.out.println(t.getName());
				// for (Column c : t.listColumns)
				// {
				// System.out.println(c.getName());
				// }
			}
			Db db = pdmHandler.getDb();
			bo.setDbObj(db);
			// System.out.println(db.getDbmsCode());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bo;
		
	}
	
}
