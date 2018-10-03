import java.io._
import scala.io._
import scala.concurrent.duration._

object Sequential1 {
    def main(args : Array[String]) : Unit = {
        val startTime = System.currentTimeMillis
        val matrixA = Source.fromFile("../../matrix/ppar1").getLines.toList.map(_.split(" ").map(_.toInt)).transpose
        val matrixB = Source.fromFile("../../matrix/ppar2").getLines.toList.map(_.split(" ").map(_.toInt))
        val matrixC = for(l <- 0 until 3) yield for(c <- 0 until 3)
            yield matrixB(l).zip(matrixA(c)).map(((b : Int, a : Int) => b * (a + b)).tupled).sum
        val file = new PrintWriter(new File("ppar3-1"))
        file.write(matrixC.map(_.mkString(" ")).mkString("\n"))
        file.close
        println("Total time: " + (System.currentTimeMillis - startTime).millis)
    }
}