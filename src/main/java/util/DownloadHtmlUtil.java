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
        String schoolId = infoJson.getString("school_id");

        List<String> majorResult = getSchoolMajor(majorUrl, statement);
        List<String> scoreResult = getSchoolScore(infoJson, schoolId, statement);
        List<String> infoResult = getSchoolInfo(infoJson, schoolId, statement);

        Result.put("info", infoResult);
        Result.put("score",scoreResult);
        Result.put("major", majorResult);
        rl.add(Result);

        conn.close();
        return rl;
    }

    private List<String> getSchoolInfo(JSONObject infoJson, java.lang.String school_id, Statement statement) throws SQLException{
        List<String> infoResult = new ArrayList<>();
        String schoolName = infoJson.getString("name");
        String belong = infoJson.getString("belong");
        String provinceId = infoJson.getString("province_id");
        String provinceName = infoJson.getString("province_name");
        String cityName = infoJson.getString("city_name");
        String levelName = infoJson.getString("level_name");
        String typeName = infoJson.getString("type_name");
        String schoolTypeName = infoJson.getString("school_type_name");
        String schoolNatureName = infoJson.getString("school_type_name");

        String dualClassName = infoJson.getString("dual_class_name");
        if (Objects.equals(dualClassName, "")){
            dualClassName = null;
        }

        String natureName = infoJson.getString("nature_name");
        String site = infoJson.getString("site");
        String schoolSite = infoJson.getString("school_site");
        String address = infoJson.getString("address");
        String content = infoJson.getString("content");

        String sqlInfo = String.format("insert into info values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                school_id, schoolName,belong, provinceId,provinceName,site,cityName,levelName,typeName,
                schoolTypeName,schoolNatureName,dualClassName,natureName,schoolSite,
                address,content);

        String school_info_item = school_id+","+ schoolName + ','+belong + ','+ provinceId +',' +provinceName+","+site + ','+cityName + ','+levelName +',' +typeName
                +","+schoolTypeName + ','+schoolNatureName + ','+dualClassName +',' +natureName+ ','+schoolSite + ','+address +',' +content;

        infoResult.add(school_info_item);

        int state = statement.executeUpdate(sqlInfo);
        System.out.println("mysql state"+": "+state);

        return infoResult;

    }

    private List<String> getSchoolScore(JSONObject infoJson, String schoolId, Statement statement) throws SQLException {
        List<String> scoreResult = new ArrayList<>();
        JSONObject provinceScoreMinJson = infoJson.getJSONObject("province_score_min");
        for (Map.Entry<String, Object> province_score_min : provinceScoreMinJson.entrySet()) {
            JSONObject provinceScoreJson = JSONObject.parseObject(province_score_min.getValue().toString());
            String provinceId = provinceScoreJson.getString("province_id");
            String type = provinceScoreJson.getString("type");
            String min = provinceScoreJson.getString("min");
            String year = provinceScoreJson.getString("year");

            String sqlScore = String.format("insert into score values('%s','%s','%s','%s','%s')",
                    schoolId,provinceId,type,min,year);

            String provinceScoreItem = schoolId+","+provinceId + ','+type + ','+min +',' +year;
            scoreResult.add(provinceScoreItem);

            int state = statement.executeUpdate(sqlScore);
            System.out.println("mysql state"+": "+state);

        }
        return scoreResult;
    }

    private List<String> getSchoolMajor(String majorUrl, Statement statement) throws IOException, SQLException {
        JSONObject schoolMajorJson = getJsonObject(majorUrl).getJSONObject("data").getJSONObject("special_detail");
        List<String> scoreResult = new ArrayList<>();
        for (Map.Entry<String, Object> majorInfo : schoolMajorJson.entrySet()) {
            if (Objects.equals(majorInfo.getValue().toString(), "[]")) {
                continue;
            }
            if (Objects.equals(majorInfo.getValue().toString(), "")) {
                continue;
            }
            JSONArray major_items = JSONArray.parseArray(majorInfo.getValue().toString());
            for (int j = 0; j < major_items.size(); j++) {
                String temp = major_items.getString(j);
                if (temp.contains("special_name")) {
                    JSONObject schoolMajorItem = JSONObject.parseObject(temp);
                    String schoolId = schoolMajorItem.getString("school_id");
                    String specialName = schoolMajorItem.getString("special_name");
                    String typeName = schoolMajorItem.getString("type_name");
                    String level3Name = schoolMajorItem.getString("level3_name");
                    String level2Name = schoolMajorItem.getString("level2_name");
                    String limitYear = schoolMajorItem.getString("limit_year");
                    String majorItem = schoolId+','+specialName+','+typeName+
                            ','+level3Name+","+level2Name+','+limitYear;

                    scoreResult.add(majorItem);

                    String sqlMajor = String.format("insert into major values('%s','%s','%s','%s','%s','%s')",
                            schoolId, specialName, typeName, level3Name, level2Name, limitYear);
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
