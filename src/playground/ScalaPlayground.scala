package playground

import analyser.Analyser
import analyser.State.State
import scala.io.Source

object ScalaPlayground extends App {
  def printCheckResult(line: String, result: Either[(String, State), Boolean]) = result match {
    case Right(_) => println(s"Line '$line' is correct.")
    case Left((str, state)) => println(s"Line '$line' isn't correct. Error happened in state: '$state', with '$str' line.")
  }

  println("Results from right inputs from file:")
  Source.fromFile("input/rightLines")
    .getLines
    .toList
    .foreach(line => printCheckResult(line, Analyser.check(line)))

  println("=====================================")

  println("Results from wring inputs from file:")
  Source.fromFile("input/wrongLines")
    .getLines
    .toList
    .foreach(line => printCheckResult(line, Analyser.check(line)))

  println("=====================================")

  println("Input line:")
  Source.stdin.getLines().foreach(line => {
    printCheckResult(line, Analyser.check(line))
    println("Input new line:")
  })
}
