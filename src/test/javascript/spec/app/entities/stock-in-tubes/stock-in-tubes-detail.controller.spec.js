'use strict';

describe('Controller Tests', function() {

    describe('StockInTubes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockInTubes, MockStockIn, MockTranship, MockFrozenBox, MockFrozenTube, MockFrozenBoxPosition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockInTubes = jasmine.createSpy('MockStockInTubes');
            MockStockIn = jasmine.createSpy('MockStockIn');
            MockTranship = jasmine.createSpy('MockTranship');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            MockFrozenBoxPosition = jasmine.createSpy('MockFrozenBoxPosition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockInTubes': MockStockInTubes,
                'StockIn': MockStockIn,
                'Tranship': MockTranship,
                'FrozenBox': MockFrozenBox,
                'FrozenTube': MockFrozenTube,
                'FrozenBoxPosition': MockFrozenBoxPosition
            };
            createController = function() {
                $injector.get('$controller')("StockInTubesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockInTubesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
