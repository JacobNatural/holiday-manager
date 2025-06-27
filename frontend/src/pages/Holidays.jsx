import Div from "../components/Div";
import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import SendData from "../services/apiService";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import ErrorsContainer from "../components/ErrorsContainer";
import {ENDPOINTS} from "../api/ENDPOINTS";


const Holidays = () => {
    const location = useLocation();
    const {data} = location.state || {};
    const [holiday, setHoliday] = useState([]);
    const {error, display, showMessage} = useTimeDisplay();

    useEffect( () => {
        SendData(null, `${ENDPOINTS.SAVE_GET_HOLIDAY}startDate=${data.startDate}&endDate=${data.endDate}`, 'GET', null)
            .then(res => {
                if(res.data){
                    setHoliday(res.data);
                }
            }).catch(err => showMessage(err.message));
    },[data.startDate, data.endDate]);


    return (<Div>
        <h2>Holidays</h2>
        <table id={"table-holiday"}>
            <thead>
            <tr>
                <th>Holiday</th>
                <th>Start date</th>
                <th>End date</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            {holiday.map((h) => (
                <tr key={h.id}>
                    <td>{h.id}</td>
                    <td>{h.startDate}</td>
                    <td>{h.endDate}</td>
                    <td>{h.status}</td>
                </tr>
            ))}
            </tbody>
        </table>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </Div>);
}

export default Holidays;