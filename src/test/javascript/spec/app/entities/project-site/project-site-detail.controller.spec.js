'use strict';

describe('Controller Tests', function() {

    describe('ProjectSite Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProjectSite, MockProjectRelate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProjectSite = jasmine.createSpy('MockProjectSite');
            MockProjectRelate = jasmine.createSpy('MockProjectRelate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProjectSite': MockProjectSite,
                'ProjectRelate': MockProjectRelate
            };
            createController = function() {
                $injector.get('$controller')("ProjectSiteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:projectSiteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
