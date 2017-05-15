'use strict';

describe('Controller Tests', function() {

    describe('StockOutBoxPosition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutBoxPosition, MockEquipment, MockArea, MockSupportRack, MockFrozenBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutBoxPosition = jasmine.createSpy('MockStockOutBoxPosition');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutBoxPosition': MockStockOutBoxPosition,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack,
                'FrozenBox': MockFrozenBox
            };
            createController = function() {
                $injector.get('$controller')("StockOutBoxPositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutBoxPositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
