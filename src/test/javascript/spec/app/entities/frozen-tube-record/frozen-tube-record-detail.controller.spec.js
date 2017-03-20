'use strict';

describe('Controller Tests', function() {

    describe('FrozenTubeRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFrozenTubeRecord, MockSampleType, MockFrozenTubeType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFrozenTubeRecord = jasmine.createSpy('MockFrozenTubeRecord');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockFrozenTubeType = jasmine.createSpy('MockFrozenTubeType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FrozenTubeRecord': MockFrozenTubeRecord,
                'SampleType': MockSampleType,
                'FrozenTubeType': MockFrozenTubeType
            };
            createController = function() {
                $injector.get('$controller')("FrozenTubeRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:frozenTubeRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
