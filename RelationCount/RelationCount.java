import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.join.TupleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RelationCount {

  public static class TokenizerMapper
       extends Mapper<Object, Text, LongWritable, TupleWritable>{

    private final static IntWritable zero = new IntWritable(0);
    private final static IntWritable one = new IntWritable(1);

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

        long id1 = Long.parseLong(value.toString().split("\\s+")[0]);
        long id2 = Long.parseLong(value.toString().split("\\s+")[1]);
        context.write(new LongWritable(id1), new TupleWritable(new IntWritable[] {one, zero}));
        context.write(new LongWritable(id2), new TupleWritable(new IntWritable[] {zero, one}));
    }
  }

  public static class IntSumReducer
       extends Reducer<LongWritable, TupleWritable, LongWritable, Text> {

    public void reduce(LongWritable key, Iterable<TupleWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum_in = 0;
      int sum_out = 0;
      for (TupleWritable val : values) {
        sum_in += ((IntWritable)val.get(1)).get();
        sum_out += ((IntWritable)val.get(0)).get();
      }
      context.write(key, new Text(sum_in + ", " + sum_out));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "relation count");
    job.setJarByClass(RelationCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setMapOutputKeyClass(LongWritable.class);
    job.setMapOutputValueClass(TupleWritable.class);
    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}