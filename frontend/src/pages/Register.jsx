import RegisterForm from "../components/RegisterForm";
import SendData from "../services/apiService";
import {useNavigate} from "react-router-dom";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import {ENDPOINTS} from "../api/ENDPOINTS";

const Register = () => {

    const navigate = useNavigate();
    const {error, display, showMessage} = useTimeDisplay();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            name: formData.get('name'),
            surname: formData.get('surname'),
            email: formData.get('email'),
            username: formData.get('username'),
            password: formData.get('password'),
            age: formData.get('age')
        };

        await SendData(data, ENDPOINTS.USER_BASE, 'POST')
            .then(() => {
                navigate('/activate');
            }).catch(error => {
                showMessage(error.messages);
            });
    }
    return (<div>
            <RegisterForm onSubmit={handleSubmit}/>
            <ErrorsContainer display={display}>{error}</ErrorsContainer>
        </div>
    );

}

export default Register;