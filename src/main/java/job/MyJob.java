package job;

import mapper.MyMapper;
import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import reduce.MyReducer;
import util.MyPath;
import util.MySystem;



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

        String my_system = System.getProperty("os.name");
        try {
            if (my_system.startsWith(MySystem.WINDOWS.value())) {
                FileInputFormat.setInputPaths(job, MyPath.WINDOWS_INPUT_PATH.string_value());
                pathIsExitsWindows(MyPath.WINDOWS_OUTPUT_PATH.string_value());
                FileOutputFormat.setOutputPath(job,MyPath.WINDOWS_OUTPUT_PATH.path_value());
            }
            if (my_system.startsWith(MySystem.LINUX.value())){
                FileInputFormat.setInputPaths(job, MyPath.HADOOP_INPUT_PATH.path_value());
                FileOutputFormat.setOutputPath(job, MyPath.HADOOP_OUTPUT_PATH.path_value());
            }
            if (my_system.startsWith(MySystem.MAC.value())){
                throw new Exception("Mac is not define, please over write here");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean flag = job.waitForCompletion(true);
        System.exit(flag ? 0 : 1);
    }

    private static void pathIsExitsWindows(String path) throws IOException {
        boolean status;
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    status = file.delete();
                    System.out.println("delete file:" + file.getName() + ",status:" + status);
                }
            }
            status = directory.delete();
            System.out.println("delete directory" + directory.getName() + ",status:" + status);
        }
    }
}

