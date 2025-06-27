import Div from "../components/Div";
import Button from "../components/Button";
import {useNavigate} from "react-router-dom";
import SendData from "../services/apiService";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";

const URI = 'http://localhost:8080/users/in';
const Settings = () => {
    const navigate = useNavigate();
    const {display, error, showMessage} = useTimeDisplay();
    const changePassword = () => {
        navigate("/change-password");
    }

    const changeEmail = () => {
        navigate("/change-email");
    }

    const deleteUser = () => {
        SendData(null, URI, 'DELETE', null)
            .then(res => navigate("/"))
            .catch(err => showMessage(err.message));

    }
    return (<Div>
        <Button id={"change-password"} onClick={changePassword}>Change password</Button>
        <Button id={"change-email"} onClick={changeEmail}>Change email</Button>
        <Button id={"delete"} onClick={deleteUser}>Delete</Button>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </Div>);
}

export default Settings;