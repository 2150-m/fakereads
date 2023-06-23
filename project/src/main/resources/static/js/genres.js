let div_items = document.getElementById("div_items");

function div_items_clear() {
    
    while (div_items.firstChild) {
        div_items.removeChild(div_items.lastChild);
    }
}

function makeItem(json) {
    let a = document.createElement("a");
    a.href = "/genres/" + json.id;

    let p = document.createElement("p");
    p.innerHTML = json.name;
    a.append(p);

    return a;
}

function div_items_populate(jsonData) {

    for (let i = 0; i < jsonData.length; i++) {
        div_items.append(makeItem(jsonData[i]));
    }
}

async function loaditems() {
    div_items_clear();

    const response = await fetch("/api/genres");
    const jsonData = await response.json();

    div_items_populate(jsonData);
}

loaditems();

