package com.pulse.util

import timber.log.Timber

/* todo
* почитать можно тут
* если коротко то основной смысл в том что на лог можно кликнуть и он перейдет на место в коде где он был вызван
* https://proandroiddev.com/android-logging-on-steroids-clickable-logs-with-location-info-de1a5c16e86f
* если не понравится выпилю */

class HyperlinkedDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement) = element.run { "($fileName:$lineNumber)$methodName()" }

}