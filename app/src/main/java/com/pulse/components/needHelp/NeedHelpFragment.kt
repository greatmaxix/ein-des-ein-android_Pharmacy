package com.pulse.components.needHelp

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.needHelp.adapter.HelpAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.animateVisibleIfNot
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.data.remote.DummyData
import com.pulse.databinding.FragmentNeedHelpBinding
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpFragment(private val viewModel: NeedHelpViewModel) : BaseMVVMFragment(R.layout.fragment_need_help) {

    // TODO change items

    private val helpAdapter by lazy { HelpAdapter() }
    private val binding by viewBinding(FragmentNeedHelpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { clickBack() }
        attachBackPressCallback { clickBack() }

        ivSearch.setDebounceOnClickListener {
            llHeaderContainer.gone()
            viewSearch.animateVisibleIfNot()
            viewSearch.requestFocus()
            toolbar.toolbar.title = getString(R.string.have_questions)
        }
        viewSearch.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                helpAdapter.filter { it.title.contains(text, true).falseIfNull() || it.text.contains(text, true).falseIfNull() }
            }
        }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }

        initHelpList()
    }

    private fun clickBack() = with(binding) {
        if (viewSearch.isVisible) {
            viewSearch.gone()
            llHeaderContainer.animateVisibleIfNot()
            toolbar.toolbar.title = null
        } else {
            navController.popBackStack()
        }
    }

    private fun initHelpList() = with(binding.rvItems) {
        adapter = helpAdapter
        setHasFixedSize(true)

        helpAdapter.notifyDataSet(DummyData.help.onEach { it.isExpanded = false }) // TODO set proper list
    }
}