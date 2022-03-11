package com.mcxinyu.echartsandroid

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.mcxinyu.echartsandroid.databinding.ActivityMainBinding
import org.intellij.lang.annotations.Language
import kotlin.math.log

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.echarts.setBackgroundColor(Color.parseColor("#00000000"))
        binding.echarts.setOption(option)
    }

    @Language("js")
    val option = """
            {
              legend: {
                orient: 'vertical',
                right: 10,
                top: 'middle',
                selectedMode: false
              },
              series: [
                {
                  name: '设备在线情况',
                  type: 'pie',
                  startAngle: 45,
                  label: {
                    formatter: '{b}: {@value}'
                  },
                  labelLine: {
                    length: 0
                  },
                  radius: '80%',
                  data: [
                    { value: 0, name: '离线' },
                    { value: 5, name: '在线' },
                  ]
                }
              ]
            }
    """.trimIndent()
}