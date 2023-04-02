package com.wright.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * This class runs an integration test.  The filename suffix 'IT' matches
 * the pattern that the failsafe plugin looks for when running integration tests.
 * The @ExtendWith annotation replaces the @RunWith annotation from JUnit4 and specifies
 * that this is an integration test.
 * <p></p>
 * Note, as far as I know, this cannot be ran from within an IDE, it needs to be ran
 * from the terminal using mvn clean install, or mvn verify.  It takes a bit longer to startup
 * as it uses Docker to spin up an instance of Cassandra for running the full application context.
 */
@SpringBootTest
class UrlShortenerApplicationTestsIT {
    @Test
    void contextLoads() {
    }
}
