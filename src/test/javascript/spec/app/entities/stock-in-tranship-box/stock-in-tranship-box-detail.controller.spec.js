'use strict';

describe('Controller Tests', function() {

    describe('StockInTranshipBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockInTranshipBox, MockTranshipBox, MockStockIn;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockInTranshipBox = jasmine.createSpy('MockStockInTranshipBox');
            MockTranshipBox = jasmine.createSpy('MockTranshipBox');
            MockStockIn = jasmine.createSpy('MockStockIn');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockInTranshipBox': MockStockInTranshipBox,
                'TranshipBox': MockTranshipBox,
                'StockIn': MockStockIn
            };
            createController = function() {
                $injector.get('$controller')("StockInTranshipBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockInTranshipBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
