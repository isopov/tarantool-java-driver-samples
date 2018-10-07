package com.sopovs.moradanen.tarantool.samples.spring.boot;

import com.sopovs.moradanen.tarantool.Result;
import com.sopovs.moradanen.tarantool.TarantoolClient;
import com.sopovs.moradanen.tarantool.TarantoolClientSource;
import com.sopovs.moradanen.tarantool.core.Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SampleTarantoolSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleTarantoolSpringBootApplication.class);
    }

}

@RestController
class TarantoolController {
    private final TarantoolClientSource tarantoolClientSource;

    public TarantoolController(TarantoolClientSource tarantoolClientSource) {
        this.tarantoolClientSource = tarantoolClientSource;
    }

    @GetMapping("/spaces")
    public List<Space> spaces() {
        try (TarantoolClient client = tarantoolClientSource.getClient()) {
            client.selectAll(Util.SPACE_VSPACE);
            Result result = client.execute();
            List<Space> list = new ArrayList<>(result.getSize());
            while (result.hasNext()) {
                result.next();
                list.add(
                        new Space(
                                result.getInt(0),
                                result.getInt(1),
                                result.getString(2),
                                result.getString(3),
                                result.getInt(4)
                        )
                );
            }
            return list;


        }
    }

    @GetMapping("/spaceByName/{name}")
    public Space space(@PathVariable String name) {
        try (TarantoolClient client = tarantoolClientSource.getClient()) {

            client.select(Util.SPACE_VSPACE, Util.INDEX_SPACE_NAME);
            client.setString(name);
            Result result = client.execute();
            if (result.getSize() == 0) {
                return null;
            }
            result.next();
            return new Space(
                    result.getInt(0),
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4)
            );
        }
    }
}


class Space {
    private final int id;
    private final int ownerId;
    private final String name;
    private final String engine;
    private final int fieldCount;

    Space(int id, int ownerId, String name, String engine, int fieldCount) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.engine = engine;
        this.fieldCount = fieldCount;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getEngine() {
        return engine;
    }

    public int getFieldCount() {
        return fieldCount;
    }
}