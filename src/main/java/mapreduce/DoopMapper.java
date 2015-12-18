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
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        int pageTabIndex = value.find("\t");
        int rankTabIndex = value.find("\t", pageTabIndex + 1);

        //dans le cas ou il y'aurai une ligne vide dans le fichier
        if(pageTabIndex == -1){
            return;
        }

        // on récupère la page extraite
        String page = Text.decode(value.getBytes(), 0, pageTabIndex);

        // la page + le page rank
        String pageWithRank = Text.decode(value.getBytes(), 0,
                rankTabIndex + 1);

        // on marque la page comme existante
        context.write(new Text(page), new Text("!"));

        // on récupère la liste des liens
        String links = Text.decode(value.getBytes(), rankTabIndex + 1,
                value.getLength() - (rankTabIndex + 1));

        // on récupère chaque page en découpant la ligne par virgule
        String[] allOtherPages = links.split(",");

        // on compte le nombre de lien sortant
        int totalLinks = allOtherPages.length;

        for (String otherPage : allOtherPages) {
            Text pageRankTotalLinks = new Text(pageWithRank + totalLinks);
            context.write(new Text(otherPage), pageRankTotalLinks);
        }

        // on récupère les liens pour la sortie
        context.write(new Text(page), new Text("|" + links));
    }
}
