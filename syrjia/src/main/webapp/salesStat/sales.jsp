<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="en" style="height: 100%;">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="salesStat/css/style.css">
    <link href="salesStat/css/mui.min.css" rel="stylesheet" type="text/css">
    <link href="salesStat/css/mui.picker.min.css" rel="stylesheet" type="text/css">
    <link href="salesStat/css/ionic.css" rel="stylesheet">
    <script type="text/javascript" src="salesStat/js/jquery.min.js"></script>
    <script src="salesStat/js/mui.min.js"></script>
    <script src="salesStat/js/mui.picker.min.js"></script>
    <script src="salesStat/js/echarts.min.js"></script>
    <script type="text/javascript" src="salesStat/js/angular.min.js"></script>
    <script src="salesStat/js/ionic.bundle.min.js"></script>
    <title>销售管理</title>
    <style type="text/css">
        .mui-poppicker-header {
            background: #f6f6f6;
            border: none;
            padding: 0;
            height: 2.5rem;
        }

        .mui-poppicker-btn-ok {
            line-height: 2.5rem;
            width: auto;
            background: none;
            font-size: 0.8rem;
            color: #cc0422;
            padding: 0 1rem;
            position: absolute;
            right: 0;
            top: 0;
        }

        .mui-poppicker-btn-cancel {
            background: none;
            font-size: 0.8rem;
            color: #a8a8a8;
            width: auto;
            padding: 0 1rem;
            position: absolute;
            left: 0;
            top: 0;
        }

        ul.focus {
            background: #dc989a;
        }

        ul.focus > li:last-child {
            background: #ffffff;
        }

        .mui-poppicker-header button {
            width: 70px !important;
        }

        @media screen and (max-width: 375px) {
            .mui-poppicker.mui-active.lobottom {
                bottom: 20px !important;
            }
        }

        .sales_con {
            overflow-y: overlay;
            height: 60%;
        }
    </style>

    <script type="text/javascript">
        function getCurrentYear() {
            return new Date().getFullYear();
        }

        function getCurrentMonth() {
            var month = new Date().getMonth() + 1;
            if (month < 10) month = '0' + month;
            return month;
        }

        function getCurrentDay() {
            var day = new Date().getDate();
            if (day < 10) day = '0' + day;
            return day;
        }

        function loadData($http, $scope, $ionicLoading, type) {
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });

            if (!type) {
                type = 'A0';
            }
            $http.get('appSalesStat/index/data.action?salesId=' + $scope.salesId + '&doctorId=' + $scope.doctorId + '&day=' + $scope.day)
                .success(function (response) {
                    $scope.dayDoneDoctorOnline = response.focusDayDone.doctorOnline;
                    $scope.dayQuotaDoctorOnline = response.focusDayQuota.doctorOnline;
                    $scope.dayDoneAllSum = response.focusDayDone.allSum;
                    $scope.dayDonePrescriptionSum = response.focusDayDone.prescriptionSum;
                    $scope.dayQuotaPrescriptionSum = response.focusDayQuota.prescriptionSum;
                    $scope.dayDoneRecuperateSum = response.focusDayDone.recuperateSum;
                    $scope.dayQuotaRecuperateSum = response.focusDayQuota.recuperateSum;
                    $scope.dayDoneConsultationSum = response.focusDayDone.consultationSum;
                    $scope.dayQuotaConsultationSum = response.focusDayQuota.consultationSum;
                    $scope.dayDoneCommoditySum = response.focusDayDone.commoditySum;
                    $scope.dayQuotaCommoditySum = response.focusDayQuota.commoditySum;
                    $scope.dayDoneAllQuantity = response.focusDayDone.allQuantity;
                    $scope.dayDonePrescriptionQuantity = response.focusDayDone.prescriptionQuantity;
                    $scope.dayQuotaPrescriptionQuantity = response.focusDayQuota.prescriptionQuantity;
                    $scope.dayDoneRecuperateQuantity = response.focusDayDone.recuperateQuantity;
                    $scope.dayQuotaRecuperateQuantity = response.focusDayQuota.recuperateQuantity;
                    $scope.dayDoneConsultationQuantity = response.focusDayDone.consultationQuantity;
                    $scope.dayQuotaConsultationQuantity = response.focusDayQuota.consultationQuantity;
                    $scope.dayDoneCommodityQuantity = response.focusDayDone.commodityQuantity;
                    $scope.dayQuotaCommodityQuantity = response.focusDayQuota.commodityQuantity;

                    $scope.monthDoneDoctorOnline = response.monthDone.doctorOnline;
                    $scope.monthQuotaDoctorOnline = response.monthQuota.doctorOnline;
                    $scope.monthDoneAllSum = response.monthDone.allSum;
                    $scope.monthDonePrescriptionSum = response.monthDone.prescriptionSum;
                    $scope.monthQuotaPrescriptionSum = response.monthQuota.prescriptionSum;
                    $scope.monthDoneRecuperateSum = response.monthDone.recuperateSum;
                    $scope.monthQuotaRecuperateSum = response.monthQuota.recuperateSum;
                    $scope.monthDoneConsultationSum = response.monthDone.consultationSum;
                    $scope.monthQuotaConsultationSum = response.monthQuota.consultationSum;
                    $scope.monthDoneCommoditySum = response.monthDone.commoditySum;
                    $scope.monthQuotaCommoditySum = response.monthQuota.commoditySum;
                    $scope.monthDoneAllQuantity = response.monthDone.allQuantity;
                    $scope.monthDonePrescriptionQuantity = response.monthDone.prescriptionQuantity;
                    $scope.monthQuotaPrescriptionQuantity = response.monthQuota.prescriptionQuantity;
                    $scope.monthDoneRecuperateQuantity = response.monthDone.recuperateQuantity;
                    $scope.monthQuotaRecuperateQuantity = response.monthQuota.recuperateQuantity;
                    $scope.monthDoneConsultationQuantity = response.monthDone.consultationQuantity;
                    $scope.monthQuotaConsultationQuantity = response.monthQuota.consultationQuantity;
                    $scope.monthDoneCommodityQuantity = response.monthDone.commodityQuantity;
                    $scope.monthQuotaCommodityQuantity = response.monthQuota.commodityQuantity;

                    var percentageChart = echarts.init(document.getElementById('sales_percentage'));
                    var percent = 0;
                    if (type === 'A0') {
                        if (parseInt(response.monthQuota.allSum) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.allSum) * 100) / parseFloat(response.monthQuota.allSum)).toFixed(2);
                    }
                    if (type === 'A1') {
                        if (parseInt(response.monthQuota.prescriptionSum) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.prescriptionSum) * 100) / parseFloat(response.monthQuota.prescriptionSum)).toFixed(2);
                    }
                    if (type === 'A2') {
                        if (parseInt(response.monthQuota.recuperateSum) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.recuperateSum) * 100) / parseFloat(response.monthQuota.recuperateSum)).toFixed(2);
                    }
                    if (type === 'A3') {
                        if (parseInt(response.monthQuota.consultationSum) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.consultationSum) * 100) / parseFloat(response.monthQuota.consultationSum)).toFixed(2);
                    }
                    if (type === 'A4') {
                        if (parseInt(response.monthQuota.commoditySum) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.commoditySum) * 100) / parseFloat(response.monthQuota.commoditySum)).toFixed(2);
                    }
                    if (type === 'B0') {
                        if (parseInt(response.monthQuota.allQuantity) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.allQuantity) * 100) / parseFloat(response.monthQuota.allQuantity)).toFixed(2);
                    }
                    if (type === 'B1') {
                        if (parseInt(response.monthQuota.prescriptionQuantity) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.prescriptionQuantity) * 100) / parseFloat(response.monthQuota.prescriptionQuantity)).toFixed(2);
                    }
                    if (type === 'B2') {
                        if (parseInt(response.monthQuota.recuperateQuantity) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.recuperateQuantity) * 100) / parseFloat(response.monthQuota.recuperateQuantity)).toFixed(2);
                    }
                    if (type === 'B3') {
                        if (parseInt(response.monthQuota.consultationQuantity) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.consultationQuantity) * 100) / parseFloat(response.monthQuota.consultationQuantity)).toFixed(2);
                    }
                    if (type === 'B4') {
                        if (parseInt(response.monthQuota.commodityQuantity) === 0)
                            percent = 100;
                        else
                            percent = ((parseFloat(response.monthDone.commodityQuantity) * 100) / parseFloat(response.monthQuota.commodityQuantity)).toFixed(2);
                    }
                    if (percent === 'NaN') percent = 0;
                    var percentageOpt = {
                        title: {
                            text: percent + '%',
                            x: 'center',
                            y: 'center',
                            textStyle: {
                                fontWeight: 'normal',
                                color: '#444444',
                                fontSize: '9'
                            }
                        },
                        color: ['#eff0f4'],
                        grid: {
                            left: "0",
                            top: "0",
                        },
                        series: [{
                            name: 'Line 1',
                            type: 'pie',
                            clockWise: true,
                            radius: ['70%', '86%'],
                            itemStyle: {
                                emphasis: {
                                    barBorderRadius: 30
                                },
                                normal: {
                                    label: {
                                        show: false
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            },
                            hoverAnimation: false,
                            data: [{
                                value: percent,
                                name: '01',
                                itemStyle: {
                                    normal: {
                                        color: '#bc282c',
                                        label: {
                                            show: false
                                        },
                                        labelLine: {
                                            show: false
                                        }
                                    }
                                }
                            }, {
                                name: '02',
                                value: 100 - percent
                            }]
                        }]
                    }
                    percentageChart.setOption(percentageOpt, true);

                    var title = '';
                    var dayDoneArray = [];
                    var dayQuotaMax = 0;
                    var dayQuotaArray = [];
                    if (type == 'A0') {
                        title = '成单额';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].allSum);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].allSum;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'A1') {
                        title = '药品金额';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].prescriptionSum);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].prescriptionSum;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'A2') {
                        title = '挂号金额';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].recuperateSum);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].recuperateSum;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'A3') {
                        title = '咨询金额';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].consultationSum);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].consultationSum;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'A4') {
                        title = '商品金额';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].commoditySum);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].commoditySum;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'B0') {
                        title = '成单量';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].allQuantity);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].allQuantity;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'B1') {
                        title = '药品单量';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].prescriptionQuantity);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].prescriptionQuantity;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'B2') {
                        title = '挂号单量';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].recuperateQuantity);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].recuperateQuantity;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'B3') {
                        title = '咨询单量';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].consultationQuantity);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].consultationQuantity;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    if (type == 'B4') {
                        title = '商品单量';
                        for (var prop in response.daysDone) {
                            dayDoneArray.push((response.daysDone)[prop].commodityQuantity);
                        }
                        for (var prop in response.daysQuota) {
                            var v = (response.daysQuota)[prop].commodityQuantity;
                            if (v > dayQuotaMax) dayQuotaMax = v;
                            dayQuotaArray.push(v);
                        }
                    }
                    var dayArry = [];
                    for (var k = 1; k <= new Date(getCurrentYear(), getCurrentMonth(), 0).getDate(); k++) {
                        dayArry.push(k + "日");
                    }
                    var topChart = echarts.init(document.getElementById('sales_top'));
                    var topOpt = {
                        "tooltip": {
                            "trigger": "axis",
                            "axisPointer": {
                                "type": "shadow"
                            },
                        },
                        "grid": {
                            "borderWidth": 0,
                            "y2": 120,
                            "right": '0',
                            "left": "35%",
                            "top": "14%",
                            "bottom": "20%"
                        },
                        "legend": {
                            left: '2%',
                            data: ['指标', title],
                            orient: 'vertical',  //垂直显示
                            y: '5%',    //延Y轴居中
                            itemWidth: 20,
                            itemHeight: 10,
                            textStyle: {
                                fontSize: 11,
                            }
                        },
                        "xAxis": [
                            {
                                "type": "category",
                                "splitLine": {
                                    "show": false
                                },
                                "axisLine": {
                                    "show": false
                                },
                                "axisTick": {
                                    "show": false
                                },
                                "splitArea": {
                                    "show": false
                                },
                                "axisLabel": {
                                    "interval": 0,
                                    "rotate": 0,
                                    "show": true,
                                    // "splitNumber": 15,
                                    "textStyle": {
                                        "fontFamily": "微软雅黑",
                                        "fontSize": 12,
                                        "color":function(param){
                                            var xData=param.split("日")[0];
                                            return (parseInt($scope.day.substr(6,2)) === parseInt(xData))?
                                                'rgb(188,40,44)':
                                                'rgb(0,0,0)';
                                        }
                                    }
                                },
                                "data": dayArry,
                            }
                        ],
                        "yAxis": [
                            {
                                "type": "value",
                                "min": 0,
                                "max": dayQuotaMax,
                                "splitLine": {
                                    "show": true
                                },
                                "axisLine": {
                                    "show": false
                                },
                                "axisTick": {
                                    "show": false
                                },
                                "splitArea": {
                                    "show": false
                                }
                            }
                        ],
                        "dataZoom": [
                            {
                                "show": false,
                                "xAxisIndex": [0],
                                "interval": 0,
                                "start": (parseInt(getCurrentDay()) - 1) * 1.5,
                                "end": 30 + (parseInt(getCurrentDay()) - 1) * 1.5,
                            },
                            {
                                "type": "inside",
                                "xAxisIndex": [0],
                                "interval": 0,
                                "start": (parseInt(getCurrentDay()) - 1) * 1.5,
                                "end": 30 + (parseInt(getCurrentDay()) - 1) * 1.5,
                            }
                        ],
                        "series": [
                            {
                                "name": "指标",
                                "type": "bar",
                                "barWidth": "30%",
                                "itemStyle": {
                                    "normal": {
                                        "color": "#BBBBBB",
                                        "barBorderRadius": 0,
                                        "label": {
                                            "show": false,
                                        }
                                    }
                                },
                                "data": dayQuotaArray
                            },
                            {
                                "name": title,
                                "type": "bar",
                                "stack": "sum",
                                "barWidth": "30%",
                                "barGap": "-100%",
                                "itemStyle": {
                                    "normal": {
                                        "barBorderRadius": 0,
                                        "color": "#bc282c",
                                        "label": {
                                            "show": false,
                                        }
                                    }
                                },
                                "data": dayDoneArray

                            }
                        ]
                    };
                    topChart.setOption(topOpt, true);

                    $ionicLoading.hide();
                });
        }

        var salesId = '<c:out value="${salesId}" escapeXml="false" />';

        angular.module('sataApp', ['ionic']).controller('statCtrl', function ($scope, $timeout, $ionicLoading, $http) {
            $scope.salesId = salesId;
            $scope.doctorId = '';
            $scope.day = getCurrentYear() + '' + getCurrentMonth() + '' + getCurrentDay();

            $scope.currentMonth = getCurrentMonth();

            $(".sales_month").find("i").text(getCurrentMonth());
            $(".sales_day").find("i").text(getCurrentDay());

            var salesList = '<c:out value="${salesList}" escapeXml="false" />';
            document.querySelector('#sales_user').addEventListener("tap", function () {
                var roadPick = new mui.PopPicker();
                roadPick.setData($.parseJSON(salesList));
                roadPick.show(function (item) {
                    var itemCallback = roadPick.getSelectedItems();
                    $('#sales_user .sales_user').html(itemCallback[0].text);
                    var selected = itemCallback[0].value;
                    if ($scope.salesId != selected) {
                        $scope.salesId = selected;
                        loadData($http, $scope, $ionicLoading);
                    }

                    $.getJSON('appSalesStat/index/findDoctors.action?salesId=' + salesId, function (json) {
                        document.querySelector('#sales_doctor').addEventListener("tap", function () {
                            var roadPick = new mui.PopPicker();
                            roadPick.setData(json);
                            roadPick.show(function (item) {
                                var itemCallback = roadPick.getSelectedItems();
                                $('#sales_doctor .sales_doctor').html(itemCallback[0].text);
                                var selected = itemCallback[0].value;
                                if ($scope.doctorId != selected) {
                                    $scope.doctorId = selected;
                                    loadData($http, $scope, $ionicLoading);
                                }
                            });
                            dType();
                        });
                    })
                });
                dType();
            });

            var doctorList = '<c:out value="${doctorList}" escapeXml="false" />';
            document.querySelector('#sales_doctor').addEventListener("tap", function () {
                var roadPick = new mui.PopPicker();
                roadPick.setData($.parseJSON(doctorList));
                roadPick.show(function (item) {
                    var itemCallback = roadPick.getSelectedItems();
                    $('#sales_doctor .sales_doctor').html(itemCallback[0].text);
                    var selected = itemCallback[0].value;
                    if ($scope.doctorId != selected) {
                        $scope.doctorId = selected;
                        loadData($http, $scope, $ionicLoading);
                    }
                });
                dType();
            });

            var monthArrary = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
            document.querySelector('#sales_month').addEventListener("tap", function () {
                var roadPick = new mui.PopPicker();
                roadPick.setData(monthArrary);
                roadPick.show(function (item) {
                    var itemCallback = roadPick.getSelectedItems();
                    $('#sales_month .sales_month i').text(itemCallback[0]);
                    var selected = getCurrentYear() + itemCallback[0] + $('#sales_day .sales_day i').text();
                    if ($scope.day != selected) {
                        $scope.currentMonth = parseInt(itemCallback[0]);
                        $scope.day = selected;
                        loadData($http, $scope, $ionicLoading);
                    }
                });
                dType();
            });

            var dayArray = [];
            for (var k = 1; k <= new Date(getCurrentYear(), getCurrentMonth(), 0).getDate(); k++) {
                dayArray.push((k < 10) ? '0' + k : k + '');
            }
            document.querySelector('#sales_day').addEventListener("tap", function () {
                var roadPick = new mui.PopPicker();
                roadPick.setData(dayArray);
                roadPick.show(function (item) {
                    var itemCallback = roadPick.getSelectedItems();
                    $('#sales_day .sales_day i').text(itemCallback[0]);
                    var selected = getCurrentYear() + $('#sales_month .sales_month i').text() + itemCallback[0];
                    if ($scope.day != selected) {
                        $scope.day = selected;
                        loadData($http, $scope, $ionicLoading);
                    }
                });
                dType();
            });

            $('ul.itemUl').bind('click', function () {
                if ($(this).hasClass('focus')) return;

                $(this).addClass('focus').siblings().removeClass('focus');
                $(this).parent().siblings().find('ul.focus').removeClass('focus');

                var type = $(this).attr('type');
                loadData($http, $scope, $ionicLoading, type);
            });

            loadData($http, $scope, $ionicLoading);
        });
    </script>
