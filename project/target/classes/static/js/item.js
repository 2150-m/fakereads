let item_id = document.getElementById("item_id");

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function makeReview(json) {
    let table = document.createElement("table");
    table.append(makeTableRow("RATING",  json.rating));
    table.append(makeTableRow("TEXT",    json.text));
    table.append(makeTableRow("DATE",    json.reviewDate));
    table.append(makeTableRow("ACCOUNT", json.accountId));
    return table;
}

async function loadbook() {
    const response = await fetch("/api/items/" + item_id.value);
    const json = await response.json();
    console.log(json);

    document.getElementById("book_title").innerHTML       = json.book.title;
    document.getElementById("book_releaseDate").innerHTML = json.book.releaseDate;
    document.getElementById("book_description").innerHTML = json.book.description;
    document.getElementById("book_numOfPages").innerHTML  = json.book.numOfPages;
    document.getElementById("book_isbn").innerHTML        = json.book.isbn;
    document.getElementById("book_rating").innerHTML      = json.book.rating;
    document.getElementById("book_genres").innerHTML      = json.book.genres;
    document.getElementById("book_coverPhoto").src        = json.book.coverPhoto;

    // TODO: display all reviews
    let reviews = document.getElementById("reviews");

    for (let i = 0; i < json.bookReviews.length; i++) {
        reviews.append(makeReview(json.bookReviews[i]));
    }
    
}

loadbook();