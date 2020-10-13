package com.pharmacy.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.pharmacy.R
import com.pharmacy.core.extensions.textColor

class OrderSteps @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val dotSize = context.resources.getDimension(R.dimen._20sdp).toInt()
    private val lineHeight = context.resources.getDimension(R.dimen._4sdp).toInt()
    private val labelTextSize = context.resources.getDimensionPixelSize(R.dimen._10ssp).toFloat()
    private val textTopMargin = context.resources.getDimension(R.dimen._8sdp).toInt()
    private val dotHorizontalMargin = context.resources.getDimension(R.dimen._35sdp).toInt()
    private val viewVerticalMargin = context.resources.getDimension(R.dimen._24sdp).toInt()
    private var selectedStateReached: Boolean = false

    init {
        clipToPadding = false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setSteps(
                arrayListOf(
                    Step(R.string.orderStepsTestText, false),
                    Step(R.string.orderStepsTestText, true),
                    Step(R.string.orderStepsTestText, false),
                    Step(R.string.orderStepsTestText, false)
                )
            )
        }
    }

    fun setSteps(steps: List<Step>) {
        clearState()

        val stepViews = steps.map {
            StepViewHolder(
                it,
                addDotView(),
                addLineView(),
                addLabelView(it)
            )
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(this@OrderSteps)

        var prevStep: StepViewHolder? = null
        stepViews.forEachIndexed { index, holder ->
            val isFirstItem = index == 0
            val isLastItem = index == steps.size - 1

            constraintSet.connect(holder.dot.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, viewVerticalMargin)
            constraintSet.connect(holder.label.id, ConstraintSet.START, holder.dot.id, ConstraintSet.START)
            constraintSet.connect(holder.label.id, ConstraintSet.END, holder.dot.id, ConstraintSet.END)
            constraintSet.connect(holder.label.id, ConstraintSet.TOP, holder.dot.id, ConstraintSet.BOTTOM, textTopMargin)
            constraintSet.connect(holder.label.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, viewVerticalMargin)
            if (!isFirstItem) { // because we need to remove first line as unused
                constraintSet.connect(holder.line.id, ConstraintSet.TOP, holder.dot.id, ConstraintSet.TOP)
                constraintSet.connect(holder.line.id, ConstraintSet.BOTTOM, holder.dot.id, ConstraintSet.BOTTOM)
            }

            if (!selectedStateReached) {
                holder.line.isSelected = true
            }
            if (holder.step.currentState) {
                holder.dot.isSelected = true
                selectedStateReached = true
            } else {
                holder.dot.isEnabled = !selectedStateReached
            }

            when {
                isFirstItem -> {
                    removeView(holder.line) // second step line is first
                    constraintSet.connect(holder.dot.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dotHorizontalMargin)
                }
                isLastItem -> {
                    constraintSet.connect(holder.dot.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dotHorizontalMargin)
                    constraintSet.connect(holder.line.id, ConstraintSet.END, holder.dot.id, ConstraintSet.START)
                    prevStep?.let {
                        constraintSet.connect(holder.line.id, ConstraintSet.START, it.dot.id, ConstraintSet.END)
                    }
                }
                else -> {
                    val nextStep = stepViews[index + 1]
                    if (index == 1) {
                        constraintSet.connect(holder.line.id, ConstraintSet.END, holder.dot.id, ConstraintSet.START)
                        prevStep?.let {
                            constraintSet.connect(holder.line.id, ConstraintSet.START, it.dot.id, ConstraintSet.END)
                        }
                    } else {
                        constraintSet.connect(holder.line.id, ConstraintSet.END, holder.dot.id, ConstraintSet.START)
                        prevStep?.let {
                            constraintSet.connect(holder.line.id, ConstraintSet.START, it.dot.id, ConstraintSet.END)
                        }
                    }
                    constraintSet.connect(holder.dot.id, ConstraintSet.START, holder.line.id, ConstraintSet.END)
                    constraintSet.connect(holder.dot.id, ConstraintSet.END, nextStep.line.id, ConstraintSet.START)
                }
            }
            prevStep = holder
        }
        constraintSet.applyTo(this@OrderSteps)
    }

    private fun clearState() {
        removeAllViews()
        selectedStateReached = false
    }

    private fun addDotView() =
        View(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(dotSize, dotSize)
            background = ContextCompat.getDrawable(context, R.drawable.selector_step_dot)
            addView(this)
        }

    private fun addLineView() =
        View(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(0, lineHeight)
            background = ContextCompat.getDrawable(context, R.drawable.selector_step_line)
            addView(this)
        }

    private fun addLabelView(step: Step) =
        MaterialTextView(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            textColor(R.color.colorGlobalWhite)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)
            text = context.getString(step.name)
            if (step.currentState) setTypeface(typeface, Typeface.BOLD)
            addView(this)
        }

    data class Step(
        @StringRes val name: Int,
        val currentState: Boolean
    )

    private data class StepViewHolder(
        val step: Step,
        val dot: View,
        val line: View,
        val label: MaterialTextView
    )
}