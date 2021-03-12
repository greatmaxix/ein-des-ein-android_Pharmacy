package com.pulse.components.needHelp

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.needHelp.NeedHelpFragmentDirections.Companion.fromNeedHelpToContactUs
import com.pulse.components.needHelp.adapter.HelpAdapter
import com.pulse.components.needHelp.model.Help
import com.pulse.components.needHelp.model.HelpItem
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentNeedHelpBinding
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpFragment(private val viewModel: NeedHelpViewModel) : BaseMVVMFragment(R.layout.fragment_need_help) {

    private val helpAdapter by lazy {
        HelpAdapter {
            navController.navigate(fromNeedHelpToContactUs())
        }
    }
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
                helpAdapter.filter { getString(it.help.title).contains(text, true).falseIfNull() || getString(it.help.content).contains(text, true).falseIfNull() }
            }
        }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }
        binding.mcvQuestions.onClickDebounce {
            showDial("+777777777777") // TODO change phone number
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
        helpAdapter.notifyDataSet(Help.values().map { HelpItem(it, false) })
    }
}