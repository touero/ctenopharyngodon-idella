package util;
import com.alibaba.fastjson.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSONObject;




public class DownloadHtmlUtil {
    public List<Map<String,Object>> DownloadHtml(String url) throws ParserConfigurationException, XPathExpressionException, IOException{
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
            List<String> score_info = getSchoolScore(school_info);
            Map<String,Object> temp = new HashMap<>();
            List<String> major_info = getSchoolMajor(Url.SCHOOL_MAJOR_URL.value(school_id));
            String result = (school_id+","+belong+","+city_name+","+name+","+level_name+","+nature_name+
                    ","+province_name+","+type_name+","+dual_class_name+","+address+","+school_site+
                    ","+content+","+score_info+","+major_info);
            temp.put("result", result);
            rl.add(temp);
        }

        return rl;
    }

    private List<String> getSchoolScore(JSONObject school_info) {
        JSONObject school_score_info = school_info.getJSONObject("province_score_min");
        List<String> scoreResult = new ArrayList<String>();
        for (Map.Entry<String, Object> score_info : school_score_info.entrySet()){
            scoreResult.add(score_info.getKey()+":"+score_info.getValue());
        }
        return Collections.singletonList(scoreResult.toString());
    }

    private List<String> getSchoolMajor(String school_major_url) throws IOException {
        JSONObject school_major_data = getJsonObject(school_major_url).getJSONObject("data").getJSONObject("special_detail");
        List<String> majorResult = new ArrayList<String>();
        for (Map.Entry<String, Object> major_info : school_major_data.entrySet()) {
            if (Objects.equals(major_info.getValue().toString(), "")) {
                continue;
            }
            JSONArray major_items = JSONArray.parseArray(major_info.getValue().toString());
            for (int j=0; j<major_items.size(); j++) {
                String temp = major_items.getString(j);
                if (temp.contains("special_name")) {
                    JSONObject school_major_item = JSONObject.parseObject(temp);
                    String special_name = school_major_item.getString("special_name");
                    String limit_year = school_major_item.getString("limit_year");
                    String type_name = school_major_item.getString("type_name");
                    String level2_name = school_major_item.getString("level2_name");
                    majorResult.add(special_name + "," + limit_year + "," + type_name + "," + level2_name);
                }
            }
        }
        return Collections.singletonList(majorResult.toString());
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
