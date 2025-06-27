import {useState} from "react";
import HolidaySearchingForm from "../components/HolidaySearchingForm";
import SendData from "../services/apiService";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import Button from "../components/Button";
import {ENDPOINTS} from "../api/ENDPOINTS";

const HolidayManager = () => {
    const [holidays, setHolidays] = useState([]);
    const {error, display, showMessage} = useTimeDisplay();
    const [editable, setEditable] = useState(false);
    const [rowId, setRowId] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            id: formData.get('holidayId'),
            userId: formData.get('userId'),
            startDate: formData.get('startDate'),
            endDate: formData.get('endDate'),
        }

        if (formData.get('status')) {
            data.status = formData.get('status');
        }

        SendData(data, ENDPOINTS.FILTER_HOLIDAYS, "POST", null)
            .then(r => {
                setHolidays(r.data);
            })
            .catch(err => showMessage(err.message));
    }

    const update = (e) => {
        setEditable(false);
        const update = holidays.map(h => {
            if (h.id === rowId) {
                h.status = e.target.value;
            }
            return h;
        })
        setHolidays(update)
        setRowId(null);
    }

    const handleSave = (holiday) => {
        SendData(null, `${ENDPOINTS.SAVE_GET_HOLIDAY}holidayId=${holiday.id}&status=${holiday.status}`, 'PATCH', null)
            .catch(e => showMessage(e.message));
    }

    return (<div className={'container'}>
        <HolidaySearchingForm onSubmit={handleSubmit}/>
        <h2>Holidays</h2>
        <table id="table-holiday">
            <thead>
            <tr>
                <th>Holiday</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Status</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            {holidays.map((holiday) => {
                return (
                    <tr key={holiday.id}>
                        <td>{holiday.id}</td>
                        <td>{holiday.startDate.replace('T', ' ')}</td>
                        <td>{holiday.endDate.replace('T', ' ')}</td>
                        {editable && holiday.id === rowId ? (
                            <td><select value={holiday.status} autoFocus={true} onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                    update(e);
                                }
                            }} onChange={(e) => update(e)
                            }>
                                <option>REJECTED</option>
                                <option>ACCEPTED</option>
                                <option>PROCESSING</option>
                            </select></td>) : (<td className="status-container" onClick={() => {
                            setEditable(true);
                            setRowId(holiday.id)
                        }}>{holiday.status}</td>)}
                        <td><Button id='save-button' onClick={() => handleSave(holiday)}>save</Button></td>
                    </tr>
                )
            })}
            </tbody>
        </table>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </div>)
}


export default HolidayManager;