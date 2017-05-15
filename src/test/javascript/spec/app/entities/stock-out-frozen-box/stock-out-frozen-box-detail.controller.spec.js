'use strict';

describe('Controller Tests', function() {

    describe('StockOutFrozenBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutFrozenBox, MockFrozenBox, MockStockOutBoxPosition, MockStockOutTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutFrozenBox = jasmine.createSpy('MockStockOutFrozenBox');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockStockOutBoxPosition = jasmine.createSpy('MockStockOutBoxPosition');
            MockStockOutTask = jasmine.createSpy('MockStockOutTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutFrozenBox': MockStockOutFrozenBox,
                'FrozenBox': MockFrozenBox,
                'StockOutBoxPosition': MockStockOutBoxPosition,
                'StockOutTask': MockStockOutTask
            };
            createController = function() {
                $injector.get('$controller')("StockOutFrozenBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutFrozenBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
