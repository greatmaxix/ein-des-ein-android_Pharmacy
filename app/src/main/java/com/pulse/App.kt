package com.pulse

import android.app.Application
import com.pulse.core.extensions.debug
import com.pulse.util.HyperlinkedDebugTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import timber.log.Timber
import java.util.*

@KoinExperimentalAPI
@ExperimentalCoroutinesApi
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initCountryCodeMapping()

        startKoin {
            androidContext(this@App)
            /*logger(AndroidLogger())*/
            /*androidLogger(Level.DEBUG)*/ //TODO Too many logs in the console
            logger(EmptyLogger())
            fragmentFactory()
            modules(Modules.listOfModules)
        }


        debug { Timber.plant(HyperlinkedDebugTree()) }
    }

    //TODO Find better solution to get country name from ISO3
    companion object {
        var localeMap: MutableMap<String, Locale> = hashMapOf()
            private set

        private fun initCountryCodeMapping() {
            val countries = Locale.getISOCountries()
            localeMap = HashMap(countries.size)
            for (country in countries) {
                val locale = Locale("", country)
                (localeMap as HashMap<String, Locale>)[locale.isO3Country.toUpperCase()] = locale
            }
        }
    }
}