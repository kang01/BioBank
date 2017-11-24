/**
 * Created by gaokangkang on 2017/10/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AddBatchBoxModalController', AddBatchBoxModalController);

    AddBatchBoxModalController.$inject = ['$uibModalInstance','$q','DTColumnBuilder','BioBankDataTable'];

    function AddBatchBoxModalController($uibModalInstance,$q,DTColumnBuilder,BioBankDataTable) {
        var vm = this;
        var storage=window.localStorage;
        vm.last = _last;
        //上一次
        function _last() {
            var keys =  _.keys(storage);
            var key = _.last(keys);
            var boxCode = storage.getItem(key);
            if(boxCode){
                vm.arrayBoxCode = _.split(boxCode, ',');
                vm.myOptions = [];
                for(var i = 0; i < vm.arrayBoxCode.length; i++){
                    var obj = {};
                    obj.value = vm.arrayBoxCode[i];
                    vm.myOptions.push(obj);
                }
            }
            storage.removeItem(key);
        }


        //添加冻存盒
        vm.boxCodeConfig = {
            valueField:'value',
            labelField:'value',
            create: true,
            persist:false,
            createOnBlur:false,
            onInitialize: function(selectize){
                vm.boxCodeSelectize = selectize;
            },
            onChange: function(value){
                if(storage.length < 6 &&  value.length){
                    var keys =  _.keys(storage);
                    storage.setItem("PLDCH"+new Date().getTime(),value);



                }else{
                    var keys =  _.keys(storage);
                    var key = _.head(keys);
                    storage.removeItem(key);

                    storage.setItem("PLDCH"+new Date().getTime(),value);
                }
            },
            onItemRemove:function (value) {
                console.log(value)
            }
        };

        vm.dtOptions = BioBankDataTable.buildDTOption("BASIC", 252, 10);
        vm.dtColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "auto"),
            DTColumnBuilder.newColumn(1).withOption("width", "auto"),
            DTColumnBuilder.newColumn(2).withOption("width", "80"),
            DTColumnBuilder.newColumn(3).withOption("width", "80"),
            DTColumnBuilder.newColumn(4).withOption("width", "80"),
            DTColumnBuilder.newColumn(5).withOption("width", "60"),
            DTColumnBuilder.newColumn(6).withOption("width", "60")
        ];
        //导入数据
        vm.importBox = function () {
            var array = vm.arrayBoxCode.sort();
            console.log(JSON.stringify(array));




            // var querys = [];
            // for(var i = 0 , len = boxes.length; i < len; i++){
            //     var queryBox = StockInService.getStockInView(boxes[i].id);
            //     querys.push(queryBox);
            //
            //     if (querys.length >= 10 || len == i + 1){
            //         $q.all(querys).then(function(datas){
            //             var boxesDetail = _.map(datas, function(res){
            //                 if(res.data){
            //                     vm.boxList.push(res.data);
            //                     return res.data;
            //                 }
            //                 vm.boxList.push(res);
            //                 return res;
            //
            //             });
            //
            //         });
            //         querys = [];
            //     }
            // }
        };



        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };
    }

})();
