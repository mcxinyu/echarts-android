package com.mcxinyu.echartsandroid.sample

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.mcxinyu.echartsandroid.sample.databinding.ActivityMainBinding
import com.mcxinyu.echartsandroid.webview.JavaScriptInterface
import com.mcxinyu.echartsandroid.webview.SampleMessage
import com.mcxinyu.echartsandroid.webview.addJavascriptInterface
import org.intellij.lang.annotations.Language

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchView.setOnCheckedChangeListener { _, isChecked ->
            delegate.localNightMode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        }

        binding.echarts.setBackgroundColor(0)

        interactWithJs(binding)

        if (delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.echarts.setThemeName("dark") { interactWithJs(binding) }
        } else {
            binding.echarts.registerThemeScript(wonderland) {
                binding.echarts.setThemeName("wonderland") { interactWithJs(binding) }
            }
        }

        binding.switchViewLocal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.echarts.setInitUrl(
                if (isChecked) "file:///android_asset/index.html"
                else "file:///android_asset/index_inner.html"
            )
            if (isChecked && buttonView.tag != "notified") {
                buttonView.tag = "notified"
                Toast.makeText(this@MainActivity, "首次网络加载 echarts.js，可能需要一些时间", Toast.LENGTH_SHORT).show()
            }
        }

        binding.m = option
    }

    private fun interactWithJs(binding: ActivityMainBinding) {
        binding.echarts.addJavascriptInterface(JavaScriptInterface("Messenger") {
            it?.let {
                val message = Gson().fromJson(it, SampleMessage::class.java)
                if (message.type == "showToast") {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            message.payload?.toString() ?: "just-call-on-message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            null
        })

        binding.echarts.runOnChecked {
            binding.echarts.evaluateJavascript(
                """javascript:
                // Messenger.postMessage(${binding.echarts.echartsInstance}.getWidth());
                ${binding.echarts.echartsInstance}.on('click', 'series', (params) => {
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

    @Language("js")
    val wonderland = """
        (function (root, factory) {
            if (typeof define === 'function' && define.amd) {
                // AMD. Register as an anonymous module.
                define(['exports', 'echarts'], factory);
            } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
                // CommonJS
                factory(exports, require('echarts'));
            } else {
                // Browser globals
                factory({}, root.echarts);
            }
        }(this, function (exports, echarts) {
            var log = function (msg) {
                if (typeof console !== 'undefined') {
                    console && console.error && console.error(msg);
                }
            };
            if (!echarts) {
                log('ECharts is not Loaded');
                return;
            }
            echarts.registerTheme('wonderland', {
                "color": [
                    "#4ea397",
                    "#22c3aa",
                    "#7bd9a5",
                    "#d0648a",
                    "#f58db2",
                    "#f2b3c9"
                ],
                "backgroundColor": "rgba(255,255,255,0)",
                "textStyle": {},
                "title": {
                    "textStyle": {
                        "color": "#666666"
                    },
                    "subtextStyle": {
                        "color": "#999999"
                    }
                },
                "line": {
                    "itemStyle": {
                        "borderWidth": "2"
                    },
                    "lineStyle": {
                        "width": "3"
                    },
                    "symbolSize": "8",
                    "symbol": "emptyCircle",
                    "smooth": false
                },
                "radar": {
                    "itemStyle": {
                        "borderWidth": "2"
                    },
                    "lineStyle": {
                        "width": "3"
                    },
                    "symbolSize": "8",
                    "symbol": "emptyCircle",
                    "smooth": false
                },
                "bar": {
                    "itemStyle": {
                        "barBorderWidth": 0,
                        "barBorderColor": "#ccc"
                    }
                },
                "pie": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "scatter": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "boxplot": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "parallel": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "sankey": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "funnel": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "gauge": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    }
                },
                "candlestick": {
                    "itemStyle": {
                        "color": "#d0648a",
                        "color0": "transparent",
                        "borderColor": "#d0648a",
                        "borderColor0": "#22c3aa",
                        "borderWidth": "1"
                    }
                },
                "graph": {
                    "itemStyle": {
                        "borderWidth": 0,
                        "borderColor": "#ccc"
                    },
                    "lineStyle": {
                        "width": "1",
                        "color": "#cccccc"
                    },
                    "symbolSize": "8",
                    "symbol": "emptyCircle",
                    "smooth": false,
                    "color": [
                        "#4ea397",
                        "#22c3aa",
                        "#7bd9a5",
                        "#d0648a",
                        "#f58db2",
                        "#f2b3c9"
                    ],
                    "label": {
                        "color": "#ffffff"
                    }
                },
                "map": {
                    "itemStyle": {
                        "areaColor": "#eeeeee",
                        "borderColor": "#999999",
                        "borderWidth": 0.5
                    },
                    "label": {
                        "color": "#28544e"
                    },
                    "emphasis": {
                        "itemStyle": {
                            "areaColor": "rgba(34,195,170,0.25)",
                            "borderColor": "#22c3aa",
                            "borderWidth": 1
                        },
                        "label": {
                            "color": "#349e8e"
                        }
                    }
                },
                "geo": {
                    "itemStyle": {
                        "areaColor": "#eeeeee",
                        "borderColor": "#999999",
                        "borderWidth": 0.5
                    },
                    "label": {
                        "color": "#28544e"
                    },
                    "emphasis": {
                        "itemStyle": {
                            "areaColor": "rgba(34,195,170,0.25)",
                            "borderColor": "#22c3aa",
                            "borderWidth": 1
                        },
                        "label": {
                            "color": "#349e8e"
                        }
                    }
                },
                "categoryAxis": {
                    "axisLine": {
                        "show": true,
                        "lineStyle": {
                            "color": "#cccccc"
                        }
                    },
                    "axisTick": {
                        "show": false,
                        "lineStyle": {
                            "color": "#333"
                        }
                    },
                    "axisLabel": {
                        "show": true,
                        "color": "#999999"
                    },
                    "splitLine": {
                        "show": true,
                        "lineStyle": {
                            "color": [
                                "#eeeeee"
                            ]
                        }
                    },
                    "splitArea": {
                        "show": false,
                        "areaStyle": {
                            "color": [
                                "rgba(250,250,250,0.05)",
                                "rgba(200,200,200,0.02)"
                            ]
                        }
                    }
                },
                "valueAxis": {
                    "axisLine": {
                        "show": true,
                        "lineStyle": {
                            "color": "#cccccc"
                        }
                    },
                    "axisTick": {
                        "show": false,
                        "lineStyle": {
                            "color": "#333"
                        }
                    },
                    "axisLabel": {
                        "show": true,
                        "color": "#999999"
                    },
                    "splitLine": {
                        "show": true,
                        "lineStyle": {
                            "color": [
                                "#eeeeee"
                            ]
                        }
                    },
                    "splitArea": {
                        "show": false,
                        "areaStyle": {
                            "color": [
                                "rgba(250,250,250,0.05)",
                                "rgba(200,200,200,0.02)"
                            ]
                        }
                    }
                },
                "logAxis": {
                    "axisLine": {
                        "show": true,
                        "lineStyle": {
                            "color": "#cccccc"
                        }
                    },
                    "axisTick": {
                        "show": false,
                        "lineStyle": {
                            "color": "#333"
                        }
                    },
                    "axisLabel": {
                        "show": true,
                        "color": "#999999"
                    },
                    "splitLine": {
                        "show": true,
                        "lineStyle": {
                            "color": [
                                "#eeeeee"
                            ]
                        }
                    },
                    "splitArea": {
                        "show": false,
                        "areaStyle": {
                            "color": [
                                "rgba(250,250,250,0.05)",
                                "rgba(200,200,200,0.02)"
                            ]
                        }
                    }
                },
                "timeAxis": {
                    "axisLine": {
                        "show": true,
                        "lineStyle": {
                            "color": "#cccccc"
                        }
                    },
                    "axisTick": {
                        "show": false,
                        "lineStyle": {
                            "color": "#333"
                        }
                    },
                    "axisLabel": {
                        "show": true,
                        "color": "#999999"
                    },
                    "splitLine": {
                        "show": true,
                        "lineStyle": {
                            "color": [
                                "#eeeeee"
                            ]
                        }
                    },
                    "splitArea": {
                        "show": false,
                        "areaStyle": {
                            "color": [
                                "rgba(250,250,250,0.05)",
                                "rgba(200,200,200,0.02)"
                            ]
                        }
                    }
                },
                "toolbox": {
                    "iconStyle": {
                        "borderColor": "#999999"
                    },
                    "emphasis": {
                        "iconStyle": {
                            "borderColor": "#666666"
                        }
                    }
                },
                "legend": {
                    "textStyle": {
                        "color": "#999999"
                    }
                },
                "tooltip": {
                    "axisPointer": {
                        "lineStyle": {
                            "color": "#cccccc",
                            "width": 1
                        },
                        "crossStyle": {
                            "color": "#cccccc",
                            "width": 1
                        }
                    }
                },
                "timeline": {
                    "lineStyle": {
                        "color": "#4ea397",
                        "width": 1
                    },
                    "itemStyle": {
                        "color": "#4ea397",
                        "borderWidth": 1
                    },
                    "controlStyle": {
                        "color": "#4ea397",
                        "borderColor": "#4ea397",
                        "borderWidth": 0.5
                    },
                    "checkpointStyle": {
                        "color": "#4ea397",
                        "borderColor": "#3cebd2"
                    },
                    "label": {
                        "color": "#4ea397"
                    },
                    "emphasis": {
                        "itemStyle": {
                            "color": "#4ea397"
                        },
                        "controlStyle": {
                            "color": "#4ea397",
                            "borderColor": "#4ea397",
                            "borderWidth": 0.5
                        },
                        "label": {
                            "color": "#4ea397"
                        }
                    }
                },
                "visualMap": {
                    "color": [
                        "#d0648a",
                        "#22c3aa",
                        "#adfff1"
                    ]
                },
                "dataZoom": {
                    "backgroundColor": "rgba(255,255,255,0)",
                    "dataBackgroundColor": "rgba(222,222,222,1)",
                    "fillerColor": "rgba(114,230,212,0.25)",
                    "handleColor": "#cccccc",
                    "handleSize": "100%",
                    "textStyle": {
                        "color": "#999999"
                    }
                },
                "markPoint": {
                    "label": {
                        "color": "#ffffff"
                    },
                    "emphasis": {
                        "label": {
                            "color": "#ffffff"
                        }
                    }
                }
            });
        }));
    """.trimIndent()
}