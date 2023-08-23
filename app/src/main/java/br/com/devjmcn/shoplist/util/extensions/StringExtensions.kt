package br.com.devjmcn.shoplist.util.extensions

import br.com.devjmcn.shoplist.util.numberFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

fun String.upFirstChar():String{
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT)
        else it.toString()
    }
}

fun String.toBigDecimalFormat(): BigDecimal {
    val replace =
        numberFormat.currency?.let { String.format("[%s,.\\s]", it.symbol) }

    val cleanString = replace?.let { this.replace(it.toRegex(), "") }
    return BigDecimal(cleanString).setScale(
        2,RoundingMode.FLOOR
    ).divide(
        BigDecimal(100), RoundingMode.FLOOR
    )
}