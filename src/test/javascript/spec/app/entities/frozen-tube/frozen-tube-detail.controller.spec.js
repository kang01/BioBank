'use strict';

describe('Controller Tests', function() {

    describe('FrozenTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFrozenTube, MockFrozenTubeType, MockSampleType, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            MockFrozenTubeType = jasmine.createSpy('MockFrozenTubeType');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FrozenTube': MockFrozenTube,
                'FrozenTubeType': MockFrozenTubeType,
                'SampleType': MockSampleType,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("FrozenTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:frozenTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
