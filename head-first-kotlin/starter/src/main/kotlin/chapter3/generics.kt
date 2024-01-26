package chapter3

open class Food

class VeganFood: Food()

interface Seller<out T> // 协变

class FoodSeller:Seller<Food>

class VeganFoodSeller:Seller<VeganFood>

interface Consumer<in T> // 逆变

class Person:Consumer<Food>

class Vegan:Consumer<VeganFood>

fun main() {
    var foodSeller:Seller<Food>
    foodSeller = FoodSeller()
    foodSeller = VeganFoodSeller()

    var veganFoodConsumer: Consumer<VeganFood>
    veganFoodConsumer = Vegan()
    veganFoodConsumer = Person()
}