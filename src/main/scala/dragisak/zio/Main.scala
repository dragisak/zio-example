package dragisak.zio

import zio._
import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import zio.interop.catz._

object Main extends App {

  type AppEnvironment = zio.clock.Clock
  type AppTask[A]     = RIO[AppEnvironment, A]

  val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
  import dsl._

  private val service: HttpRoutes[AppTask] = HttpRoutes.of[AppTask] {
    case GET -> Root => Ok("Hello World")
  }

  private val app: HttpApp[AppTask] = Router[AppTask](
    "/hello" -> service
  ).orNotFound

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
      BlazeServerBuilder[AppTask] //requires a Clock environment
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(app)
        .serve
        .compile[AppTask, AppTask, ExitCode]
        .drain
        .foldM(
          _ => ZIO.succeed(1),
          _ => ZIO.succeed(0)
        )
    }
  }
}
