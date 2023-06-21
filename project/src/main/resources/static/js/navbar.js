let navbar = document.getElementById("navbar");


// STATIC BUTTONS
let span1 = document.createElement("span");
span1.id = "navbar_static";
let span1_li1 = document.createElement("li"); let span1_li1_a = document.createElement("a"); span1_li1_a.innerHTML = "HOME";   span1_li1_a.href = "/home";   span1_li1.append(span1_li1_a);
let span1_li2 = document.createElement("li"); let span1_li2_a = document.createElement("a"); span1_li2_a.innerHTML = "ITEMS";  span1_li2_a.href = "/items";  span1_li2.append(span1_li2_a);
let span1_li3 = document.createElement("li"); let span1_li3_a = document.createElement("a"); span1_li3_a.innerHTML = "USERS";  span1_li3_a.href = "/users";  span1_li3.append(span1_li3_a);
let span1_li4 = document.createElement("li"); let span1_li4_a = document.createElement("a"); span1_li4_a.innerHTML = "GENRES"; span1_li4_a.href = "/genres"; span1_li4.append(span1_li4_a);
span1.append(span1_li1);
span1.append(span1_li2);
span1.append(span1_li3);
span1.append(span1_li4);

navbar.append(span1);

// DYNAMIC BUTTONS
let span2 = document.createElement("span");
span2.id = "navbar_dynamic";
navbar.append(span2);

async function load_dynamic() {
    const response = await fetch("/api/myaccount");

    if (response.ok) {
        const jsonData = await response.json();
        console.log(jsonData);
        let span2_li1 = document.createElement("li"); let span2_li1_a = document.createElement("a"); span2_li1_a.innerHTML = "MY ACCOUNT"; span2_li1_a.href = "/myaccount"; span2_li1.append(span2_li1_a);
        let span2_li2 = document.createElement("li"); let span2_li2_a = document.createElement("a"); span2_li2_a.innerHTML = "LOGOUT";     span2_li2_a.href = "/logout";    span2_li2.append(span2_li2_a);
        span2.append(span2_li1);
        span2.append(span2_li2);
    }
    else {
        let span2_li1 = document.createElement("li"); let span2_li1_a = document.createElement("a"); span2_li1_a.innerHTML = "LOGIN";    span2_li1_a.href = "/login";    span2_li1.append(span2_li1_a);
        let span2_li2 = document.createElement("li"); let span2_li2_a = document.createElement("a"); span2_li2_a.innerHTML = "REGISTER"; span2_li2_a.href = "/register"; span2_li2.append(span2_li2_a);
        span2.append(span2_li1);
        span2.append(span2_li2);
    }

}

load_dynamic();



