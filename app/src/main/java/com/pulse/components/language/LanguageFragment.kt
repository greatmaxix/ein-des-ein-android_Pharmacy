package com.pulse.components.language

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.language.adapter.LanguagesAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.databinding.FragmentLanguageBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LanguageFragment(private val viewModel: LanguageViewModel) : BaseMVVMFragment(R.layout.fragment_language) {

    private val binding by viewBinding(FragmentLanguageBinding::bind)
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._8sdp) }
    private val languageAdapter = LanguagesAdapter {
        // TODO change language
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        rvLanguage.adapter = languageAdapter
        rvLanguage.addItemDecorator(true, spacing)
    }

    override fun onBindLiveData() {
        observe(viewModel.languageLiveData, languageAdapter::notifyItems)
    }
}