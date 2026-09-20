package gatling.simulations;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.headerRegex;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Performance test for the InventoryItem entity.
 *
 * @see <a href="https://github.com/jhipster/generator-jhipster/tree/v9.4.0/generators/gatling#logging-tips">Logging tips</a>
 */
public class InventoryItemGatlingTest extends Simulation {

    String baseURL = Optional.ofNullable(System.getProperty("baseURL")).orElse("http://localhost:8080");

    HttpProtocolBuilder httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources(); // Silence all resources like css or js so they don't clutter the results

    Map<String, String> headersHttp = Map.of("Accept", "application/json");

    Map<String, String> headersHttpAuthentication = Map.of("Content-Type", "application/json", "Accept", "application/json");

    Map<String, String> headersHttpAuthenticated = Map.of("Accept", "application/json", "Authorization", "${access_token}");

    ChainBuilder scn = exec(http("First unauthenticated request").get("/api/account").headers(headersHttp).check(status().is(401)))
        .exitHereIfFailed()
        .pause(10)
        .exec(
            http("Authentication")
                .post("/api/authenticate")
                .headers(headersHttpAuthentication)
                .body(StringBody("{\"username\":\"admin\", \"password\":\"admin\"}"))
                .asJson()
                .check(header("Authorization").saveAs("access_token"))
        )
        .exitHereIfFailed()
        .pause(2)
        .exec(http("Authenticated request").get("/api/account").headers(headersHttpAuthenticated).check(status().is(200)))
        .pause(10)
        .repeat(2)
        .on(
            exec(http("Get all inventoryItems").get("/api/inventory-items").headers(headersHttpAuthenticated).check(status().is(200)))
                .pause(Duration.ofSeconds(10), Duration.ofSeconds(20))
                .exec(
                    http("Create new inventoryItem")
                        .post("/api/inventory-items")
                        .headers(headersHttpAuthenticated)
                        .body(
                            StringBody(
                                "{" +
                                    "\"receivedDate\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"manufacturedDate\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"expiryDate\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"retestDate\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"containerId\": \"SAMPLE_TEXT\"" +
                                    ", \"batchNo\": \"SAMPLE_TEXT\"" +
                                    ", \"mfgBatchNo\": \"SAMPLE_TEXT\"" +
                                    ", \"lotNoStr\": \"SAMPLE_TEXT\"" +
                                    ", \"binNumber\": \"SAMPLE_TEXT\"" +
                                    ", \"comments\": \"SAMPLE_TEXT\"" +
                                    ", \"quantityOnHandTotal\": 0" +
                                    ", \"availableToPromiseTotal\": 0" +
                                    ", \"accountingQuantityTotal\": 0" +
                                    ", \"oldQuantityOnHand\": 0" +
                                    ", \"oldAvailableToPromise\": 0" +
                                    ", \"serialNumber\": \"SAMPLE_TEXT\"" +
                                    ", \"softIdentifier\": \"SAMPLE_TEXT\"" +
                                    ", \"activationNumber\": \"SAMPLE_TEXT\"" +
                                    ", \"activationValidThru\": \"2020-01-01T00:00:00.000Z\"" +
                                    ", \"unitCost\": 0" +
                                    "}"
                            )
                        )
                        .asJson()
                        .check(status().is(201))
                        .check(headerRegex("Location", "(.*)").saveAs("new_inventoryItem_url"))
                )
                .exitHereIfFailed()
                .pause(10)
                .repeat(5)
                .on(exec(http("Get created inventoryItem").get("${new_inventoryItem_url}").headers(headersHttpAuthenticated)).pause(10))
                .exec(http("Delete created inventoryItem").delete("${new_inventoryItem_url}").headers(headersHttpAuthenticated))
                .pause(10)
        );

    ScenarioBuilder users = scenario("Test the InventoryItem entity").exec(scn);

    {
        setUp(
            users.injectOpen(rampUsers(Integer.getInteger("users", 100)).during(Duration.ofMinutes(Integer.getInteger("ramp", 1))))
        ).protocols(httpConf);
    }
}
