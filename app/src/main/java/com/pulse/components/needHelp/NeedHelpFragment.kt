package com.pulse.components.needHelp

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.needHelp.NeedHelpFragmentDirections.Companion.fromNeedHelpToContactUs
import com.pulse.components.needHelp.adapter.HelpAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.mapper.HelpMapper
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
                helpAdapter.filter { text.stringResNotContainsIgnoreCase(it.help.title) || text.stringResNotContainsIgnoreCase(it.help.content) }
            }
        }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }
        binding.mcvQuestions.mockToast()
//        binding.mcvQuestions.onClickDebounce {
//            // TODO set phone number to showDial("")
//        }

        initHelpList()
    }

    private fun CharSequence.stringResNotContainsIgnoreCase(@StringRes value: Int) = getString(value).contains(this, true).falseIfNull()

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
        helpAdapter.notifyDataSet(HelpMapper.map())
    }
}