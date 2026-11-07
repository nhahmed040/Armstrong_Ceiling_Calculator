package com.armstrong.ceilingcalculator

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.*
import android.widget.*
import android.text.InputType
import kotlin.math.*

class MainActivity : Activity() {
    private lateinit var length: EditText
    private lateinit var width: EditText
    private lateinit var mode: Spinner
    private lateinit var plan: PlanView
    private lateinit var report: TextView
    private lateinit var runnerPrice: EditText
    private lateinit var anglePrice: EditText
    private lateinit var longPrice: EditText
    private lateinit var shortPrice: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 14, 16, 12)
            setBackgroundColor(Color.rgb(247,249,252))
        }
        page.addView(TextView(this).apply {
            text = "ARMSTRONG CEILING CALCULATOR"
            textSize = 20f; setTextColor(Color.rgb(22,48,78))
            typeface = Typeface.create("sans-serif", Typeface.BOLD); letterSpacing = .025f
        })
        page.addView(label("Room dimensions (metres)"))
        val dimensions = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        length = field("6.0", "Length")
        width = field("6.0", "Width")
        dimensions.addView(length, LinearLayout.LayoutParams(0,-2,1f))
        dimensions.addView(width, LinearLayout.LayoutParams(0,-2,1f))
        page.addView(dimensions)
        page.addView(label("Grid arrangement"))
        mode = Spinner(this)
        mode.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Balanced Centered","Corner Bound"))
        page.addView(mode)
        page.addView(label("Plan view  •  Blue: main runners  •  Grey dashed: cross tees  •  Red: wall angle"))
        plan = PlanView(this)
        page.addView(plan, LinearLayout.LayoutParams(-1,0,1f))
        val actions = LinearLayout(this)
        actions.addView(Button(this).apply { text="Calculate"; setOnClickListener { calculate() } }, LinearLayout.LayoutParams(0,-2,1f))
        actions.addView(Button(this).apply { text="Reset view"; setOnClickListener { plan.reset() } }, LinearLayout.LayoutParams(0,-2,1f))
        page.addView(actions)
        page.addView(label("Editable material prices (ETB)"))
        val prices = LinearLayout(this)
        runnerPrice=field("700","Runner 3.6m")
        anglePrice=field("450","Angle 3.0m")
        longPrice=field("300","1200 tee")
        shortPrice=field("200","600 tee")
        listOf(runnerPrice,anglePrice,longPrice,shortPrice).forEach { prices.addView(it,LinearLayout.LayoutParams(0,-2,1f)) }
        page.addView(prices)
        report = TextView(this).apply {
            textSize=14f; setTextColor(Color.rgb(42,55,72)); setPadding(12,12,12,12)
            setBackgroundColor(Color.WHITE); typeface=Typeface.create("sans-serif",Typeface.NORMAL)
        }
        page.addView(report)
        val scroll=ScrollView(this); scroll.addView(page); setContentView(scroll)
        calculate()
    }

    private fun label(s:String)=TextView(this).apply {
        text=s; textSize=13f; setTextColor(Color.rgb(70,85,105)); setPadding(0,11,0,4)
        typeface=Typeface.create("sans-serif-medium",Typeface.NORMAL)
    }
    private fun field(v:String,h:String)=EditText(this).apply {
        setText(v); hint=h; textSize=13f; setSingleLine(true)
        inputType=InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        setPadding(6,4,6,4)
    }
    private fun value(e:EditText, fallback:Double)=e.text.toString().toDoubleOrNull()?.takeIf{it>0}?:fallback

    private fun calculate() {
        val l=value(length,6.0); val w=value(width,6.0)
        val g=LayoutEngine.make(l,w,mode.selectedItemPosition==0)
        plan.update(g,l,w)
        val rp=value(runnerPrice,700.0); val ap=value(anglePrice,450.0)
        val lp=value(longPrice,300.0); val sp=value(shortPrice,200.0)
        val area=l*w
        val boards=ceil(area/.36).toInt()
        val c1=g.runnerBars*rp; val c2=g.angleBars*ap; val c3=g.longCount*lp; val c4=g.shortCount*sp
        report.text = "TAKEOFF SUMMARY\nArea: %.2f m²    Perimeter: %.2f m\n".format(area,2*(l+w)) +
            "600 × 600 mm ceiling boards: $boards pcs (area-based)\n" +
            "Main runners: ${g.runnerBars} bars × 3.6 m\nWall angle: ${g.angleBars} bars × 3.0 m\n" +
            "1200 mm cross tees: ${g.longCount} pcs\n600 mm cross tees: ${g.shortCount} pcs\n\n" +
            "Estimated grid material cost: ETB ${"%.2f".format(c1+c2+c3+c4)}\n" +
            "Runners ${"%.0f".format(c1)}  •  Wall angle ${"%.0f".format(c2)}\n" +
            "1200 tees ${"%.0f".format(c3)}  •  600 tees ${"%.0f".format(c4)}\n\n" +
            "Preliminary estimate only. Confirm the detailed cut plan and reusable offcuts before ordering."
    }
}

data class GridLayout(val mainX:List<Float>,val teeY:List<Float>,val shortX:List<Float>,val runnerBars:Int,val angleBars:Int,val longCount:Int,val shortCount:Int)

object LayoutEngine {
    fun make(length:Double,width:Double,centered:Boolean):GridLayout {
        val xs= mutableListOf<Float>()
        if(centered) {
            val bays=max(1,ceil(width/1.2).toInt())
            val first=((width-(bays-1)*1.2)/2.0).coerceIn(0.0,1.2)
            var x=first
            while(x<width-.001){if(x>.001)xs.add(x.toFloat());x+=1.2}
        } else {
            var x=1.2
            while(x<width-.001){xs.add(x.toFloat());x+=1.2}
        }
        val ys= mutableListOf<Float>()
        var y=.6
        while(y<length-.001){ys.add(y.toFloat());y+=.6}
        val shortXs= mutableListOf<Float>()
        var x=.6
        while(x<width-.001){shortXs.add(x.toFloat());x+=1.2}
        val runnerMetres=(xs.size+1)*length
        val runnerBars=ceil(runnerMetres/3.6).toInt()
        val angleBars=ceil(2*(length+width)/3.0).toInt()
        val longCount=ys.size*max(1,xs.size+1)
        val shortCount=shortXs.size*max(1,ceil(length/1.2).toInt())
        return GridLayout(xs,ys,shortXs,runnerBars,angleBars,longCount,shortCount)
    }
}

class PlanView(context:android.content.Context):View(context) {
    private var l=6.0; private var w=6.0
    private var layout=LayoutEngine.make(6.0,6.0,true)
    private var zoom=1f; private var panX=0f; private var panY=0f
    private var oldX=0f; private var oldY=0f
    private val scaleDetector=ScaleGestureDetector(context,object:ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(d:ScaleGestureDetector):Boolean { zoom=(zoom*d.scaleFactor).coerceIn(.5f,5f);invalidate();return true }
    })
    private val wall=Paint(3).apply{color=Color.rgb(204,48,58);style=Paint.Style.STROKE;strokeWidth=4f}
    private val main=Paint(3).apply{color=Color.rgb(31,105,205);style=Paint.Style.STROKE;strokeWidth=5f}
    private val tee=Paint(3).apply{color=Color.rgb(111,122,136);style=Paint.Style.STROKE;strokeWidth=2.5f;pathEffect=DashPathEffect(floatArrayOf(8f,6f),0f)}
    private val text=Paint(3).apply{color=Color.rgb(45,59,78);textSize=22f;typeface=Typeface.create("sans-serif-medium",0)}
    fun update(g:GridLayout,length:Double,width:Double){layout=g;l=length;w=width;zoom=1f;panX=0f;panY=0f;invalidate()}
    fun reset(){zoom=1f;panX=0f;panY=0f;invalidate()}
    override fun onDraw(c:Canvas){
        super.onDraw(c);c.drawColor(Color.rgb(250,251,253))
        val unit=min((width-90f).toDouble()/w,(height-90f).toDouble()/l).coerceAtLeast(1.0).toFloat()
        val left=(width-w.toFloat()*unit)/2f+panX
        val top=(height-l.toFloat()*unit)/2f+panY
        c.save();c.translate(left,top);c.scale(zoom,zoom)
        val wp=(w*unit).toFloat();val hp=(l*unit).toFloat()
        c.drawColor(Color.WHITE);c.drawRect(0f,0f,wp,hp,wall)
        layout.mainX.forEach{c.drawLine(it*unit,0f,it*unit,hp,main)}
        layout.teeY.forEach{c.drawLine(0f,it*unit,wp,it*unit,tee)}
        layout.shortX.forEach{x->var y=.0;while(y<l-.001){
            val y1=(y+.0).coerceAtLeast(0.0);val y2=(y+.6).coerceAtMost(l)
            c.drawLine(x*unit,y1.toFloat()*unit,x*unit,y2.toFloat()*unit,tee);y+=1.2
        }}
        c.restore()
        text.textSize=22f;c.drawText("%.2f m".format(w),width/2f-32f,top-12f,text)
        c.save();c.rotate(-90f,left-18f,height/2f);c.drawText("%.2f m".format(l),left-18f,height/2f,text);c.restore()
        text.textSize=15f;c.drawText("Drag to pan  •  Pinch with two fingers to zoom",12f,height-12f,text)
    }
    override fun onTouchEvent(e:MotionEvent):Boolean{
        scaleDetector.onTouchEvent(e)
        when(e.actionMasked){
            MotionEvent.ACTION_DOWN->{oldX=e.x;oldY=e.y}
            MotionEvent.ACTION_MOVE->if(e.pointerCount==1&&!scaleDetector.isInProgress){panX+=e.x-oldX;panY+=e.y-oldY;oldX=e.x;oldY=e.y;invalidate()}
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->performClick()
        };return true
    }
    override fun performClick():Boolean{super.performClick();return true}
}
