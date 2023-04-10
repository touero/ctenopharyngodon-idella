package mapper;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import util.DownloadHtmlUtil;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyMapper extends Mapper<Object, Text, Text,NullWritable> {
    DownloadHtmlUtil downloadHtmlUtil = new DownloadHtmlUtil();
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String url = value.toString();
        List<Map<String,Object>> crawler = new ArrayList<Map<String,Object>>();
        try {
            crawler = downloadHtmlUtil.DownloadHtml(url);
            System.out.println(crawler);
        } catch (ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }
        Text record = new Text();
        if (crawler.size()==0){
            record.set("");
            context.write(record,NullWritable.get());

        }else{
            record.set(crawler.toString());
            context.write(record,NullWritable.get());
        }

    }
}

