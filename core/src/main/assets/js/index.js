var myChart = null;

window.onload = function () {
    myChart = echarts.init(document.getElementById("chart"));

    function setOption(option) {
        myChart.setOption(option, true)
    }

    function reset() {
        myChart.reset()
    }

    window.onresize = function () {
        myChart.onresize()
    }
}
