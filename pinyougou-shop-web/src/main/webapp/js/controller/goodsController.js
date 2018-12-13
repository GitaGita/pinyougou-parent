 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location  ,goodsService,itemCatService,typeTemplateService,uploadService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
    $scope.findOne=function(){
        var id= $location.search()['id'];//获取参数值
        if(id==null){
            return ;
        }
            goodsService.findOne(id).success(
            function(response){
                $scope.entity= response;
                editor.html($scope.entity.goodsDesc.introduction);
                $scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
                for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
                }
            }
        );
    }

    //判断某一个规格选项是否被选择
    $scope.checkAttributeValue=function(specName,optionName){
	    var item=$scope.entity.goodsDesc.specificationItems;
	    var Objcet=$scope.searchObjectByKey(item,'attributeName',specName);
	    if(Objcet==null){
	        return false;
        }else {
	        if(Objcet.attributeValue.indexOf(optionName)>=0){
	            return true;
            }else {
	            return false;
            }

        }
    }

	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
				    alert("保存成功");
					//清空数据
		        	$scope.entity=[];
		        	editor.html=("");
		        	location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	//增加
	 $scope.add=function(){
		$scope.entity.goodsDesc.introduction=editor.html();
		goodsService.add($scope.entity).success(
			function (response) {
				if (response.success){
					alert("保存成功")
					$scope.entity={};
					editor.html('');
				}else{
					alert(response.message);
				}
            }
		);
	 }
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    $scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]}  };
    //上传图片
    $scope.uploadFile=function(){
        uploadService.uploadFile().success(
            function(response){
                if(response.success){
                    $scope.image_entity.url= response.message;
                }else{
                    alert(response.message);
                }
            }
        );
    }
    $scope.add_image_entity=function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
	//查询一级下拉列表
	$scope.selectItemCatList=function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List=response;
            }
		);
    }
    //二级下拉列表变化,watch用于监控变量的值，当变量的值变化时就会执行相应的函数
    $scope.$watch('entity.goods.category1Id',function (newvalue,oldvalue) {
		itemCatService.findByParentId(newvalue).success(
			function (response) {
			$scope.itemCat2List=response;
        });
    });
	//三级下拉列表随二级变化
	$scope.$watch('entity.goods.category2Id',function (newvalue,oldvalue) {
		itemCatService.findByParentId(newvalue).success(
			function (response) {
				$scope.itemCat3List=response;
            }
		);
    });
	$scope.$watch('entity.goods.category3Id',function (newvalue,oldvalue) {
		itemCatService.findOne(newvalue).success(
			function (response) {
				$scope.entity.goods.typeTemplateId=response.typeId;
            }
		);
    });

    $scope.$watch('entity.goods.typeTemplateId', function(newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function(response){
                $scope.typeTemplate=response;//获取类型模板
                $scope.typeTemplate.brandIds= JSON.parse( $scope.typeTemplate.brandIds);//品牌列表
                if($location.search()['id']==null) {
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
                }
            }
        );
        typeTemplateService.findSpecList(newValue).success(
        	function (response) {
        		$scope.specList=response;
            }
		);
    });

    $scope.updateSpecAttribute=function($event,name,value){

        var object= $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems ,'attributeName', name);

        if(object!=null){
            if($event.target.checked ){
                object.attributeValue.push(value);
            }else{//取消勾选
                object.attributeValue.splice( object.attributeValue.indexOf(value ) ,1);//移除选项
                //如果选项都取消了，将此条记录移除
                if(object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(object),1);
                }

            }
        }else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }

    }

    //创建SKU列表
    $scope.createItemList=function(){

        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'} ];//列表初始化

        var items= $scope.entity.goodsDesc.specificationItems;

        for(var i=0;i<items.length;i++){
            $scope.entity.itemList= addColumn( $scope.entity.itemList, items[i].attributeName,items[i].attributeValue );
        }

    }

    addColumn=function(list,columnName,columnValues){

        var newList=[];
        for(var i=0;i< list.length;i++){
            var oldRow=  list[i];
            for(var j=0;j<columnValues.length;j++){
                var newRow=  JSON.parse( JSON.stringify(oldRow)  );//深克隆
                newRow.spec[columnName]=columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }
    //定义商品审核状态数组
    $scope.status=['未审核','已审核','审核未通过','关闭'];
    //定义分类数组
    $scope.itemCatList=[];
    $scope.findItemCatList=function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i=0;i<response.length;i++){
                    $scope.itemCatList[response[i].id]=response[i].name;
                }
            }
        );
    }
    
});	
