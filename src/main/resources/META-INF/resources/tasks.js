
var apiUrl = "/app/rest/tasks";

function submitRegistration() {
    var model = {
        title: $("#titleInput").val(),
        description: $("#descriptionInput").val(),
        done: $("#done").is(":checked")
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
