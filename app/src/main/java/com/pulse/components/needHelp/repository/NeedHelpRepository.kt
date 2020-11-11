package com.pulse.components.needHelp.repository

class NeedHelpRepository(
    private val rds: NeedHelpRemoteDataSource,
    private val lds: NeedHelpLocalDataSource
)