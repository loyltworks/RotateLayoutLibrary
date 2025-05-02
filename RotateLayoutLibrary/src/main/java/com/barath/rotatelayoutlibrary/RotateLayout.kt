package com.barath.rotatelayoutlibrary

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup

class RotateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var rotationAngle = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.RotateLayout, 0, 0).apply {
            try {
                rotationAngle = getInt(R.styleable.RotateLayout_rotate, 0) % 360
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val child = getChildAt(0)

        if (child != null) {
            if (rotationAngle == 90 || rotationAngle == 270) {
                // Swap specs for measuring
                measureChild(child, heightMeasureSpec, widthMeasureSpec)
                val childWidth = child.measuredHeight
                val childHeight = child.measuredWidth
                setMeasuredDimension(childWidth, childHeight)
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                setMeasuredDimension(childWidth, childHeight)
            }
        } else {
            setMeasuredDimension(0, 0)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val child = getChildAt(0) ?: return
        child.layout(0, 0, child.measuredWidth, child.measuredHeight)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        when (rotationAngle) {
            90 -> {
                canvas.translate(width.toFloat(), 0f)
                canvas.rotate(90f)
            }
            180 -> {
                canvas.translate(width.toFloat(), height.toFloat())
                canvas.rotate(180f)
            }
            270 -> {
                canvas.translate(0f, height.toFloat())
                canvas.rotate(270f)
            }
        }

        super.dispatchDraw(canvas)
        canvas.restore()
    }
}
