'use strict';

describe('Controller Tests', function() {

    describe('PositionChangeRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPositionChangeRecord, MockPositionChange;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPositionChangeRecord = jasmine.createSpy('MockPositionChangeRecord');
            MockPositionChange = jasmine.createSpy('MockPositionChange');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PositionChangeRecord': MockPositionChangeRecord,
                'PositionChange': MockPositionChange
            };
            createController = function() {
                $injector.get('$controller')("PositionChangeRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:positionChangeRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
