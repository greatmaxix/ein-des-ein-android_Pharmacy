package com.pulse.components.analyzes.list

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.list.AnalyzesFragmentDirections.Companion.fromAnalyzesToAnalyzeOrder
import com.pulse.components.analyzes.list.AnalyzesFragmentDirections.Companion.globalToAnalyzeCategories
import com.pulse.components.analyzes.list.adapter.AnalyzesAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentAnalyzesBinding
import org.koin.core.component.KoinApiExtension
import kotlin.random.Random

@KoinApiExtension
class AnalyzesFragment(val viewModel: AnalyzesViewModel) : BaseMVVMFragment(R.layout.fragment_analyzes) {

    private val binding by viewBinding(FragmentAnalyzesBinding::bind)
    private val analyzesAdapter = AnalyzesAdapter(
        { navController.navigate(fromAnalyzesToAnalyzeOrder(it)) },
        ::showDial
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.add) {
            if (it.itemId == R.id.add) {
                navController.navigate(globalToAnalyzeCategories())
            }
            true
        }

        rvAnalyzes.adapter = analyzesAdapter
        viewEmptyContent.setButtonAction {
            navController.navigate(globalToAnalyzeCategories())
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(viewModel.analyzesListLiveData) {
            val isEmpty = Random.nextBoolean()
            binding.viewEmptyContent.visibleOrGone(isEmpty)
            binding.rvAnalyzes.visibleOrGone(!isEmpty)
            analyzesAdapter.notifyItems(it)
        }
    }
}