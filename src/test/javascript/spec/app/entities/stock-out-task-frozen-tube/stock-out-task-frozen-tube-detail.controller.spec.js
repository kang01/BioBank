'use strict';

describe('Controller Tests', function() {

    describe('StockOutTaskFrozenTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutTaskFrozenTube, MockStockOutTask, MockStockOutPlanFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutTaskFrozenTube = jasmine.createSpy('MockStockOutTaskFrozenTube');
            MockStockOutTask = jasmine.createSpy('MockStockOutTask');
            MockStockOutPlanFrozenTube = jasmine.createSpy('MockStockOutPlanFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutTaskFrozenTube': MockStockOutTaskFrozenTube,
                'StockOutTask': MockStockOutTask,
                'StockOutPlanFrozenTube': MockStockOutPlanFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("StockOutTaskFrozenTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutTaskFrozenTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