</head>
<body ng-app="sataApp" ng-controller="statCtrl" style="height: 100%;">
<div class="sales_topcon">
    <div id="sales_top" style="width: 100%;height: 8rem;"></div>
    <div class="sales_percentage">
        <h3><i ng-bind="currentMonth"></i>月份</h3>
        <div id="sales_percentage" style="width: 100%;height:3rem;"></div>
    </div>
</div>
<ul class="sales_check">
    <input type="hidden" ng-value="salesId">
    <input type="hidden" ng-value="doctorId">
    <input type="hidden" ng-value="day">
    <li id="sales_user">
        <button type="text" class="sales_user" readonly>本人</button>
    </li>
    <li id="sales_doctor">
        <button type="text" class="sales_doctor" readonly>全部</button>
    </li>
    <li id="sales_month">
        <button type="text" class="sales_month" readonly><i></i><span>月</span>
        </button>
    </li>
    <li id="sales_day">
        <button type="text" class="sales_day" readonly><i></i><span>日</span></button>
    </li>
</ul>
<div class="sales_con">
    <div class="sales_list">
        <ul class="sales_title">
            <li><h3>医生上线数量</h3></li>
            <li>
                <span><i ng-bind="monthDoneDoctorOnline">0</i>人(完成)</span>
                <span><i ng-bind="monthQuotaDoctorOnline">0</i>人(指标)</span>
            </li>
            <li>
                <span><i ng-bind="dayDoneDoctorOnline">0</i>人(完成)</span>
                <span><i ng-bind="dayQuotaDoctorOnline">0</i>人(指标)</span>
            </li>
        </ul>
    </div>
    <div class="sales_list">
        <ul class="sales_title itemUl focus" type="A0">
            <li><h3>总成单额</h3></li>
            <li>
                <span>¥<i ng-bind="monthDoneAllSum">0</i>(完成)</span>
            </li>
            <li>
                <span>¥<i ng-bind="dayDoneAllSum">0</i>(完成)</span>
            </li>
        </ul>
        <ul class="itemUl" type="A1">
            <li><h3>药费</h3></li>
            <li>
                <span>¥<i ng-bind="monthDonePrescriptionSum">0</i>(完成)</span>
                <span>¥<i ng-bind="monthQuotaPrescriptionSum">0</i>(指标)</span>
            </li>
            <li>
                <span>¥<i ng-bind="dayDonePrescriptionSum">0</i>(完成)</span>
                <span>¥<i ng-bind="dayQuotaPrescriptionSum">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="A2">
            <li class="itemUl"><h3>挂号</h3></li>
            <li>
                <span>¥<i ng-bind="monthDoneRecuperateSum">0</i>(完成)</span>
                <span>¥<i ng-bind="monthQuotaRecuperateSum">0</i>(指标)</span>
            </li>
            <li>
                <span>¥<i ng-bind="dayDoneRecuperateSum">0</i>(完成)</span>
                <span>¥<i ng-bind="dayQuotaRecuperateSum">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="A3">
            <li><h3>咨询</h3></li>
            <li>
                <span>¥<i ng-bind="monthDoneConsultationSum">0</i>(完成)</span>
                <span>¥<i ng-bind="monthQuotaConsultationSum">0</i>(指标)</span>
            </li>
            <li>
                <span>¥<i ng-bind="dayDoneConsultationSum">0</i>(完成)</span>
                <span>¥<i ng-bind="dayQuotaConsultationSum">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="A4">
            <li><h3>商品</h3></li>
            <li>
                <span>¥<i ng-bind="monthDoneCommoditySum">0</i>(完成)</span>
                <span>¥<i ng-bind="monthQuotaCommoditySum">0</i>(指标)</span>
            </li>
            <li>
                <span>¥<i ng-bind="dayDoneCommoditySum">0</i>(完成)</span>
                <span>¥<i ng-bind="dayQuotaCommoditySum">0</i>(指标)</span>
            </li>
        </ul>
    </div>
    <div class="sales_list">
        <ul class="sales_title itemUl" type="B0">
            <li><h3>总成单</h3></li>
            <li>
                <span><i ng-bind="monthDoneAllQuantity">0</i>(完成)</span>
            </li>
            <li>
                <span><i ng-bind="dayDoneAllQuantity">0</i>(完成)</span>
            </li>
        </ul>
        <ul class="itemUl" type="B1">
            <li><h3>药费</h3></li>
            <li>
                <span><i ng-bind="monthDonePrescriptionQuantity">0</i>(完成)</span>
                <span><i ng-bind="monthQuotaPrescriptionQuantity">0</i>(指标)</span>
            </li>
            <li>
                <span><i ng-bind="dayDonePrescriptionQuantity">0</i>(完成)</span>
                <span><i ng-bind="dayQuotaPrescriptionQuantity">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="B2">
            <li><h3>挂号</h3></li>
            <li>
                <span><i ng-bind="monthDoneRecuperateQuantity">0</i>(完成)</span>
                <span><i ng-bind="monthQuotaRecuperateQuantity">0</i>(指标)</span>
            </li>
            <li>
                <span><i ng-bind="dayDoneRecuperateQuantity">0</i>(完成)</span>
                <span><i ng-bind="dayQuotaRecuperateQuantity">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="B3">
            <li><h3>咨询</h3></li>
            <li>
                <span><i ng-bind="monthDoneConsultationQuantity">0</i>(完成)</span>
                <span><i ng-bind="monthQuotaConsultationQuantity">0</i>(指标)</span>
            </li>
            <li>
                <span><i ng-bind="dayDoneConsultationQuantity">0</i>(完成)</span>
                <span><i ng-bind="dayQuotaConsultationQuantity">0</i>(指标)</span>
            </li>
        </ul>
        <ul class="itemUl" type="B4">
            <li><h3>商品</h3></li>
            <li>
                <span><i ng-bind="monthDoneCommodityQuantity">0</i>(完成)</span>
                <span><i ng-bind="monthQuotaCommodityQuantity">0</i>(指标)</span>
            </li>
            <li>
                <span><i ng-bind="dayDoneCommodityQuantity">0</i>(完成)</span>
                <span><i ng-bind="dayQuotaCommodityQuantity">0</i>(指标)</span>
            </li>
        </ul>
    </div>
</div>
</body>
<script type="text/javascript">
    function dType() {
        var u = navigator.userAgent, app = navigator.appVersion;
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isiOS) {
            $(".mui-active").addClass("lobottom")
        }
        return;
    }
</script>
</html>