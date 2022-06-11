
var apiUrl = "/app/rest/tasks";

function submitRegistration() {
    var model = {
        email: $("#emailInput").val(),
        name: $("#nameInput").val(),
        userId: $("#userIdInput").val(),
        school: $("#schoolInput").val(),
        comment: $("#commentInput").val()
    };
    $.post({ url: apiUrl, data: JSON.stringify(model), contentType: "application/json"})
        .done(function() { 
            $("#registration-success-box").show();
            $("#registration-error-box").hide();
            $("#form-registration")[0].reset();
            loadRegistrations();
        })
        .fail(function() {
            $("#registration-success-box").hide();
            $("#registration-error-box").show();
        });
}

function loadRegistrations() {
    // var model = [
    //     { "email": "aaaa@gmail.com", "name": "A Oswald", "userid": "aa", "school": null, "comment": "nix\r\nda" }, 
    //     { "email": "bbbb@gmail.com", "name": "B Oswald", "userid": "bb", "school": "KSR", "comment": null }
    // ];
    $.getJSON(apiUrl, function (model) { 
        var template = document.getElementById('template-registrations').innerHTML;
        var output = Mustache.render(template, model);
        $("#target-registrations").html(output);
    });
}

$(document).ready(function () {
    $("#form-registration").submit(function (event) {
        submitRegistration();
        event.preventDefault();
    });
    loadRegistrations();
});
