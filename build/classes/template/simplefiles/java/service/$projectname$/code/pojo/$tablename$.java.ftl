package ${bo.projectName?lower_case}.code.pojo;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import baselib.baseclass.BasePojo;
//增加字段注释

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModelProperty;
///////////////////////////////////////////////////////////
// ObjectID : ${bo.lastHandleTable.objectid}
//${bo.lastHandleTable.name}
//${bo.lastHandleTable.comment}

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ${bo.lastHandleTable.code}  extends BasePojo implements java.io.Serializable {
  
    @ApiModelProperty(value = "主键列表（公共字段）")
    private List<Object> keylist;
		
	 
	 	public List<Object> getKeylist() {
			return keylist;
		}
		public void setKeylist(List<Object> keylist) {
			this.keylist = keylist;
		}
    
    
	protected static final long serialVersionUID = ${bo.lastHandleTable.radomLong}l;
	 <#list bo.lastHandleTable.listColumns as c>
	 	/*
		 ${c_index}.${c.name}
	 	 备注:${c.comment}
		*/
	 	@ApiModelProperty(value = "${c.name}")
	 	private ${c.dataType} ${c.code?uncap_first} ;
	 	public ${c.dataType} get${c.code?cap_first} ()
	 	{
	 		return this.${c.code?uncap_first};
	 	}
	 	public void set${c.code?cap_first} (${c.dataType} value)
	 	{
	 		this.${c.code?uncap_first}	=	value;
	 		if(value!=null)
			this.isAllPropNull=false;
	 	}		 
	 </#list>
	public void setSession(Map map) {
		// TODO Auto-generated method stub
	}
}
 
 
 
 

 
		 

 


