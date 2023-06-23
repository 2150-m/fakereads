let navbar = document.getElementById("navbar");

// STATIC BUTTONS
let span1 = document.createElement("span");
span1.id = "navbar_static";

function createLi(text, link) {
    let li = document.createElement("li");
    let a = document.createElement("a");
    a.innerHTML = text;
    a.href = link;
    li.append(a);
    return li;
}

span1.append(createLi("🏠",   "/home"));
span1.append(createLi("📚",   "/items"));
span1.append(createLi("👥",   "/users"));
span1.append(createLi("✍️",  "/authors"));
span1.append(createLi("🕮",  "/genres"));

navbar.append(span1);

// DYNAMIC BUTTONS
let span2 = document.createElement("span");
span2.id = "navbar_dynamic";
navbar.append(span2);

async function load_dynamic() {
    const response = await fetch("/api/myaccount");

    if (response.ok) {
        const myaccountjson = await response.json();
        
        if (myaccountjson.accountRole == "ADMINISTRATOR") {
            span2.append(createLi("🚨", "/activations"));
            span2.append(createLi("➕", "/manage"));
        }

        span2.append(createLi("👤",                                 "/myaccount"));
        span2.append(createLi("⚙",                                 "/update"));
        span2.append(createLi("🚪 (" + myaccountjson.username + ")", "/logout"));
        
    }
    else {
        span2.append(createLi("🔑", "/login"));
        span2.append(createLi("📝", "/register"));
    }
}

load_dynamic();



