/**
 * Created by gaokangkang on 2018/1/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TagsModalController', TagsModalController);

    TagsModalController.$inject = ['$uibModalInstance','$uibModal','items','BioBankSelectize'];

    function TagsModalController($uibModalInstance,$uibModal,items,BioBankSelectize) {

        var vm = this;
        vm.entity = {
            tag1:null,
            tag2:null,
            tag3:null,
            tag4:null
        };
        var _selectedSample = items.selectSampleData;
        //标签
        var _tagSelectize = {
            create : true,
            persist:false,
            clearMaxItemFlag : true
        };
        vm.TagConfig = BioBankSelectize.buildSettings(_tagSelectize);
        if(_selectedSample.length == 1){
            vm.TagConfig.onInitialize = function (initialize) {
                var tagSelectize = initialize;
                var tagOption = [];
                var tags = [];
                var tags1 = [];
                var tags2 = [];
                tags1.push(_selectedSample[0].tag1);
                tags1.push(_selectedSample[0].tag2);
                tags1.push(_selectedSample[0].tag3);
                if(_selectedSample[0].tag4){
                    tags2 = _.split(_selectedSample[0].tag4, ',');
                }
                tags = _.concat(tags1, tags2);
                _.forEach(tags,function (tag) {
                    var obj = {};
                    obj.text = tag;
                    obj.value =tag;
                    tagOption.push(obj);
                });

                tagSelectize.addOption(tagOption);
                tagSelectize.setValue(tags);
            }
        }
        //标签
        function _updateTagsData() {
            var tags1 = [];
            var tags2 = [];

            _.forEach(vm.tags,function (tag,i) {
                if(i < 3){
                    tags1.push(tag);
                }else{
                    tags2.push(tag);
                }
            });
            _.forEach(tags1,function (tag,i) {
                if(tag){
                    var index = i+1;
                    vm.entity["tag"+index] = tag;
                }
            });
            if(tags2.length){
                vm.entity.tag4 = _.join(tags2,",");
            }

        }
        vm.ok = function () {
            _updateTagsData();
            $uibModalInstance.close(vm.entity);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
