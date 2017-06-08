'use strict';

describe('Controller Tests', function() {

    describe('TranshipBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTranshipBox, MockTranship, MockFrozenBox, MockTranshipBoxPosition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTranshipBox = jasmine.createSpy('MockTranshipBox');
            MockTranship = jasmine.createSpy('MockTranship');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockTranshipBoxPosition = jasmine.createSpy('MockTranshipBoxPosition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TranshipBox': MockTranshipBox,
                'Tranship': MockTranship,
                'FrozenBox': MockFrozenBox,
                'TranshipBoxPosition': MockTranshipBoxPosition
            };
            createController = function() {
                $injector.get('$controller')("TranshipBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:transhipBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
