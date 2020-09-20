package week3.LeetCode

object NRepeatedElementInSize2NArray extends App {
  def repeatedNTimes(A: Array[Int]): Int = {
    val hashMap = A.groupBy(identity).mapValues(_.length)
    var ans: Int = 0
    for ((k, e) <- hashMap) {
      if (e > 1) {
        ans = k
      }
    }
    ans
  }
}
