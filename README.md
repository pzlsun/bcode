# bcode
基于FreeMarker 模板引擎的代码生成工具,以每一个数据库表为单位,生成各种语言的代码
输入源是pdm文件,推荐使用PowerDesigner v16.5,  低版本生成的pdm文件可能会发生pdm文件解析错误

1.编写freemarker 代码模板,工程中有模板示例,src/template/ , 并有freemarker语法简介.关于freemarker,请自行查阅资料

2. Servlet工程,可以使用Tomcat部署

3.部署运行后,访问http://localhost:8080/buildcode, 上传pdm文件,填写自己的工程名,然后提交

4.网页上会显示生成的代码的zip下载链接,下载解压后可使用

