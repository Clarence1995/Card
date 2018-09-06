<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<html lang="cn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="${APP_PATH}/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${APP_PATH}/static/css/bootstrap-theme.min.css" rel="stylesheet"/>


    <title>Synchro</title>
</head>
<body>
<div class="container synchro_div">
    <div>
        <h1 style="text-align: center">数据同步</h1>
    </div>
    <div class="part">
        <div class="part_header">
            113采集库同步到卡管正式库(处理数据中心处理后的照片的那批人)
        </div>
        <form id="syn_form">
            <%-- 包含文件路径、线程数--%>
            <div>
                <label>文件路径</label>
                <input type="text" class="form-control" placeholder="文件路径" name="imgPath"/>
            </div>
            <div>
                <label>线程数</label>
                <input type="text" class="form-control" placeholder="线程数" name="threadCount" value="10"/>
            </div>
            <div>
                <label>同步状态</label>
                <input type="text" class="form-control" placeholder="同步状态" name="synchroStatus" value="45"/>
            </div>
            <div>
                <label>身份证号</label>
                <input type="text" class="form-control" placeholder="身份证号" name="certNum"/>
            </div>
            <div>
                <label>日志保存路径</label>
                <input type="text" class="form-control" placeholder="日志保存路径" name="logPath"/>
            </div>
            <div class="form_btn_syn">
                <button type="submit" class="btn btn-default" id="syn_btn">开始同步</button>
            </div>
        </form>
    </div>

    <div class="part">
        <div class="part_header">
            照片规集,生成压缩包
        </div>
        <form id="syn_imgZip">
            <%-- 包含文件路径、线程数--%>
            <div>
                <label>文件路径</label>
                <input type="text" class="form-control" placeholder="解压照片文件夹路径" name="imgPath"
                       value="E:\\synchroClarence\\ImgShouldbeSynchro\\test\\">
            </div>
            <div>
                <label>线程数</label>
                <input type="text" class="form-control" placeholder="线程数" name="threadCount" value="10">
            </div>
            <div>
                <label>目标文件夹</label>
                <input type="text" class="form-control" placeholder="目标文件夹" name="targetPath" value="E:\\">
            </div>
            <div>
                <label>ZIP根名</label>
                <input type="text" class="form-control" placeholder="ZIP根名" name="zipRootName" value="img">
            </div>
            <div>
                <label>单个文件夹包含图片数量</label>
                <input type="text" class="form-control" placeholder="单个文件夹包含图片数量" name="imgCount" value="7000">
            </div>
            <div class="form_btn_syn">
                <button type="submit" class="btn btn-default" id="syn_imgZip_btn">开始同步</button>
            </div>
        </form>
    </div>

    <div class="part">
        <div class="part_header">
            113采集库同步到卡管正式库(处理数据中心处理后的照片的那批人)
        </div>
        <form id="syn_excel">
            <%-- 包含文件路径、线程数--%>
                <div>
                    <label>Excel文件路径</label>
                    <input type="text" class="form-control" placeholder="Excel文件路径" name="excelSynchro"/>
                </div>
                <div>
                    <label>采集人员同步状态</label>
                    <input type="text" class="form-control" placeholder="采集人员同步状态" name="collectUserStatus"/>
                </div>
                <div>
                    <label>日志记录路径</label>
                    <input type="text" class="form-control" placeholder="日志记录路径" name="logFilePath" />
                </div>
                <div>
                    <label>是否查找数据库公安图片</label>
                    <input type="text" class="form-control" placeholder="是否查找数据库公安图片" name="gonaAnSearch" value="true"/>
                </div>
                <div>
                    <label>德生宝照片文件夹</label>
                    <input type="text" class="form-control" placeholder="德生宝照片文件夹" name="TSBFilePath" />
                </div>
                <div>
                    <label>线程数</label>
                    <input type="text" class="form-control" placeholder="线程数" name="threadNum" value="10">
                </div>
            <div class="form_btn_syn">
                <button type="submit" class="btn btn-default" id="syn_excel_btn">开始Excel同步</button>
            </div>
        </form>
    </div>

</div>

<script src="${APP_PATH}/static/js/jQuery.js"></script>
<script src="${APP_PATH}/static/js/bootstrap.min.js"></script>
<script>
    // 点击开始同步
    $("#syn_btn").click(function () {
        // alert("开始同步");
        var serializeStr = $("#syn_form").serialize();
        var d = {};
        var t = $("#syn_form").serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        // alert(JSON.stringify(d));

        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/card/synchro/collectToCardImgOuter",
            data: JSON.stringify(d),
            contentType: "application/json",
            success: function (result) {
                alert("success:" + result);
            }
        });
    });

    // 点击开始同步
    $("#syn_imgZip_btn").click(function () {
        var d = {};
        var t = $("#syn_imgZip").serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        // alert(JSON.stringify(d));
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/card/utils/zipImg",
            data: JSON.stringify(d),
            contentType: "application/json",
            success: function (result) {
                alert("success:" + result);
            }
        });
    });

    $("#syn_excel_btn").click(function () {
        console.log('开始同步')
        var serializeStr = $("#syn_excel").serialize();
        var d = {};
        var t = $("#syn_excel").serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/card/synchro/synchroFromExcel",
            data: JSON.stringify(d),
            contentType: "application/json",
            success: function (result) {
                alert("success:" + result);
            }
        });
    });

</script>
<style>
    .part {
        height: 500px;
        width: 500px;
        float: left;
        margin: 5px;
        padding: 5px;
        border: solid 1px #777;
        border-radius: 5px;
    }

    .part_header {
        margin: 2px;
    }

    .form_btn_syn {
        padding-top: 12px;
    }
</style>
</body>
</html>