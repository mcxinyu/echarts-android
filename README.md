# echarts-android
[![](https://jitpack.io/v/mcxinyu/echarts-android.svg)](https://jitpack.io/#mcxinyu/echarts-android)

基于 js 方式实现的 [ECharts](https://github.com/apache/echarts) Android 项目。

## 起源
- 目前 Android 实现 [ECharts](https://github.com/apache/echarts) 的开源项目中，基本都是通过建立类似 ORM 的对象映射关系的实现。说真的这种实现方式既不灵活（例如后端下发参数配置后，前端还要再转一遍，其实已经是 h5 了，那直接 js 不就行了）；也无法做到快速更新 ECharts 版本。
- 我之前开发 flutter 项目的时候，使用过 [flutter_echarts](https://github.com/entronad/flutter_echarts)，他的实现思路就很好，我们也是通过后端直接下发 ECharts 配置信息（option参数），实现各端直接展示（例如 flutter、小程序、h5）。
- 所以，本库参考了 [flutter_echarts](https://github.com/entronad/flutter_echarts) 思路，实现了 andriod 端的 ECharts。（iOS 也可以采用类似实现，难度不高）

## 功能
- [x] 支持 ECharts 基本功能
- [ ] 支持 Android 与 JS 交互
- [ ] 支持自由更换 ECharts 版本
- [ ] 支持自由增加 ECharts 扩展

## 使用
发布在 [Jitpack echarts-android](https://jitpack.io/#mcxinyu/echarts-android)，简单配置即可使用。

## 文档
doing

## 优点
- 没有繁琐的对象映射
- 配置灵活，无需写一堆面向对象的对象
- 可使用 ECharts 所有特性，ECharts提供什么，你就可以使用什么
- 各端开发人员甚至可以无需关系 ECharts 的实现，只需使用后端下发的配置

## 缺点
- 需要一点 js 基础，但是我不觉得这是什么缺点（（梗）毕竟现在的全栈定义已经不仅仅是写代码的了，美工、商务、推销、运维都是全栈内容之一）。aha

## 关于性能
无论是类似 ORM 的对象映射关系的实现还是直接 js 的实现，都是通过 webview 直接展示，那性能短板就是 webview，或者说网页通病吧。
