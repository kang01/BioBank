'use strict';

describe('Controller Tests', function() {

    describe('Relations Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRelations, MockFrozenBoxType, MockFrozenTubeType, MockSampleType, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRelations = jasmine.createSpy('MockRelations');
            MockFrozenBoxType = jasmine.createSpy('MockFrozenBoxType');
            MockFrozenTubeType = jasmine.createSpy('MockFrozenTubeType');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Relations': MockRelations,
                'FrozenBoxType': MockFrozenBoxType,
                'FrozenTubeType': MockFrozenTubeType,
                'SampleType': MockSampleType,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("RelationsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:relationsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
