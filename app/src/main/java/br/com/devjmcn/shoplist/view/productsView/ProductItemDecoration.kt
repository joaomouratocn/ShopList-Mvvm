package br.com.devjmcn.shoplist.view.productsView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import br.com.devjmcn.shoplist.R

class ProductItemDecoration:ItemDecoration() {
    private lateinit var categoryMap:Map<Int,Int>
    private val paint = Paint()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val params = view.layoutParams as? RecyclerView.LayoutParams
        if (params == null) {
            super.getItemOffsets(outRect, view, parent, state)
        } else {
            val position = params.viewAdapterPosition
            if (categoryMap.containsKey(position)) {
                outRect.set(0, 80, 0, 0)
            } else {
                super.getItemOffsets(outRect, view, parent, state)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (categoryMap.containsKey(position)) {
                val categoryString = getCategoryString(parent.context, position)
                val bitmap = showHeader(parent, categoryString)
                c.drawBitmap(bitmap, 0.toFloat(), view.top.toFloat() - 100, paint)
            } else {
                super.onDraw(c, parent, state)
            }
        }
    }

    private fun showHeader(parent: RecyclerView, categoryString:String):Bitmap{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hearder_recycle_products_category, parent, false) as TextView
        view.text = categoryString
        val bitmap = Bitmap.createBitmap(parent.width, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, parent.width, parent.height)
        view.draw(canvas)
        return bitmap
    }

    private fun getCategoryString(context: Context, position: Int):String{
        return context.resources.getStringArray(R.array.categories)[categoryMap.getValue(position)]
    }

    fun loadCategory(categoryMap: Map<Int, Int>) {
        this.categoryMap = categoryMap
    }
}