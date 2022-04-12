package com.mcxinyu.echartsandroid.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mcxinyu.echartsandroid.sample.databinding.ActivityMainBinding
import com.mcxinyu.echartsandroid.webview.JavaScriptInterface
import com.mcxinyu.echartsandroid.webview.addJavascriptInterface
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

        binding.switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            delegate.localNightMode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        }

        binding.m = option

//        interactWithJs(binding)

        binding.echarts.postDelayed({
//            binding.echarts.setThemeScript("'dark'") {
//                interactWithJs(binding)
//            }
        }, 3000)
    }

    private fun interactWithJs(binding: ActivityMainBinding) {
        binding.echarts.addJavascriptInterface(JavaScriptInterface("Messenger") {
            runOnUiThread {
                Toast.makeText(this, it ?: "just-call-on-message", Toast.LENGTH_SHORT).show()
            }

            null
        })

        binding.echarts.runOnChecked {
            binding.echarts.evaluateJavascript(
                """javascript:
                // Messenger.postMessage(${binding.echarts.jsChartName}.getWidth());
                ${binding.echarts.jsChartName}.on('click', 'series', (params) => {
                    Messenger.postMessage(JSON.stringify({
                      type: 'showToast',
                      payload: params.data,
                    }));
                });
                void(0);
            """.trimIndent()
            ) {}
        }
    }

    @Language("js")
    val option = """
        {
          title: {
            text: 'Accumulated Waterfall Chart'
          },
          legend: {
            data: ['Expenses', 'Income'],
            top: 24
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: (function () {
              let list = [];
              for (let i = 1; i <= 11; i++) {
                list.push('Nov ' + i);
              }
              return list;
            })()
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: 'Placeholder',
              type: 'bar',
              stack: 'Total',
              itemStyle: {
                borderColor: 'transparent',
                color: 'transparent'
              },
              emphasis: {
                itemStyle: {
                  borderColor: 'transparent',
                  color: 'transparent'
                }
              },
              data: [0, 900, 1245, 1530, 1376, 1376, 1511, 1689, 1856, 1495, 1292]
            },
            {
              name: 'Income',
              type: 'bar',
              stack: 'Total',
              label: {
                show: true,
                position: 'top'
              },
              data: [900, 345, 393, '-', '-', 135, 178, 286, '-', '-', '-']
            },
            {
              name: 'Expenses',
              type: 'bar',
              stack: 'Total',
              label: {
                show: true,
                position: 'bottom'
              },
              data: ['-', '-', '-', 108, 154, '-', '-', '-', 119, 361, 203]
            }
          ]
        }
    """.trimIndent()
}