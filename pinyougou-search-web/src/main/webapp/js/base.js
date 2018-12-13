// 定义模块:
var app = angular.module("pinyougou",[]);
//因为Angular.js的安全机制，无法将后端生成的html直接输出到前端，需要用到$sce服务进行HTML转换
app.filter('trustHtml',['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);
    }
}]);
