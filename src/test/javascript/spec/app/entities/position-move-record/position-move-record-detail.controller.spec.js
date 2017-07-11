'use strict';

describe('Controller Tests', function() {

    describe('PositionMoveRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPositionMoveRecord, MockEquipment, MockArea, MockSupportRack, MockFrozenBox, MockFrozenTube, MockProject, MockProjectSite;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPositionMoveRecord = jasmine.createSpy('MockPositionMoveRecord');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockArea = jasmine.createSpy('MockArea');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            MockProject = jasmine.createSpy('MockProject');
            MockProjectSite = jasmine.createSpy('MockProjectSite');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PositionMoveRecord': MockPositionMoveRecord,
                'Equipment': MockEquipment,
                'Area': MockArea,
                'SupportRack': MockSupportRack,
                'FrozenBox': MockFrozenBox,
                'FrozenTube': MockFrozenTube,
                'Project': MockProject,
                'ProjectSite': MockProjectSite
            };
            createController = function() {
                $injector.get('$controller')("PositionMoveRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:positionMoveRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
