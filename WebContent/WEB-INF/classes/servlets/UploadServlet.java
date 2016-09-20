package servlets;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs.utils.DeleteDirectory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.cs.builder.HandleTemplate;
import com.cs.builder.IDataRead;
import com.cs.builder.PDMRead;
import com.cs.objects.BuildObj;


/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 打印所有参数
            // Handle(request, response);
        } catch (Exception e) {
        }
    }

    private static void BuildPathAndFile(File f)
            throws IOException {
        f.getParentFile().mkdirs();
        if (!f.exists()) f.createNewFile();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 打印所有参数
            Handle(request, response);
        } catch (Exception e) {
        }
    }

    // 上传 pdm文件 生成代码

    public resultCls UploadFile(HttpServletRequest request)
            throws UnsupportedEncodingException {
        resultCls rslt = new resultCls();

        request.setCharacterEncoding("UTF-8");

        // 文件上传处理工厂
        FileItemFactory factory = new DiskFileItemFactory();

        // 创建文件上传处理器
        ServletFileUpload upload = new ServletFileUpload(factory);

        // 开始解析请求信息
        List<?> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (items == null) return rslt;
        // 对所有请求信息进行判断
        Iterator<?> iter = items.iterator();
        FileItem itemFile = null;
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            // 信息为普通的格式
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                String value = item.getString();
                request.setAttribute(fieldName, value);
            } else {
                itemFile = item;
            }
        }

        if (itemFile != null) {
            String projectName = "temp";
            if (request.getAttribute("projectName") != null && !"".equals(request.getAttribute("projectName"))) {
                projectName = (String) request.getAttribute("projectName");
            } else {
                rslt.reason = "projectName 不能为空！";
                return rslt;
            }
            String fileName = itemFile.getName();
            int pos = fileName.lastIndexOf(".");
            String extFileName = fileName.substring(pos);
            if (extFileName.toLowerCase().indexOf(".pdm") < 0) {
                rslt.reason = "后缀名必须是pdm!";
                return rslt;
            }

            String cerFilePath = String.format("%supload/%s%s", request.getSession().getServletContext().getRealPath("/"),
                    projectName, extFileName);
            File f = new File(cerFilePath);
            // 将文件写入

            try {
                BuildPathAndFile(f);
                itemFile.write(f);
                rslt.filepath = cerFilePath;
                rslt.projectName = projectName;
                rslt.result = true;
                return rslt;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            rslt.reason = "上传文件不能为空!";
        }
        // 保存其他数据。
        return rslt;

    }

    class resultCls {

        public String filepath = "";
        public String projectName = "";
        public boolean result = false;// 结果
        public String reason = "";

    }

    protected void Handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        resultCls rst = UploadFile(request);
        String data = rst.reason;
        if (rst.result) {
            // 获取上传文件名
            HandleTemplate ht = new HandleTemplate();

            IDataRead pdmRead = new PDMRead();
            BuildObj bo = pdmRead.ReadData(rst.filepath);
            bo.setProjectName(rst.projectName);
            bo.setOutPutPath(request.getSession().getServletContext().getRealPath("/")+"upload/" + rst.projectName + "/");
            this.BuildPathAndFile(new File(bo.getOutPutPath() + "/note.text"));
            ht.BuildAll(bo);

            //	ZipPro z = new ZipPro(String.format("/tmp/%s.zip", rst.projectName));
            String zipFilename = String.format(request.getSession().getServletContext().getRealPath("/")+"upload/%s.zip",
                    rst.projectName);
            ZipPro z = new ZipPro(zipFilename);
            z.compress(bo.getOutPutPath());
            File file = new File(bo.getOutPutPath());
            DeleteDirectory.deleteDir(file);

            // 写明要下载的文件的大小
//            response.setContentLength((int) file.length());
//            response.setHeader("Content-Disposition", "attachment;filename="
//                    + rst.projectName+".zip");// 设置在下载框默认显示的文件名
//            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
//            // 读出文件到response
//            // 这里是先需要把要把文件内容先读到缓冲区
//            // 再把缓冲区的内容写到response的输出流供用户下载
//            FileInputStream fileInputStream = new FileInputStream(zipFilename);
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(
//                    fileInputStream);
//            byte[] b = new byte[bufferedInputStream.available()];
//            bufferedInputStream.read(b);
//            OutputStream outputStream = response.getOutputStream();
//            outputStream.write(b);
//            // 人走带门
//            bufferedInputStream.close();
//            outputStream.flush();
//            outputStream.close();
//            return;

//            String downloadFile = String.format("/bd/%s.zip", rst.projectName);
            data = String.format("[<a href='/upload/%s.zip'>%s.zip</a>]", rst.projectName, rst.projectName );
        } else {

        }
        this.WriteStream(data, request, response);
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            System.out.println("name:" + name + " value:" + value);
            returnMap.put(name, value);
        }
        return returnMap;
    }

    // 输出内容
    protected void WriteStream(String outMsg, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().append(outMsg);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
