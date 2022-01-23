package com.lepla.bryan.colorflood2

class StackKt<T>()  {
    private val data = mutableListOf<T>()

    constructor(list: List<T>) : this() { list.forEach {data.add(it)} }

    constructor(elem : T) : this() { data.add(elem) }

    fun pop() = data.removeLast()
    fun peek() = data.last()!!
    fun push(elem: T) = data.add(elem)!!
    fun isEmpty() = data.isEmpty()
    fun clear() = data.clear()
    fun toList() = data.toList()
}