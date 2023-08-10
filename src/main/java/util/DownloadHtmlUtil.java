package util;
import com.alibaba.fastjson.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.alibaba.fastjson.JSONObject;


public class DownloadHtmlUtil {
    public List<Map<String, Object>> DownloadHtml(String url) throws ParserConfigurationException, XPathExpressionException, IOException, SQLException, ClassNotFoundException {
        List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
        Map<String,Object> Result = new HashMap<>();

        Class.forName(MySqlInfo.JDBC_DRIVER.value());
        java.sql.Connection conn = DriverManager.getConnection(
                MySqlInfo.DB_URL.value(),
                MySqlInfo.USER.value(),
                MySqlInfo.PASSWORD.value()
        );
        if (conn.isClosed()) {
            System.out.println("connect mysql is error");
        }
        Statement statement = conn.createStatement();

        String[] rulTemp = url.split(" ");
        String infoUrl = rulTemp[0];
        String majorUrl = rulTemp[1];
        JSONObject infoJson = getJsonObject(infoUrl).getJSONObject("data");
        String school_id = infoJson.getString("school_id");

        List<String> majorResult = getSchoolMajor(majorUrl, statement);
        List<String> scoreResult = getSchoolScore(infoJson, school_id, statement);
        List<String> infoResult = getSchoolInfo(infoJson, school_id, statement);

        Result.put("info", infoResult);
        Result.put("score",scoreResult);
        Result.put("major", majorResult);
        rl.add(Result);

        conn.close();
        return rl;
    }
    private List<String> getSchoolInfo(JSONObject infoJson, java.lang.String school_id, Statement statement) throws SQLException{
        List<String> infoResult = new ArrayList<>();
        String name = infoJson.getString("name");
        String belong = infoJson.getString("belong");
        String province_id = infoJson.getString("province_id");
        String province_name = infoJson.getString("province_name");
        String city_name = infoJson.getString("city_name");
        String level_name = infoJson.getString("level_name");
        String type_name = infoJson.getString("type_name");
        String school_type_name = infoJson.getString("school_type_name");
        String school_nature_name = infoJson.getString("school_type_name");

        String dual_class_name = infoJson.getString("dual_class_name");
        if (Objects.equals(dual_class_name, "")){
            dual_class_name = null;
        }

        String nature_name = infoJson.getString("nature_name");
        String site = infoJson.getString("site");
        String school_site = infoJson.getString("school_site");
        String address = infoJson.getString("address");
        String content = infoJson.getString("content");

        String sqlInfo = String.format("insert into info values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                school_id,name,belong,province_id,province_name,site,city_name,level_name,type_name,
                school_type_name,school_nature_name,dual_class_name,nature_name,school_site,
                address,content);

        String school_info_item = school_id+","+name + ','+belong + ','+province_id +',' +province_name+","+site + ','+city_name + ','+level_name +',' +type_name
                +","+school_type_name + ','+school_nature_name + ','+dual_class_name +',' +nature_name+ ','+school_site + ','+address +',' +content;

        infoResult.add(school_info_item);

        int state = statement.executeUpdate(sqlInfo);
        System.out.println("mysql state"+": "+state);

        return infoResult;

    }

    private List<String> getSchoolScore(JSONObject infoJson, String school_id, Statement statement) throws SQLException {
        List<String> scoreResult = new ArrayList<>();
        JSONObject province_score_min_json = infoJson.getJSONObject("province_score_min");
        for (Map.Entry<String, Object> province_score_min : province_score_min_json.entrySet()) {
            JSONObject province_score_json = JSONObject.parseObject(province_score_min.getValue().toString());
            String province_id = province_score_json.getString("province_id");
            String type = province_score_json.getString("type");
            String min = province_score_json.getString("min");
            String year = province_score_json.getString("year");

            String sqlScore = String.format("insert into score values('%s','%s','%s','%s','%s')",
                    school_id,province_id,type,min,year);

            String province_score_item = school_id+","+province_id + ','+type + ','+min +',' +year;
            scoreResult.add(province_score_item);

            int state = statement.executeUpdate(sqlScore);
            System.out.println("mysql state"+": "+state);

        }
        return scoreResult;
    }


    private List<String> getSchoolMajor(String majorUrl, Statement statement) throws IOException, SQLException {
        JSONObject school_major_json = getJsonObject(majorUrl).getJSONObject("data").getJSONObject("special_detail");
        List<String> scoreResult = new ArrayList<>();
        for (Map.Entry<String, Object> major_info : school_major_json.entrySet()) {
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
                    JSONObject school_major_item = JSONObject.parseObject(temp);
                    String school_id = school_major_item.getString("school_id");
                    String special_name = school_major_item.getString("special_name");
                    String type_name = school_major_item.getString("type_name");
                    String level3_name = school_major_item.getString("level3_name");
                    String level2_name = school_major_item.getString("level2_name");
                    String limit_year = school_major_item.getString("limit_year");
                    String major_item = school_id+','+special_name+','+type_name+
                            ','+ level3_name+","+level2_name+','+limit_year;

                    scoreResult.add(major_item);

                    String sqlMajor = String.format("insert into major values('%s','%s','%s','%s','%s','%s')",
                            school_id,special_name,type_name,level3_name,level2_name,limit_year);
                    int state = statement.executeUpdate(sqlMajor);
                    System.out.println("mysql state"+": "+state);

                }
            }
        }
        return scoreResult;
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
