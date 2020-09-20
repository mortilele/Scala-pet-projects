package week3

import scala.annotation.tailrec

object FindIsElementInListRecursively extends App {

  @tailrec
  def isElementInList(list: List[Int], x: Int): Boolean = {
    list match {
      case Nil => false
      case list if (list.head == x) => true
      case _ :: tail => isElementInList(tail, x)
    }
  }

  println(isElementInList(List(1, 2, 3, 5), 6))
}
