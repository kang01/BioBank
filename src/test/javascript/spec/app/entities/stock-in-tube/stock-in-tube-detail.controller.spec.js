'use strict';

describe('Controller Tests', function() {

    describe('StockInTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockInTube, MockStockInBox, MockFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockInTube = jasmine.createSpy('MockStockInTube');
            MockStockInBox = jasmine.createSpy('MockStockInBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockInTube': MockStockInTube,
                'StockInBox': MockStockInBox,
                'FrozenTube': MockFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("StockInTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockInTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
