import akka.actor._

sealed trait Msg
    case class Read(pathA : String, pathB : String)                                                extends Msg
    case class Write(coordinate: (Int, Int), item : Int)                                           extends Msg
    case class Calculate(coordinate: (Int, Int), a : Array[String], b : String, writer : ActorRef) extends Msg