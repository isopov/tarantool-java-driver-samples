package com.sopovs.moradanen.tarantool.samples.simple;

import com.sopovs.moradanen.tarantool.*;

public class SimpleSample {

    public static void main(String[] args) {
        try (TarantoolClientSource clientSource = new TarantoolPooledClientSource("localhost", 3301, 1);
             TarantoolClient client = clientSource.getClient()) {
            client.selectAll("_vspace");
            Result result = client.execute();
            while (result.next()) {
                System.out.println(result.getString(2));
            }
        }
    }
}
