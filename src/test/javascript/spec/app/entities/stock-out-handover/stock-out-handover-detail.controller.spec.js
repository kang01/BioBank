'use strict';

describe('Controller Tests', function() {

    describe('StockOutHandover Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutHandover, MockStockOutTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutHandover = jasmine.createSpy('MockStockOutHandover');
            MockStockOutTask = jasmine.createSpy('MockStockOutTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutHandover': MockStockOutHandover,
                'StockOutTask': MockStockOutTask
            };
            createController = function() {
                $injector.get('$controller')("StockOutHandoverDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutHandoverUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
