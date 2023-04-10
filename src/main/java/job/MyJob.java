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



import java.io.File;
import java.io.IOException;

import static org.apache.hadoop.hdfs.server.common.Storage.deleteDir;

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

        job.setJobName("CrawlerSchoolAll");

        Boolean IsInHadoop = Boolean.FALSE;

        if (IsInHadoop){
        FileInputFormat.setInputPaths(job, new Path("/Input/url.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/scrapyOutput"));
        }
        else {

            FileInputFormat.setInputPaths(job, "InputFile/shoolUrl.txt");
            File directory = new File("D:\\hadoopTest\\ScrapySchoolAll\\OutputFile");
            if (directory.exists()) {
                File[] files = directory.listFiles();
                assert files != null;
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDir(file);
                    } else {
                        file.delete();
                        System.out.println(file.getName() + "：：文件已删除");
                    }
                }
                //最终把该目录也删除
                directory.delete();
                System.out.println(directory.getName() + "：：目录已删除");
            }
            FileOutputFormat.setOutputPath(job, new Path("OutputFile"));


            boolean flag = job.waitForCompletion(true);
            System.exit(flag ? 0 : 1);
        }
    }
}
