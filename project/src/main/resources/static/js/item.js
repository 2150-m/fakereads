let item_id = document.getElementById("item_id").value;

function td(text) {
    let td = document.createElement("td");
    td.innerHTML = text;
    return td;
}

function td_a(json) {
    let td = document.createElement("td");
    let a = document.createElement("a");
    a.innerHTML = json.accountId;
    a.href = "/users/" + json.accountId;
    td.append(a);
    return td;
}

function makeReview(json) {
    let tr = document.createElement("tr");
    tr.append(td(json.rating));
    tr.append(td(json.text));
    tr.append(td(json.reviewDate));
    tr.append(td_a(json));
    return tr;
}



async function addbook_wReview(shelfId, bookId) {

    let v_rating = document.getElementById("form_postReview_rating").value;
    let v_text = document.getElementById("form_postReview_text").value;

    const response = await fetch('/api/myaccount/shelves/' + shelfId + '/addbook/' + bookId, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            rating: v_rating,
            text: v_text,
        })
    });

    const text = await response.text();
    console.log(text);
    document.getElementById("shelf_status").innerHTML = text;

    document.getElementById("form_postReview_popup").style.display = "block";
}

async function addbook(shelfId, bookId) {

    const response = await fetch('/api/myaccount/shelves/' + shelfId + '/addbook/' + bookId, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
    });

    const text = await response.text();
    console.log(text);
    document.getElementById("shelf_status").innerHTML = text;
}

async function addToShelf(bookId) {

    let sel = document.getElementById("shelf");
    var selectedShelfName = sel.options[sel.selectedIndex].name;
    let selectedShelfId = sel.value;

    if (selectedShelfName.toUpperCase() == "READ") {
        document.getElementById("form_postReview_popup").style.display = "block";


        let btn_postReview = document.getElementById("form_postReview_btn");
        btn_postReview.onclick = function() {
            addbook_wReview(selectedShelfId, bookId);
        }
    }
    else {
        addbook(selectedShelfId, bookId);
    }
}


async function displayBookControls(bookId) {
    const response = await fetch("/api/myaccount");
    
    if (response.ok) {
        
        const json = await response.json();

        // populate select box
        let options = document.getElementById("shelf");
        for (let i = 0; i < json.shelves.length; i++) {
            let option = document.createElement("option");
            option.value = json.shelves[i].id;
            option.name = json.shelves[i].name;
            option.innerHTML = json.shelves[i].name;
            options.append(option);
        }

        // add to shelf button
        let btn = document.getElementById("shelf_add");
        btn.onclick = function() { addToShelf(bookId); }


        // display controls
        document.getElementById("book_controls").style.display = "block"; // TODO: add none by default
    }
}

async function loadBook() {
    const response = await fetch("/api/items/" + item_id);
    const itemjson = await response.json();
    console.log(itemjson);

    document.getElementById("book_title").innerHTML       = itemjson.book.title;
    document.getElementById("book_releaseDate").innerHTML = itemjson.book.releaseDate;
    document.getElementById("book_description").innerHTML = itemjson.book.description;
    document.getElementById("book_numOfPages").innerHTML  = itemjson.book.numOfPages;
    document.getElementById("book_isbn").innerHTML        = itemjson.book.isbn;
    document.getElementById("book_rating").innerHTML      = itemjson.book.rating;
    document.getElementById("book_genres").innerHTML      = itemjson.book.genres;
    document.getElementById("book_coverPhoto").src        = itemjson.book.coverPhoto;


    // check if logged in / display book controls
    displayBookControls(itemjson.book.id);


    // TODO: display all reviews
    let reviews = document.getElementById("reviews");

    for (let i = 0; i < itemjson.bookReviews.length; i++) {
        reviews.append(makeReview(itemjson.bookReviews[i]));
    }
    
}

loadBook();