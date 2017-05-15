'use strict';

describe('Controller Tests', function() {

    describe('StockOutRequiredSample Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStockOutRequiredSample, MockStockOutRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStockOutRequiredSample = jasmine.createSpy('MockStockOutRequiredSample');
            MockStockOutRequirement = jasmine.createSpy('MockStockOutRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StockOutRequiredSample': MockStockOutRequiredSample,
                'StockOutRequirement': MockStockOutRequirement
            };
            createController = function() {
                $injector.get('$controller')("StockOutRequiredSampleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:stockOutRequiredSampleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
