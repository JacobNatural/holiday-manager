import UserManagerForm from "../components/UserManagerForm";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import {useEffect, useState} from "react";
import SendData from "../services/apiService";
import Button from "../components/Button";
import {useLocation} from "react-router-dom";
import {ENDPOINTS} from "../api/ENDPOINTS";

const UserManager = () => {
    const {error, display, showMessage} = useTimeDisplay();
    const [users, setUsers] = useState(() => {
        return JSON.parse(sessionStorage.getItem('users')) || []
    });
    const [name, setName] = useState(null);
    const [surname, setSurname] = useState(null);
    const [email, setEmail] = useState(null);
    const [username, setUsername] = useState(null);
    const [holidayHoursEditable, setHolidayHoursEditable] = useState(false);
    const [rowIdHolidayHours, setRowIdHolidayHours] = useState(null);
    const [rowIdRole, setRowIdRole] = useState(null);
    const [roleEditable, setRoleEditable] = useState(false);
    const location = useLocation();

    useEffect(() => {
        const saved = sessionStorage.getItem('users');
        if (saved) setUsers(JSON.parse(saved));
    }, []);

    useEffect(() => {
        sessionStorage.setItem('users', JSON.stringify(users));
    }, [users]);

    useEffect(() => {
        return () =>
            sessionStorage.removeItem('users');
    }, [location.pathname]);

    const onSubmit = (e) => {
        e.preventDefault();
        const data = {
            name: name,
            surname: surname,
            email: email,
            username: username
        }
        SendData(data, ENDPOINTS.USER_FILTER, 'PATCH', null)
            .then(users => setUsers(users.data))
            .catch(e => showMessage(e.message));
    }

    const updateUsers = (predicate, property, value) => {
        const updatedUsers = users.map(user => {
            if (predicate(user)) {
                user[property] = value;
            }
            return user;
        })
        setUsers(updatedUsers);
    }

    const saveData = (user) => {
        const data = {
            userId: user.id,
            holidayHours: user.holidaysHours,
            role: user.role,
        }

        SendData(data, ENDPOINTS.USER_UPDATE, 'PATCH', null)
            .catch(e => showMessage(e.message));
    }


    return (<div className="container" onSubmit={onSubmit}>
        <UserManagerForm onSubmit={onSubmit}
                         name={name} nameOnChange={x => setName(x.target.value)}
                         surname={surname} surnameOnChange={x => setSurname(x.target.value)}
                         email={email} emailOnChange={x => setEmail(x.target.value)}
                         username={username} usernameOnChange={x => setUsername(x.target.value)}/>
        <table id="table-user">
            <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Surname</th>
                <th>Username</th>
                <th>Email</th>
                <th>Holiday hours</th>
                <th>Age</th>
                <th>Role</th>
                <th></th>
            </tr>
            </thead>
            <tbody id="tbody">
            {users.map(user => (
                <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.name}</td>
                    <td>{user.surname}</td>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    {holidayHoursEditable && user.id === rowIdHolidayHours ?
                        <td><input defaultValue={user.holidaysHours} onKeyDown={(e) => {
                            if (e.key === "Enter") {
                                updateUsers(x => x.id === rowIdHolidayHours, 'holidaysHours', e.target.value);
                                setHolidayHoursEditable(false);
                                setRowIdHolidayHours(null);
                            }
                        }}></input></td> : <td onClick={() => {
                            setHolidayHoursEditable(true);
                            setRowIdHolidayHours(user.id);
                        }}>{user.holidaysHours}</td>}
                    <td>{user.age}</td>
                    {roleEditable && user.id === rowIdRole ? (<td onKeyDown={(e) => {
                            updateUsers(x => x.id === rowIdRole, 'role', e.target.value);
                            setRoleEditable(false);
                            setRowIdRole(null);

                        }}><select>
                            <option value={'ROLE_ADMIN'}>ADMIN</option>
                            <option value={'ROLE_WORKER'}>WORKER</option>
                        </select></td>) :
                        (<td onClick={() => {
                            setRoleEditable(true);
                            setRowIdRole(user.id);
                        }}>{user.role.replace('ROLE_', '')}</td>)
                    }
                    <td><Button id={'save-button'} onClick={() => saveData(user)}>save</Button></td>
                </tr>
            ))}
            </tbody>
        </table>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </div>)
}

export default UserManager;