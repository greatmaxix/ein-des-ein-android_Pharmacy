package com.pharmacy.myapp.orders

enum class StateQuery(val stateQuery: String) {
    ALL(""), IN_PROGRESS("in_progress"), DONE("done"), CANCELED("canceled")
}