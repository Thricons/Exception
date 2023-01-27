package cn.exception.utils.map;

import cn.exception.utils.WebUtil;
import cn.exception.utils.resources.Base64Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class SRGMapEncode {
    /**读取SRG Mapping**/
    public static String getMCPMapField(String s) throws IOException {
        //目前只有MCP Map
        InputStream is = Base64Resources.getResources(WebUtil.get(""), "0x00178gdsj", false);
        //初始化
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //开始read
        String line = null;
        while ((line = br.readLine()) != null){
            if(line.contains(s) && line.startsWith("FD: ")){
                //进行一个格式化
                String[] sb = line.split(" ");
                //分为3部分,只要中间那部分
                //再分割一次，最后的那个就可能是对应的field
                sb = sb[2].split("/");
                //最后判断
                if(sb[sb.length].equals(s)) {
                    return sb[sb.length];
                }else {
                    break;
                }
            }else {
                break;
            }
        }
        return null;
    }
}
