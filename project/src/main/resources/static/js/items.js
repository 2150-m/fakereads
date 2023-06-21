

let btn = document.getElementById("btn_search");

let div_items = document.getElementById("div_items");



async function loaditems() {
    items_clear(div_items);
    const response = await fetch("/api/items");
    const jsonData = await response.json();
    items_populate(div_items, jsonData);
}

loaditems();



// SEARCH
let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

async function searchItem(search) {

    div_items_clear();
    const response = await fetch("/api/items/search=" + search);
    const jsonData = await response.json();
    div_items_populate(jsonData);
}

function search() {
    searchItem(txt_search.value);
}

txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }



