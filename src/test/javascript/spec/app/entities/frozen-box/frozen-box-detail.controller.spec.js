'use strict';

describe('Controller Tests', function() {

    describe('FrozenBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFrozenBox, MockFrozenBoxType, MockSampleType, MockProject, MockProjectSite, MockTranship, MockEquipment, MockArea, MockSupportRack;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenBoxType = jasmine.createSpy('MockFrozenBoxType');
            MockSampleType = jasmine.createSpy('MockSampleType');
            MockProject = jasmine.createSpy('MockProject');
            MockProjectSite = jasmine.createSpy('MockProjectSite');
            MockTranship = jasmine.createSpy('MockTranship');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FrozenBox': MockFrozenBox,
                'FrozenBoxType': MockFrozenBoxType,
                'SampleType': MockSampleType,
                'Project': MockProject,
                'ProjectSite': MockProjectSite,
                'Tranship': MockTranship,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack
            };
            createController = function() {
                $injector.get('$controller')("FrozenBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:frozenBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
