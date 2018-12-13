app.controller('searchController',function($scope,$location,searchService){
	
	//搜索
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap=response;
				buildPageLabel();
			}
		);		
	}
	
	$scope.searchMap={'keywords':'','brand':'','spec':{},'category':'','price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
	$scope.addSearchItem=function (key, value) {
		if(key=="brand"||key=="category"||key=="price"){
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
        $scope.search();
    }
    $scope.removeSearchItem=function (key) {
		if(key=="category"||key=="brand"||key=="price"){
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
        $scope.search();
    }
    //构建分页标签
    buildPageLabel=function(){
        //构建分页栏
        $scope.pageLabel=[];
        var firstPage=1;//开始页码
        var lastPage=$scope.resultMap.totalPages;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点
        if($scope.resultMap.totalPages>5){  //如果页码数量大于5
            if($scope.searchMap.pageNo<=3){//如果当前页码小于等于3 ，显示前5页
                lastPage=5;
                $scope.firstDot=false;//前面没点
            }else if( $scope.searchMap.pageNo>= $scope.resultMap.totalPages-2 ){//显示后5页
                firstPage=$scope.resultMap.totalPages-4;
                $scope.lastDot=false;//后边没点
            }else{  //显示以当前页为中心的5页
                firstPage=$scope.searchMap.pageNo-2;
                lastPage=$scope.searchMap.pageNo+2;
            }
        }else{
            $scope.firstDot=false;//前面无点
            $scope.lastDot=false;//后边无点
        }
        //构建页码
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }

    $scope.queryByPage=function (pageNo) {
		if(pageNo<1||pageNo>$scope.resultMap.totalPages){
			return;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();
    }
//判断当前页为第一页
    $scope.isTopPage=function(){
        if($scope.searchMap.pageNo==1){
            return true;
        }else{
            return false;
        }
    }

    //判断当前页是否未最后一页
    $scope.isEndPage=function(){
        if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
            return true;
        }else{
            return false;
        }
    }
    //排序查询
    $scope.sortSearch=function (sortField,sort) {
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    }

    $scope.loadkeywords=function(){
        $scope.searchMap.keywords=  $location.search()['keywords'];
        $scope.search();
    }


});