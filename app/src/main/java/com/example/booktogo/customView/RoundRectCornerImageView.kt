package com.example.booktogo.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.example.booktogo.R

@SuppressLint("AppCompatCustomView")
class RoundRectCornerImageView : ImageView {
    private var radius : Float = 0.0f
    private lateinit var path : Path
    private lateinit var rect : RectF
    constructor(context: Context?) : super(context){
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(attrs)
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    fun init(set: AttributeSet?){
        path = Path()
        if (set == null){
            return
        }
        var ta : TypedArray = context.obtainStyledAttributes(set, R.styleable.CircleImage)
        radius = ta.getFloat(R.styleable.CircleImage_img_radius,18.0f)
        ta.recycle()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas){
        rect = RectF(0f,0f, this.width.toFloat(), this.height.toFloat())
        path.addRoundRect(rect,radius,radius,Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}