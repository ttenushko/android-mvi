package com.ttenushko.mvi


public interface Dispatcher<T> {
    public fun dispatch(value: T)
}