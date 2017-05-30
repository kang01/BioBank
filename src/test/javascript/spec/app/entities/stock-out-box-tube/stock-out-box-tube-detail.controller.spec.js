'use strict';

describe('Controller Tests', function() {

    describe('StockOutBoxTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutBoxTube, MockStockOutFrozenBox, MockFrozenTube, MockStockOutTaskFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutBoxTube = jasmine.createSpy('MockStockOutBoxTube');
            MockStockOutFrozenBox = jasmine.createSpy('MockStockOutFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            MockStockOutTaskFrozenTube = jasmine.createSpy('MockStockOutTaskFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutBoxTube': MockStockOutBoxTube,
                'StockOutFrozenBox': MockStockOutFrozenBox,
                'FrozenTube': MockFrozenTube,
                'StockOutTaskFrozenTube': MockStockOutTaskFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("StockOutBoxTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutBoxTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
