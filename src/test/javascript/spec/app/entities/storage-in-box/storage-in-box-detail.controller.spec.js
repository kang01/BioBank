'use strict';

describe('Controller Tests', function() {

    describe('StorageInBox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStorageInBox, MockStorageIn, MockEquipment, MockSupportRack, MockArea;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStorageInBox = jasmine.createSpy('MockStorageInBox');
            MockStorageIn = jasmine.createSpy('MockStorageIn');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockSupportRack = jasmine.createSpy('MockSupportRack');
            MockArea = jasmine.createSpy('MockArea');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StorageInBox': MockStorageInBox,
                'StorageIn': MockStorageIn,
                'Equipment': MockEquipment,
                'SupportRack': MockSupportRack,
                'Area': MockArea
            };
            createController = function() {
                $injector.get('$controller')("StorageInBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:storageInBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
