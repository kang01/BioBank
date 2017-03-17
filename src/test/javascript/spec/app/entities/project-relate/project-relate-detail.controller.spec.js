'use strict';

describe('Controller Tests', function() {

    describe('ProjectRelate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectRelate, MockProject, MockProjectSite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectRelate = jasmine.createSpy('MockProjectRelate');
            MockProject = jasmine.createSpy('MockProject');
            MockProjectSite = jasmine.createSpy('MockProjectSite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectRelate': MockProjectRelate,
                'Project': MockProject,
                'ProjectSite': MockProjectSite
            };
            createController = function() {
                $injector.get('$controller')("ProjectRelateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:projectRelateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
