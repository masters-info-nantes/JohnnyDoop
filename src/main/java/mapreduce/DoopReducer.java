package main.java.mapreduce;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by nicolas on 12/5/15.
 */
public class DoopReducer extends Reducer{


    @Override
    protected void reduce(Object key, Iterable values, Context context) throws IOException, InterruptedException {
        super.reduce(key, values, context);
    }
}
