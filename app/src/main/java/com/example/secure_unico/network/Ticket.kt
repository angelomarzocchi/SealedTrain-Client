package com.example.secure_unico.network


class Ticket(

    val startValidation: String,
    val type: String,
    val startingPoint: String,
    val endingPoint: String,
    val qrcode: String,
    val id: String


) {
    fun getTicketType(): String {
       return when (type) {
          "FULL_YEAR" -> "Full Year"
          "SEVEN_DAYS" -> "Seven Days"
          "ONE_DAY" -> "One Day"
          else -> "Error"
       }
    }

    fun getValidation(): String {
       return "${startValidation.subSequence(0,10)}\n${startValidation.subSequence(11,startValidation.length)}"
    }

    fun getRoute(): String {
       return "$startingPoint - $endingPoint"
    }
 }
