'use strict';

describe('Controller Tests', function() {

    describe('TranshipBoxPosition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTranshipBoxPosition, MockEquipment, MockArea, MockSupportRack, MockTranshipBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTranshipBoxPosition = jasmine.createSpy('MockTranshipBoxPosition');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockTranshipBox = jasmine.createSpy('MockTranshipBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TranshipBoxPosition': MockTranshipBoxPosition,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack,
                'TranshipBox': MockTranshipBox
            };
            createController = function() {
                $injector.get('$controller')("TranshipBoxPositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:transhipBoxPositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
