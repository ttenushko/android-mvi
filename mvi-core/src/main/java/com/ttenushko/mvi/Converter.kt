package com.ttenushko.mvi


public interface Converter<I, O> {
    public fun convert(input: I): O
}

public fun <I, O> converter(block: (I) -> O): Converter<I, O> =
    object : Converter<I, O> {
        override fun convert(input: I): O {
            return block(input)
        }
    }