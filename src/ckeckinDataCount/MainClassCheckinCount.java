package ckeckinDataCount;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

	/**
	 * @author Nikhil Patil <patilnikhils19@gmail.com>
	 * Apr 6, 2017
	 * MainClassCheckinCount.java
	 */

	public class MainClassCheckinCount {
			public static void main(String[] args) throws IOException, ClassNotFoundException,InterruptedException {
					if (args.length != 2) {
							System.out.printf("Usage: <jar file> <input dir> <output dir>\n");
							System.exit(-1);
							}
					
					Configuration conf =new Configuration();
					DistributedCache.addFileToClassPath(new Path("/projectData/json-20160212.jar"), conf); 
					Job job=Job.getInstance(conf);
					job.addFileToClassPath(new Path("/projectData/json-20160212.jar"));
					job.setJarByClass(MainClassCheckinCount.class);
					job.setMapperClass(WordMapperCheckinCount.class);
					job.setReducerClass(WordReducerCheckinCount.class);
					job.setOutputKeyClass(Text.class);
					job.setOutputValueClass(Text.class);
					job.setInputFormatClass(TextInputFormat.class);
					job.setOutputFormatClass(TextOutputFormat.class);
					FileInputFormat.setInputPaths(job, new Path(args[0]));
					FileOutputFormat.setOutputPath(job, new Path(args[1]));
					if(job.waitForCompletion(true)){
						System.out.println("Job finished!");
					}
					else{
						System.out.println("Job failed!");
					}
					System.exit(job.waitForCompletion(true) ? 0 : 1);
			}
}