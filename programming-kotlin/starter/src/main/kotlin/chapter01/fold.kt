package chapter01

/**
 * 常见高阶函数
 * fold函数：累加函数，与reduce方法类似，不过可以设置初始值，
 * 如果初始值可以是StringBuilder，可用来拼接字符串，
 * 参数acc的类型与初始值一致
 */
fun factorial2(n: Int): Int { //求阶乘
    if (n == 0) return 1
    return (1..n).reduce { acc, i -> acc * i }
}

fun main(args: Array<String>) {
    println("打印0到6的阶乘")
    (0..6).map(::factorial2).forEach(::println) //分别求0到6的阶乘再遍历打印

    val list = listOf(1,2,3,4)
    println("打印1到4的和")
    println(list.fold(0, {acc, i -> acc + i })) //以0为初始值，求1到4的和

    println("打印0到6求得各自阶乘后的和")
    //fold方法相比reduce方法类似，不过可以设置初始值
    println((0..6).map(::factorial2).fold(5) { acc, i -> acc + i })

    println("打印0到6求得各自阶乘后拼接的字符串")
    //分别求0到6的阶乘再让阶乘的值之后拼接字符串再打印
    println((0..6).map(::factorial2).fold(StringBuilder()) { acc, i ->
        acc.append(i).append(",")
    })

    println("打印0到6逆序求得各自阶乘后拼接的字符串")
    //foldRight是fold的逆序操作
    println((0..6).map(::factorial2).foldRight(StringBuilder()) { i, acc ->
        acc.append(i).append(",")
    })

    println((1..6).joinToString(",")) //拼接字符串的另一种方法
}