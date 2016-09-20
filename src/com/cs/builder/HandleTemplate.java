package com.cs.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.cs.objects.BuildObj;
import com.cs.objects.Table;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class HandleTemplate {

	static String SimplePath = "/template/simplefiles";

	static Locale locale = Locale.ENGLISH;

	// 获取Group模板
	public List<String> GetTemplates(String path) {
		List<String> results = new ArrayList<String>();
		URL url = this.getClass().getResource(path);
		if (url != null) {
			path = this.getClass().getResource(path).getPath(); // 相对路径转绝对路径

			results = UtilsPath.GetPaths(path);
		}

		return results;
	}

	// 处理全部

	public void BuildAll(BuildObj bo) {
		BuildSimpleObj(bo);
	}

	// 处理实体
	private void BuildSimpleObj(BuildObj bo) {
		List<String> paths = GetTemplates(SimplePath);
		for (String path : paths) {
			if (path.toLowerCase().indexOf(".ftl") > 0) BuildFile(path, bo);
		}

	}

	protected void BuildFile(String filePath, BuildObj bo) {

		// freemarker支持多语言国际化，只要把模板名称按照java资源文件的写法就可以了,也就是name_语言_国家地区.ftl
		// 如果找不到对应的语言，就会用默认语言的模板。

		String p = this.getClass().getResource(SimplePath).getPath();
		filePath = filePath.replace(p, "");
		Configuration freemarkerCfg = new Configuration();
		freemarkerCfg.setClassForTemplateLoading(this.getClass(), SimplePath);
		freemarkerCfg.setEncoding(Locale.getDefault(), "UTF-8");
		Template template;
		Locale.setDefault(Locale.ENGLISH);
		try {

			template = freemarkerCfg.getTemplate(filePath, locale);
			String outPath = bo.getOutPutPath() + "/" + bo.getProjectName();

			template.setEncoding("UTF-8");
			HashMap<String, BuildObj> root = new HashMap<String, BuildObj>();
			root.put("bo", bo);
			for (Table t : bo.getTableList()) {

				bo.setLastHandleTable(t);
				if ("".equals(bo.getLastHandleTable().getCode()) || bo.getLastHandleTable() == null
						|| bo.getLastHandleTable().getCode() == null) continue;// 空表名跳过

				String fileName = filePath;// filePath.substring(lastPathSpPos);
				fileName = fileName.replace(".ftl", "");
				fileName = fileName.replace("$projectname$", bo.getProjectName().toLowerCase());
				fileName = fileName.replace("$tablename$", bo.getLastHandleTable().getCode());

				// System.out.println(fileName+"----bb-------"+bo.getLastHandleTable().getCode());
				// 将 ${projectName} 替换成
				// 将 $tableName$ 替换成表名

				File f = new File(outPath + fileName);
				if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
//				java.io.FileWriter writer = new FileWriter(f);
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
				template.process(root, writer);
				writer.flush();
				writer.close();

				f = null;
				writer = null;// 释放
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}