/**
 * Created by jpierre on 2/23/16.
 */
$(function(){

});

var phonecatApp = angular.module('builds-app', []);
var user = new Gh3.User("nucklehead");
var repo = new Gh3.Repository("play-java-mongodb-docker-testng-allure-ci", user);



phonecatApp.controller('buildsCtrl', function ($scope, $sce) {

    repo.fetch(function (err, res) {
        if (err) {
            throw "outch ..."
        }
        console.log(repo.full_name);
        repo.fetchBranches(function (err, res) {
            if (err) {
                throw "outch ..."
            }
            var branch = repo.getBranchByName("gh-pages");
            console.log(branch.name);
            branch.fetchContents(function (err, res) {
                if (err) {
                    throw "outch ..."
                }
                var dir = branch.getDirByName('builds');
                dir.fetchContents(function (err, res) {
                    if (err) {
                        throw "outch ..."
                    }
                    console.log(dir.getContents());
                    $scope.builds = dir.getContents();
                    $scope.$apply();
                    dir.reverseContents();
                    dir.eachContent(function (content) {
                        console.log(content.name, content.type, content.size);
                    });
                });
            });
        });
    });

    console.log($scope.builds);
    $scope.buildUrl = $sce.trustAsResourceUrl("builds/firstTest/index.html");
    $scope.selectedIndex = 0;

    $scope.showResults = function(build, index){
        $scope.selectedIndex=index;
        $scope.buildUrl = $sce.trustAsResourceUrl("builds/" + build.name + "/index.html");
    }
});

