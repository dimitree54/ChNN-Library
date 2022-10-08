package we.rashchenko.utils

import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.HashSet

class UniqueQueue<T>: Queue<T>{
    private val queue = ArrayDeque<T>()
    private val set = HashSet<T>()

    override val size: Int
        get() = queue.size

    override fun contains(element: T): Boolean {
        return set.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return set.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    override fun iterator(): MutableIterator<T> {
        return queue.iterator()
    }

    override fun add(element: T): Boolean {
        if (set.contains(element)) {
            return false
        }
        return queue.add(element).also {
            if (it) {
                set.add(element)
            }
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        set.addAll(elements)
        return queue.addAll(elements)
    }

    override fun clear() {
        queue.clear()
        set.clear()
    }

    override fun element(): T {
        return queue.first()
    }

    override fun offer(element: T): Boolean {
        return add(element)
    }

    override fun peek(): T? {
        return try {
            element()
        } catch (e: NoSuchElementException) {
            null
        }
    }

    override fun poll(): T? {
        return try {
            remove()
        } catch (e: NoSuchElementException) {
            null
        }
    }

    override fun remove(): T {
        return queue.removeFirst().also { set.remove(it) }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return elements.map { remove(it) }.any { it }
    }

    override fun remove(element: T): Boolean {
        return queue.remove(element).also{
            if (it) {
                set.remove(element)
            }
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return set.retainAll(elements.toSet())
    }

    override fun toString(): String {
        return queue.toString()
    }
}