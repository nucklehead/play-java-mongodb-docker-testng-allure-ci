/**
 * Created by jpierre on 2/23/16.
 */
$(function(){
    var user = new Gh3.User("nucklehead");
    var repo = new Gh3.Repository("play-java-mongodb-docker-testng-allure-ci", user);

    repo.fetch(function (err, res) {
        if(err) { throw "outch ..." }
        console.log(repo.full_name);
        repo.fetchBranches(function (err, res) {
            if(err) { throw "outch ..." }
            var branch = repo.getBranchByName("gh-pages");
            console.log(branch.name);
            branch.fetchContents(function (err, res) {
                if(err) { throw "outch ..." }
                var dir = branch.getDirByName('stylesheets');
                dir.fetchContents(function (err, res) {
                    if(err) { throw "outch ..." }
                    console.log(dir.getContents());
                    dir.reverseContents();
                    dir.eachContent(function (content) {
                        console.log(content.name, content.type, content.size);
                    });
                });
            });
        })
    });
});

