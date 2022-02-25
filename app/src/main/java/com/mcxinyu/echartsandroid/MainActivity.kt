package com.mcxinyu.echartsandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcxinyu.echartsandroid.databinding.ActivityMainBinding

/**
 *
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2022/2/24.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val option = """
                    {
                      xAxis: {
                        type: 'value',
                        show: false
                      },
                      yAxis: {
                        type: 'category',
                        boundaryGap: false,
                        onZero: true,
                        data: [
                          '',
                          '',
                          '0:00',
                          '1:00',
                          '2:00',
                          '3:00',
                          '4:00',
                          '5:00',
                          '6:00',
                          '7:00',
                          '8:00',
                          '9:00',
                          '10:00',
                          '11:00',
                          '12:00',
                          '13:00',
                          '14:00',
                          '15:00',
                          '16:00',
                          '17:00',
                          '18:00',
                          '19:00',
                          '20:00',
                          '21:00',
                          '22:00',
                          '23:00',
                          '24:00',
                          '',
                          ''
                        ]
                      },
                      dataZoom: [
                        {
                          show: false,
                          start: 0,
                          end: 100
                        },
                        {
                          show: true,
                          yAxisIndex: 0,
                          filterMode: 'empty',
                          width: 30,
                          height: '80%',
                          showDataShadow: false,
                        }
                      ],
                      series: [
                        {
                          data: [
                            0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            0, 0, 1, 0, 0
                          ],
                          type: 'bar',
                          showBackground: false,
                          barCategoryGap: '0%',
                          stack: 'total',
                          itemStyle: {
                            borderColor: 'transparent',
                            color: ''
                          }
                        },
                        {
                          name: 'Placeholder',
                          type: 'bar',
                          stack: 'total',
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
                          data: [8]
                        }
                      ]
                    }
                """.trimIndent()
        binding.echarts.setOption(option)
    }
}