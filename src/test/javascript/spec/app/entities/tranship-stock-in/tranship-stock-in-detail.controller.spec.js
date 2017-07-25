'use strict';

describe('Controller Tests', function() {

    describe('TranshipStockIn Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTranshipStockIn, MockTranship, MockStockIn;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTranshipStockIn = jasmine.createSpy('MockTranshipStockIn');
            MockTranship = jasmine.createSpy('MockTranship');
            MockStockIn = jasmine.createSpy('MockStockIn');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TranshipStockIn': MockTranshipStockIn,
                'Tranship': MockTranship,
                'StockIn': MockStockIn
            };
            createController = function() {
                $injector.get('$controller')("TranshipStockInDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:transhipStockInUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
