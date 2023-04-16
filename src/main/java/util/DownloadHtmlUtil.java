package util;
import com.alibaba.fastjson.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;




public class DownloadHtmlUtil {
    public List<Map<String,Object>> DownloadHtml(String url) throws ParserConfigurationException, XPathExpressionException, IOException, InterruptedException {
        List<Map<String,Object>> rl = new ArrayList<Map<String,Object>>();


        JSONObject jsonObject = getJsonObject(url);
        String state = jsonObject.get("message").toString();
        System.out.println(state + ":" + url);
        JSONArray items = jsonObject.getJSONObject("data").getJSONArray("item");
        for (int i = 0; i < items.size(); i++) {
            String item = items.getString(i);
            JSONObject json_item = JSONObject.parseObject(item);
            String school_id = json_item.get("school_id").toString();
            String belong = json_item.get("belong").toString();
            String city_name = json_item.get("city_name").toString();
            String name = json_item.get("name").toString();
            String level_name = json_item.get("level_name").toString();
            String nature_name = json_item.get("nature_name").toString();
            String province_name = json_item.get("province_name").toString();
            String type_name = json_item.get("type_name").toString();
            String dual_class_name = json_item.get("dual_class_name").toString();
            String base_url = "https://static-data.gaokao.cn/www/2.0";
            String school_info_url = base_url + ("/school/school_id/info.json").replace("school_id", school_id);
            JSONObject school_info = getJsonObject(school_info_url);
            String address = school_info.get("address").toString();
            String school_site = school_info.get("school_site").toString();
            String content = school_info.get("content").toString();
            String school_major_url = base_url + ("/school/school_id/pc_special.json").replace("shool_id",school_id);
            // fixme return type
            getSchoolMajor(school_major_url);


        }




        return rl;
    }

    private void getSchoolMajor(String school_major_url) throws IOException {
        JSONObject school_major_items = getJsonObject(school_major_url);
        // todo get major

    }

    private JSONObject getJsonObject(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .header("Cookie","Hm_lvt_0a962de82782bad64c32994d6c9b06d3=1681049933; gr_user_id=a762e1b4-6095-4638-9b52-b40b0a4a1194")
                .timeout(10000).ignoreContentType(true).execute();

        String body = res.body();
        return JSONObject.parseObject(body);
    }

}
