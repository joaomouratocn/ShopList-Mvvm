package br.com.devjmcn.shoplist.util

import java.text.NumberFormat
import java.util.Locale

val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())