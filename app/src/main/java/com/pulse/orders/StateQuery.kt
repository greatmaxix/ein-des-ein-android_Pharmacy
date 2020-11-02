package com.pulse.orders

enum class StateQuery(val stateQuery: String) {
    ALL(""), IN_PROGRESS("new"), DONE("done"), CANCELED("canceled")
}