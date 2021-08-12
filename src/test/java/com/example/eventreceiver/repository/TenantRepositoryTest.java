/*

package com.example.eventreceiver.repository;

import com.example.eventreceiver.domain.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableMongoRepositories(basePackages = "com.example.eventreceiver.repository")
class TenantRepositoryTest
{

    @Autowired
    private TenantRepository tenantRepository;

    @BeforeEach
    void setUp() {
        List<Tenant> tenants = new ArrayList<>() {{
            add(new Tenant("t-1", new String[]{"the-cure", "the-doors"}, new String[]{"bmth", "ddt", "metallica"}));
            add(new Tenant("t-2", null, new String[]{"acdc", "gachi", "led-zeppelin"}));
            add(new Tenant("t-3", new String[]{"motorhead", "the-eagles"}, new String[]{"billy-talent", "joy-division", "ffdp"}));
            add(new Tenant("t-4", new String[]{"arctic-monkeys", "maneskin"}, null));
            add(new Tenant("t-5", null, null));
            add(new Tenant("t-6", new String[]{"scorpions"}, new String[]{"audioslave"}));
        }};
        tenants.forEach(
                tenantRepository::save
        );

    }

    @Test
    void findByTenantExistingId() {
        System.err.println(tenantRepository);
        var foundTenant = tenantRepository.findByTenantId("t-1").get();
        assertThat(foundTenant.getUrls())
                .isEqualTo(new String[]{"bmth", "ddt", "metallica"});
    }
}
*/
