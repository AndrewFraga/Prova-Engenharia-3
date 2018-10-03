import java.io._
import scala.io._
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import akka.routing.RoundRobinPool
import akka.remote.routing.RemoteRouterConfig

class Writer(order : Int, startTime : Long) extends Actor {
    val resultFile = new PrintWriter(new File("result"))
    val statusFile = new PrintWriter(new File("status"))
    var positiveIntegers = 0
    var negativeIntegers = 0
    var zeros            = 0
    var waitingItems = ArrayBuffer[((Int, Int), Int)]()
    var nextItem     = (0, 0)
    
    private def writeItem(item : Int) : Unit = {
        resultFile.write(item + " ")
        nextItem = (nextItem._1, nextItem._2 + 1)
        if (nextItem._2 == order) {
            nextItem = (nextItem._1 + 1, 0)
            println(nextItem._1 + "/" + order)
            resultFile.write("\n")
        }
        if (nextItem._1 == order) {
            resultFile.close
            val statusFile = new PrintWriter(new File("status"))
            statusFile.write("Positive integers: " + positiveIntegers + "\n")
            statusFile.write("Negative integers: " + negativeIntegers + "\n")
            statusFile.write("Zeros: " + zeros + "\n")
            statusFile.write("Total time: " + (System.currentTimeMillis - startTime).millis + "\n")
            statusFile.close
            println("Done")
        }
        else {
            waitingItems.find(_._1 == nextItem) match {
                case Some(element) => { 
                    waitingItems -= element
                    writeItem(element._2)
                }
                case None => {}
            }
        }
    }
    
    def receive : Receive = {
        case Write(coordinate, item) => {
            if (item > 0)
                    positiveIntegers += 1
                else
                    if (item < 0)
                        negativeIntegers += 1
                    else
                        zeros +=1
            
            if (nextItem == coordinate)
                writeItem(item)
            else
                waitingItems += ((coordinate, item))
        }
    }
}

class Reader(writer : ActorRef) extends Actor {
    def receive : Receive = {
        case Read(pathA, pathB) => {
            // val addresses = Seq(AddressFromURIString("akka.tcp://System@127.0.0.1:2553"))
            // val worker = context.actorOf(RemoteRouterConfig(RoundRobinPool(2), addresses).props(Props(new Worker(writer))))
            // val worker = context.actorOf(Props(new Worker(writer)), "remotePool")
            val worker = context.actorSelection("akka.tcp://System@127.0.0.1:2571/user/Worker")
            val matrixA    = Source.fromFile(pathA).getLines.toArray.map(_.split(" ")).transpose
            var coordinate = (0, 0)
            
            for (lineB <- Source.fromFile(pathB).getLines) {
                for (lineA <- matrixA) {
                    worker ! Calculate(coordinate, lineA, lineB, writer)
                    coordinate = (coordinate._1, coordinate._2 + 1)
                }
                coordinate = (coordinate._1 + 1, 0)
            }
        }
    }
}

object Master {
    def main(args : Array[String]) : Unit = {
        println("Digite a ordem da matriz: ")
        val order = StdIn.readInt
        
        println("Digite o path da matriz A: ")
        val pathA = StdIn.readLine
        
        println("Digite o path da matriz B: ")
        val pathB = StdIn.readLine
        
        val system = ActorSystem("System")
        val writer = system.actorOf(Props(new Writer(order, System.currentTimeMillis)), "Writer")
        val reader = system.actorOf(Props(new Reader(writer)), "Reader")
        
        reader ! Read(pathA, pathB)
    }
}