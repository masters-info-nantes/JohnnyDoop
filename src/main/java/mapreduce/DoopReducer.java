package mapreduce;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by nicolas on 12/5/15.
 */
public class DoopReducer extends Reducer<Text, Text, Text, Text> {

    private static final float randomSurferSimulation = 0.85F;

    @Override
    public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        boolean isExistingPage = false;
        String[] split;
        float participantRank = 0;
        String links = "";
        String pageWithRank;

        for (Text value : values) {
            pageWithRank = value.toString();

            if (pageWithRank.equals("!")) {
                isExistingPage = true;
                continue;
            }

            if (pageWithRank.startsWith("|")) {
                links = "\t" + pageWithRank.substring(1);
                continue;
            }

            //parse the file with tabulation
            split = pageWithRank.split("\t");

            float pageRank = Float.valueOf(split[1]);
            int nbOutLink = Integer.valueOf(split[2]);

            participantRank += (pageRank / nbOutLink);
        }

        if (!isExistingPage) {
            return;
        }
        float newRank = (randomSurferSimulation * participantRank) + (1 - randomSurferSimulation);

        context.write(page, new Text(newRank + links));
    }
}
