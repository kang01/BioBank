'use strict';

describe('Controller Tests', function() {

    describe('StockOutPlanFrozenTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutPlanFrozenTube, MockStockOutPlan, MockFrozenBox, MockFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutPlanFrozenTube = jasmine.createSpy('MockStockOutPlanFrozenTube');
            MockStockOutPlan = jasmine.createSpy('MockStockOutPlan');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutPlanFrozenTube': MockStockOutPlanFrozenTube,
                'StockOutPlan': MockStockOutPlan,
                'FrozenBox': MockFrozenBox,
                'FrozenTube': MockFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("StockOutPlanFrozenTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutPlanFrozenTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
