
let btn = document.getElementById("btn_search");

let div_items = document.getElementById("div_items");

async function loaditems(search = "") {
    items_clear(div_items);

    let url = "/api/items"
    if (search != "") { url = "/api/items/search=" + search }
    const response = await fetch(url);
    const jsonData = await response.json();
    items_populate(div_items, jsonData);
}

loaditems();

// SEARCH
let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

function search() {
    loaditems(txt_search.value);
}

txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }



