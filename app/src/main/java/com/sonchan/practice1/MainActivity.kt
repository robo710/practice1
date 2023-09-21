package com.sonchan.practice1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.sonchan.practice1.databinding.ActivityEndBinding
import com.sonchan.practice1.databinding.ActivityMainBinding
import com.sonchan.practice1.databinding.ActivityStartBinding
import kotlinx.coroutines.delay
import java.util.Random
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    var pnum = 3
    var k = 1
    val point_list = mutableListOf<Float>()
    var isBlind = false
    val btn = listOf<Button>(binding1.backBtn, binding1.btnMain)

    private lateinit var binding1: ActivityMainBinding
    private lateinit var binding2: ActivityStartBinding
    private lateinit var binding3: ActivityEndBinding

    fun start(){
        binding2 = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        binding2.btnBlind.setOnClickListener {
            isBlind = !isBlind
            if(isBlind == true) {
                binding2.btnBlind.text = "Blind Mod ON"
            } else {
                binding2.btnBlind.text = "Blind Mod Off"
            }
        }

        binding2.pNum.text = pnum.toString()

        binding2.btnMinus.setOnClickListener {
            pnum--
            if(pnum <= 0){
                pnum = 1
            }
            binding2.pNum.text = pnum.toString()
        }
        binding2.btnPlus.setOnClickListener {
            pnum++
            binding2.pNum.text = pnum.toString()
        }
        binding2.btnStart.setOnClickListener {
            main()
        }
    }

    fun main(){
        binding1 = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding1.root)

        var timerTask: Timer? = null
        var stage:Int = 1
        var second:Int = 0
        val random = Random()
        val num = random.nextInt(1001)

        binding1.randomNum.text = (num.toFloat()/100).toString()
        binding1.btnMain.text = "시작"
        binding1.people.text = "${k}번 참가자"

        binding1.backBtn.setOnClickListener {
            point_list.clear()
            k = 1
            timerTask?.cancel()
            start()
        }

        binding1.btnMain.setOnClickListener {
            stage++
            if(stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    second++
                    runOnUiThread {
                        if(isBlind == false){
                            binding1.timer.text = (second.toFloat()/100).toString()
                        } else if(isBlind == true && stage == 2){
                            binding1.timer.text = "???"
                        }
                    }
                }
                binding1.btnMain.text = "정지"
            } else if(stage == 3) {
                binding1.timer.text = (second.toFloat()/100).toString()
                timerTask?.cancel()
                val point = abs(second - num).toFloat()/100
                point_list.add(point)
                binding1.point.text = ("point : " + point.toString())
                binding1.btnMain.text = "다음"
                stage = 0
            } else if(stage ==  1) {
                if(k < pnum){
                    k++
                    main()
                } else{
                    end()
                }
            }
        }
    }

    fun end(){
        binding3 = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding3.root)

        binding3.pNum.text = point_list.max().toString()
        var index_last = point_list.indexOf(point_list.max())
        binding3.player.text = (index_last + 1).toString() + "번 참가자"

        binding3.btnBack.setOnClickListener {
            point_list.clear()
            k = 1
            start()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start()
    }
}