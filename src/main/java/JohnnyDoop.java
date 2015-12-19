import crawler.DoopCrawler;
import mapreduce.DoopMapper;
import mapreduce.DoopReducer;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Created by nicolas on 12/15/15.
 */
public class JohnnyDoop {


    public JohnnyDoop(String inputFile, String outputFile) throws IOException {
        pageRank(inputFile, outputFile);
    }


    public void pageRank(String inputFile, String outputFile) throws IOException {
        //Remove dir
        FileUtils.deleteDirectory(new File(outputFile));
        Configuration conf = new Configuration();

        Job pageRankCalculation = Job.getInstance(conf, "JohnnyDoop");
        pageRankCalculation.setJarByClass(JohnnyDoop.class);

        pageRankCalculation.setOutputKeyClass(Text.class);
        pageRankCalculation.setOutputValueClass(Text.class);



        FileInputFormat.setInputPaths(pageRankCalculation, new Path(inputFile));
        FileOutputFormat.setOutputPath(pageRankCalculation, new Path(outputFile+"/iter0"));

        pageRankCalculation.setMapperClass(DoopMapper.class);
        pageRankCalculation.setReducerClass(DoopReducer.class);

        // on attend la complétion du processus
        try {
            pageRankCalculation.waitForCompletion(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        for (int i = 1; i <= 5; i++) {
            conf = new Configuration();

            pageRankCalculation = Job.getInstance(conf, "JohnnyDoop");
            pageRankCalculation.setJarByClass(JohnnyDoop.class);

            pageRankCalculation.setOutputKeyClass(Text.class);
            pageRankCalculation.setOutputValueClass(Text.class);



            FileInputFormat.setInputPaths(pageRankCalculation, new Path(outputFile+"/iter" + (i - 1)));
            FileOutputFormat.setOutputPath(pageRankCalculation, new Path(outputFile+"/iter" + i));

            pageRankCalculation.setMapperClass(DoopMapper.class);
            pageRankCalculation.setReducerClass(DoopReducer.class);

            // on attend la complétion du processuss
            try {
                pageRankCalculation.waitForCompletion(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }






    public static void main(String[] args) {

        /*
            args[0] -> command
            args[1] -> crawler seed
            args[2] -> file output
            args[3] -> depth
         */

        //Launching with default parameters
        if(args.length==0)
        {
            System.out.println("Default launch without parameter, crawling ulule.com with a depth of 1, check the result in ulule.com.txt");
            args = new String[]{"crawl","http://ulule.com","../ulule.com.txt","1"};
        }

        switch (args[0]){
            case "crawl":
                if(args.length < 4) {
                    System.out.println("Missing parameters for crawler, example: -crawl 'http://website.com' 'output_file.txt' 'depth'");
                    System.exit(0);
                }

                DoopCrawler doopCrawler = new DoopCrawler();
                doopCrawler.crawl(args[1], args[2], Integer.valueOf(args[3]));
                break;

            case "rank":
                if(args.length<2)
                {
                    System.out.println("Please specify the file with crawled urls example: -rank inputFile outputFile");
                    System.exit(0);
                }
                try {
                    new JohnnyDoop(args[1], args[2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


    }
}
