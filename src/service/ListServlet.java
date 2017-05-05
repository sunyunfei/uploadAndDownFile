package service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syf on 2017/5/5.
 */
public class ListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取上传文件的目录
        String uploadFilePath =  this.getServletContext().getRealPath("/WEB-INF/upload");
        //存储要下载的文件名
        Map<String,String> fileNameMap = new HashMap<>();
        //递归遍历filepath目录下的所有文件和目录，将文件的文件名存储到map集合中

        //File既可以代表一个文件也可以代表一个目录
        listfile(new File(uploadFilePath),fileNameMap);
        //将Map集合发送到listfile.jsp页面进行显示
        req.setAttribute("fileNameMap",fileNameMap);
        req.getRequestDispatcher("/WEB-INF/listfile.jsp").forward(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req,resp);
    }

    /**
     * 递归遍历指定目录下的所有文件
     * @param file
     * @param map
     */
    public void listfile(File file,Map<String,String>map){

        //如果file代表的不是一个文件，而是一个目录
        if (!file.isFile()){

            //列出该目录下的所有文件和目录
            File files[] = file.listFiles();
            //遍历
            for (File f :
                    files) {
                //递归
                listfile(f,map);
            }
        }else {

            /**
            * 处理文件名，上传后的文件是以uuid_文件名的形式去重新命名的，去除文件名的uuid_部分
              file.getName().indexOf("_")检索字符串中第一次出现"_"字符的位置，如果文件名类似于：9349249849-88343-8344_阿_凡_达.avi
              那么file.getName().substring(file.getName().indexOf("_")+1)处理之后就可以得到阿_凡_达.avi部分
            */
            String realName = file.getName().substring(file.getName().indexOf("_")+1);
            //file.getName()得到的是文件的原始名称，这个名称是唯一的，因此可以作为key，realName是处理过后的名称，有可能会重复
            map.put(file.getName(),realName);
        }
    }
}
