package com.ttenushko.mvi


public fun interface Converter<I, O> {
    public fun convert(input: I): O
}
