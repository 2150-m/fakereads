let item_id = document.getElementById("item_id").value;

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

async function api_addbook(itemjson) {

    var selectedShelfId = document.getElementById("shelf").value;

    const response = await fetch('/api/myaccount/shelves/' + selectedShelfId + '/addbook/' + itemjson.book.id, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
    });


    const text = await response.text();

    document.getElementById("shelf_status").innerHTML = text;

}

async function displayBookControls(itemjson) {
    const response = await fetch("/api/myaccount");
    const json = await response.json();

    if (response.ok) {
        

        // populate select box
        let options = document.getElementById("shelf");
        for (let i = 0; i < json.shelves.length; i++) {
            let option = document.createElement("option");
            option.value = json.shelves[i].id;
            option.innerHTML = json.shelves[i].name;
            options.append(option);
        }

        // add to shelf button
        let btn = document.getElementById("shelf_add");
        btn.onclick = function() { api_addbook(itemjson); }


        // display controls
        let user_controls = document.getElementById("book_controls");
        user_controls.style.display = "block"; // TODO: add none by default
    }
}

async function loadBook() {
    const response = await fetch("/api/items/" + item_id);
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


    // check if logged in / display book controls
    displayBookControls(json);


    // TODO: display all reviews
    let reviews = document.getElementById("reviews");

    for (let i = 0; i < json.bookReviews.length; i++) {
        reviews.append(makeReview(json.bookReviews[i]));
    }
    
}

loadBook();