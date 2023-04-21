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
            JSONObject resultJson = new JSONObject();
            String item = items.getString(i);
            JSONObject json_item = JSONObject.parseObject(item);
            String school_id = json_item.getString("school_id");
            resultJson.put("school_id",school_id);
            resultJson.put("belong",json_item.getString("belong"));
            resultJson.put("city_name",json_item.getString("city_name"));
            resultJson.put("name",json_item.getString("name"));
            resultJson.put("level_name",json_item.getString("level_name"));
            resultJson.put("nature_name",json_item.getString("nature_name"));
            resultJson.put("province_name",json_item.getString("province_name"));
            resultJson.put("type_name",json_item.getString("type_name"));
            resultJson.put("dual_class_name",json_item.getString("dual_class_name"));
            JSONObject school_info = getJsonObject(Url.SCHOOL_INFO_URL.value(school_id)).getJSONObject("data");
            resultJson.put("address",school_info.getString("address"));
            resultJson.put("school_site",school_info.getString("school_site"));
            resultJson.put("content",school_info.getString("content"));
            JSONObject scoreResult = getSchoolScore(school_info);
            resultJson.put("school_score",scoreResult);
            List<String> majorResult = getSchoolMajor(Url.SCHOOL_MAJOR_URL.value(school_id));
            resultJson.put("school_major", majorResult);
            rl.add(resultJson);
        }
        return rl;
    }


    private JSONObject getSchoolScore(JSONObject school_info) {
        JSONObject school_score_info = school_info.getJSONObject("province_score_min");
        JSONObject scoreResult = new JSONObject();
        scoreResult.putAll(school_score_info);
        return scoreResult;
    }

    private List<String> getSchoolMajor(String school_major_url) throws IOException {
        JSONObject school_major_data = getJsonObject(school_major_url).getJSONObject("data").getJSONObject("special_detail");
        JSONObject majorResult = null;
        List<String> majorList = new ArrayList<String>();
        for (Map.Entry<String, Object> major_info : school_major_data.entrySet()) {
            if (Objects.equals(major_info.getValue().toString(), "[]")) {
                continue;
            }
            if (Objects.equals(major_info.getValue().toString(), "")) {
                continue;
            }
            JSONArray major_items = JSONArray.parseArray(major_info.getValue().toString());
            for (int j = 0; j < major_items.size(); j++) {
                String temp = major_items.getString(j);

                if (temp.contains("special_name")) {
                    majorResult = new JSONObject();
                    JSONObject school_major_item = JSONObject.parseObject(temp);
                    majorResult.put("special_name", school_major_item.getString("special_name"));
                    majorResult.put("limit_year", school_major_item.getString("limit_year"));
                    majorResult.put("type_name", school_major_item.getString("type_name"));
                    majorResult.put("level2_name", school_major_item.getString("level2_name"));
                    majorList.add(String.valueOf(majorResult));
                }
            }
        }
        return majorList;
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
