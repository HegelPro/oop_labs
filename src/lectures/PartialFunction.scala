package lectures

object PartialFunction extends App {
  val aFunction = List(1, 2) map {x => x+ 1}
  println(aFunction)

  val func = new {
    def apply(x: Int) =x match {
      case 3 => 4

    }
  }

  val withHello = new {
    def unapply(str: String) =
      if (str == "hello") Some("Hi")
      else None
  }

  scala.io.Source.stdin.getLines().foreach(line => {
    val answer = line match {
      case withHello(answer) => answer
    }
    println(answer)
  })
}
