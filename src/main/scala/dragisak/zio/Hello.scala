package dragisak.zio

final case class Hello(greeting: String)

object Hello {
  import io.circe.Codec
  import io.circe.generic.semiauto._
  implicit val helloCodec: Codec[Hello] = deriveCodec[Hello]
}
