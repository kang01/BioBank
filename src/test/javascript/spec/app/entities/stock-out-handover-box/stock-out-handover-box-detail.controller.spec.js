'use strict';

describe('Controller Tests', function() {

    describe('StockOutHandoverBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutHandoverBox, MockEquipment, MockArea, MockSupportRack, MockStockOutFrozenBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutHandoverBox = jasmine.createSpy('MockStockOutHandoverBox');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockStockOutFrozenBox = jasmine.createSpy('MockStockOutFrozenBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutHandoverBox': MockStockOutHandoverBox,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack,
                'StockOutFrozenBox': MockStockOutFrozenBox
            };
            createController = function() {
                $injector.get('$controller')("StockOutHandoverBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutHandoverBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
