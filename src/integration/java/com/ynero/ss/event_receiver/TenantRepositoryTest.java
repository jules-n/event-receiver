package com.ynero.ss.event_receiver;

import com.ynero.ss.event_receiver.domain.DeviceData;
import com.ynero.ss.event_receiver.domain.Tenant;
import com.ynero.ss.event_receiver.persistence.TenantRepository;
import com.ynero.ss.event_receiver.persistence.TenantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@DirtiesContext
class TenantRepositoryTest {
    public static final String MONGO_VERSION = "4.4.4";
    public static final String REDIS_VERSION = "5.0.7";
    private static final long REDIS_MEMORY = 1024*1024*1024;
    @Autowired
    protected ReactiveMongoOperations mongo;

    @Autowired
    private TenantRepository tenantRepository;

    @Container
    protected static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:" + MONGO_VERSION);

    @Container
    public static final GenericContainer REDIS = new GenericContainer<>(DockerImageName.parse("redis:" + REDIS_VERSION))
            //.withClasspathResourceMapping("redis.conf", "./redis.conf", BindMode.READ_ONLY)
            .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig()
                    .withMemory(REDIS_MEMORY)
                    .withMemorySwap(0L)
            )
            .withExposedPorts(6379)
            .withEnv("maxmemory", "256mb")
            .withEnv("maxmemory-policy", "allkeys-lru");

    @DynamicPropertySource
    protected static void mongoProperties(DynamicPropertyRegistry reg) {
        reg.add("spring.data.mongodb.uri", () -> {
            return MONGO_CONTAINER.getReplicaSetUrl();
        });
    }

    @DynamicPropertySource
    protected static void redisProperties(DynamicPropertyRegistry reg) {
        reg.add("spring.data.redis.host", () -> {
            return REDIS.getContainerIpAddress();
        });
    }

    @AfterEach
    protected void cleanupAllDataInDb() {
        StepVerifier
                .create(mongo.getCollectionNames()
                        .flatMap(col -> mongo.remove(new Query(), col))
                        .collectList()
                )
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Autowired
    private TenantService tenantService;

    @BeforeEach
    void setUp() throws Exception {
        var deviceData = new DeviceData("deviceId", "type");
        List<Tenant> tenants = new ArrayList<>() {{
            add(new Tenant("t-1", new HashSet<String>() {{
                add("the-cure");
                add("the-doors");
            }}, new HashSet<String>() {{
                add("bmth");
                add("ddt");
                add("metallica");
            }}, deviceData, null));
            add(new Tenant("t-2", null, Set.of("acdc", "gachi", "led-zeppelin"), deviceData, null));
            add(new Tenant("t-3", Set.of("motorhead", "the-eagles"), Set.of("billy-talent", "joy-division", "ffdp"), deviceData, null));
            add(new Tenant("t-4", Set.of("arctic-monkeys", "maneskin"), null, deviceData, null));
            add(new Tenant("t-5", null, null, deviceData, null));
            add(new Tenant("t-6", Set.of("scorpions"), Set.of("audioslave"), deviceData, null));
        }};

        for (Tenant tenant : tenants) {
            tenantService.save(tenant);
        }

    }

    @Test
    void findByTenantExistingId() {
        var foundTenant = tenantService.findByTenantId("t-1").get();
        assertThat(foundTenant.getUrls())
                .contains("bmth", "ddt", "metallica");
    }

    @Test
    void findTenantIdByUrl() {
        var expected = "t-6";
        var actual = tenantRepository.findTenantIdByUrls("audioslave");
        assertThat(actual).isEqualTo(expected);
    }
}
