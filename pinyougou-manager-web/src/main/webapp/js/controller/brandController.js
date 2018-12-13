app.controller('brandController', function ($scope,brandService,$controller) {
    $controller('baseController',{$scope:$scope});
    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }


    //分页查询
    $scope.findPage=function(page,size){
        $http.get('../brand/findPage.do?pageNum='+page+'&pageSize='+size).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems=response.total;
            }
        );
    }
    //根据id查询唯一实体
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function(response){
                $scope.entity=response;
            }
        )
    }
    //保存
    $scope.save=function(){
        var object=null;//方法名
        if($scope.entity.id!=null){
            object=brandService.update($scope.entity);
        }else{
            object=brandService.add($scope.entity);
        }
        object.success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新
                }else{
                    alert(response.message);
                }
            }
        );
    }

    //删除
    $scope.del=function () {
        if(confirm('确定要删除吗？')){
            brandService.del($scope.selectIds).success(
                function (response) {
                    if (response.success){
                        $scope.reloadList();
                    }else{
                        alert(response.message);
                    }
                }
            );
        }
    }
    //带条件的分页查询
    $scope.searchEntity={};
    $scope.search=function(page,rows){
        brandService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;//显示当前页数据
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );

    }
});