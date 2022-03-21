package com.mcxinyu.echartsandroid.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcxinyu.echartsandroid.sample.databinding.ActivityMainBinding
import org.intellij.lang.annotations.Language

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
        binding.echarts.option = option
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
                  startAngle: 90,
                  label: {
                    formatter: '{b}: {@value}'
                  },
                  labelLine: {
                    length: 0
                  },
                  radius: '80%',
                  data: [
                    { value: 3, name: '离线' },
                    { value: 5, name: '在线' },
                  ]
                }
              ]
            }
    """.trimIndent()
}