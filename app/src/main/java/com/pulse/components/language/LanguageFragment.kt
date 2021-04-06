package com.pulse.components.language

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.language.adapter.LanguagesAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.observe
import com.pulse.core.locale.LocaleEnum
import com.pulse.databinding.FragmentLanguageBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LanguageFragment : BaseToolbarFragment<LanguageViewModel>(R.layout.fragment_language, LanguageViewModel::class) {

    private val binding by viewBinding(FragmentLanguageBinding::bind)
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._8sdp) }
    private val languageAdapter = LanguagesAdapter(::changeLanguage)

    override fun initUI() = with(binding) {
        showBackButton()
        rvLanguage.adapter = languageAdapter
        rvLanguage.addItemDecorator(true, spacing)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.languageState, languageAdapter::notifyItems)
    }

    private fun changeLanguage(locale: LocaleEnum) = viewModel.setLanguage(locale)
}