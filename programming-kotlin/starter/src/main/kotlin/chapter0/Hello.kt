package chapter0
fun main() {
    var func:(String) -> Int = {
        println(it)
        10
    }
    func("hello")
}