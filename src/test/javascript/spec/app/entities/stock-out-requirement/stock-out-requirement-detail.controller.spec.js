'use strict';

describe('Controller Tests', function() {

    describe('StockOutRequirement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutRequirement, MockStockOutApply, MockSampleType, MockSampleClassification, MockFrozenTubeType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutRequirement = jasmine.createSpy('MockStockOutRequirement');
            MockStockOutApply = jasmine.createSpy('MockStockOutApply');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockSampleClassification = jasmine.createSpy('MockSampleClassification');
            MockFrozenTubeType = jasmine.createSpy('MockFrozenTubeType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutRequirement': MockStockOutRequirement,
                'StockOutApply': MockStockOutApply,
                'SampleType': MockSampleType,
                'SampleClassification': MockSampleClassification,
                'FrozenTubeType': MockFrozenTubeType
            };
            createController = function() {
                $injector.get('$controller')("StockOutRequirementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutRequirementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
