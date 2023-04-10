package reduce;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyReducer extends Reducer<Text, NullWritable, Text,NullWritable> {

    @Override
    public void reduce(Text key, Iterable<NullWritable> values,
                       Context context) throws IOException, InterruptedException {

        context.write(key,NullWritable.get());
    }
}
