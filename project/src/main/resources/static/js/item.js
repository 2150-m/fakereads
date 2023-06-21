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

async function getBookAndReviews() {
    const response = await fetch("/api/items/" + item_id.value);
    const json = await response.json();
    console.log(json);

    let book = document.getElementById("book");

    let table = document.createElement("table");
    table.append(makeTableRow("TITLE: ",        json.book.title));
    table.append(makeTableRow("RELEASE DATE: ", json.book.releaseDate));
    table.append(makeTableRow("DESCRIPTION: ",  json.book.description));
    table.append(makeTableRow("NUM OF PAGES: ", json.book.numOfPages));
    table.append(makeTableRow("ISBN: ",         json.book.isbn));
    table.append(makeTableRow("RATING: ",       json.book.rating));
    table.append(makeTableRow("GENRES: ",       json.book.genres));
    book.append(table);
    let img = document.createElement("img");
    img.src = json.book.coverPhoto;
    book.append(img);


    // TODO: display all reviews
    let reviews = document.getElementById("reviews");

    for (let i = 0; i < json.bookReviews.length; i++) {
        reviews.append(makeReview(json.bookReviews[i]));
    }
    
}

getBookAndReviews();