'use strict';

describe('Controller Tests', function() {

    describe('StorageIn Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStorageIn, MockTranship, MockProject, MockProjectSite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStorageIn = jasmine.createSpy('MockStorageIn');
            MockTranship = jasmine.createSpy('MockTranship');
            MockProject = jasmine.createSpy('MockProject');
            MockProjectSite = jasmine.createSpy('MockProjectSite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StorageIn': MockStorageIn,
                'Tranship': MockTranship,
                'Project': MockProject,
                'ProjectSite': MockProjectSite
            };
            createController = function() {
                $injector.get('$controller')("StorageInDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:storageInUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
