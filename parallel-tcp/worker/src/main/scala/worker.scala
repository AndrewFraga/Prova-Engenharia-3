import java.io._
import scala.io._
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import akka.routing.RoundRobinPool

class Worker extends Actor {
    def receive : Receive = {
        case Calculate(coordinate, a, b, writer) =>
            writer ! Write(coordinate, b.split(" ").map(_.toInt).zip(a.map(_.toInt)).map(((b : Int, a : Int) => b * (a + b)).tupled).sum)
    }
}

object Prova {
    def main(args : Array[String]) : Unit = {
        val system = ActorSystem("System")
        val worker = system.actorOf(Props[Worker], "Worker")
    }
}