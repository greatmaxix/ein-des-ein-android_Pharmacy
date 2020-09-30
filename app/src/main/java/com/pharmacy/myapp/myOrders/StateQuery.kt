package com.pharmacy.myapp.myOrders

enum class StateQuery(val stateQuery: String) {
    ALL(""), IN_PROGRESS("in_progress"), DONE("done"), CANCELED("canceled")
}