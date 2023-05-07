$(document).ready(function () {

    // remove pre-loader start
    setTimeout(function () {
        $('.loader-bg').fadeOut('slow', function () {
            $(this).remove();
        });
    }, 400);
    // remove pre-loader end
})
