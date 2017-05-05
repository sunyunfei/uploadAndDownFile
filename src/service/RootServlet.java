package service;

import javax.servlet.http.HttpServlet;
import java.io.File;

/**
 * Created by syf on 2017/5/5.
 */
public class RootServlet extends HttpServlet {

    /**
     * 防止一个目录下面出现太多的文件，使用hash算法打散存储
     * @param fileName
     * @param savePath
     * @return
     */
    public String makeOrGetPath(String fileName,String savePath){

        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashCode = fileName.hashCode();
        int dir1 = hashCode&0xf;//0-15
        int dir2 = (hashCode&0xf0)>>4;//0-15
        //构造新的保存目录
        String dir = savePath + "//"+dir1 + "//" + dir2;
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }

        System.out.println("文件名:"+fileName + "    文件路径:"+dir);
        return dir;
    }
}
