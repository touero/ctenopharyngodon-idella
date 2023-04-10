package util;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadHtmlUtil {
    public List<Map<String,Object>> DownloadHtml(String url) throws ParserConfigurationException, XPathExpressionException {
        List<Map<String,Object>> rl = new ArrayList<Map<String,Object>>();
        Connection connection = Jsoup.connect(url).timeout(3000);
        String html = null;
        try {
            html = connection.get().body().html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(html==null) return rl;
        System.out.println(html);

        return rl;
    }
}
//        // xpath
//        String titleXpath = "//h1[@class='entry-title']/text()";
//        String voteXpath = "//div/strong[1]/text()";
//        String scoreXpath = "//strong[2]/text()";
//        String introduceXpath = "//div[@class='entry-content']/p[1]";
//        String urlXpath = "//p[2]/a/text()";
//        String timeXpath = "//time[@class='entry-date']/text()";
//        String detailXpath = "//footer[@class='entry-meta']/text()";
//
//        titleResult = PaserXpath(dom,titleXpath);
//        voteResult = PaserXpath(dom,voteXpath);
//        scoreResult = PaserXpath(dom,scoreXpath);
//        introduceResult = PaserXpath(dom,introduceXpath);
//        urlResult = PaserXpath(dom,urlXpath);
//        if (urlResult.toString().length()>2){
//            urlResult = PaserXpath(dom,urlXpath);
//        }else{
//            urlResult.add("invalid");
//        }
//        timeResult = PaserXpath(dom,timeXpath);
//        detailResult = PaserXpath(dom,detailXpath);


//        for (int i=0;i< titleResult.size();i++){
//            Map<String,Object> temp = new HashMap<>();
//            temp.put("detail",detailResult.get(i).replaceAll("\n",""));
//            temp.put("time",timeResult.get(i));
//            temp.put("url",urlResult.get(i));
//            temp.put("introduce",introduceResult.get(i));
//            temp.put("score",scoreResult.get(i));
//            temp.put("vote",voteResult.get(i));
//            temp.put("title",titleResult.get(i));
//            rl.add(temp);
//        }
//
//        return rl;
//    }

//    public List<String> PaserXpath(Document dom, String xph) throws XPathExpressionException {
//
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        List<String> result = new ArrayList<>();
//        Object etContent = xPath.evaluate(xph,dom, XPathConstants.NODESET);
//        NodeList nodelist = (NodeList) etContent;
//        for(int i=0;i<nodelist.getLength();i++){
//            Node node = nodelist.item(i);
//            String val = node.getNodeValue()==null?node.getTextContent():node.getNodeValue();
//            result.add(val);
//        }
//        return result;
//    }
//}
