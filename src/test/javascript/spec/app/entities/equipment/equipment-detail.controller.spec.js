'use strict';

describe('Controller Tests', function() {

    describe('Equipment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEquipment, MockEquipmentGroup, MockEquipmentModle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockEquipmentGroup = jasmine.createSpy('MockEquipmentGroup');
            MockEquipmentModle = jasmine.createSpy('MockEquipmentModle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Equipment': MockEquipment,
                'EquipmentGroup': MockEquipmentGroup,
                'EquipmentModle': MockEquipmentModle
            };
            createController = function() {
                $injector.get('$controller')("EquipmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:equipmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
