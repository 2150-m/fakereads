
async function api_logout() {
    const response = await fetch('/api/logout', {method: 'POST'});
    window.location.href = "home";
}

api_logout();