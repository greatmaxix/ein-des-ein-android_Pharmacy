package com.pulse.components.stub

import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val stubModule = module {

    fragment { StubFragment() }
}