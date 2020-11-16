package com.pulse.components.needHelp

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.pulse.R
import com.pulse.components.needHelp.adapter.HelpAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.animateVisibleIfNot
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.data.remote.DummyData
import kotlinx.android.synthetic.main.fragment_need_help.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpFragment(private val vm: NeedHelpViewModel) : BaseMVVMFragment(R.layout.fragment_need_help) {

    // TODO change items

    private val helpAdapter by lazy { HelpAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { clickBack() }

        ivSearch.setDebounceOnClickListener {
            llHeaderContainer.gone()
            searchViewNeedHelp.animateVisibleIfNot()
            searchViewNeedHelp.requestFocus()
            toolbar?.title = getString(R.string.have_questions)
        }

        attachBackPressCallback { clickBack() }

        searchViewNeedHelp.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                helpAdapter.filter { it.title.contains(text, true).falseIfNull() || it.text.contains(text, true).falseIfNull() }
            }
        }
        searchViewNeedHelp.onBackClick = {
            requireActivity().onBackPressed()
        }

        initHelpList()
    }

    private fun clickBack() {
        if (searchViewNeedHelp.isVisible) {
            searchViewNeedHelp.gone()
            llHeaderContainer.animateVisibleIfNot()
            toolbar?.title = null
        } else {
            navController.popBackStack()
        }
    }

    private fun initHelpList() {
        rvItemsNeedHelp.adapter = helpAdapter
        rvItemsNeedHelp.setHasFixedSize(true)

        helpAdapter.notifyDataSet(DummyData.help.apply { forEach { it.isExpanded = false } }) // TODO set proper list
    }
}