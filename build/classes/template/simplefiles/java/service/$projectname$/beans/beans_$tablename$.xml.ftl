<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
	<!-- DAO -->
	<bean id="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_dao_bean" class="${bo.projectName?lower_case}.code.dao.${bo.lastHandleTable.code}_DAO_Impl">
		<property name="sqlMapClient">
			<ref bean="sqlmapclient_bean" />
		</property>
		<property name="dataSource">
			<ref bean="${bo.dbType!""}_datasource_bean" />
		</property>
	</bean>
		
	<!-- BO -->
	<bean id="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_bo_bean" class="${bo.projectName?lower_case}.code.bo.${bo.lastHandleTable.code}_BO_Impl">
		<property name="dao">
			<ref bean="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_dao_bean" />
		</property>
	</bean> 
	<!-- 为BO事务代理 -->
	<bean id="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_proxy_bean" parent="transaction_proxy_bean">
	  	<property name="transactionManager">
	  		<ref bean="${bo.dbType}_transactionManager_bean" />
	  	</property>
	  	<property name="target">
		  	<ref bean="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_bo_bean"/>
	  	</property>
	  	 
  	</bean>
  	
  	
	<!-- Service -->
	<bean id="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_service_bean" class="${bo.projectName?lower_case}.code.service.${bo.lastHandleTable.code}_Service_Impl">
		<property name="bo">
			<ref bean="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_proxy_bean" />
		</property>
		<property name="dao">
			<ref bean="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_dao_bean" />
		</property>
	</bean>
	
	<!-- 对外接口 -->
	<bean id="i_${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_service"
		class="org.springframework.remoting.caucho.HessianServiceExporter">
		<property name="service">
			<ref bean="${bo.projectName?lower_case}_${bo.lastHandleTable.code?lower_case}_service_bean" />
		</property>
		<property name="serviceInterface" value="${bo.projectName?lower_case}.code.service.IService">
		</property>
	</bean>
	
</beans>