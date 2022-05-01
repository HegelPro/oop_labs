package analyser

import scala.io.Source

object State extends Enumeration {
  type State = Value
  val S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11 = Value
}
import State._

object Analyser extends App {
  def startsWithFunction = "^FUNCTION"
  def startsWithLetter = "^[a-zA-Z]"
  def startsWithLetterOrNumber = "^[a-zA-Z0-9]"
  def startsWithOpenBracket = "^\\("
  def startsWithVar = "^VAR"
  def startsWithСolon = "^\\:"
  def startsWithCloseBracketPlusСolon = "^\\)\\:"
  def startsWithСomma = "^\\,"
  def startsWithSemicolon = "^\\;"
  def startsWithType = "^(REAL|INTEGER|CHAR|BYTE)"

  def checkS0(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithFunction.*$$") => Right(str.replaceFirst(startsWithFunction, ""), S1)
    case str => Left(str, S0)
  }

  def checkS1(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithLetter.*$$") => Right(str.replaceFirst(startsWithLetter, ""), S2)
    case str => Left(str, S1)
  }

  def checkS2(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithLetterOrNumber.*$$") => Right((str.replaceFirst(startsWithLetterOrNumber, ""), S2))
    case str if str.matches(s"$startsWithOpenBracket.*$$") => Right(str.replaceFirst(startsWithOpenBracket, ""), S3)
    case str => Left(str, S2)
  }

  def checkS3(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithVar.*$$") => Right(str.replaceFirst(startsWithVar, ""), S4)
    case str if str.matches(s"$startsWithLetter.*$$") => Right(str.replaceFirst(startsWithLetter, ""), S5)
    case str => Left(str, S3)
  }

  def checkS4(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithLetter.*$$") => Right(str.replaceFirst(startsWithLetter, ""), S5)
    case str => Left(str, S4)
  }

  def checkS5(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithLetterOrNumber.*$$") => Right(str.replaceFirst(startsWithLetterOrNumber, ""), S5)
    case str if str.matches(s"$startsWithСolon.*$$") => Right(str.replaceFirst(startsWithСolon, ""), S6)
    case str if str.matches(s"$startsWithСomma.*$$") => Right(str.replaceFirst(startsWithСomma, ""), S7)
    case str => Left(str, S5)
  }

  def checkS6(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithType.*$$") => Right(str.replaceFirst(startsWithType, ""), S8)
    case str => Left(str, S6)
  }

  def checkS7(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithLetter.*$$") => Right(str.replaceFirst(startsWithLetter, ""), S5)
    case str => Left(str, S7)
  }

  def checkS8(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithSemicolon.*$$") => Right(str.replaceFirst(startsWithSemicolon, ""), S3)
    case str if str.matches(s"$startsWithCloseBracketPlusСolon.*$$") => Right(str.replaceFirst(startsWithCloseBracketPlusСolon, ""), S9)
    case str => Left(str, S8)
  }

  def checkS9(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithType.*$$") => Right(str.replaceFirst(startsWithType, ""), S10)
    case str => Left(str, S9)
  }

  def checkS10(str: String): Either[(String, State), (String, State)] = str match {
    case str if str.matches(s"$startsWithSemicolon.*$$") => Right(str.replaceFirst(startsWithSemicolon, ""), S11)
    case str => Left(str, S10)
  }

  def checkControl(checkResult: Either[(String, State), (String, State)]): Either[(String, State), String] = checkResult match {
    case Right(rightResult) => rightResult match {
      case (str: String, S0) => checkControl(checkS0(str))
      case (str: String, S1) => checkControl(checkS1(str))
      case (str: String, S2) => checkControl(checkS2(str))
      case (str: String, S3) => checkControl(checkS3(str))
      case (str: String, S4) => checkControl(checkS4(str))
      case (str: String, S5) => checkControl(checkS5(str))
      case (str: String, S6) => checkControl(checkS6(str))
      case (str: String, S7) => checkControl(checkS7(str))
      case (str: String, S8) => checkControl(checkS8(str))
      case (str: String, S9) => checkControl(checkS9(str))
      case (str: String, S10) => checkControl(checkS10(str))
      case (str: String, S11) => Right(str)
    }
    case Left((str, state)) => Left(str, state)
  }

  def check(str: String): Either[(String, State), Boolean] = {
    val normalizedStr = str.replaceAll(" ", "")
    checkControl(Right(normalizedStr, State.S0))
      .map(_.isEmpty)
  }

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
