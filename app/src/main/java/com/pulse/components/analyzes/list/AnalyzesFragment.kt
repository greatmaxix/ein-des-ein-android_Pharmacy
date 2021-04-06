package com.pulse.components.analyzes.list

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.list.AnalyzesFragmentDirections.Companion.fromAnalyzesToAnalyzeOrder
import com.pulse.components.analyzes.list.AnalyzesFragmentDirections.Companion.globalToAnalyzeCategories
import com.pulse.components.analyzes.list.adapter.AnalyzesAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentAnalyzesBinding
import org.koin.core.component.KoinApiExtension
import kotlin.random.Random

@KoinApiExtension
class AnalyzesFragment : BaseToolbarFragment<AnalyzesViewModel>(R.layout.fragment_analyzes, AnalyzesViewModel::class, R.menu.add) {

    private val binding by viewBinding(FragmentAnalyzesBinding::bind)
    private val analyzesAdapter = AnalyzesAdapter(
        { navController.navigate(fromAnalyzesToAnalyzeOrder(it)) },
        ::showDial
    )

    override fun initUI() = with(binding) {
        showBackButton()

        rvAnalyzes.adapter = analyzesAdapter
        viewEmptyContent.setButtonAction {
            navController.navigate(globalToAnalyzeCategories())
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.analyzesListState) {
            val isEmpty = Random.nextBoolean()
            binding.viewEmptyContent.visibleOrGone(isEmpty)
            binding.rvAnalyzes.visibleOrGone(!isEmpty)
            analyzesAdapter.notifyItems(it)
        }
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(menuItemsFlow) {
            if (it.itemId == R.id.add) navController.navigate(globalToAnalyzeCategories())
        }
    }
}