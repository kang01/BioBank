'use strict';

describe('Controller Tests', function() {

    describe('StockOutReqFrozenTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutReqFrozenTube, MockFrozenBox, MockFrozenTube, MockStockOutRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutReqFrozenTube = jasmine.createSpy('MockStockOutReqFrozenTube');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            MockStockOutRequirement = jasmine.createSpy('MockStockOutRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutReqFrozenTube': MockStockOutReqFrozenTube,
                'FrozenBox': MockFrozenBox,
                'FrozenTube': MockFrozenTube,
                'StockOutRequirement': MockStockOutRequirement
            };
            createController = function() {
                $injector.get('$controller')("StockOutReqFrozenTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutReqFrozenTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
