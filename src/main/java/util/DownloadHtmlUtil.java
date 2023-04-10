package util;
import com.alibaba.fastjson.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

public class DownloadHtmlUtil {
    public List<Map<String,Object>> DownloadHtml(String url) throws ParserConfigurationException, XPathExpressionException, IOException {
        List<Map<String,Object>> rl = new ArrayList<Map<String,Object>>();
        Connection.Response res = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .timeout(10000).ignoreContentType(true).execute();//.get();

        String body = res.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String state = jsonObject.get("message").toString();
        System.out.println(state + ":" + url);
        JSONArray items = jsonObject.getJSONObject("data").getJSONArray("item");
        for (int i = 0; i < items.size(); i++){
            String item = items.getString(i);
            JSONObject json_item = JSONObject.parseObject(item);
            Map<String, Object> temp = new HashMap<>();
            temp.put("school_name", json_item.getString("name"));
            temp.put("location", json_item.getString("province_name"));
            temp.put("level_name", json_item.getString("level_name"));
            temp.put("school_id", json_item.getString("school_id"));
            rl.add(temp);
        }

        return rl;
    }
}
