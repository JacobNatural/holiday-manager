import SendData from "../services/apiService";
import {useEffect, useState} from "react";
import {ENDPOINTS} from "../api/ENDPOINTS";


export const GetUserData = () => {

    const [user, setUser] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        SendData(null, ENDPOINTS.GET_USER_DATA, 'GET', null)
            .then(data => {
                setUser(data)
            })
            .catch(error => {
                console.log(ENDPOINTS.GET_USER_DATA);
                setError(error.message.replaceAll(/\n/g, '\n'));
            },);
    },[]);

    return (
        <div id={'get-user-data'} className="user-data">
            {error && (
                <div id={"errors"} style={{display: 'grid', color: 'red'}}>{error}</div>
            )}
            {user && (
                <>
                    <p id={"id"}><b>Id:</b> {user.data.id}</p>
                    <p id="username"><b>Username:</b> {user.data.username}</p>
                    <p id="name"><b>Name:</b> {user.data.name}</p>
                    <p id="surname"><b>Surname:</b> {user.data.surname}</p>
                    <p id="email"><b>Email:</b> {user.data.email}</p>
                    <p id="age"><b>Age:</b> {user.data.age}</p>
                    <p id="hours"><b>Holiday hours:</b> {user.data.holidaysHours}</p>
                </>
            )}
        </div>
    )
}

export default GetUserData;