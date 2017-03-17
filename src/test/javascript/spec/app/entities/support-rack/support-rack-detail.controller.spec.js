'use strict';

describe('Controller Tests', function() {

    describe('SupportRack Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSupportRack, MockSupportRackType, MockArea;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockSupportRackType = jasmine.createSpy('MockSupportRackType');
            MockArea = jasmine.createSpy('MockArea');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SupportRack': MockSupportRack,
                'SupportRackType': MockSupportRackType,
                'Area': MockArea
            };
            createController = function() {
                $injector.get('$controller')("SupportRackDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:supportRackUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
