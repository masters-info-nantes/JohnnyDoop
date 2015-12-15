package main.java.mapreduce;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by nicolas on 12/6/15.
 */
public class DoopMapper extends Mapper {


    @Override
    protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);
    }
}
