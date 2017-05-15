'use strict';

describe('Controller Tests', function() {

    describe('StockOutFrozenTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutFrozenTube, MockStockOutFrozenBox, MockFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutFrozenTube = jasmine.createSpy('MockStockOutFrozenTube');
            MockStockOutFrozenBox = jasmine.createSpy('MockStockOutFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutFrozenTube': MockStockOutFrozenTube,
                'StockOutFrozenBox': MockStockOutFrozenBox,
                'FrozenTube': MockFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("StockOutFrozenTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutFrozenTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
