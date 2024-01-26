package chapter1

fun doSometing(msg:String,i:Int) : Unit {
    if (i > 0) {
        var x = 0
        while (x < i) {
            println(msg)
            x++
        }
    }
}

fun timesThree(x:Int):Int {
    var y = x * 3
    return y
}


fun maxValue(args : Array<Int>):Int {
    var max = args[0]
    var x = 1
    while (x < args.size) {
        var item = args[x]
        max = if (max >= item) max else item
        x = x + 1
    }
    return max
}

fun main(args: Array<String>) {
    doSometing("Hello",3)
    println(timesThree(5))
    maxValue(arrayOf(1,2,3,4,5))
    maxValue(arrayOf(5,4,3,2,1))
}