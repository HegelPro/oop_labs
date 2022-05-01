package lectures

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only el: ${head}")
    case _ =>
  }

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 20) None
      else Some(person.name, person.age)
  }

  val bob = new Person("Bob", 19)

  val greeting = bob match {
    case Person(name, age) => s"name$name, $age"
  }
  println(greeting)

//  val isSingleDi = ()
}
