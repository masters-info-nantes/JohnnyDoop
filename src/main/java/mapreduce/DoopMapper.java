package mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by nicolas on 12/6/15.
 */
public class DoopMapper extends Mapper<LongWritable, Text, Text, Text> {


    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        int pageTabIndex = value.find("\t");
        int rankTabIndex = value.find("\t", pageTabIndex + 1);

        //check for empty line
        if(pageTabIndex == -1){return;}

        // get extracted page
        String page = Text.decode(value.getBytes(), 0, pageTabIndex);

        String pageWithRank = Text.decode(value.getBytes(), 0, rankTabIndex + 1);

        // mark as existing page
        context.write(new Text(page), new Text("!"));

        // get links
        String links = Text.decode(value.getBytes(), rankTabIndex + 1, value.getLength() - (rankTabIndex + 1));

        // parse with colon
        String[] allOtherPages = links.split(",");

        // count ouput links
        int totalLinks = allOtherPages.length;

        for (String otherPage : allOtherPages) {
            Text pageRankTotalLinks = new Text(pageWithRank + totalLinks);
            context.write(new Text(otherPage), pageRankTotalLinks);
        }

        context.write(new Text(page), new Text("|" + links));
    }
}
