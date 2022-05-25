package com.mvvm.basic.support.inline

//?.let{}.orElse{}
inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}