package com.sopovs.moradanen.tarantool.samples.spring.session;


import com.sopovs.moradanen.tarantool.spring.session.TarantoolSessionRepository;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@ContextConfiguration(initializers = {SampleSessionApplicationTest.Initializer.class})
public class SampleSessionApplicationTest {
    @ClassRule
    public static GenericContainer TARANTOOL = new GenericContainer<>("tarantool/tarantool:1.7").withExposedPorts(3301);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TarantoolSessionRepository repo;

    @Test
    public void testGet() throws Exception {
        String sessionId = mockMvc.perform(get("/"))
                .andReturn().getResponse().getContentAsString();

        assertNotNull(repo.findById(sessionId));
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
