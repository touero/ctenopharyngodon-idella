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
            String school_info_url = base_url + ("/school/school_id/info.json")
                    .replace("school_id", school_id);
            JSONObject school_info = getJsonObject(school_info_url);
            System.out.println(school_info.get("message").toString() + school_info_url);
            String address = school_info.get("address").toString();
            String school_site = school_info.get("school_site").toString();
            String content = school_info.get("content").toString();
            String school_major_url = base_url + ("/school/school_id/pc_special.json")
                    .replace("school_id",school_id);
            getSchoolMajor(school_major_url);
            getSchoolScore(school_id);


        }




        return rl;
    }

    private void getSchoolScore(String school_id) throws IOException {
        String score_url = "https://api.eol.cn/web/api/" +
                "?e_sort=zslx_rank,min&e_sorttype=desc," +
                "desc&local_province_id&local_type_id=1&" +
                "pag1&school_id=args&size&" +
                "uri=apidata/api/gk/score/province&" +
                "year=2022&signsafe=61631c0843e48e52f2b238637cf68f65"
                        .replace("args", school_id);
        JSONObject school_score = getJsonObject(score_url);
        JSONArray items = school_score.getJSONObject("data").getJSONArray("item");
        for (int i = 0; i < items.size(); i++) {
            String item = items.getString(i);
            JSONObject school_score_item = JSONObject.parseObject(item);
            // todo
        }
    }

    private void getSchoolMajor(String school_major_url) throws IOException {
        List<String> major = new ArrayList<String>();
        JSONObject school_major_data = getJsonObject(school_major_url);
        System.out.println(school_major_data.get("message").toString() + school_major_url);
        JSONArray school_major_items = school_major_data.getJSONObject("data")
                .getJSONObject("special_detail").
                getJSONArray("2");
        for (int j = 0 ; j < school_major_items.size(); j++){
            String temp_major = school_major_items.getString(j);
            JSONObject school_major_item = JSONObject.parseObject(temp_major);
            String special_name = school_major_item.get("special_name").toString();
            String limit_year = school_major_item.get("limit_year").toString();
            String type_name = school_major_item.get("type_name").toString();
            String level2_name = school_major_item.get("level2_name").toString();
            // todo return type


        }


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
