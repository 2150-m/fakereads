let genres = document.getElementById("genres");

function makeGenre(json) {
    let a = document.createElement("a");
    a.className = "genre_link";
    a.href = "/genres/" + json.id;

    let h2 = document.createElement("h2");
    h2.className = "genre";
    h2.innerHTML = json.name;
    a.append(h2);

    return a;
}

function genres_populate(jsonData) {

    for (let i = 0; i < jsonData.length; i++) {
        genres.append(makeGenre(jsonData[i]));
    }
}

async function loadGenres() {

    const response = await fetch("/api/genres");
    const jsonData = await response.json();

    genres_populate(jsonData);
}

loadGenres();

