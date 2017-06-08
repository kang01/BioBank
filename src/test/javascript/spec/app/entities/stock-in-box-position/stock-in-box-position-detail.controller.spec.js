'use strict';

describe('Controller Tests', function() {

    describe('StockInBoxPosition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockInBoxPosition, MockEquipment, MockArea, MockSupportRack, MockStockInBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockInBoxPosition = jasmine.createSpy('MockStockInBoxPosition');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockStockInBox = jasmine.createSpy('MockStockInBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockInBoxPosition': MockStockInBoxPosition,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack,
                'StockInBox': MockStockInBox
            };
            createController = function() {
                $injector.get('$controller')("StockInBoxPositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockInBoxPositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
