package com.cs.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UtilsPath
{
	static List<String> paths = new ArrayList<String>();
	
	public static synchronized List<String> GetPaths(String path)
	{
		paths = new ArrayList<String>();
	
		
		File f = new File(path);
		if (f != null && f.exists() && f.isDirectory())
		{
			_GetPaths(f);
		}
		return paths;
	}
	
	private static synchronized void _GetPaths(File f)
	{
		if (f != null)
		{
			File[] files = f.listFiles();
			if(files!=null)
			for( File ff :files){
				if(ff.exists() &&ff.isDirectory()){
					_GetPaths(ff);
				}
				else
				{
					paths.add(ff.getPath());
				}
			}
			
		} 
	}
}
