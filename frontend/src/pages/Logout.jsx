import Button from "../components/Button";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import ErrorsContainer from "../components/ErrorsContainer";
import {useNavigate} from "react-router-dom";
import SendData from "../services/apiService";
import {useContext} from "react";
import {AuthContext} from "../context/CreateContext";
import {ENDPOINTS} from "../api/ENDPOINTS";

const Logout = () => {
    const {display, error, showMessage} = useTimeDisplay();
    const navigate = useNavigate();
    const {setAuth} = useContext(AuthContext);

    const handleLogout = (e) => {
        e.preventDefault();
        SendData(null, ENDPOINTS.LOGOUT, 'POST', null)
            .then(() => {
                localStorage.removeItem('auth');
                setAuth(false);
                navigate('/login');
            })
            .catch(error => showMessage(error.message));
    }

    return (<div className={'logout-container'}><Button onClick={handleLogout} id={'logout'}>Logout</Button>
            <ErrorsContainer display={display}>{error}</ErrorsContainer></div>
    )
}

export default Logout;