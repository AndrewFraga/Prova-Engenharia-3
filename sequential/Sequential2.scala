import java.io._
import scala.io._
import scala.concurrent.duration._

object Sequential2 {
    def main(args : Array[String]) : Unit = {
        val startTime = System.currentTimeMillis
        val matrixA = Source.fromFile("../../matrix/ppar1").getLines.toArray.map(_.split(" ")).transpose
        val file    = new PrintWriter(new File("ppar3-2"))
        for (lineB <- Source.fromFile("../../matrix/ppar2").getLines) {
            for (lineA <- matrixA)
                file.write(lineB.split(" ").map(_.toInt).zip(lineA.map(_.toInt)).map(((b : Int, a : Int) => b * (a + b)).tupled).sum + " ")
            file.write("\n")
        }
        file.close
        println("Total time: " + (System.currentTimeMillis - startTime).millis)
    }
}