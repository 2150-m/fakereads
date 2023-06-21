let genre_id  = document.getElementById("genre_id");

let div_items = document.getElementById("div_items");

async function getGenreAndBooks() {
    const response = await fetch("/api/genres/" + genre_id.value);
    const json = await response.json();
    console.log(json);
    

    let genre = document.getElementById("genre");
    genre.innerHTML = json.name;

    // TODO: fetch books
    let books = document.getElementById("books");
    items_populate(books, json.books);
    
}

getGenreAndBooks();