package com.github.alekslitvinenk

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.github.alekslitvinenk.db.Queries._
import slick.jdbc.MySQLProfile.api._

import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val db = Database.forConfig("imdb")

  val route =
    path("title") {
      get {
        parameter('search.as[String]) { search =>
          onSuccess(db.run(searchByTitle(search))) { result =>
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"$result"))
          }
        }
      }
    } ~ path("rating") {
      get {
        parameter('search.as[String]) { search =>
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>$search</h1>"))
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}