package com.github.alekslitvinenk

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.github.alekslitvinenk.db.Queries
import com.github.alekslitvinenk.domain.Protocol.SearchResult
import com.github.alekslitvinenk.domain.ProtocolFormat.JsonSupport
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Main extends App with JsonSupport {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val db = Database.forConfig("imdb")
  val queries = Queries(db)

  val route =
    path("title") {
      get {
        parameter('search.as[String]) { search =>
          onSuccess(queries.searchFilmByTitle(search)) { result =>
            complete(SearchResult(result))
          }
        }
      }
    } ~ path("genre") {
      get {
        parameter('search.as[String]) { search =>
          onSuccess(queries.searchTop10RatedFilmsByGenre(search)) { result =>
            complete(SearchResult(result))
          }
        }
      }
    } ~ path("/index.html") {
      get {
        getFromResource("/web/index.html")
      }
    } ~ pathSingleSlash {
      redirect("index.html", StatusCodes.PermanentRedirect)
    } ~ {
      getFromResourceDirectory("web")
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}