'use strict';

describe('Controller Tests', function() {

    describe('ProjectSampleClass Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectSampleClass, MockProject, MockSampleType, MockSampleClassification;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectSampleClass = jasmine.createSpy('MockProjectSampleClass');
            MockProject = jasmine.createSpy('MockProject');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockSampleClassification = jasmine.createSpy('MockSampleClassification');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectSampleClass': MockProjectSampleClass,
                'Project': MockProject,
                'SampleType': MockSampleType,
                'SampleClassification': MockSampleClassification
            };
            createController = function() {
                $injector.get('$controller')("ProjectSampleClassDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:projectSampleClassUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
