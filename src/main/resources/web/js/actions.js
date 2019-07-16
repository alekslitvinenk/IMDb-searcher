$(document).ajaxStart(function () {
    $("#console").append("<div id='indicator'>please wait <img src='images/indicator.gif'/></div>");
});

$(document).ajaxStop(function () {
    $("#indicator").remove();
});

function searchByTitleAction() {
  var title = $("#primaryTitleField").val();

  $("#console").append("<div>" + title + "</div>");

  $.ajax({
    type: 'GET',
    dataType:'JSON',
    url: 'title',
    data: 'search=' + title,
    success: function (response) {
      buildSearchResults(response.items)
    },
    error: function (response) {
      $("#console").append("<div>" + error + "</div>");
    }
  });
}

function searchByGenreAction() {
  var genre = $("#genreField").val();

  $("#console").append("<div>" + genre + "</div>");
}

function buildSearchResults(arr) {
  var results = $("#results");
  results.empty();

  if(arr.length > 1) {
    var last = arr.pop();
    arr = arr.map(function (e) {return e + ";"});
    arr.push(last);
  }

  arr.forEach(function (q) {
    results.append("<p>" + q + "</p>");
  })
}
