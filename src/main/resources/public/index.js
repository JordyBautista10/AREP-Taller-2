function loadGetMsg() {
  let nameVar = document.getElementById("name").value;
  const xhttp = new XMLHttpRequest();
  xhttp.onload = function() {
    var jsonResponse = JSON.parse(this.responseText);
    if (jsonResponse.Response === "False") {
      document.getElementById("getrespmsg").innerHTML = "No information found for the movie.";
    } else {
      var htmlContent = "<b>Title: </b>" + jsonResponse.Title + "<br>";
      htmlContent += "<b> Year: </b>" + jsonResponse.Year + "<br>";
      htmlContent += "<b> RatedRated: </b>" + jsonResponse.Rated + "<br>";
      htmlContent += "<b> Released: </b>" + jsonResponse.Released + "<br>";
      htmlContent += "<b> Runtime: </b>" + jsonResponse.Runtime + "<br>";
      htmlContent += "<b> Genre: </b>" + jsonResponse.Genre + "<br>";
      htmlContent += "<b> Director: </b>" + jsonResponse.Director + "<br>";
      htmlContent += "<b> Writer: </b>" + jsonResponse.Writer + "<br>";
      htmlContent += "<b> Actors: </b>" + jsonResponse.Actors + "<br>";
      htmlContent += "<b> Plot: </b>" + jsonResponse.Plot + "<br>";
      htmlContent += "<b> Language: </b>" + jsonResponse.Language + "<br>";
      htmlContent += "<b> Country: </b>" + jsonResponse.Country + "<br>";
      htmlContent += "<b> Awards: </b>" + jsonResponse.Awards + "<br>";
      htmlContent += '<b> Ratings: </b>' + formatRanking(jsonResponse.Ratings) + '<br>';
      htmlContent += "<b> Metascore: </b>" + jsonResponse.Metascore + "<br>";
      htmlContent += "<b> imdbRating: </b>" + jsonResponse.imdbRating + "<br>";
      htmlContent += "<b> imdbVotes: </b>" + jsonResponse.imdbVotes + "<br>";
      htmlContent += "<b> imdbID: </b>" + jsonResponse.imdbID + "<br>";
      htmlContent += "<b> imdbVotes: </b>" + jsonResponse.imdbVotes + "<br>";
      htmlContent += "<b> Type: </b>" + jsonResponse.Type + "<br>";
      htmlContent += "<b> DVD: </b>" + jsonResponse.DVD + "<br>";
      htmlContent += "<b> BoxOffice: </b>" + jsonResponse.BoxOffice + "<br>";
      htmlContent += "<b> Production: </b>" + jsonResponse.Production + "<br>";
      htmlContent += "<b> Website: </b>" + jsonResponse.Website + "<br>";
      htmlContent += "<b> Response: </b>" + jsonResponse.Response + "<br>";
      htmlContent += "<b> Poster: </b>" + "<img src=" + jsonResponse.Poster + " > <br>";
      htmlContent += "<b> Info: </b>" + JSON.stringify(jsonResponse) + "<br>";
      document.getElementById("getrespmsg").innerHTML = htmlContent;
    }
  };
  xhttp.open("GET", "http://localhost:35000/Busqueda?t=" + nameVar);
  xhttp.send();
}

function formatRanking(ranking) {
  let html = '<table border="1">';
  html += '<tr><th>Source</th><th>Value</th></tr>';

  for (let i = 0; i < ranking.length; i++) {
    html += '<tr>';
    html += '<td>' + ranking[i].Source + '</td>';
    html += '<td>' + ranking[i].Value + '</td>';
    html += '</tr>';
  }

  html += '</table>';
  return html;
}