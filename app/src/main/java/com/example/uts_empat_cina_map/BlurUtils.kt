import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

object BlurUtils {

    fun applyBlur(view: View, radius: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val renderScript = RenderScript.create(view.context)
        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val output = Allocation.createTyped(renderScript, input.type)
        val script = ScriptIntrinsicBlur.create(renderScript, input.element)
        script.setRadius(radius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)

        renderScript.destroy()
        return bitmap
    }
}

//Nama : Jackson Lawrence
//NIM : 00000070612
//Mata Kuliah : Mobile Application Programming (LAB) | IF 570 - AL
