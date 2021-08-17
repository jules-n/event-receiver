package com.example.eventreceiver;

import com.example.eventreceiver.domain.Tenant;
import com.example.eventreceiver.persistence.TenantRepository;
import com.example.eventreceiver.persistence.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@DirtiesContext
class TenantRepositoryTest {
	public static final String MONGO_VERSION = "4.4.4";

	@Autowired
	protected ReactiveMongoOperations mongo;

	@Autowired
	private TenantRepository tenantRepository;

	@Container
	protected static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:" + MONGO_VERSION);

	@DynamicPropertySource
	protected static void mongoProperties(DynamicPropertyRegistry reg) {
		reg.add("spring.data.mongodb.uri", () -> {
			return MONGO_CONTAINER.getReplicaSetUrl();
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
		List<Tenant> tenants = new ArrayList<>() {{
			add(new Tenant("t-1", new String[]{"the-cure", "the-doors"}, new String[]{"bmth", "ddt", "metallica"}));
			add(new Tenant("t-2", null, new String[]{"acdc", "gachi", "led-zeppelin"}));
			add(new Tenant("t-3", new String[]{"motorhead", "the-eagles"}, new String[]{"billy-talent", "joy-division", "ffdp"}));
			add(new Tenant("t-4", new String[]{"arctic-monkeys", "maneskin"}, null));
			add(new Tenant("t-5", null, null));
			add(new Tenant("t-6", new String[]{"scorpions"}, new String[]{"audioslave"}));
		}};

		for (Tenant tenant : tenants) {
			tenantService.save(tenant);
		}

	}

	@Test
	void findByTenantExistingId() {
		var foundTenant = tenantService.findByTenantId("t-1").get();
		assertThat(foundTenant.getUrls())
				.isEqualTo(new String[]{"bmth", "ddt", "metallica"});
	}

	@Test
	void findTenantIdByUrl(){
		var expected = "t-6";
		var actual = tenantRepository.findTenantIdByUrls("audioslave");
		assertThat(actual).isEqualTo(expected);
	}
}
