import ForgotPasswordForm from "../components/ForgotPasswordForm";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import ErrorsContainer from "../components/ErrorsContainer";
import SendData from "../services/apiService";
import {useNavigate} from "react-router-dom";
import {ENDPOINTS} from "../api/ENDPOINTS";

const ForgotPassword = () => {
    const {error, display, showMessage} = useTimeDisplay();
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const dataForm = new FormData(e.target);

        const data ={email: dataForm.get('email')}
        SendData(data, ENDPOINTS.LOST_PASSWORD, 'PATCH', null)
            .then(() => navigate('/new-password'))
            .catch(e => showMessage(e.message))
    }

    return (<div>
        <ForgotPasswordForm onSubmit={handleSubmit}/>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </div>)
}

export default ForgotPassword;