package service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by syf on 2017/5/5.
 */
public class DownServlet extends RootServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //得到要下载的文件名
        String fileName = req.getParameter("filename");
        fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
        //上传的文件都是保存在/WEB-INF/upload目录下的子目录当中
        String fileSaveRootPath = this.getServletContext().getRealPath("/WEB-INF/upload");
        //通过文件名找出文件的所在目录
        String path = makeOrGetPath(fileName,fileSaveRootPath);
        //得到要下载的文件
        File file = new File(path + "//" + fileName);
        //如果文件不存在
        if (!file.exists()){

            req.setAttribute("message","您要下载的资源已被删除");
            req.getRequestDispatcher("/WEB-INF/message.jsp").forward(req,resp);
            return;
        }
        //处理文件名
        String realname = fileName.substring(fileName.indexOf("_")+1);
        //设置响应头，控制浏览器下载该文件
        resp.setHeader("content-disposition","attachment:filename="+ URLEncoder.encode(realname,"UTF-8"));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(path + "//" + fileName);
        //创建输出流
        OutputStream out = resp.getOutputStream();
        //创建缓冲区
        byte buffer[] = new byte[1024];
        int len = 0;
        //循环输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0){

            //输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer,0,len);
        }

        //关闭文件输入流
        in.close();
        out.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
