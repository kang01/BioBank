'use strict';

describe('Controller Tests', function() {

    describe('StockOutHandOver Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutHandOver, MockStockOutTask, MockStockOutApply, MockStockOutPlan;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutHandOver = jasmine.createSpy('MockStockOutHandOver');
            MockStockOutTask = jasmine.createSpy('MockStockOutTask');
            MockStockOutApply = jasmine.createSpy('MockStockOutApply');
            MockStockOutPlan = jasmine.createSpy('MockStockOutPlan');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutHandOver': MockStockOutHandOver,
                'StockOutTask': MockStockOutTask,
                'StockOutApply': MockStockOutApply,
                'StockOutPlan': MockStockOutPlan
            };
            createController = function() {
                $injector.get('$controller')("StockOutHandOverDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutHandOverUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
