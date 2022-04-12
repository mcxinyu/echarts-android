var chart = null;
window.onload = function () {
  chart = echarts.init(document.getElementById('chart'));
  window.onresize = function () { chart.resize(); }
}
