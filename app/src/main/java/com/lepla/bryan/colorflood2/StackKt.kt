package com.lepla.bryan.colorflood2

import java.util.*

class StackKt<T>()  {
    private val _stack = Stack<T>()

    constructor(list: List<T>) : this() { list.forEach {_stack.push(it)} }

    constructor(elem : T) : this() { _stack.push(elem) }

    //fun pop() = _stack.pop()
    fun peek() = _stack.peek()!!
    fun push(elem: T) = _stack.push(elem)!!
    //fun isEmpty() = _stack.isEmpty()
    //fun clear() = _stack.clear()
    fun toList() = _stack.toList()
}