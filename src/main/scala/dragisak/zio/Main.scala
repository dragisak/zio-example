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

  private val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
  import dsl._

  private val plainService = HttpRoutes.of[AppTask] {
    case GET -> Root / "hello" / name => Ok(s"Hello $name")
  }

  private val jsonService: HttpRoutes[AppTask] = {
    import org.http4s.circe.CirceEntityCodec._
    HttpRoutes.of[AppTask] {
      case GET -> Root / "hello" / name => Ok(Hello(s"World $name"))
    }
  }

  private val app: HttpApp[AppTask] = Router[AppTask](
    "/plain" -> plainService,
    "/json"  -> jsonService
  ).orNotFound

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
      BlazeServerBuilder[AppTask] //requires a Clock environment
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(app)
        .serve
        .compile[AppTask, AppTask, ExitCode]
        .drain
        .fold(
          _ => 1,
          _ => 0
        )
    }
  }
}
