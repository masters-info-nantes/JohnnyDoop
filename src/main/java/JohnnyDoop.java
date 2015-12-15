import crawler.DoopCrawler;

/**
 * Created by nicolas on 12/15/15.
 */
public class JohnnyDoop {


    public JohnnyDoop(String inputFile) {

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
