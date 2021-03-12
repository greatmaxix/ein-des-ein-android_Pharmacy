package com.pulse.data.mapper

import com.pulse.components.needHelp.model.Help
import com.pulse.components.needHelp.model.HelpAdapterModel

object HelpMapper {

    fun map() = Help.values().map { HelpAdapterModel(it, false) }
}