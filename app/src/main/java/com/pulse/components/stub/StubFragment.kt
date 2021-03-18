package com.pulse.components.stub

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.databinding.FragmentStubBinding

class StubFragment : BaseMVVMFragment(R.layout.fragment_stub) {

    private val args by navArgs<StubFragmentArgs>()
    private val binding by viewBinding(FragmentStubBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        ivPicture.setImageResource(args.stubType.imageRes)
        toolbar.toolbar.title = getString(args.stubType.toolbarTitle)
        mtvEmptyTitle.setText(args.stubType.title)
        mtvEmptySubtitle.setText(args.stubType.description)
    }
}