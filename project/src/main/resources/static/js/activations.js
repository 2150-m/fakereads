


function td(text) {
    let td = document.createElement("td");
    td.innerHTML = text;
    return td;
}

function td_button(text, func) {
    let td = document.createElement("td");
    let btn = document.createElement("button")
    btn.innerHTML = text;
    btn.onclick = func;
    td.append(btn);
    return td;
}

function td_a(json) {
    let td = document.createElement("td");
    let a = document.createElement("a");
    a.innerHTML = json.author.username;
    a.href = "/users/" + json.author.id;
    td.append(a);
    return td;
}

async function api_accept(msg, id) {
    const response = await fetch('/api/admin/activations/' + id + '/accept', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: msg})
    });

    console.log(response);

    const text = await response.text();
    console.log(text);

    if (response.ok) {
        window.location.href = "/activations"; // refresh page
    }
}

async function api_reject(msg, id) {
    const response = await fetch('/api/admin/activations/' + id + '/reject', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({message: msg})
    });

    console.log(response);

    const text = await response.text();
    console.log(text);

    if (response.ok) {
        window.location.href = "/activations"; // refresh page
    }
}

function activationAccept(json) {
    
    let msg = prompt("SEND MAIL MESSAGE", "accepted")
    api_accept(msg, json.id);
}

function activationReject(json) {
    let msg = prompt("SEND MAIL MESSAGE", "rejected")
    api_reject(msg, json.id);
}


function makeActivation(json) {


    let tr = document.createElement("tr");
    tr.append(td(json.mailAddress));
    tr.append(td(json.phoneNumber));
    tr.append(td(json.message));
    tr.append(td(json.requestDate));
    tr.append(td(json.status));
    tr.append(td_a(json));
    tr.append(td_button("ACCEPT", function () { activationAccept(json);  }));
    tr.append(td_button("REJECT", function () { activationReject(json); }));

    return tr
}



async function load() {

    const response = await fetch("/api/admin/activations");
    const json = await response.json();

    console.log(json)

    let activations = document.getElementById("activations");

    for (let i = 0; i < json.length; i++) {
        activations.append(makeActivation(json[i]));
    }
}

load();



