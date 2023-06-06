package bettler2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

  val pauseValue = 1

  private val httpProtocol = http
    .baseUrl("http://localhost:8085")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("de,en-US;q=0.7,en;q=0.3")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0")


   private val scn = scenario("RecordedSimulation")
      .exec(
      http("request_0:GET_http://localhost:8085/controller/test")
        .get("/controller/newgame?p1=pvp")
    )
    .pause(pauseValue)
    .exec(
      http("request_0:GET_http://localhost:8085/controller/newgame?p1=pvp")
        .get("/controller/newgame?p1=pvp")
    )
    .pause(pauseValue)
    .exec(
      http("request_1:GET_http://localhost:8085/controller/play?p1=_D9")
        .get("/controller/play?p1=_D9")
    )
    .pause(pauseValue)
    .exec(
      http("request_2:GET_http://localhost:8085/controller/skip")
        .get("/controller/skip")
    )
    .pause(pauseValue)
    .exec(
      http("request_3:GET_http://localhost:8085/controller/skip")
        .get("/controller/skip")
    )
    .pause(pauseValue)
    .exec(
      http("request_4:GET_http://localhost:8085/controller/save")
        .get("/controller/save")
    )
    .pause(pauseValue)
    .exec(
      http("request_5:GET_http://localhost:8085/controller/play?p1=_C10")
        .get("/controller/play?p1=_C10")
    )
    .pause(pauseValue)
    .exec(
      http("request_6:GET_http://localhost:8085/controller/load")
        .get("/controller/load")
    )
    .pause(pauseValue)
    .exec(
      http("request_7:GET_http://localhost:8085/controller/play?p1=_CQ")
        .get("/controller/play?p1=_CQ")
    )
    .pause(pauseValue)
    .exec(
      http("request_8:GET_http://localhost:8085/controller/undo")
        .get("/controller/undo")
    )
    .pause(pauseValue)
    .exec(
      http("request_9:GET_http://localhost:8085/controller/redo")
        .get("/controller/redo")
    )

  // Define the spike testing scenario
val spikeScenario = scenario("Spike Testing_allCommands")
      .exec(
      http("request_0:GET_http://localhost:8085/controller/test")
        .get("/controller/newgame?p1=pvp")
    )
    .pause(pauseValue)
  .exec(
    http("request_0:GET_http://localhost:8085/controller/newgame?p1=test")
      .get("/controller/newgame?p1=pvp")
  )
  .pause(pauseValue)
  .exec(
    http("request_1:GET_http://localhost:8085/controller/play?p1=_D9")
      .get("/controller/play?p1=_D9")
  )
  .pause(pauseValue)
  .exec(
    http("request_2:GET_http://localhost:8085/controller/skip")
      .get("/controller/skip")
  )
  .pause(pauseValue)
  .exec(
    http("request_3:GET_http://localhost:8085/controller/skip")
      .get("/controller/skip")
  )
  .pause(pauseValue)
  .exec(
    http("request_4:GET_http://localhost:8085/controller/save")
      .get("/controller/save")
  )
  .pause(pauseValue)
  .exec(
    http("request_5:GET_http://localhost:8085/controller/play?p1=_C10")
      .get("/controller/play?p1=_C10")
  )
  .pause(pauseValue)
  .exec(
    http("request_6:GET_http://localhost:8085/controller/load")
      .get("/controller/load")
  )
  .pause(pauseValue)
  .exec(
    http("request_7:GET_http://localhost:8085/controller/play?p1=_CQ")
      .get("/controller/play?p1=_CQ")
  )
  .pause(pauseValue)
  .exec(
    http("request_8:GET_http://localhost:8085/controller/undo")
      .get("/controller/undo")
  )
  .pause(pauseValue)
  .exec(
    http("request_9:GET_http://localhost:8085/controller/redo")
      .get("/controller/redo")
  )

// Define the endurance testing scenario
val enduranceScenario = scenario("Endurance Testing_allCommands")
      .exec(
      http("request_0:GET_http://localhost:8085/controller/test")
        .get("/controller/newgame?p1=pvp")
    )
    .pause(pauseValue)
  .exec(
    http("request_0:GET_http://localhost:8085/controller/newgame?p1=test")
      .get("/controller/newgame?p1=pvp")
  )
  .pause(pauseValue)
  .exec(
    http("request_1:GET_http://localhost:8085/controller/play?p1=_D9")
      .get("/controller/play?p1=_D9")
  )
  .pause(pauseValue)
  .exec(
    http("request_2:GET_http://localhost:8085/controller/skip")
      .get("/controller/skip")
  )
  .pause(pauseValue)
  .exec(
    http("request_3:GET_http://localhost:8085/controller/skip")
      .get("/controller/skip")
  )
  .pause(pauseValue)
  .exec(
    http("request_4:GET_http://localhost:8085/controller/save")
      .get("/controller/save")
  )
  .pause(pauseValue)
  .exec(
    http("request_5:GET_http://localhost:8085/controller/play?p1=_C10")
      .get("/controller/play?p1=_C10")
  )
  .pause(pauseValue)
  .exec(
    http("request_6:GET_http://localhost:8085/controller/load")
      .get("/controller/load")
  )
  .pause(pauseValue)
  .exec(
    http("request_7:GET_http://localhost:8085/controller/play?p1=_CQ")
      .get("/controller/play?p1=_CQ")
  )
  .pause(pauseValue)
  .exec(
    http("request_8:GET_http://localhost:8085/controller/undo")
      .get("/controller/undo")
  )
  .pause(pauseValue)
  .exec(
    http("request_9:GET_http://localhost:8085/controller/redo")
      .get("/controller/redo")
  )

// Define the spike and endurance test configurations
val spikeTestConfig = rampUsersPerSec(10) to 100 during (1 minute)
val enduranceTestConfig = constantUsersPerSec(50) during (1 minutes)

// Configure the test execution
setUp(
  scn.inject(atOnceUsers(1000)).andThen(
    spikeScenario.inject(spikeTestConfig).andThen(
      enduranceScenario.inject(enduranceTestConfig)
    )
  )
).protocols(httpProtocol)
}