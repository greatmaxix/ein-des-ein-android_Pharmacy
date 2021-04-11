package com.pulse.components.stub

import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.databinding.FragmentStubBinding

class StubFragment : BaseToolbarFragment<StubViewModel>(R.layout.fragment_stub, StubViewModel::class) {

    private val args by navArgs<StubFragmentArgs>()
    private val binding by viewBinding(FragmentStubBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        ivPicture.setImageResource(args.stubType.imageRes)
        setTitle(getString(args.stubType.toolbarTitle))
        mtvEmptyTitle.setText(args.stubType.title)
        mtvEmptySubtitle.setText(args.stubType.description)
    }
}