package com.cs.objects;

import java.util.*;
import java.text.*;

public class EnumClass {

	public enum Default {
		DATE_TIME {
			// 实现抽象方法
			String getValue() {
				return DateFormat.getDateInstance().format(new Date());
			}
		},
		CLASSPATH {
			String getValue() {
				return System.getenv("CLASSPATH");
			}
		},
		VERSION {
			String getValue() {
				return System.getProperty("java.version");
			}
		};
		// 定义一个抽象方法
		abstract String getValue();
	}

	public enum State {
		ON(100), OFF(200), UNKOWN(300);

		Integer value = 100;  
		private State(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	};
	
	public enum DataBase {
		UNKOWN
		{  
			public Boolean IsEnumStr(String enumStr) { 
				return !MSSQL.IsEnumStr(enumStr)&&
				!SYBASE.IsEnumStr(enumStr)&&
				!ORACLE.IsEnumStr(enumStr); 
			}
			
		},
		MSSQL {
			public Boolean IsEnumStr(String enumStr) {
				return this.GetEnumByStr(enumStr).equals(MSSQL);
			}
		},
		SYBASE {
			public Boolean IsEnumStr(String enumStr) {
				return this.GetEnumByStr(enumStr).equals(SYBASE);
			}
		},
		ORACLE {
			public Boolean IsEnumStr(String enumStr) {
				return this.GetEnumByStr(enumStr).equals(ORACLE);
			}
		},
		
		MYSQL {
			public Boolean IsEnumStr(String enumStr) {
				return this.GetEnumByStr(enumStr).equals(MYSQL);
			}
		}
		;

		abstract Boolean IsEnumStr(String enumStr);

		public DataBase GetEnumByStr(String str) {
			DataBase result = DataBase.UNKOWN;
			try {
				result = DataBase.valueOf(str.toUpperCase());
			} catch (Exception e) {
				 
			}
			return result;
		}
	};

}