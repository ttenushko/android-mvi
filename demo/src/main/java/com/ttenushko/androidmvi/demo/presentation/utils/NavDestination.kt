package com.ttenushko.androidmvi.demo.presentation.utils


import androidx.collection.SparseArrayCompat
import androidx.navigation.NavAction
import androidx.navigation.NavDestination
import java.lang.reflect.Field

fun NavDestination.getActionIdByDestinationId(destinationId: Int): Int? =
    actions()?.getActionIdByDestinationId(destinationId)

private fun Map<Int, NavAction>.getActionIdByDestinationId(destinationId: Int): Int? =
    entries.firstOrNull { it.value.destinationId == destinationId }?.key

@Suppress("UNCHECKED_CAST")
private fun NavDestination.actions(): Map<Int, NavAction>? =
    try {
        this.findField { "mActions" == it.name }?.let {
            val map = HashMap<Int, NavAction>()
            val isAccessible = it.isAccessible
            it.isAccessible = true
            (it.get(this) as SparseArrayCompat<NavAction>).let { sparseArray ->
                for (i in 0 until sparseArray.size()) {
                    val key = sparseArray.keyAt(i)
                    val value = sparseArray.get(key)!!
                    map[key] = value
                }
            }
            it.isAccessible = isAccessible
            map
        }
    } catch (error: Throwable) {
        null
    }

private fun Any.findField(predicate: (Field) -> Boolean): Field? {
    var clazz: Class<*>? = this::class.java
    while (clazz != Any::class.java) {
        clazz!!.declaredFields.forEach { field ->
            if (predicate(field)) {
                return field
            }
        }
        clazz = clazz.superclass
    }
    return null
}
