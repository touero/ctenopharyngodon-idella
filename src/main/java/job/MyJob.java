package job;

import mapper.MyMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import reduce.MyReducer;


import java.io.IOException;

public class MyJob {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(MyJob.class);
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setMapOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setJobName("CrawlerTask");

        FileInputFormat.setInputPaths(job, new Path("/Input/url.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/scrapyOutput"));
//        FileInputFormat.setInputPaths(job,"D:/hadoop/input/url.txt");
//        FileOutputFormat.setOutputPath(job,new Path("D:/hadoop/output/Scrapydata"));


        boolean flag = job.waitForCompletion(true);
        System.exit(flag?0:1);
    }
}
