const URL_REFRESH = 'http://localhost:8080/users/refresh';

const SendData = async (data, url, method, navigate) => {

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    }
    if (data) {
        options.body = JSON.stringify(data);
    }

    let response = await fetch(url, options);
    if (response.status === 403) {
        const refreshResponse = await fetch((URL_REFRESH), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
        });
        if (refreshResponse.ok) {
            response = await fetch(url, options);
        } else {
            navigate("/login");
        }
    }
    if (!response.ok) {
        throw new Error(`${await response.json().then(err => err.error)}`);
    }

    const body = await response.text();
    return body ? JSON.parse(body.toString()) : null;
}

export default SendData;