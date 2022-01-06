package com.copropre.common.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

class BalanceView(context: Context, attrs: AttributeSet) : View(context, attrs) {


    var value = 0f
    var average = 0f
    var maxOffset = 0f


    private val paddingSide = 20f

    override fun onDraw(canvas: Canvas?) {
        //draw the View
        val mpaint = Paint()
        mpaint.color = Color.BLACK
        mpaint.strokeWidth = 4f


        //top
        canvas?.drawLine(paddingSide,0f, this.width.toFloat()-paddingSide, 0f, mpaint)
        //bottom
        canvas?.drawLine(paddingSide,this.height.toFloat(), this.width.toFloat()-paddingSide, this.height.toFloat(), mpaint)
        //left
        canvas?.drawLine(paddingSide,0f, paddingSide, this.height.toFloat(), mpaint)
        canvas?.drawLine(0f,this.height.toFloat()/2, paddingSide, this.height.toFloat()/2, mpaint)
        //right
        canvas?.drawLine(this.width.toFloat()-paddingSide,0f, this.width.toFloat()-paddingSide, this.height.toFloat(), mpaint)
        canvas?.drawLine(this.width.toFloat()-paddingSide,this.height.toFloat()/2, this.width.toFloat(), this.height.toFloat()/2, mpaint)

        // value
        var rect = Rect()
        rect.top = 2
        rect.bottom = this.height - 2
        if (value >= average) {
            mpaint.color = Color.GREEN
            rect.left = this.width/2
            if (maxOffset  == 0f) {
                rect.right = this.width/2
            } else {
                rect.right = Math.min(this.width/2 * (1+(value-average)/maxOffset).toInt(), (this.width - paddingSide).toInt() - 2)
            }

        } else {
            mpaint.color = Color.RED
            if (maxOffset == 0f) {
                rect.left = this.width/2
            } else {
                rect.left = Math.max(this.width / 2 * (1 + (value - average) / maxOffset).toInt(), paddingSide.toInt() + 2)
            }
            rect.right = this.width/2
        }
        canvas?.drawRect(rect,mpaint)


        //center

        mpaint.color = Color.BLACK
        for(i in 1..5) { // equivalent of 1 <= i && i <= 5
            canvas?.drawLine(i*this.width.toFloat()/6,0f, i*this.width.toFloat()/6, this.height.toFloat(), mpaint)
        }





    }


}