package com.ttenushko.mvi


public interface Converter<I, O> {
    public fun convert(intent: I): O
}