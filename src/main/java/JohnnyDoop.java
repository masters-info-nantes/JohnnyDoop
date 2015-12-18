import crawler.DoopCrawler;
import mapreduce.DoopMapper;
import mapreduce.DoopReducer;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import java.io.File;
import java.io.IOException;

/**
 * Created by nicolas on 12/15/15.
 */
public class JohnnyDoop {


    public JohnnyDoop(String inputFile) throws IOException {
        pageRank(inputFile);
    }

    public void pageRank(String inputFile) throws IOException {
        Configuration conf = new Configuration();

        Job pageRankCalculation = Job.getInstance(conf, "pageRank");
        pageRankCalculation.setJarByClass(JohnnyDoop.class);

        pageRankCalculation.setOutputKeyClass(Text.class);
        pageRankCalculation.setOutputValueClass(Text.class);

        // on retire les fichiers de la dernière expérience
        File file = new File(System.getProperty("user.dir")
                + "../../PageRankResults");
        FileUtils.deleteDirectory(file);

        FileInputFormat.setInputPaths(pageRankCalculation, new Path(inputFile));
        FileOutputFormat.setOutputPath(pageRankCalculation, new Path(
                "../../PageRankResults/turn1"));

        pageRankCalculation.setMapperClass(DoopMapper.class);
        pageRankCalculation.setReducerClass(DoopReducer.class);

        // on attend la complétion du processus
        pageRankCalculation.waitForCompletion(true);
        // itération sur les résultats précédent
        for (int i = 2; i <= new Integer(args[1]); i++) {
            conf = new Configuration();

            pageRankCalculation = Job.getInstance(conf, "pageRank");
            pageRankCalculation.setJarByClass(JohnnyDoop.class);

            pageRankCalculation.setOutputKeyClass(Text.class);
            pageRankCalculation.setOutputValueClass(Text.class);

            // // on retire les fichiers de la dernière expérience
            // File file = new File("");

            FileInputFormat.setInputPaths(pageRankCalculation, new Path(
                    "../PageRankResults/turn" + (i - 1) + "/part-r-00000"));
            FileOutputFormat.setOutputPath(pageRankCalculation, new Path(
                    "../PageRankResults/turn" + i));

            pageRankCalculation.setMapperClass(PageRankMapper.class);
            pageRankCalculation.setReducerClass(PageRankReducer.class);
            // on attend la complétion du processuss
            pageRankCalculation.waitForCompletion(true);
        }

    }


    public static void main(String[] args) {

        /*
            args[1] -> crawler seed
            args[2] -> file output
         */
        for(int i = 0; i < args.length; i++){
            System.out.println(args[i]);
        }

        switch (args[0]){
            case "crawl":
                if(args.length < 4) {
                    System.out.println("Missing parameters for crawler");
                    System.exit(0);
                }

                DoopCrawler doopCrawler = new DoopCrawler();
                doopCrawler.crawl(args[1], args[2], Integer.valueOf(args[3]));
                break;

            case "rank":
                new JohnnyDoop(args[1]);
                break;
        }


    }
}
