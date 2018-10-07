package com.sopovs.moradanen.tarantool.samples.spring.boot;

import com.sopovs.moradanen.tarantool.core.Util;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@ContextConfiguration(initializers = {SampleTarantoolSpringBootApplicationTest.Initializer.class})
public class SampleTarantoolSpringBootApplicationTest {
    @ClassRule
    public static GenericContainer TARANTOOL = new GenericContainer<>("tarantool/tarantool:1.7").withExposedPorts(3301);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/spaceByName/_vspace"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("_vspace"))
                .andExpect(jsonPath("$.id").value(Util.SPACE_VSPACE));
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.tarantool.port=" + TARANTOOL.getMappedPort(3301)
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
