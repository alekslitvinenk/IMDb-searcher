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

  $.ajax({
    type: 'GET',
    dataType:'JSON',
    url: 'genre',
    data: 'search=' + genre,
    success: function (response) {
      buildSearchResults(response.items)
    },
    error: function (response) {
      $("#console").append("<div>" + error + "</div>");
    }
  });
}

function buildSearchResults(arr) {
  var results = $("#results");
  results.empty();

  console.log(arr)

  arr.forEach(function (film) {

    var str = "<p><b>Title: </b>" + film.title +
        ". <b>Duration: </b>" + film.runTime + " min. "
        + "<b>Rating: </b>" + film.rating.toFixed(1) + "<br>"
        + "<b>Genres:</b> " + film.genres + ". "
        + "<b>Year: </b> " + film.startYear + "<br>"
        + "<b>Cast and Crew: </b> " + film.castAndCrew + "</p><hr>"

    results.append(str);
  })
}