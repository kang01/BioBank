'use strict';

describe('Controller Tests', function() {

    describe('BoxAndTube Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBoxAndTube, MockFrozenBox, MockFrozenTube;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBoxAndTube = jasmine.createSpy('MockBoxAndTube');
            MockFrozenBox = jasmine.createSpy('MockFrozenBox');
            MockFrozenTube = jasmine.createSpy('MockFrozenTube');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BoxAndTube': MockBoxAndTube,
                'FrozenBox': MockFrozenBox,
                'FrozenTube': MockFrozenTube
            };
            createController = function() {
                $injector.get('$controller')("BoxAndTubeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bioBankApp:boxAndTubeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
