package util;
import com.alibaba.fastjson.JSONArray;
import org.jetbrains.annotations.NotNull;
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
        JSONArray items = jsonObject.getJSONObject("data").getJSONArray("item");
        for (int i = 0; i < items.size(); i++) {
            String item = items.getString(i);
            JSONObject json_item = JSONObject.parseObject(item);
            String school_id = json_item.getString("school_id");
            String belong = json_item.getString("belong");
            String city_name = json_item.getString("city_name");
            String name = json_item.getString("name");
            String level_name = json_item.getString("level_name");
            String nature_name = json_item.getString("nature_name");
            String province_name = json_item.getString("province_name");
            String type_name = json_item.getString("type_name");
            String dual_class_name = json_item.getString("dual_class_name");
            if (dual_class_name.length()==0){
                dual_class_name = null;
            }
            JSONObject school_info = getJsonObject(Url.SCHOOL_INFO_URL.value(school_id)).getJSONObject("data");
            String address = school_info.getString("address");
            String school_site = school_info.getString("school_site");
            String content = school_info.getString("content");
            getSchoolMajor(Url.SCHOOL_MAJOR_URL.value(school_id));
            getSchoolScore(Url.SCHOOL_SCORE_URL.value(school_id));



        }




        return rl;
    }

    private void getSchoolScore(String url) throws IOException {
        JSONObject school_score = getJsonObject(url);
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
        JSONArray school_major_items = school_major_data.getJSONObject("data")
                .getJSONObject("special_detail").
                getJSONArray("2");
        for (int j = 0 ; j < school_major_items.size(); j++){
            String temp_major = school_major_items.getString(j);
            JSONObject school_major_item = JSONObject.parseObject(temp_major);
            String special_name = school_major_item.getString("special_name");
            String limit_year = school_major_item.getString("limit_year");
            String type_name = school_major_item.getString("type_name");
            String level2_name = school_major_item.getString("level2_name");
            // todo return type


        }


    }

    private @NotNull JSONObject getJsonObject(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .header("Accept", Header.ACCEPT.value())
                .header("Accept-Encoding", Header.ACCEPT_ENCODING.value())
                .header("Accept-Language",Header.ACCEPT_LANGUAGE.value())
                .header("Content-Type", Header.CONTENT_TYPE.value())
                .header("User-Agent",Header.USER_AGENT.value())
                .header("Cookie",Header.COOKIE.value())
                .timeout(10000).ignoreContentType(true).execute();

        String body = res.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        System.out.println(jsonObject.getString("message") + ":" +url);
        return jsonObject;
    }

}
