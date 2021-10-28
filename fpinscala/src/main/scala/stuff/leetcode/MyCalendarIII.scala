package stuff.leetcode

import scala.collection.mutable
import scala.collection.mutable.TreeMap

// https://leetcode.com/problems/my-calendar-iii/
object MyCalendarIII extends App {

  class MyCalendarThree() {

    val map: mutable.TreeMap[Int, Int] = mutable.TreeMap.empty

    def book(start: Int, end: Int): Int = {
      map.put(start, map.getOrElse(start, 0) + 1)
      map.put(end, map.getOrElse(end, 0) - 1)

      val (_, ans) = map.values.foldLeft((0, 0)) {
        case ((active, ans), value) =>
          (active + value, Math.max(active, ans))
      }
      ans
    }

  }

  val myCalendarThree = new MyCalendarIII.MyCalendarThree

  println(myCalendarThree.book(24, 40))
  println(myCalendarThree.book(43, 50))
  println(myCalendarThree.book(27, 43))
  println(myCalendarThree.book(5, 21))
  println(myCalendarThree.book(30, 40))
  println(myCalendarThree.book(14, 29))
  println(myCalendarThree.book(3, 19))
  println(myCalendarThree.book(3, 14))
  println(myCalendarThree.book(25, 39))
  println(myCalendarThree.book(6, 19))

  println(myCalendarThree.map)

}
