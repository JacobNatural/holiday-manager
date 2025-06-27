import Div from "../components/Div";
import GetUserData from "./GetUser";
import HolidayAddCheckForm from "../components/HolidaAddCheckForm";
import {useNavigate} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import SendData from "../services/apiService";
import Button from "../components/Button";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import {ENDPOINTS} from "../api/ENDPOINTS";


const ActualWorkerUser = () => {

    const navigate = useNavigate();
    const [action, setAction] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const {error, display, showMessage} = useTimeDisplay();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const dataForm = new FormData(e.target);
        const data = {startDate: dataForm.get('start-date'), endDate: dataForm.get('end-date')};

        if(action === 'add'){
            await SendData(data,ENDPOINTS.ADD_HOLIDAYS, 'POST', null)
                .then((res) => {
                    setStartDate('');
                    setEndDate('');
                })
                .catch((err) => {
                    showMessage(err.message);
                });
        }else if(action === 'get'){
            navigate("/holidays",{
                state: {data}
            })
        }
    }

    const handleSetting = () =>{
        navigate("/settings ")
    }

    return (
        <Div id={"actual-user-worker"}>
            <h2>Actual Worker</h2>
            <Button id={'settings'} onClick={handleSetting}>Settings</Button>
            <GetUserData/>
            <HolidayAddCheckForm onSubmit={handleSubmit} addHoliday={() => setAction('add')} getHoliday={() => setAction('get')}
            startDate={startDate} setStartDate={setStartDate} endDate={endDate} setEndDate={setEndDate}/>
            <ErrorsContainer display={display}>{error}</ErrorsContainer>
        </Div>
    )
}

export default ActualWorkerUser;